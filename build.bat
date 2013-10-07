@echo off
set /p gamedir=<config\gamedir.txt
set moddir=%gamedir%\mods\PlanetDefend


cmd /c mvn clean package
del /Q /S %moddir%
md %moddir%
md %moddir%\jars
copy target\PlanetDefend-jar-with-dependencies.jar %moddir%\jars\PlanetDefend-all.jar
xcopy /E /Y moddata\* %moddir%\

