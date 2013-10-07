@echo off
set /p gamedir=<config\gamedir.txt
cmd /c build.bat
set curdir=%CD%
cd %gamedir%\starsector-core
cmd /c starsector.bat
cd /d %curdir%