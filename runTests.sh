#!/bin/bash

sourceFiles=$(find src/ -name "*.java")

javaModulesFolder="out/production/Projekt"

javaPath="/usr/lib/jvm/java-1.17.0-openjdk-amd64/bin/"

junitJupiterJars="jar/junit-jupiter-api-5.9.1.jar:jar/junit-jupiter-engine-5.9.1.jar:jar/junit-platform-commons-1.9.1.jar:jar/junit-platform-engine-1.9.1.jar"
mockitoJar="jar/mockito-core-5.11.0.jar"
byteBuddyJar="jar/byte-buddy-1.14.12.jar"
byteBuddyAgentJar="jar/byte-buddy-agent-1.14.12.jar"
postgresJar="jar/postgresql-42.7.3.jar"
apiguardianJar="jar/apiguardian-api-1.1.2.jar"
junitPlatformConsoleJar="jar/junit-platform-console-standalone-1.9.1.jar"  # Update this path

${javaPath}javac -cp "${junitJupiterJars}:${mockitoJar}:${byteBuddyJar}:${byteBuddyAgentJar}:${postgresJar}:${apiguardianJar}:${junitPlatformConsoleJar}" ${sourceFiles} -d ${javaModulesFolder}

if [ $? -eq 0 ]; then
    echo "Compilation successful. Running Java tests..."

    ${javaPath}java -jar "${junitPlatformConsoleJar}" --classpath "${javaModulesFolder}:${junitJupiterJars}:${mockitoJar}:${byteBuddyJar}:${byteBuddyAgentJar}:${postgresJar}:${apiguardianJar}" --scan-class-path
else
    echo "Compilation failed. Please check your source code."
fi
