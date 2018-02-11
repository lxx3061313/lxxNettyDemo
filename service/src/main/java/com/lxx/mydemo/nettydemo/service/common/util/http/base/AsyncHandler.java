package com.lxx.mydemo.nettydemo.service.common.util.http.base;

public interface AsyncHandler {

    void onStatusReceived(StatusLine status);

    /**
     * 处理收到的 body 内容
     *
     * @param content 收到的 body 内容
     * @return 是否需要累积 body 内容到 response 中，返回 false 则不累积数据， response 数据会为空
     */
    boolean onBodyContentReceived(byte[] content);

    void onRequestCompleted(AsyncHttpResponse response);

    void onThrowable(Throwable t);
}
