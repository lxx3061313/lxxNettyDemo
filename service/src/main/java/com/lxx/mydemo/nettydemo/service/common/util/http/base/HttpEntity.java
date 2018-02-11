package com.lxx.mydemo.nettydemo.service.common.util.http.base;

/**
 * @author zhenwei.liu
 * @since 2016-12-20
 */
public interface HttpEntity<T> {

    String getMimeType();

    String getCharset();

    String getContentEncoding();

    T getContent();
}
