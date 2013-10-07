@echo off
set /p gamedir=<config\gamedir.txt
set tmpdir=target

::
:: install core jars to maven repository
::
set coredir=%gamedir%\starsector-core
set groupId=starsector.core
set version=0.6.1a

::
:: copy jars to temp dir
::
mkdir %tmpdir%
set coreLibListFile=config\corelibs.txt

setLocal EnableDelayedExpansion
for /f "tokens=* delims= " %%a in (%coreLibListFile%) do (
set name=%%a
echo read jar name !name! from corelibs.txt

set artifactId=%%a
set jarfile=%gamedir%\starsector-core\!name!.jar

echo copy !jarfile! %tmpdir%\!name!.jar
copy !jarfile! %tmpdir%\!name!.jar
set jarfile=%tmpdir%\!name!.jar

echo "installing jar !jarfile! to maven repo"
cmd /c mvn install:install-file -DgroupId=%groupId% -DartifactId=!artifactId! -Dversion=%version% -Dfile=!jarfile! -Dpackaging=jar
echo installed jar %jarfile% to maven repo
)

::
:: install thirdparty jars
::
set groupId=starsector.mod
set lazyLibJar=%gamedir%\mods\LazyLib\jars\LazyLib.jar
copy %lazyLibJar% %tmpdir%\LazyLib.jar
cmd /c mvn install:install-file -DgroupId=%groupId% -DartifactId=LazyLib -Dversion=%version% -Dfile=%tmpdir%\LazyLib.jar -Dpackaging=jar

::
:: build core
::
cmd /c build-core.bat

::
:: try to get the library sources
::
cmd /c mvn dependency:sources

