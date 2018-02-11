package com.lxx.mydemo.nettydemo.service.common.util.http.base;

/**
 * @author zhenwei.liu
 * @since 2016-12-20
 */
public class BasicStatusLine implements StatusLine {

    private final ProtocolVersion protocolVersion;
    private final int statusCode;
    private final String reasonPhrase;

    public BasicStatusLine(ProtocolVersion protocolVersion, int statusCode, String reasonPhrase) {
        this.protocolVersion = protocolVersion;
        this.statusCode = statusCode;
        this.reasonPhrase = reasonPhrase;
    }

    @Override
    public ProtocolVersion getProtocolVersion() {
        return protocolVersion;
    }

    @Override
    public int getStatusCode() {
        return statusCode;
    }

    @Override
    public String getReasonPhrase() {
        return reasonPhrase;
    }
}
