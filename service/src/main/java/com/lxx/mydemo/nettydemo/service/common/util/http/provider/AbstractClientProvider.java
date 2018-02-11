package com.lxx.mydemo.nettydemo.service.common.util.http.provider;

import com.lxx.mydemo.nettydemo.service.common.util.http.base.ClientConfig;
import com.lxx.mydemo.nettydemo.service.common.util.http.base.HttpResponse;
import java.io.Closeable;

/**
 * @author zhenwei.liu
 * @since 2016-07-18
 */
public abstract class AbstractClientProvider<Client, Request, Response, R extends HttpResponse, Config> implements ClientProvider<Client, Request, Response, R>, Closeable {

    private ClientConfig config;
    protected volatile Client client;

    protected AbstractClientProvider(ClientConfig config) {
        this.config = config;
    }

    protected ClientConfig getClientConfig() {
        return config;
    }

    @Override
    public Client getClient() {
        if (client == null) {
            synchronized (this) {
                if (client == null) {
                    client = createClient(createConfig(config));
                }
            }
        }
        return client;
    }

    protected abstract Client createClient(Config config);

    protected abstract Config createConfig(ClientConfig config);
}
