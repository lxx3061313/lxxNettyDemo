package com.lxx.mydemo.nettydemo.service.common.util;

import com.google.common.base.Charsets;
import com.google.common.hash.Hashing;
import java.util.concurrent.ThreadLocalRandom;

/**
 * @author lixiaoxiong
 * @version 2017-10-26
 */
public class TokenGenerator {
    public static String generate(String userId) {
        return Hashing.md5().newHasher()
                .putString(userId, Charsets.UTF_8)
                .putInt(ThreadLocalRandom.current().nextInt(1000))
                .putLong(System.currentTimeMillis()).hash()
                .toString();
    }


    public static String generateRandomPass(String userId) {
        return Hashing.md5().newHasher()
                .putString(userId, Charsets.UTF_8)
                .putInt(ThreadLocalRandom.current().nextInt(1000))
                .putLong(System.currentTimeMillis()).hash()
                .toString().substring(0, 6);
    }
}
