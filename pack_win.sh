# -----------------------------------------
# Requires building the .jar artifact first
# -----------------------------------------

# Change these lines to whatever you need
# JDK 
WIN_JDK="openjdk/bin"
# Artifact
ART_PATH="out/artifacts/tweaker_editor_jar"
ART_NAME="tweaker-editor.jar"


echo "Using JDK from $WIN_JDK"

# Remove temporary files
rm -rf pack
# Remove previous installers
rm -f *.exe

# Copy the things we want to pack
mkdir pack
# /!\ You may want to change this path /!\
cp -r $ART_PATH/$ART_NAME pack/
cp README.md pack/
cp LICENSE.md pack/

# Pack the stuff
$WIN_JDK/jpackage --input pack/ --module-path mods/ --add-modules javafx.controls,javafx.fxml --name "Open Tweaker Editor" --app-version 0.1 --vendor "OTE Team" --main-jar $ART_NAME --license-file LICENSE.md --icon img/icon.ico --win-menu --win-dir-chooser
