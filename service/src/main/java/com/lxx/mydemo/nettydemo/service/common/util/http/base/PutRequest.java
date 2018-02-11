package com.lxx.mydemo.nettydemo.service.common.util.http.base;

import java.net.URI;

/**
 * @author zhenwei.liu
 * @since 2016-12-20
 */
public class PutRequest extends HttpEntityRequest {

    public PutRequest(URI uri, HttpEntity entity) {
        super(uri, entity);
    }

    @Override
    public Method getMethod() {
        return Method.PUT;
    }
}
