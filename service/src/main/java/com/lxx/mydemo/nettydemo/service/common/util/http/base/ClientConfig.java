package com.lxx.mydemo.nettydemo.service.common.util.http.base;

import java.util.concurrent.TimeUnit;
import javax.net.ssl.SSLContext;

/**
 * @author zhenwei.liu
 * @since 2016-07-01
 */
public class ClientConfig {

    private int requestTimeout = 5000; // 5 seconds
    private int connectTimeout = 1000; // 1 second
    private boolean poolConnections = true;
    private boolean autoRedirect = false;
    private boolean autoCompression = false;
    private int maxConnections = 100;
    private int maxConnectionsPerHost = 20;
    private long pooledConnectionIdleTimeoutMs = TimeUnit.SECONDS.toMillis(120); // 连接池保存连接时间, 秒
    private SSLContext sslContext;
    private PoolKeyStrategy poolKeyStrategy;

    public boolean isPoolConnections() {
        return poolConnections;
    }

    public void setPoolConnections(boolean poolConnections) {
        this.poolConnections = poolConnections;
    }

    public boolean isAutoRedirect() {
        return autoRedirect;
    }

    public void setAutoRedirect(boolean autoRedirect) {
        this.autoRedirect = autoRedirect;
    }

    public boolean isAutoCompression() {
        return autoCompression;
    }

    public void setAutoCompression(boolean autoCompression) {
        this.autoCompression = autoCompression;
    }

    public int getMaxConnections() {
        return maxConnections;
    }

    public void setMaxConnections(int maxConnections) {
        this.maxConnections = maxConnections;
    }

    public int getMaxConnectionsPerHost() {
        return maxConnectionsPerHost;
    }

    public void setMaxConnectionsPerHost(int maxConnectionsPerHost) {
        this.maxConnectionsPerHost = maxConnectionsPerHost;
    }

    public SSLContext getSslContext() {
        return sslContext;
    }

    public void setSslContext(SSLContext sslContext) {
        this.sslContext = sslContext;
    }

    public PoolKeyStrategy getPoolKeyStrategy() {
        return poolKeyStrategy;
    }

    public void setPoolKeyStrategy(PoolKeyStrategy poolKeyStrategy) {
        this.poolKeyStrategy = poolKeyStrategy;
    }

    public ClientConfig setPooledConnectionIdleTimeoutMs(long pooledConnectionIdleTimeoutMs) {
        this.pooledConnectionIdleTimeoutMs = pooledConnectionIdleTimeoutMs;
        return this;
    }

    public long getPooledConnectionIdleTimeoutMs() {
        return pooledConnectionIdleTimeoutMs;
    }

    public int getRequestTimeout() {
        return requestTimeout;
    }

    public ClientConfig setRequestTimeout(int requestTimeout) {
        this.requestTimeout = requestTimeout;
        return this;
    }

    public int getConnectTimeout() {
        return connectTimeout;
    }

    public ClientConfig setConnectTimeout(int connectTimeout) {
        this.connectTimeout = connectTimeout;
        return this;
    }

    // 兼容老接口
    public void setConnectionTimeout(int connectTimeout) {
        this.connectTimeout = connectTimeout;
    }

}
