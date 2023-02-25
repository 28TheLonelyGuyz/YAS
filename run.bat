@echo off

title Run
color f
goto debug

:debug
cd src
cls
java lonli.Main
echo.
echo.
pause
goto debug