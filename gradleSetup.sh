#!/usr/bin/env bash
GRADLE_PROPERTIES=$HOME"/.gradle/gradle.properties"
export GRADLE_PROPERTIES

if [ ! -f "$GRADLE_PROPERTIES" ]; then
     echo "Creating Gradle Properties file..."
     touch $GRADLE_PROPERTIES

     echo "Writing jmeterHome to gradle.properties..."
     echo "jmeterHome=$JMETER_HOME" >> $GRADLE_PROPERTIES
     echo "Writing jmeterProperties to gradle.properties..."
     echo "jmeterProperties=$JMETER_HOME/bin/jmeter.properties" >> $GRADLE_PROPERTIES
fi