package com.lxx.mydemo.nettydemo.service.common.util.http.base;

public class RequestConfig {

    private int requestTimeout = 5000; // 5 seconds
    private int connectTimeout = 1000; // 1 second
    private ProxyServer proxyServer;

    public int getRequestTimeout() {
        return requestTimeout;
    }

    public void setRequestTimeout(int requestTimeout) {
        this.requestTimeout = requestTimeout;
    }

    public int getConnectTimeout() {
        return connectTimeout;
    }

    public void setConnectTimeout(int connectTimeout) {
        this.connectTimeout = connectTimeout;
    }

    // 兼容老接口
    public void setConnectionTimeout(int connectTimeout) {
        this.connectTimeout = connectTimeout;
    }

    public ProxyServer getProxyServer() {
        return proxyServer;
    }

    public RequestConfig setProxyServer(ProxyServer proxyServer) {
        this.proxyServer = proxyServer;
        return this;
    }

    public RequestConfig setProxyServer(String host, int port) {
        return setProxyServer(new ProxyServer(host, port));
    }
}