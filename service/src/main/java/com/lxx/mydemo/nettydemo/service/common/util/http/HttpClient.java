package com.lxx.mydemo.nettydemo.service.common.util.http;

import com.google.common.util.concurrent.ListenableFuture;
import com.lxx.mydemo.nettydemo.service.common.util.http.base.AsyncHandler;
import com.lxx.mydemo.nettydemo.service.common.util.http.base.AsyncHttpResponse;
import com.lxx.mydemo.nettydemo.service.common.util.http.base.HttpRequest;
import com.lxx.mydemo.nettydemo.service.common.util.http.base.SyncHttpResponse;
import java.io.Closeable;
import java.io.IOException;
import java.util.Map;

public interface HttpClient extends Closeable {

    SyncHttpResponse syncGet(String url) throws IOException;

    SyncHttpResponse syncPost(String url, Map<String, String> formParam) throws IOException;

    SyncHttpResponse syncPost(String url, String bodyData) throws IOException;

    SyncHttpResponse syncPost(String url, byte[] bodyData) throws IOException;

    SyncHttpResponse syncRequest(HttpRequest request) throws IOException;

    // async

    ListenableFuture<AsyncHttpResponse> asyncGet(String url);

    ListenableFuture<AsyncHttpResponse> asyncGet(String url, AsyncHandler handler);

    ListenableFuture<AsyncHttpResponse> asyncPost(String url, Map<String, String> formParam);

    ListenableFuture<AsyncHttpResponse> asyncPost(String url, Map<String, String> formParam,
            AsyncHandler handler);

    ListenableFuture<AsyncHttpResponse> asyncPost(String url, String bodyData);

    ListenableFuture<AsyncHttpResponse> asyncPost(String url, String bodyData, AsyncHandler
            handler);

    ListenableFuture<AsyncHttpResponse> asyncPost(String url, byte[] bodyData);

    ListenableFuture<AsyncHttpResponse> asyncPost(String url, byte[] bodyData, AsyncHandler
            handler);

    ListenableFuture<AsyncHttpResponse> asyncRequest(HttpRequest request);

    ListenableFuture<AsyncHttpResponse> asyncRequest(HttpRequest request, AsyncHandler handler);
}
