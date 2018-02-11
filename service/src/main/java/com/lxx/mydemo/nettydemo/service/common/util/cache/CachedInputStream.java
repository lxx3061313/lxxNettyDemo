package com.lxx.mydemo.nettydemo.service.common.util.cache;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import javax.servlet.ServletInputStream;

/**
 * 可以缓存从流中读取的数据的代理流类，用于日志记录
 */
public class CachedInputStream extends ServletInputStream implements CachedStream{
    private ByteArrayOutputStream cachedOutputStream;
    private ServletInputStream srcInputStream;
    private int maxCacheSize;

    public CachedInputStream(ServletInputStream srcInputStream, int initCacheSize, int maxCacheSize) {
        CachedStreamUtils.checkCacheSizeParam(initCacheSize, maxCacheSize);
        this.srcInputStream = srcInputStream;
        this.cachedOutputStream = new ByteArrayOutputStream(initCacheSize);
        this.maxCacheSize = maxCacheSize;
    }

    @Override
    public int read() throws IOException {
        int b = srcInputStream.read();
        if(b != -1 && cachedOutputStream.size() < maxCacheSize) {
            CachedStreamUtils.safeWrite(cachedOutputStream, b);
        }
        return b;
    }

    @Override
    public byte[] getCached(){
        return cachedOutputStream.toByteArray();
    }

    @Override
    public void close() throws IOException {
        super.close();
        cachedOutputStream.close();
    }
}
