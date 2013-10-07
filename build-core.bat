set /P gamedir=<config\gamedir.txt
set /P version=<config\version.txt
set curdir=%CD%
set workdir=target

::
:: build parent
::
mkdir %workdir%\parent
xcopy /E /Y config\parent %workdir%\parent
cd %workdir%\parent
cmd /c mvn clean install -Dversion=%version%
cd %curdir%

::
:: build core
::
mkdir %workdir%\core
xcopy /E /Y config\core %workdir%\core
xcopy /E /Y %gamedir%\starsector-core %workdir%\core\starsector-core > NUL
cd %workdir%\core
cmd /c mvn clean install -Dversion=%	%
cd %curdir%
