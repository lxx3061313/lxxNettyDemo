package com.lxx.mydemo.nettydemo.service.common.web.spring.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 用于对JsonBody标注的结果，执行跳转
 * 
 * @author zhongyuan.zhang
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Redirect {

    String[] hosts() default {};

    /**
     * token校验密钥
     * 
     * @return
     */
    String secret() default "";

    /**
     * 跳转目标对请求取值字段
     * 
     * @return
     */
    String dataKey() default "r";

    /**
     * 跳转目标对请求取校验码
     * 
     * @return
     */
    String tokenKey() default "cbt";
}
