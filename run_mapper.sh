#!/bin/bash
# - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -
# File            run_mapper.sh
# Description     Compiles Java source files and runs the application
# Author          Xin Cai
# Modified Date   Dec.27 2023
# - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -

# Create bin folder if it doesn't exist
mkdir -p bin

# Remove .class files
rm -f bin/*.class

# Compile Java source files
javac -d bin src/*.java

# Check if compilation was successful
if [ $? -eq 0 ]; then
    # Run the app
    java -cp bin MapApp
else
    echo "Eroror: compilation failed."
fi
