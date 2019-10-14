#!/bin/bash

OJDK8_PATH=openjdk-jdk8u
PATCH_PATH=`pwd -P`/jdk8-patches

cd ../${OJDK8_PATH}/jdk

patch -p1 < ${PATCH_PATH}/8-m20.8228711.patch
patch -p1 < ${PATCH_PATH}/8-m21.8230728.patch

