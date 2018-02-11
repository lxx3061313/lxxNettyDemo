package com.lxx.mydemo.nettydemo.service.common.util.http.support.async;


import com.ning.http.client.AsyncCompletionHandler;
import com.ning.http.client.HttpResponseBodyPart;
import com.ning.http.client.HttpResponseHeaders;
import com.ning.http.client.HttpResponseStatus;
import com.lxx.mydemo.nettydemo.service.common.util.http.base.AsyncHandler;
import com.lxx.mydemo.nettydemo.service.common.util.http.base.AsyncHttpResponse;
import com.lxx.mydemo.nettydemo.service.common.util.http.base.BasicStatusLine;
import com.lxx.mydemo.nettydemo.service.common.util.http.base.ProtocolVersion;
import com.lxx.mydemo.nettydemo.service.common.util.http.provider.AsyncClientProvider;

/**
 * @author zhenwei.liu
 * @since 2016-08-03
 */
public class DefaultAsyncCompletionHandler extends AsyncCompletionHandler<AsyncHttpResponse> {

    private AsyncHandler handler;
    private AsyncClientProvider provider;

    public DefaultAsyncCompletionHandler(AsyncHandler handler, AsyncClientProvider provider) {
        this.handler = handler;
        this.provider = provider;
    }

    @Override
    public STATE onStatusReceived(HttpResponseStatus status) throws Exception {
        int code = status.getStatusCode();
        String text = status.getStatusText();
        handler.onStatusReceived(
                new BasicStatusLine(new ProtocolVersion(status.getProtocolName()
                        , status.getProtocolMajorVersion(), status.getProtocolMinorVersion()),
                        code, text));
        return super.onStatusReceived(status);
    }

    @Override
    public STATE onHeadersReceived(HttpResponseHeaders headers) throws Exception {
        return super.onHeadersReceived(headers);
    }

    @Override
    public STATE onBodyPartReceived(HttpResponseBodyPart content) throws Exception {
        boolean accumulate = handler.onBodyContentReceived(content.getBodyPartBytes());
        if (accumulate) {
            return super.onBodyPartReceived(content);
        } else {
            return STATE.CONTINUE;
        }
    }

    @Override
    public AsyncHttpResponse onCompleted(com.ning.http.client.Response response) throws Exception {
        AsyncHttpResponse r = provider.createResponse(response);
        handler.onRequestCompleted(r);
        return r;
    }

    @Override
    public void onThrowable(Throwable t) {
        handler.onThrowable(t);
    }
}
