package com.lxx.mydemo.nettydemo.service.common.util.http.base;

import java.net.URI;

public interface HttpRequest extends HttpMessage {

    URI getURI();

    Method getMethod();

    RequestConfig getConfig();
}
