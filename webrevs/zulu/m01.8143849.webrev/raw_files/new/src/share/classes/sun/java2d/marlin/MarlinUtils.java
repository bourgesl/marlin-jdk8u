/*
 * Copyright (c) 2015, Oracle and/or its affiliates. All rights reserved.
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS FILE HEADER.
 *
 * This code is free software; you can redistribute it and/or modify it
 * under the terms of the GNU General Public License version 2 only, as
 * published by the Free Software Foundation.  Oracle designates this
 * particular file as subject to the "Classpath" exception as provided
 * by Oracle in the LICENSE file that accompanied this code.
 *
 * This code is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License
 * version 2 for more details (a copy is included in the LICENSE file that
 * accompanied this code).
 *
 * You should have received a copy of the GNU General Public License version
 * 2 along with this work; if not, write to the Free Software Foundation,
 * Inc., 51 Franklin St, Fifth Floor, Boston, MA 02110-1301 USA.
 *
 * Please contact Oracle, 500 Oracle Parkway, Redwood Shores, CA 94065 USA
 * or visit www.oracle.com if you need additional information or have any
 * questions.
 */

package sun.java2d.marlin;

import sun.misc.JavaLangAccess;
import sun.misc.SharedSecrets;

public final class MarlinUtils {
    // TODO: use sun.util.logging.PlatformLogger once in JDK9
    private static final java.util.logging.Logger log;

    static {
        if (MarlinConst.useLogger) {
            log = java.util.logging.Logger.getLogger("sun.java2d.marlin");
        } else {
            log = null;
        }
    }

    private MarlinUtils() {
        // no-op
    }

    public static void logInfo(final String msg) {
        if (MarlinConst.useLogger) {
            log.info(msg);
        } else if (MarlinConst.enableLogs) {
            System.out.print("INFO: ");
            System.out.println(msg);
        }
    }

    public static void logException(final String msg, final Throwable th) {
        if (MarlinConst.useLogger) {
//            log.warning(msg, th);
            log.log(java.util.logging.Level.WARNING, msg, th);
        } else if (MarlinConst.enableLogs) {
            System.out.print("WARNING: ");
            System.out.println(msg);
            th.printStackTrace(System.err);
        }
    }

    // Returns the caller's class and method's name; best effort
    // if cannot infer, return the logger's name.
    static String getCallerInfo(String className) {
        String sourceClassName = null;
        String sourceMethodName = null;

        JavaLangAccess access = SharedSecrets.getJavaLangAccess();
        Throwable throwable = new Throwable();
        int depth = access.getStackTraceDepth(throwable);

        boolean lookingForClassName = true;
        for (int ix = 0; ix < depth; ix++) {
            // Calling getStackTraceElement directly prevents the VM
            // from paying the cost of building the entire stack frame.
            StackTraceElement frame = access.getStackTraceElement(throwable, ix);
            String cname = frame.getClassName();
            if (lookingForClassName) {
                // Skip all frames until we have found the first frame having the class name.
                if (cname.equals(className)) {
                    lookingForClassName = false;
                }
            } else {
                if (!cname.equals(className)) {
                    // We've found the relevant frame.
                    sourceClassName = cname;
                    sourceMethodName = frame.getMethodName();
                    break;
                }
            }
        }

        if (sourceClassName != null) {
            return sourceClassName + " " + sourceMethodName;
        } else {
            return "unknown";
        }
    }
}
