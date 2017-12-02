#!/usr/bin/env bash

rm -rf ../target/universal/puzzlebrawl-0.1-SNAPSHOT/*
unzip ../target/universal/puzzlebrawl-0.1-SNAPSHOT.zip -d ../target/universal/

rsync -zrv --delete ../target/universal/puzzlebrawl-0.1-SNAPSHOT/* kyle@puzzlebrawl.com:~/apps/puzzlebrawl.com
