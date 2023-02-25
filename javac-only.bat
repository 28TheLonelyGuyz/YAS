@echo off

title Javac only
color f
goto debug

:debug
cd src
cls
javac -g lonli/Main.java
echo.
echo.
pause
goto debug