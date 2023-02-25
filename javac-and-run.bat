@echo off

title Javac and Run
color f
goto debug

:debug
cd src
cls
javac -g lonli/Main.java
java lonli.Main
echo.
echo.
pause
goto debug