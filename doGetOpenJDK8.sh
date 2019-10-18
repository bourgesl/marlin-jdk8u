#!/bin/bash

#git clone -b release https://github.com/AdoptOpenJDK/openjdk-jdk8u.git

hg clone http://hg.openjdk.java.net/jdk8u/jdk8u-dev/ openjdk-jdk8u

cd openjdk-jdk8u
bash get_source.sh
bash ./configure --with-user-release-suffix=marlin

