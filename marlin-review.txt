0. unshuffled all patches from jdk/jdk (14) to 8 format


1. m01 Review:
m01.8143849.patch + merge with marlin changes only from m01b.8149896.patch

FloatMath.java:
- removed powerOfTwoD(int n) as done in m01b.8149896.patch


MarlinCache.java:
MarlinTileGenerator.java:

import jdk.internal.misc.Unsafe;				=>			import sun.misc.Unsafe;


MarlinUtils:

import jdk.internal.misc.JavaLangAccess;		=>			import sun.misc.JavaLangAccess;
import jdk.internal.misc.SharedSecrets;			=>			import sun.misc.SharedSecrets;


OffHeapArray.java:

import sun.awt.util.ThreadGroupUtils;			=>			import sun.misc.ThreadGroupUtils;
import jdk.internal.misc.Unsafe;				=>			import sun.misc.Unsafe;


Renderer.java:

import jdk.internal.misc.Unsafe;				=>			import sun.misc.Unsafe;

- line change from m01b.8149896.patch:
private static final double POWER_2_TO_32 = FloatMath.powerOfTwoD(32);			=>			private static final double POWER_2_TO_32 = 0x1.0p32;


RenderingEngine.java:

- Many lines changes to fix JDK8 ServiceLoader to use MarlinRenderingEngine by default:

             AccessController.doPrivileged(new PrivilegedAction<RenderingEngine>() {
                 public RenderingEngine run() {
                     final String ductusREClass = "sun.dc.DuctusRenderingEngine";
-                    String reClass =
-                        System.getProperty("sun.java2d.renderer", ductusREClass);
-                    if (reClass.equals(ductusREClass)) {
+                    final String marlinREClass = "sun.java2d.marlin.MarlinRenderingEngine";
+
+                    String reClass = System.getProperty("sun.java2d.renderer");
+                    if (reClass == null || reClass.equals(ductusREClass)) {
                         try {
                             Class<?> cls = Class.forName(ductusREClass);
                             return (RenderingEngine) cls.newInstance();
@@ -133,17 +134,27 @@
                     }
 
                     ServiceLoader<RenderingEngine> reLoader =
-                        ServiceLoader.loadInstalled(RenderingEngine.class);
+                            ServiceLoader.loadInstalled(RenderingEngine.class);
 
                     RenderingEngine service = null;
+                    RenderingEngine marlinService = null;
 
                     for (RenderingEngine re : reLoader) {
                         service = re;
-                        if (re.getClass().getName().equals(reClass)) {
-                            break;
+                        String serviceName = re.getClass().getName();
+                        if (serviceName.equals(reClass)) {
+                            return service;
+                        }
+                        if (serviceName.equals(marlinREClass)) {
+                            marlinService = service;
                         }
                     }
-                    return service;
+                    // use Marlin as default renderer
+                    if (marlinService != null) {
+                        return marlinService;
+                    } else {
+                        return service;
+                    }
                 }
             });


- Add MarlinRenderingEngine in sun.java2d.pipe.RenderingEngine property files:

src/share/classes/sun/java2d/pisces/META-INF/services/sun.java2d.pipe.RenderingEngine
+
+#  Marlin Rendering Engine module
+sun.java2d.marlin.MarlinRenderingEngine

src/solaris/classes/sun/java2d/pisces/META-INF/services/sun.java2d.pipe.RenderingEngine
+
+#  Marlin Rendering Engine module
+sun.java2d.marlin.MarlinRenderingEngine


