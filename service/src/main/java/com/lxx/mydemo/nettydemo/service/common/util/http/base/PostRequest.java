package com.lxx.mydemo.nettydemo.service.common.util.http.base;

import java.net.URI;

public class PostRequest extends HttpEntityRequest {

    public PostRequest(URI uri, HttpEntity entity) {
        super(uri, entity);
    }

    @Override
    public Method getMethod() {
        return Method.POST;
    }
}
