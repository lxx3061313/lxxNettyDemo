package com.lxx.mydemo.nettydemo.service.common.util.http.base;

public abstract class AsyncHandlerBase implements AsyncHandler {

    /**
     * 如果你不需要处理异步数据, 可以使用该 handler
     */
    public static final AsyncHandlerBase EMPTY_HANDLER = new AsyncHandlerBase() {

        @Override
        public boolean onBodyContentReceived(byte[] content) {
            return false;
        }

        @Override
        public void onRequestCompleted(AsyncHttpResponse response) {

        }

        @Override
        public void onThrowable(Throwable t) {

        }
    };

    @Override
    public void onStatusReceived(StatusLine responseStatus) {

    }

    public boolean onBodyContentReceived(byte[] content) {
        return true;
    }
}
