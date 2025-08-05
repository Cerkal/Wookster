#!/bin/bash
set -e

cd "$(dirname "$0")"
cd ..

echo "[1/4] Cleaning..."
rm -rf out game.jar
mkdir -p out

echo "[2/4] Compiling..."
javac -d out $(find src -name "*.java")

echo "[3/4] Copying resources..."
cp -r src/res out/

echo "[4/4] Creating manifest and JAR..."
echo "Main-Class: main.Main" > manifest.txt
jar cfm game.jar manifest.txt -C out .

echo "✅ Build complete. Run with:"
echo "  java -jar game.jar"
