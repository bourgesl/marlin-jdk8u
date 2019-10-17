#!/bin/bash

OJDK8_PATH=openjdk-jdk8u
PATCH_PATH=`pwd -P`

cd ../${OJDK8_PATH}/jdk

patch -p1 < ${PATCH_PATH}/m01.8143849.patch
patch -p1 < ${PATCH_PATH}/m02.8145055.patch
patch -p1 < ${PATCH_PATH}/m03.8144630.patch 
patch -p1 < ${PATCH_PATH}/m04.8144446.patch 
patch -p1 < ${PATCH_PATH}/m05.8144445.patch 
patch -p1 < ${PATCH_PATH}/m06.8144526.patch 
patch -p1 < ${PATCH_PATH}/m07.8144654.patch 
patch -p1 < ${PATCH_PATH}/m08.8144718.patch 
patch -p1 < ${PATCH_PATH}/m09.8149338.patch 
patch -p1 < ${PATCH_PATH}/m10.8148886.patch 
patch -p1 < ${PATCH_PATH}/m11.8144938.patch 
patch -p1 < ${PATCH_PATH}/m12.8159093.patch 
patch -p1 < ${PATCH_PATH}/m13.8159638.patch 
patch -p1 < ${PATCH_PATH}/m14.8180055.patch 
patch -p1 < ${PATCH_PATH}/m15.8191814.patch 
patch -p1 < ${PATCH_PATH}/m16.8198885.patch 
patch -p1 < ${PATCH_PATH}/m17.8200526.patch 
patch -p1 < ${PATCH_PATH}/m18.8202580.patch 
patch -p1 < ${PATCH_PATH}/m19.8210335.patch 

