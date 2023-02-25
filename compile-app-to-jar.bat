@echo off

title Compile to Jar
goto start

:start
echo Enter filename for jar file
set /p filename=
goto compile

:compile
set /a rand0=%random%
set /a rand1=%random%
set /a rand2=%random%
set /a id=(%rand0%)*(%rand1%)/(%rand2%)
set manifest_filename="%id%-manifest"

cd assets_packer
echo Packing assets...
java -jar "yas-assets-packer.jar" ../assets ../app_bin/assistant.pak excluded_files.txt
cd ..

rem Creating the manifest file
echo Main-Class: lonli.Main> %manifest_filename%
echo.>> %manifest_filename%

jar -cvfm "app_bin/%filename%" %manifest_filename% lonli/*.class lonli/*.txt lonli/lua/*.class lonli/lua/*.txt lonli/modsupport/*.class lonli/modsupport/*.txt copyfiles/*.class copyfiles/*.txt lonliengine/*.class lonliengine/*.txt org/luaj/* *.class org/json/*

del %manifest_filename%
echo.
echo.
echo Done.
echo Press any key to exit...
pause >nul
exit