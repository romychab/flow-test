#!/bin/bash

PASS_KEY=$1
PASS_FILE=$2
PASS_HOST=$3
PASS_PATH=$4

tar -cf - dokka | sshpass -P 'passphrase' -f $PASS_FILE \
  ssh -i $PASS_KEY $PASS_HOST \
  "cd $PASS_PATH/ && rm -rf flowtest && tar xf - && mv dokka flowtest"
