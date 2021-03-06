/*
 * Copyright (c) 2015, 2016, Oracle and/or its affiliates. All rights reserved.
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

import java.awt.geom.Path2D;
import java.lang.ref.WeakReference;
import java.util.concurrent.atomic.AtomicInteger;
import sun.java2d.ReentrantContext;
import sun.java2d.ReentrantContextProvider;
import static sun.java2d.marlin.ArrayCache.*;
import sun.java2d.marlin.MarlinRenderingEngine.NormalizingPathIterator;
import static sun.java2d.marlin.MarlinUtils.logInfo;

/**
 * This class is a renderer context dedicated to a single thread
 */
final class RendererContext extends ReentrantContext implements MarlinConst {

    // RendererContext creation counter
    private static final AtomicInteger CTX_COUNT = new AtomicInteger(1);
    // RendererContext statistics
    final RendererStats stats = (DO_STATS || DO_MONITORS)
                                       ? RendererStats.getInstance(): null;

    private static final boolean USE_CACHE_HARD_REF = DO_STATS
        || (MarlinRenderingEngine.REF_TYPE == ReentrantContextProvider.REF_WEAK);

    /**
     * Create a new renderer context
     *
     * @return new RendererContext instance
     */
    static RendererContext createContext() {
        final RendererContext newCtx = new RendererContext("ctx"
                    + Integer.toString(CTX_COUNT.getAndIncrement()));

        if (DO_STATS || DO_MONITORS) {
            RendererStats.ALL_CONTEXTS.add(newCtx);
        }
        return newCtx;
    }

    // context name (debugging purposes)
    final String name;
    // dirty flag indicating an exception occured during pipeline in pathTo()
    boolean dirty = false;
    // dynamic array caches kept using weak reference (low memory footprint)
    WeakReference<ArrayCachesHolder> refArrayCaches = null;
    // hard reference to array caches (for statistics)
    ArrayCachesHolder hardRefArrayCaches = null;
    // shared data
    final float[] float6 = new float[6];
    // shared curve (dirty) (Renderer / Stroker)
    final Curve curve = new Curve();
    // MarlinRenderingEngine NormalizingPathIterator NearestPixelCenter:
    final NormalizingPathIterator nPCPathIterator;
    // MarlinRenderingEngine NearestPixelQuarter NormalizingPathIterator:
    final NormalizingPathIterator nPQPathIterator;
    // MarlinRenderingEngine.TransformingPathConsumer2D
    final TransformingPathConsumer2D transformerPC2D;
    // recycled Path2D instance
    Path2D.Float p2d = null;
    final Renderer renderer;
    final Stroker stroker;
    // Simplifies out collinear lines
    final CollinearSimplifier simplifier = new CollinearSimplifier();
    final Dasher dasher;
    final MarlinTileGenerator ptg;
    final MarlinCache cache;
    // flag indicating the shape is stroked (1) or filled (0)
    int stroking = 0;

    /**
     * Constructor
     *
     * @param name context name (debugging)
     */
    RendererContext(final String name) {
        if (LOG_CREATE_CONTEXT) {
            MarlinUtils.logInfo("new RendererContext = " + name);
        }

        this.name = name;

        // NormalizingPathIterator instances:
        nPCPathIterator = new NormalizingPathIterator.NearestPixelCenter(float6);
        nPQPathIterator  = new NormalizingPathIterator.NearestPixelQuarter(float6);

        // MarlinRenderingEngine.TransformingPathConsumer2D
        transformerPC2D = new TransformingPathConsumer2D();

        // Renderer:
        cache = new MarlinCache(this);
        renderer = new Renderer(this); // needs MarlinCache from rdrCtx.cache
        ptg = new MarlinTileGenerator(renderer);

        stroker = new Stroker(this);
        dasher = new Dasher(this);
    }

    /**
     * Disposes this renderer context:
     * clean up before reusing this context
     */
    void dispose() {
        stroking = 0;
        // reset hard reference to array caches if needed:
        if (!USE_CACHE_HARD_REF) {
            hardRefArrayCaches = null;
        }
        // if context is maked as DIRTY:
        if (dirty) {
            // may happen if an exception if thrown in the pipeline processing:
            // force cleanup of all possible pipelined blocks (except Renderer):

            // NormalizingPathIterator instances:
            this.nPCPathIterator.dispose();
            this.nPQPathIterator.dispose();
            // Dasher:
            this.dasher.dispose();
            // Stroker:
            this.stroker.dispose();

            // mark context as CLEAN:
            dirty = false;
        }
    }

    // Array caches
    ArrayCachesHolder getArrayCachesHolder() {
        // Use hard reference first (cached resolved weak reference):
        ArrayCachesHolder holder = hardRefArrayCaches;
        if (holder == null) {
            // resolve reference:
            holder = (refArrayCaches != null)
                     ? refArrayCaches.get()
                     : null;
            // create a new ArrayCachesHolder if none is available
            if (holder == null) {
                if (LOG_CREATE_CONTEXT) {
                    MarlinUtils.logInfo("new ArrayCachesHolder for "
                                        + "RendererContext = " + name);
                }

                holder = new ArrayCachesHolder();

                if (USE_CACHE_HARD_REF) {
                    // update hard reference:
                    hardRefArrayCaches = holder;
                }

                // update weak reference:
                refArrayCaches = new WeakReference<ArrayCachesHolder>(holder);
            }
        }
        return holder;
    }

    // dirty byte array cache
    ByteArrayCache getDirtyByteArrayCache(final int length) {
        final int bucket = ArrayCache.getBucketDirtyBytes(length);
        return getArrayCachesHolder().dirtyByteArrayCaches[bucket];
    }

    byte[] getDirtyByteArray(final int length) {
        if (length <= MAX_DIRTY_BYTE_ARRAY_SIZE) {
            return getDirtyByteArrayCache(length).getArray();
        }

        if (DO_STATS) {
            incOversize();
        }

        if (DO_LOG_OVERSIZE) {
            logInfo("getDirtyByteArray[oversize]: length=\t" + length);
        }

        return new byte[length];
    }

    void putDirtyByteArray(final byte[] array) {
        final int length = array.length;
        // odd sized array are non-cached arrays (initial arrays)
        // ensure to never store initial arrays in cache:
        if (((length & 0x1) == 0) && (length <= MAX_DIRTY_BYTE_ARRAY_SIZE)) {
            getDirtyByteArrayCache(length).putDirtyArray(array, length);
        }
    }

    byte[] widenDirtyByteArray(final byte[] in,
                               final int usedSize, final int needSize)
    {
        final int length = in.length;
        if (DO_CHECKS && length >= needSize) {
            return in;
        }
        if (DO_STATS) {
            incResizeDirtyByte();
        }

        // maybe change bucket:
        // ensure getNewSize() > newSize:
        final byte[] res = getDirtyByteArray(getNewSize(usedSize, needSize));

        System.arraycopy(in, 0, res, 0, usedSize); // copy only used elements

        // maybe return current array:
        // NO clean-up of array data = DIRTY ARRAY
        putDirtyByteArray(in);

        if (DO_LOG_WIDEN_ARRAY) {
            logInfo("widenDirtyByteArray[" + res.length + "]: usedSize=\t"
                    + usedSize + "\tlength=\t" + length + "\tneeded length=\t"
                    + needSize);
        }
        return res;
    }

    // int array cache
    IntArrayCache getIntArrayCache(final int length) {
        final int bucket = ArrayCache.getBucket(length);
        return getArrayCachesHolder().intArrayCaches[bucket];
    }

    int[] getIntArray(final int length) {
        if (length <= MAX_ARRAY_SIZE) {
            return getIntArrayCache(length).getArray();
        }

        if (DO_STATS) {
            incOversize();
        }

        if (DO_LOG_OVERSIZE) {
            logInfo("getIntArray[oversize]: length=\t" + length);
        }

        return new int[length];
    }

    // unused
    int[] widenIntArray(final int[] in, final int usedSize,
                        final int needSize, final int clearTo)
    {
        final int length = in.length;
        if (DO_CHECKS && length >= needSize) {
            return in;
        }
        if (DO_STATS) {
            incResizeInt();
        }

        // maybe change bucket:
        // ensure getNewSize() > newSize:
        final int[] res = getIntArray(getNewSize(usedSize, needSize));

        System.arraycopy(in, 0, res, 0, usedSize); // copy only used elements

        // maybe return current array:
        putIntArray(in, 0, clearTo); // ensure all array is cleared (grow-reduce algo)

        if (DO_LOG_WIDEN_ARRAY) {
            logInfo("widenIntArray[" + res.length + "]: usedSize=\t"
                    + usedSize + "\tlength=\t" + length + "\tneeded length=\t"
                    + needSize);
        }
        return res;
    }

    void putIntArray(final int[] array, final int fromIndex,
                     final int toIndex)
    {
        final int length = array.length;
        // odd sized array are non-cached arrays (initial arrays)
        // ensure to never store initial arrays in cache:
        if (((length & 0x1) == 0) && (length <= MAX_ARRAY_SIZE)) {
            getIntArrayCache(length).putArray(array, length, fromIndex, toIndex);
        }
    }

    // dirty int array cache
    IntArrayCache getDirtyIntArrayCache(final int length) {
        final int bucket = ArrayCache.getBucket(length);
        return getArrayCachesHolder().dirtyIntArrayCaches[bucket];
    }

    int[] getDirtyIntArray(final int length) {
        if (length <= MAX_ARRAY_SIZE) {
            return getDirtyIntArrayCache(length).getArray();
        }

        if (DO_STATS) {
            incOversize();
        }

        if (DO_LOG_OVERSIZE) {
            logInfo("getDirtyIntArray[oversize]: length=\t" + length);
        }

        return new int[length];
    }

    int[] widenDirtyIntArray(final int[] in,
                             final int usedSize, final int needSize)
    {
        final int length = in.length;
        if (DO_CHECKS && length >= needSize) {
            return in;
        }
        if (DO_STATS) {
            incResizeDirtyInt();
        }

        // maybe change bucket:
        // ensure getNewSize() > newSize:
        final int[] res = getDirtyIntArray(getNewSize(usedSize, needSize));

        System.arraycopy(in, 0, res, 0, usedSize); // copy only used elements

        // maybe return current array:
        // NO clean-up of array data = DIRTY ARRAY
        putDirtyIntArray(in);

        if (DO_LOG_WIDEN_ARRAY) {
            logInfo("widenDirtyIntArray[" + res.length + "]: usedSize=\t"
                    + usedSize + "\tlength=\t" + length + "\tneeded length=\t"
                    + needSize);
        }
        return res;
    }

    void putDirtyIntArray(final int[] array) {
        final int length = array.length;
        // odd sized array are non-cached arrays (initial arrays)
        // ensure to never store initial arrays in cache:
        if (((length & 0x1) == 0) && (length <= MAX_ARRAY_SIZE)) {
            getDirtyIntArrayCache(length).putDirtyArray(array, length);
        }
    }

    // dirty float array cache
    FloatArrayCache getDirtyFloatArrayCache(final int length) {
        final int bucket = ArrayCache.getBucket(length);
        return getArrayCachesHolder().dirtyFloatArrayCaches[bucket];
    }

    float[] getDirtyFloatArray(final int length) {
        if (length <= MAX_ARRAY_SIZE) {
            return getDirtyFloatArrayCache(length).getArray();
        }

        if (DO_STATS) {
            incOversize();
        }

        if (DO_LOG_OVERSIZE) {
            logInfo("getDirtyFloatArray[oversize]: length=\t" + length);
        }

        return new float[length];
    }

    float[] widenDirtyFloatArray(final float[] in,
                                 final int usedSize, final int needSize)
    {
        final int length = in.length;
        if (DO_CHECKS && length >= needSize) {
            return in;
        }
        if (DO_STATS) {
            incResizeDirtyFloat();
        }

        // maybe change bucket:
        // ensure getNewSize() > newSize:
        final float[] res = getDirtyFloatArray(getNewSize(usedSize, needSize));

        System.arraycopy(in, 0, res, 0, usedSize); // copy only used elements

        // maybe return current array:
        // NO clean-up of array data = DIRTY ARRAY
        putDirtyFloatArray(in);

        if (DO_LOG_WIDEN_ARRAY) {
            logInfo("widenDirtyFloatArray[" + res.length + "]: usedSize=\t"
                    + usedSize + "\tlength=\t" + length + "\tneeded length=\t"
                    + needSize);
        }
        return res;
    }

    void putDirtyFloatArray(final float[] array) {
        final int length = array.length;
        // odd sized array are non-cached arrays (initial arrays)
        // ensure to never store initial arrays in cache:
        if (((length & 0x1) == 0) && (length <= MAX_ARRAY_SIZE)) {
            getDirtyFloatArrayCache(length).putDirtyArray(array, length);
        }
    }

    /* class holding all array cache instances */
    static final class ArrayCachesHolder {
        // zero-filled int array cache:
        final IntArrayCache[] intArrayCaches;
        // dirty array caches:
        final IntArrayCache[] dirtyIntArrayCaches;
        final FloatArrayCache[] dirtyFloatArrayCaches;
        final ByteArrayCache[] dirtyByteArrayCaches;

        ArrayCachesHolder() {
            intArrayCaches = new IntArrayCache[BUCKETS];
            dirtyIntArrayCaches = new IntArrayCache[BUCKETS];
            dirtyFloatArrayCaches = new FloatArrayCache[BUCKETS];
            dirtyByteArrayCaches = new ByteArrayCache[BUCKETS];

            for (int i = 0; i < BUCKETS; i++) {
                intArrayCaches[i] = new IntArrayCache(ARRAY_SIZES[i]);
                // dirty array caches:
                dirtyIntArrayCaches[i] = new IntArrayCache(ARRAY_SIZES[i]);
                dirtyFloatArrayCaches[i] = new FloatArrayCache(ARRAY_SIZES[i]);
                dirtyByteArrayCaches[i] = new ByteArrayCache(DIRTY_BYTE_ARRAY_SIZES[i]);
            }
        }
    }
}
