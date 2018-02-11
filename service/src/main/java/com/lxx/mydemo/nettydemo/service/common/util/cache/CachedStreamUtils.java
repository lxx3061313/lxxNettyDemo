/*
 * Copyright (c) 2013 Qunar.com. All Rights Reserved.
 */
package com.lxx.mydemo.nettydemo.service.common.util.cache;

import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author rongqian.xu created on 9/18/2014 8:01 PM
 * @version $Id$
 */
public class CachedStreamUtils {
    private final static Logger logger = LoggerFactory.getLogger(CachedStreamUtils.class);

    /**
     * 检查缓存的参数是否有效，无效跑出IllegalArgumentException
     * @param initCacheSize
     * @param maxCacheSize
     */
    public static void checkCacheSizeParam(int initCacheSize, int maxCacheSize) {
        if(initCacheSize <= 0){
            throw new IllegalArgumentException("init cache size is invalid!");
        }

        if(maxCacheSize <= 0){
            throw new IllegalArgumentException("max cache size is valid!");
        }
        if(initCacheSize > maxCacheSize){
            throw new IllegalArgumentException("init cache is large than max cache size!!");
        }
    }

    public static void main(String[] args) throws UnsupportedEncodingException {
        String url = "asdlk=alskdjl";
        System.out.println(URLEncoder.encode(url, "utf-8"));
    }

    /**
     * 安全写入流中，如果遇到异常会吞掉，不抛异常
     * @param out
     * @param val
     */
    public static void safeWrite(OutputStream out, int val){
        try {
            out.write(val);
        } catch (IOException e) {
            logger.debug("", e);
             //ignore
        }
    }
}
