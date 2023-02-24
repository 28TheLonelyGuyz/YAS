@echo off

cd assets_packer
java -jar yas-assets-packer.jar ../assets ../assistant.pak excluded_files.txt
cd ..