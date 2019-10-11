# marlin-jdk8u
Patches to integrate the Marlin renderer into OpenJDK 8 Updates


------------------------------------------------
# Marlin renderer patches history (jdk/client) |
------------------------------------------------

* 2015
34419:14108cfd0823			m01.8143849.patch			8143849: Integrate Marlin renderer per JEP 265
35979:4462913d471a			NEW m01b.8149896.patch		8149896: Remove unnecessary values in FloatConsts and DoubleConsts		=> Merge with m01.8143849.patch

34689:4b5bf9f960c8			m02.8145055.patch			8145055: Marlin renderer causes unaligned write accesses
34801:a7740dae1f3a			m03.8144630.patch			8144630: Use PrivilegedAction to create Thread in Marlin RendererStats
34814:09435f7f0013			m04.8144446.patch			8144446: Automate the Marlin crash test
34815:81e87daa9876			m05.8144445.patch			8144445: Maximum size checking in Marlin ArrayCache utility methods is not optimal
34422:438c2710ab20			m06.8144526.patch			8144526: Remove Marlin logging use of deleted internal API
34816:5ff696b1bbac			m07.8144654.patch			8144654: Improve Marlin logging

* 2016
35445:86db4c432b19			SKIP	 					8147443: Use the Common Cleaner in Marlin OffHeapArray
35645:a96d68e3fda2			m08.8144718.patch			8144718: Pisces / Marlin Strokers may generate invalid curves with huge coordinates and round joins
35665:e90002447fd5			SKIP 						8146076: Fail of sun/java2d/marlin/CeilAndFloorTests.java with Jigsaw
36446:c06d6e681158			m09.8149338.patch			8149338: JVM Crash caused by Marlin renderer not handling NaN coordinates
36458:25786a73a5fc			m10.8148886.patch			8148886: SEGV in sun.java2d.marlin.Renderer._endRendering
36902:bb30d89aa00e			m11.8144938.patch			8144938: Handle properly coordinate overflow in Marlin Renderer
39519:21bfc4452441			m12.8159093.patch			8159093: Fix coding conventions in Marlin renderer
40421:d5ee65e2b0fb			m13.8159638.patch			8159638: Improve array caches and renderer stats in Marlin renderer

* 2017
47126:188ef162f019			m14.8180055.patch			8180055: Upgrade the Marlin renderer in Java2D  => verify zulu patch m20.8180055.patch

48258:fd7fbc929001			m15.8191814.patch			8191814: Marlin rasterizer spends time computing geometry for stroked segments that do not intersect the clip

* 2018
49318:1ea202af7a97			m16.8198885.patch			8198885: upgrade Marlin (java2d) to 0.9.1
49524:a38e7ef21cc0			m17.8200526.patch			8200526: Test sun/java2d/marlin/ClipShapeTest.java times out
50019:793e481c7641			m18.8202580.patch			8202580: Dashed BasicStroke randomly painted incorrectly, may freeze application
51887:4ec74929fbfe			m19.8210335.patch			8210335: Clipping problems with complex affine transforms: negative scaling factors or small scaling factors

* 2019
55816:13178f7e75d5			NEW m20.8228711.patch		8228711: Path rendered incorrectly when it goes outside the clipping region
56131:7f55aad34ac4			NEW m21.8230728.patch		8230728: Thin stroked shapes are not rendered if affine transform has flip bit


