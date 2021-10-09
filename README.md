# marlin-jdk8u
Patches to integrate the Marlin renderer into OpenJDK 8 Updates


# Marlin renderer patches history (jdk/client)
```̀
* 2015
34419:14108cfd0823  [x]     m01.8143849.patch			8143849: Integrate Marlin renderer per JEP 265
35979:4462913d471a			SKIP						8149896: Remove unnecessary values in FloatConsts and DoubleConsts		=> Merged with m01.8143849.patch

34689:4b5bf9f960c8  [x]		m02.8145055.patch			8145055: Marlin renderer causes unaligned write accesses
34801:a7740dae1f3a  [x]		m03.8144630.patch			8144630: Use PrivilegedAction to create Thread in Marlin RendererStats
34814:09435f7f0013  [x]		m04.8144446.patch			8144446: Automate the Marlin crash test
34815:81e87daa9876  [x]		m05.8144445.patch			8144445: Maximum size checking in Marlin ArrayCache utility methods is not optimal
34422:438c2710ab20  [x]		m06.8144526.patch			8144526: Remove Marlin logging use of deleted internal API
34816:5ff696b1bbac  [x]		m07.8144654.patch			8144654: Improve Marlin logging

* 2016
35445:86db4c432b19			SKIP	 					8147443: Use the Common Cleaner in Marlin OffHeapArray
35645:a96d68e3fda2  [x]		m08.8144718.patch			8144718: Pisces / Marlin Strokers may generate invalid curves with huge coordinates and round joins
35665:e90002447fd5			SKIP 						8146076: Fail of sun/java2d/marlin/CeilAndFloorTests.java with Jigsaw
36446:c06d6e681158  [x]		m09.8149338.patch			8149338: JVM Crash caused by Marlin renderer not handling NaN coordinates
36458:25786a73a5fc  [x]		m10.8148886.patch			8148886: SEGV in sun.java2d.marlin.Renderer._endRendering
36902:bb30d89aa00e	[x]		m11.8144938.patch			8144938: Handle properly coordinate overflow in Marlin Renderer : Backport JDK-8269499 - 8ub311
39519:21bfc4452441	[x]		m12.8159093.patch			8159093: Fix coding conventions in Marlin renderer : Backport JDK-8269500 - 8ub311
40421:d5ee65e2b0fb	[x]		m13.8159638.patch			8159638: Improve array caches and renderer stats in Marlin renderer : Backport JDK-8269501 - 8ub311

* 2017
47126:188ef162f019	[x]		m14.8180055.patch			8180055: Upgrade the Marlin renderer in Java2D  => verify zulu patch m20.8180055.patch : Backport JDK-8269502 - 8ub311
48258:fd7fbc929001	[x]		m15.8191814.patch			8191814: Marlin rasterizer spends time computing geometry for stroked segments that do not intersect the clip : backport JDK-8269503 - 8ub311

* 2018
49318:1ea202af7a97	[x]		m16.8198885.patch			8198885: upgrade Marlin (java2d) to 0.9.1 : backport JDK-8269504 - 8ub311
49524:a38e7ef21cc0	[x]		m17.8200526.patch			8200526: Test sun/java2d/marlin/ClipShapeTest.java times out : backport JDK-8269505 - 8ub311
50019:793e481c7641	[x]		m18.8202580.patch			8202580: Dashed BasicStroke randomly painted incorrectly, may freeze application : backport JDK-8269506 - 8ub311
51887:4ec74929fbfe	[x]		m19.8210335.patch			8210335: Clipping problems with complex affine transforms: negative scaling factors or small scaling factors : backport JDK-8269507 - 8ub311

* 2019
55816:13178f7e75d5	[x]		NEW m20.8228711.patch		8228711: Path rendered incorrectly when it goes outside the clipping region : backport JDK-8269508 - 8ub311
56131:7f55aad34ac4	[x]		NEW m21.8230728.patch		8230728: Thin stroked shapes are not rendered if affine transform has flip bit : backport JDK-8269509 - 8ub311
```̀

OracleJDK 8u b311 has latest Marlin renderer (0.9.1.3) !
But OpenJDK8u b312 is still at patch 10 (0.7.3.2) !
