package com.lxx.mydemo.nettydemo.service.common.util.cache;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import javax.servlet.ServletOutputStream;

/**
 * 可以缓存写入流中的数据的代理流类，用于日志记录
 */
public class CachedOutputStream extends ServletOutputStream implements CachedStream {
    private ByteArrayOutputStream cachedOutputStream;
    private ServletOutputStream srcOutputStream;
    private int maxCacheSize;

    public CachedOutputStream(ServletOutputStream srcOutputStream, int initCacheSize, int maxCacheSize) {
        CachedStreamUtils.checkCacheSizeParam(initCacheSize, maxCacheSize);
        this.srcOutputStream = srcOutputStream;
        this.cachedOutputStream = new ByteArrayOutputStream(initCacheSize);
        this.maxCacheSize = maxCacheSize;
    }

    @Override
    public byte[] getCached() {
        return cachedOutputStream.toByteArray();
    }

    @Override
    public void write(int b) throws IOException {
        this.srcOutputStream.write(b);
        if(this.cachedOutputStream.size() < maxCacheSize) {
            CachedStreamUtils.safeWrite(cachedOutputStream, b);
        }
    }

    @Override
    public void close() throws IOException {
        super.close();
        this.cachedOutputStream.close();
    }
}
