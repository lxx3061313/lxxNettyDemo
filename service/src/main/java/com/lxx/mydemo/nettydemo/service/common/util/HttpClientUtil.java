package com.lxx.mydemo.nettydemo.service.common.util;

import com.google.common.util.concurrent.ListenableFuture;
import com.lxx.mydemo.nettydemo.service.common.util.http.DefaultHttpClient;
import com.lxx.mydemo.nettydemo.service.common.util.http.base.AsyncHttpResponse;
import com.lxx.mydemo.nettydemo.service.common.util.http.base.ClientConfig;
import com.lxx.mydemo.nettydemo.service.common.util.json.JsonUtil;
import org.springframework.stereotype.Service;

/**
 * @author lixiaoxiong
 * @version 2018-01-22
 */
@Service
public class HttpClientUtil {
    private final static DefaultHttpClient  httpclient;
    static {
        ClientConfig clientConfig = new ClientConfig();
        clientConfig.setPoolConnections(false);
        // 建立socket的时间
        clientConfig.setConnectionTimeout(4000);
        // 异步情况下一次整体请求的时间 | 同步情况下指每一个tcp包之间的间隔
        // 这里需要http超时时间短于core的dubbo超时时间
        // 否则his锁号成功但响应慢，会导致core抛锁号超时异常，并且无法成单，解锁号，用户彻底挂不了号
        clientConfig.setRequestTimeout(4000);
        clientConfig.setMaxConnectionsPerHost(100);
        clientConfig.setMaxConnections(1000);
        httpclient = new DefaultHttpClient(clientConfig);
    }

    public ListenableFuture<AsyncHttpResponse> syncGet(String url) {
        return httpclient.asyncGet(url);
    }

    public ListenableFuture<AsyncHttpResponse> syncPost(String url, Object param) {
        return httpclient.asyncPost(url, JsonUtil.of(param));
    }
}
