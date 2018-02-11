package com.lxx.mydemo.nettydemo.service.common.util.http.base;

import java.net.URI;

public class DeleteRequest extends BasicHttpRequest {

    public DeleteRequest(URI uri) {
        super(uri);
    }

    @Override
    public Method getMethod() {
        return Method.DELETE;
    }
}