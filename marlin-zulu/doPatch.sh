#!/bin/bash

cd jdk8u-dev/jdk

patch -p1 < ../../patches/marlin-zulu/m02.8145055.patch
patch -p1 < ../../patches/marlin-zulu/m02.8145055.patch
patch -p1 < ../../patches/marlin-zulu/m03.8144630.patch 
patch -p1 < ../../patches/marlin-zulu/m04.8144446.patch 
patch -p1 < ../../patches/marlin-zulu/m05.8144445.patch 
patch -p1 < ../../patches/marlin-zulu/m06.8144526.patch 
patch -p1 < ../../patches/marlin-zulu/m07.8144654.patch 
patch -p1 < ../../patches/marlin-zulu/m08.8144718.patch 
patch -p1 < ../../patches/marlin-zulu/m09.8149338.patch 
patch -p1 < ../../patches/marlin-zulu/m10.8148886.patch 
patch -p1 < ../../patches/marlin-zulu/m11.8144938.patch 
patch -p1 < ../../patches/marlin-zulu/m12.8159093.patch 
patch -p1 < ../../patches/marlin-zulu/m13.8159638.patch 
patch -p1 < ../../patches/marlin-zulu/m14.8180055.patch 
patch -p1 < ../../patches/marlin-zulu/m15.8191814.patch 
patch -p1 < ../../patches/marlin-zulu/m16.8198885.patch 
patch -p1 < ../../patches/marlin-zulu/m17.8200526.patch 
patch -p1 < ../../patches/marlin-zulu/m18.8202580.patch 
patch -p1 < ../../patches/marlin-zulu/m19.8210335.patch 
patch -p1 < ../../patches/marlin-zulu/m20.8180055.patch 

make images

