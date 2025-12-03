#!/bin/sh

# Gradle wrapper script
# This file must have Unix line endings (LF)

MYSELF=$(realpath "$0")
GRADLE_HOME=$(dirname "$MYSELF")/.gradle
exec "\( GRADLE_HOME/wrapper/gradle-wrapper.jar" " \)@"
