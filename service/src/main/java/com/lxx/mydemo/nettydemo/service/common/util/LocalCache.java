package com.lxx.mydemo.nettydemo.service.common.util;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import java.util.concurrent.TimeUnit;
import org.springframework.stereotype.Service;

/**
 * @author lixiaoxiong
 * @version 2018-02-02
 */
@Service
public class LocalCache {

    private static int OPEN_ID_EXPIRES_TIME = 20;

    private Cache<String, String> cache = CacheBuilder.newBuilder().maximumSize(100000).expireAfterAccess(OPEN_ID_EXPIRES_TIME,
            TimeUnit.MINUTES).build();

    public void setValue(String key, String value) {
        cache.put(key, value);
    }

    public String getValue(String key) {
        return cache.getIfPresent(key);
    }
}
