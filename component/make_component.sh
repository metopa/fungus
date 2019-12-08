#!/usr/bin/env bash

COMPONENT_DIR="component_temp_dir"
LANGUAGE_PATH="$COMPONENT_DIR/jre/languages/fungus"

rm -rf COMPONENT_DIR

mkdir -p "$LANGUAGE_PATH"
cp ../language/target/funguslanguage.jar "$LANGUAGE_PATH"

mkdir -p "$LANGUAGE_PATH/launcher"
cp ../launcher/target/fungus-launcher.jar "$LANGUAGE_PATH/launcher/"

mkdir -p "$LANGUAGE_PATH/bin"
cp ../fungus $LANGUAGE_PATH/bin/

touch "$LANGUAGE_PATH/native-image.properties"

mkdir -p "$COMPONENT_DIR/META-INF"
{
    echo "Bundle-Name: Fungus Language";
    echo "Bundle-Symbolic-Name: cz.metopa.fungus";
    echo "Bundle-Version: 19.2.0";
    echo 'Bundle-RequireCapability: org.graalvm; filter:="(&(graalvm_version=19.2.0)(os_arch=amd64))"';
    echo "x-GraalVM-Polyglot-Part: False"
} > "$COMPONENT_DIR/META-INF/MANIFEST.MF"

(
cd $COMPONENT_DIR || exit 1
jar cfm ../fungus-component.jar META-INF/MANIFEST.MF .

echo "bin/fungus = ../jre/languages/fungus/bin/fungus" > META-INF/symlinks
jar uf ../fungus-component.jar META-INF/symlinks

{
    echo "jre/languages/fungus/bin/fungus = rwxrwxr-x"
} > META-INF/permissions
jar uf ../fungus-component.jar META-INF/permissions
)
rm -rf $COMPONENT_DIR
