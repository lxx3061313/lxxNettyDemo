package com.lxx.mydemo.nettydemo.service.common.web;

import com.google.common.base.Preconditions;
import com.google.common.base.Splitter;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.google.common.primitives.Primitives;
import java.math.BigDecimal;
import java.text.MessageFormat;
import java.util.List;
import java.util.Set;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Description: QPropertiesEditor
 *
 * just a convert
 *
 * @author yushen.ma
 * @version 2015-04-24 14:26
 */
public enum QPropertiesEditor {

    INSTANCE;

    final private Logger logger = LoggerFactory.getLogger(getClass());

    final private Splitter splitter = Splitter.on(",");

    public Object apply(String plainValue, Class<?> targetValueClass) {
        Preconditions.checkArgument(isSupport(targetValueClass), "暂不支持该字段类型");
        Preconditions.checkArgument(null != plainValue);
        plainValue = StringUtils.trimToEmpty(plainValue);
        // wrap, this is important, cause of primitive class is such fucking special
        if (targetValueClass.isPrimitive()) {
            targetValueClass = Primitives.wrap(targetValueClass);
        }
        if (targetValueClass == Integer.class) {
            return Integer.parseInt(plainValue);
        } else if (targetValueClass == Long.class) {
            return Long.parseLong(plainValue);
        } else if (targetValueClass == String.class) {
            return plainValue;
        } else if (Double.class == targetValueClass) {
            return Double.parseDouble(plainValue);
        } else if (Float.class == targetValueClass) {
            return Float.parseFloat(plainValue);
        } else if (Void.class == targetValueClass) {
            return Void.TYPE;
        } else if (Short.class == targetValueClass) {
            return Short.parseShort(plainValue);
        } else if (Character.class == targetValueClass) {
            return plainValue.charAt(0);
        } else if (Byte.class == targetValueClass) {
            return plainValue.getBytes()[0];
        } else if (BigDecimal.class == targetValueClass) {
            return new BigDecimal(plainValue);
        } else if (Boolean.class == targetValueClass) {
            return Boolean.valueOf(plainValue);
        } else if (List.class == targetValueClass) {
            if (StringUtils.isBlank(plainValue)) {
                return Lists.newArrayList();
            }
            return splitter.splitToList(plainValue);
        } else if (Set.class == targetValueClass) {
            if (StringUtils.isBlank(plainValue)) {
                return Sets.newHashSet();
            }
            return Sets.newHashSet(splitter.splitToList(plainValue));
        }
        throw new IllegalArgumentException(
                MessageFormat.format("UNSUPPORTED TYPE : \"{0}\" and VALUE : \"{1}\"", targetValueClass, plainValue));
    }

    public boolean isSupport(Class<?> clazz) {
        if (clazz.isPrimitive()) { // 费劲
            clazz = Primitives.wrap(clazz);
        }
        // @see http://docs.oracle.com/javase/specs/jls/se8/html/jls-17.html#jls-17.7
        if (clazz == Double.class || clazz == Long.class ) {
            logger.info("long and double is not safe to replace in Java Language memory model" +
                    " For the purposes of the Java programming language memory model, " +
                    " a single write to a non-volatile long or double value is treated as two separate writes: one to each 32-bit half." +
                    " This can result in a situation where a thread sees the first 32 bits of a 64-bit value from one write," +
                    " and the second 32 bits from another write.\n" +
                    "\n ");
            return false;
        }
        return Set.class == clazz || List.class == clazz ||
                String.class == clazz || BigDecimal.class == clazz
                || Primitives.isWrapperType(clazz);
    }
}
