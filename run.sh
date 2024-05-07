#!/bin/bash

sourceFiles=$(find src/ -name "*.java")

javaModulesFolder="out/production/Projekt"
javaPath="/usr/lib/jvm/java-1.17.0-openjdk-amd64/bin/"
#javaPath=""

${javaPath}javac ${sourceFiles} -d ${javaModulesFolder}

if [ $? -eq 0 ]; then
    echo "Compilation successful. Running Java program..."
    ${javaPath}java -cp "${javaModulesFolder}:jar/postgresql-42.7.3.jar" main.Main
else
    echo "Compilation failed. Please check your source code."
fi
