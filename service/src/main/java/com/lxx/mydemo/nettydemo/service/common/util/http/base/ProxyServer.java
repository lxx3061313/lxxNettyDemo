package com.lxx.mydemo.nettydemo.service.common.util.http.base;

/**
 * @author zhenwei.liu
 * @since 2016-08-31
 */
public class ProxyServer {

    private String host;
    private int port;

    public ProxyServer(String host, int port) {
        this.host = host;
        this.port = port;
    }

    public String getHost() {
        return host;
    }

    public int getPort() {
        return port;
    }

}
