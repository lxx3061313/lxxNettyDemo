package com.lxx.mydemo.nettydemo.service.common.util.http.base;

import java.net.URI;

/**
 * @author zhenwei.liu
 * @since 2016-12-20
 */
public abstract class HttpEntityRequest extends BasicHttpRequest {

    private final HttpEntity entity;

    public HttpEntityRequest(URI uri, HttpEntity entity) {
        super(uri);
        this.entity = entity;
    }

    public HttpEntity getEntity() {
        return entity;
    }
}
