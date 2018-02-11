package com.lxx.mydemo.nettydemo.service.common.util.pojo.exception;


import com.lxx.mydemo.nettydemo.service.common.util.pojo.CodeMessage;

/**
 * 这个类仅作为消息传递, 不携带stack
 *
 * @author miao.yang susing@gmail.com
 * @since 14-6-23.
 */
public class RuntimeErrorMessage extends RuntimeException implements CodeMessage {

    private final int code;
    private Object data;

    public RuntimeErrorMessage(int code) {
        this.code = code;
    }

    public RuntimeErrorMessage(int code, String message) {
        super(message);
        this.code = code;
    }


    @Override
    public int getStatus() {
        return code;
    }

    @Override
    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }

    @Override
    public Throwable initCause(Throwable cause) {
        return this;
    }

    @Override
    public Throwable fillInStackTrace() {
        return this;
    }
}
