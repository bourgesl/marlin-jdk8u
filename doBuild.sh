#!/bin/bash

cd marlin-zulu
bash doPatch.sh
cd ..

cd openjdk-jdk8u
make images

