package com.lxx.mydemo.nettydemo.service.common.web;


import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Description: QValue
 *
 * Annotation at the field level
 * that indicates a default value expression for the affected argument.
 *
 * <p>A common use case is to assign default field values using
 * "systemProperties.myProp" style expressions.
 *
 * <p>This is more effective than the normal way to reload qConfig
 *
 * <p>And it will support normal properties also
 *
 * @author yushen.ma
 * @version 2015-04-24 13:06
 */
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface MValue {

    /**
     * The actual value expression: e.g. "systemProperties.myProp".
     *
     * or if for default profile
     * systemProperties.profile
     *
     * I don't know what will happen to 64-bit value.
     * That is not operation safe take good care for ur safe.
     * even bother ur program in such a short time when you push profile to ur server
     *
     * so Double.class and Float.class is Forbidden
     */
    String value();

}
