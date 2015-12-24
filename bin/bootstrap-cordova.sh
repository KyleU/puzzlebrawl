#!/usr/bin/env bash
cd ../offline
rm -rf cordova
cordova create cordova com.puzzlebrawl PuzzleBrawl
cd cordova
cordova platform add ios
cordova platform add amazon-fireos
cordova platform add android
cordova platform add blackberry10
cordova platform add firefoxos

rm config.xml
cp ../src/cordova/config.xml .
