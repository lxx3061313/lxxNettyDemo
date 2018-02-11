package com.lxx.mydemo.nettydemo.service.common.util.http.base;

import java.util.Collection;
import java.util.Map;

/**
 * @author zhenwei.liu
 * @since 2016-12-20
 */
public interface HttpMessage {

    ProtocolVersion getProtocolVersion();

    String getFirstHeader(String name);

    Collection<String> getHeader(String name);

    Map<String, Collection<String>> getAllHeaders();

    void addHeader(String name, String val);

    void setHeader(String name, String val);

    void removeHeader(String name);
}
