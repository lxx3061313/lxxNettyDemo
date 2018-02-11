package com.lxx.mydemo.nettydemo.service.common.web.spring.annotation;

import com.lxx.mydemo.nettydemo.service.common.util.json.JsonFeature;
import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 用于标注Json或JSONP请求
 *
 * @author miao.yang susing@gmail.com
 * @since 2014-03-15
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface JsonBody {

    /**
     * JSONP函数参数名
     *
     * @return
     */
    String callback() default "callback";

    String debugTag() default "debugTag";

    Version version() default Version.v2;

    JsonFeature[] enable() default {};

    JsonFeature[] disable() default {};

    public static enum Version {
        v1, v2
    }
}