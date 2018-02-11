package com.lxx.mydemo.nettydemo.service.common.util.cache;

import java.io.IOException;
import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;

public class CachedHttpServletRequestWrapper extends HttpServletRequestWrapper implements
        CachedStreamEntity {

    private CachedInputStream cachedInputStream;

    public CachedHttpServletRequestWrapper(HttpServletRequest httpServletRequest, int initCacheSize, int maxCacheSize)
            throws IOException {
        super(httpServletRequest);
        this.cachedInputStream = new CachedInputStream(httpServletRequest.getInputStream(), initCacheSize, maxCacheSize);
    }

    @Override
    public ServletInputStream getInputStream() throws IOException {
        return cachedInputStream;
    }

    @Override
    public CachedStream getCachedStream() {
        return cachedInputStream;
    }

    @Override
    public void flushStream() {
        //do nothing
    }
}
