package com.lxx.mydemo.nettydemo.service.common.util;

import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.function.Consumer;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 避免一些异常情况的工具类
 *
 * @author yushen.ma
 * @version 1.0 2016-04-11
 */
public class Safes {

    final private static Logger logger = LoggerFactory.getLogger(Safes.class);

    public static <K, V> Map<K, V> of(Map<K, V> source) {
        return Optional.ofNullable(source).orElse(new HashMap<K, V>());
    }

    public static <T> Iterator<T> of(Iterator<T> source) {
        return Optional.ofNullable(source).orElse(new ArrayList<T>().iterator());
    }

    public static <T> Collection<T> of(Collection<T> source) {
        return Optional.ofNullable(source).orElse(Lists.newArrayList());
    }

    public static <T> Iterable<T> of(Iterable<T> source) {
        return Optional.ofNullable(source).orElse(new ArrayList<T>());
    }

    public static <T> List<T> of(List<T> source) {
        return Optional.ofNullable(source).orElse(new ArrayList<T>());
    }

    public static <T> Set<T> of(Set<T> source) {
        return Optional.ofNullable(source).orElse(new HashSet<T>());
    }

    public static BigDecimal of(BigDecimal source) {
        return Optional.ofNullable(source).orElse(BigDecimal.ZERO);
    }

    public static BigDecimal toBigDecimal(String source, BigDecimal defaultValue) {
        Preconditions.checkNotNull(defaultValue);
        try {
            return new BigDecimal(StringUtils.trimToEmpty(source));
        } catch (Throwable t) {
            logger.warn("未能识别的boolean类型, source:{}", source, t);
            return defaultValue;
        }
    }

    public static int toInt(String source, int defaultValue) {
        if (StringUtils.isBlank(source)) {
            return defaultValue;
        }
        try {
            return Integer.parseInt(StringUtils.trimToEmpty(source));
        } catch (Throwable t) {
            logger.warn("未能识别的整形 {}", source);
            return defaultValue;
        }
    }

    public static long toLong(String source, long defaultValue) {
        if (StringUtils.isBlank(source)) {
            return defaultValue;
        }
        try {
            return Long.parseLong(StringUtils.trimToEmpty(source));
        } catch (Throwable t) {
            logger.warn("未能识别的长整形 {}", source);
            return defaultValue;
        }
    }

    public static boolean toBoolean(String source, boolean defaultValue) {
        if (StringUtils.isBlank(source)) {
            return defaultValue;
        }
        try {
            return Boolean.parseBoolean(StringUtils.trimToEmpty(source));
        } catch (Throwable t) {
            logger.warn("未能识别的boolean类型, source:{}", source, t);
            return defaultValue;
        }
    }

    public static void run(Runnable runnable, Consumer<Throwable> error) {
        try {
            runnable.run();
        } catch (Throwable t) {
            error.accept(t);
        }
    }

}
