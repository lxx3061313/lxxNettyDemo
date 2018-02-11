package com.lxx.mydemo.nettydemo.service.common.util.http;


import com.google.common.util.concurrent.ListenableFuture;
import com.ning.http.client.AsyncHttpClient;
import com.lxx.mydemo.nettydemo.service.common.util.http.base.AsyncHandler;
import com.lxx.mydemo.nettydemo.service.common.util.http.base.AsyncHandlerBase;
import com.lxx.mydemo.nettydemo.service.common.util.http.base.AsyncHttpResponse;
import com.lxx.mydemo.nettydemo.service.common.util.http.base.ClientConfig;
import com.lxx.mydemo.nettydemo.service.common.util.http.base.HttpRequest;
import com.lxx.mydemo.nettydemo.service.common.util.http.base.SyncHttpResponse;
import com.lxx.mydemo.nettydemo.service.common.util.http.base.builder.EntityRequestBuilder;
import com.lxx.mydemo.nettydemo.service.common.util.http.base.builder.RequestBuilder;
import com.lxx.mydemo.nettydemo.service.common.util.http.provider.ApacheClientProvider;
import com.lxx.mydemo.nettydemo.service.common.util.http.provider.AsyncClientProvider;
import com.lxx.mydemo.nettydemo.service.common.util.http.support.async.DefaultAsyncCompletionHandler;
import com.lxx.mydemo.nettydemo.service.common.util.http.support.async.GuavaListenableFuture;
import java.io.IOException;
import java.util.Map;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.impl.client.CloseableHttpClient;

public class DefaultHttpClient implements HttpClient {

    private static AsyncHandler createDefHandler() {
        return new AsyncHandlerBase() {
            @Override
            public void onRequestCompleted(AsyncHttpResponse response) {

            }

            @Override
            public void onThrowable(Throwable t) {

            }
        };
    }

    private volatile ApacheClientProvider syncProvider;
    private volatile AsyncClientProvider asyncProvider;

    public DefaultHttpClient() {
        this(new ClientConfig());
    }

    public DefaultHttpClient(ClientConfig config) {
        syncProvider = new ApacheClientProvider(config);
        asyncProvider = new AsyncClientProvider(config);
    }

    @Override
    public SyncHttpResponse syncGet(String url) throws IOException {
        return syncRequest(RequestBuilder.createGet(url).build());
    }

    @Override
    public SyncHttpResponse syncPost(String url, Map<String, String> formParam) throws IOException {
        EntityRequestBuilder post = RequestBuilder.createPost(url);
        formParam.entrySet().forEach(e -> post.addFormData(e.getKey(), e.getValue()));
        return syncRequest(post.build());
    }

    @Override
    public SyncHttpResponse syncPost(String url, String bodyData) throws IOException {
        return syncRequest(RequestBuilder.createPost(url).setBody(bodyData).build());
    }

    @Override
    public SyncHttpResponse syncPost(String url, byte[] bodyData) throws IOException {
        return syncRequest(RequestBuilder.createPost(url).setBody(bodyData).build());
    }

    @Override
    public SyncHttpResponse syncRequest(HttpRequest request) throws IOException {
        CloseableHttpClient syncClient = syncProvider.getClient();

        try {
            CloseableHttpResponse response = syncClient.execute(syncProvider.createRequest
                    (request));
            SyncHttpResponse r = syncProvider.createResponse(response);
            return r;
        } catch (IOException e) {
            throw e;
        } finally {
        }
    }

    @Override
    public ListenableFuture<AsyncHttpResponse> asyncGet(String url) {
        return asyncGet(url, createDefHandler());
    }

    @Override
    public ListenableFuture<AsyncHttpResponse> asyncGet(String url, AsyncHandler handler) {
        return asyncRequest(RequestBuilder.createGet(url).build(), handler);
    }

    @Override
    public ListenableFuture<AsyncHttpResponse> asyncPost(String url,
            Map<String, String> formParam) {
        return asyncPost(url, formParam, createDefHandler());
    }

    @Override
    public ListenableFuture<AsyncHttpResponse> asyncPost(String url, Map<String, String>
            formParam, AsyncHandler handler) {
        EntityRequestBuilder post = RequestBuilder.createPost(url);
        if (formParam != null) {
            formParam.entrySet().forEach(e -> post.addFormData(e.getKey(), e.getValue()));
        }
        return asyncRequest(post.build(), handler);
    }

    @Override
    public ListenableFuture<AsyncHttpResponse> asyncPost(String url, String bodyData) {
        return asyncPost(url, bodyData, createDefHandler());
    }

    @Override
    public ListenableFuture<AsyncHttpResponse> asyncPost(String url, String bodyData,
            AsyncHandler handler) {
        return asyncRequest(RequestBuilder.createPost(url).setBody(bodyData).build(), handler);
    }

    @Override
    public ListenableFuture<AsyncHttpResponse> asyncPost(String url, byte[] bodyData) {
        return asyncPost(url, bodyData, createDefHandler());
    }

    @Override
    public ListenableFuture<AsyncHttpResponse> asyncPost(String url, byte[] bodyData,
            AsyncHandler handler) {
        return asyncRequest(RequestBuilder.createPost(url).setBody(bodyData).build(), handler);
    }

    /**
     * 回调 trace 结构
     * http_async / 业务线程 1
     * - http_async_request 1.1
     * - http 远端服务 1.1.1
     * - future.listener 1.1.2
     * - asyncHandler 1.1.3
     */
    @Override
    public ListenableFuture<AsyncHttpResponse> asyncRequest(final HttpRequest request, final
    AsyncHandler handler) {
        AsyncHttpClient asyncClient = asyncProvider.getClient();
        com.ning.http.client.Request ningRequest = asyncProvider.createRequest(request);
        return new GuavaListenableFuture<>(
                asyncClient.executeRequest(ningRequest,
                        new DefaultAsyncCompletionHandler(handler, asyncProvider)));
    }

    @Override
    public ListenableFuture<AsyncHttpResponse> asyncRequest(HttpRequest request) {
        return asyncRequest(request, createDefHandler());
    }

    @Override
    public void close() throws IOException {
        if (syncProvider != null) {
            syncProvider.close();
        }

        if (asyncProvider != null) {
            asyncProvider.close();
        }
    }
}
