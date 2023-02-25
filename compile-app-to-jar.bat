@echo off
title Compile to Jar
goto start

:start
echo Enter name for jar file
set /p filename=
goto compile

:compile
set /a id="%random%"
set manifest_filename="%id%-manifest"

:: Packing assets
cd assets_packer
echo Packing assets...
java -jar "yas-assets-packer.jar" ../assets ../app_bin/assistant.pak excluded_files.txt
cd ..

:: Creating manifest file
echo Main-Class: lonli.Main> %manifest_filename%
echo.>> %manifest_filename%

:: Packing and compiling all class files
cd src
jar -cvfm "../app_bin/%filename%.jar" "../%manifest_filename%" lonli/*.class lonli/*.txt lonli/lua/*.class lonli/lua/*.txt lonli/modsupport/*.class lonli/modsupport/*.txt copyfiles/*.class copyfiles/*.txt lonliengine/*.class lonliengine/*.txt org/luaj/* org/json/* *.class

:: Finishing stuff
cd ..
del %manifest_filename%
echo.
echo.
echo Done.
echo Press any key to exit...
pause >nul
exit
