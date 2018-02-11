package com.lxx.mydemo.nettydemo.service.common.util.http;

import com.lxx.mydemo.nettydemo.service.common.util.http.base.ClientConfig;
import com.lxx.mydemo.nettydemo.service.common.util.http.base.PoolKeyStrategy;
import java.util.concurrent.TimeUnit;
import javax.net.ssl.SSLContext;

/**
 * @author zhenwei.liu
 * @since 2017-01-03
 */
public class HttpClientBuilder {

    private boolean poolConnections = true;
    private boolean autoRedirect = false;
    private boolean autoCompression = false;
    private int maxConnections = 100;
    private int maxConnectionsPerHost = 20;
    private long pooledConnectionIdleTimeoutMs = TimeUnit.SECONDS.toMillis(120); // 连接池保存连接时间, 秒
    private SSLContext sslContext;
    private PoolKeyStrategy poolKeyStrategy;

    private HttpClientBuilder() {
    }

    public static HttpClientBuilder create() {
        return new HttpClientBuilder();
    }

    public HttpClient build() {
        ClientConfig config = new ClientConfig();
        config.setPoolConnections(poolConnections);
        config.setAutoRedirect(autoRedirect);
        config.setAutoCompression(autoCompression);
        config.setMaxConnections(maxConnections);
        config.setMaxConnectionsPerHost(maxConnectionsPerHost);
        config.setPooledConnectionIdleTimeoutMs(pooledConnectionIdleTimeoutMs);
        config.setSslContext(sslContext);
        config.setPoolKeyStrategy(poolKeyStrategy);
        return new DefaultHttpClient(config);
    }

    public boolean isPoolConnections() {
        return poolConnections;
    }

    public HttpClientBuilder setPoolConnections(boolean poolConnections) {
        this.poolConnections = poolConnections;
        return this;
    }

    public boolean isAutoRedirect() {
        return autoRedirect;
    }

    public HttpClientBuilder setAutoRedirect(boolean autoRedirect) {
        this.autoRedirect = autoRedirect;
        return this;
    }

    public boolean isAutoCompression() {
        return autoCompression;
    }

    public HttpClientBuilder setAutoCompression(boolean autoCompression) {
        this.autoCompression = autoCompression;
        return this;
    }

    public int getMaxConnections() {
        return maxConnections;
    }

    public HttpClientBuilder setMaxConnections(int maxConnections) {
        this.maxConnections = maxConnections;
        return this;
    }

    public int getMaxConnectionsPerHost() {
        return maxConnectionsPerHost;
    }

    public HttpClientBuilder setMaxConnectionsPerHost(int maxConnectionsPerHost) {
        this.maxConnectionsPerHost = maxConnectionsPerHost;
        return this;
    }

    public long getPooledConnectionIdleTimeoutMs() {
        return pooledConnectionIdleTimeoutMs;
    }

    public HttpClientBuilder setPooledConnectionIdleTimeoutMs(long pooledConnectionIdleTimeoutMs) {
        this.pooledConnectionIdleTimeoutMs = pooledConnectionIdleTimeoutMs;
        return this;
    }

    public SSLContext getSslContext() {
        return sslContext;
    }

    public HttpClientBuilder setSslContext(SSLContext sslContext) {
        this.sslContext = sslContext;
        return this;
    }

    public PoolKeyStrategy getPoolKeyStrategy() {
        return poolKeyStrategy;
    }

    public HttpClientBuilder setPoolKeyStrategy(
            PoolKeyStrategy poolKeyStrategy) {
        this.poolKeyStrategy = poolKeyStrategy;
        return this;
    }
}
