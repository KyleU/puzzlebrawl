#!/usr/bin/env bash

rm -rf ../target/universal/puzzlebrawl-0.1-SNAPSHOT/*
unzip ../target/universal/puzzlebrawl-0.1-SNAPSHOT.zip -d ../target/universal/

rsync -zrv --delete -e "ssh -i /Users/kyle/.ssh/aws-ec2-key.pem" ../target/universal/puzzlebrawl-0.1-SNAPSHOT/* ubuntu@puzzlebrawl.com:~/deploy/puzzlebrawl
