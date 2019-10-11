Given by Andrew Brygin (azul.com) from Zulu team:
https://mail.openjdk.java.net/pipermail/jdk8u-dev/2019-September/010321.html

52a6785e245a|8143849: Integrate Marlin renderer per JEP 265
07a98e143c92|8145055: Marlin renderer causes unaligned write accesses
16b38c56dba4|8144630: Use PrivilegedAction to create Thread in Marlin RendererStats
b36b9934a4c3|8144446: Automate the Marlin crash test
292eed87287b|8144445: Maximum size checking in Marlin ArrayCache utility methods is not optimal
6aa4a7ea1682|8144526: Remove Marlin logging use of deleted internal API
1a8ba52fbe88|8144654: Improve Marlin logging
8b81a02893ba|8144718: Pisces / Marlin Strokers may generate invalid curves with huge coordinates and round joins
f0734a32b8f7|8149338: JVM Crash caused by Marlin renderer not handling NaN coordinates
95dbc93b1277|8148886: SEGV in sun.java2d.marlin.Renderer._endRendering
8cfae1022822|8144938: Handle properly coordinate overflow in Marlin Renderer
b47d577777fa|8159093: Fix coding conventions in Marlin renderer
03f1e982b2ea|8159638: Improve array caches and renderer stats in Marlin renderer
84ee3282bbb8|8180055: Upgrade the Marlin renderer in Java2D
5be8f7e7d446|8191814: Marlin rasterizer spends time computing geometry for stroked segments that do not intersect the clip
f6958e555594|8198885: upgrade Marlin (java2d) to 0.9.1
f413c2aee392|8200526: Test sun/java2d/marlin/ClipShapeTest.java times out
f6b60e32b9c8|8202580: Dashed BasicStroke randomly painted incorrectly, may freeze application
ea8b9c1a9982|8210335: Clipping problems with complex affine transforms: negative scaling factors or small scaling factors
00cb53d6194d|8180055: Upgrade the Marlin renderer in Java2D (update service list)



"
Hello Laurent,

 I am sorry for the delay.

 I have applied Marlin patches from Zulu 8 repo to jdk8u-dev,
 and generated webrevs.

 There are 20 patches related to Marlin integration:

01 8143849: Integrate Marlin renderer per JEP 265
02 8145055: Marlin renderer causes unaligned write accesses
03 8144630: Use PrivilegedAction to create Thread in Marlin RendererStats
04 8144446: Automate the Marlin crash test
05 8144445: Maximum size checking in Marlin ArrayCache utility methods is not optimal
06 8144526: Remove Marlin logging use of deleted internal API
07 8144654: Improve Marlin logging
08 8144718: Pisces / Marlin Strokers may generate invalid curves with huge coordinates and round joins
09 8149338: JVM Crash caused by Marlin renderer not handling NaN coordinates
10 8148886: SEGV in sun.java2d.marlin.Renderer._endRendering
11 8144938: Handle properly coordinate overflow in Marlin Renderer
12 8159093: Fix coding conventions in Marlin renderer
13 8159638: Improve array caches and renderer stats in Marlin renderer
14 8180055: Upgrade the Marlin renderer in Java2D
15 8191814: Marlin rasterizer spends time computing geometry for stroked segments that do not intersect the clip
16 8198885: upgrade Marlin (java2d) to 0.9.1
17 8200526: Test sun/java2d/marlin/ClipShapeTest.java times out
18 8202580: Dashed BasicStroke randomly painted incorrectly, may freeze application
19 8210335: Clipping problems with complex affine transforms: negative scaling factors or small scaling factors
20 8180055: Upgrade the Marlin renderer in Java2D (update service list)

 Webrevs are available here:

http://cr.openjdk.java.net/~bae/marlin/webrevs/

 Just in case, patches are available here, cleanly applicable to
 current state of jdk8u-dev:

http://cr.openjdk.java.net/~bae/marlin/

Thanks,
Andrew
"


