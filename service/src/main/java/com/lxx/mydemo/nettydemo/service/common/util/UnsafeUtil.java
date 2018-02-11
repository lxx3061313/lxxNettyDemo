package com.lxx.mydemo.nettydemo.service.common.util;

import java.lang.reflect.Field;
import sun.misc.Unsafe;

/**
 * @author yushen.ma
 * @version 2016-04-08
 */
public class UnsafeUtil {

    private static Unsafe instance;

    static {
        try {
            Field f;
            f = Unsafe.class.getDeclaredField("theUnsafe");
            f.setAccessible(true);
            instance = (Unsafe) f.get(null);
        } catch (Throwable ignore) {
        }
    }

    /**
     * 可以抛出一个受检异常
     */
    public static RuntimeException throwException(Throwable t) {
        instance.throwException(t);
        return new RuntimeException(t);
    }

}
