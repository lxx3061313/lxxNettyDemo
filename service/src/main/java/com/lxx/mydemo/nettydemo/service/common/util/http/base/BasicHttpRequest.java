package com.lxx.mydemo.nettydemo.service.common.util.http.base;

import java.net.URI;

/**
 * @author zhenwei.liu
 * @since 2016-12-20
 */
public abstract class BasicHttpRequest extends AbstractHttpMessage implements HttpRequest {

    private final URI uri;
    private ProtocolVersion protocolVersion;
    private RequestConfig config;

    public BasicHttpRequest(URI uri) {
        this.uri = uri;
        this.protocolVersion = new ProtocolVersion(uri.getScheme(), 1, 1);
    }

    @Override
    public URI getURI() {
        return uri;
    }

    @Override
    public ProtocolVersion getProtocolVersion() {
        return protocolVersion;
    }

    @Override
    public RequestConfig getConfig() {
        return config;
    }

    public void setConfig(RequestConfig config) {
        this.config = config;
    }

    public void setProtocolVersion(ProtocolVersion protocolVersion) {
        this.protocolVersion = protocolVersion;
    }
}
