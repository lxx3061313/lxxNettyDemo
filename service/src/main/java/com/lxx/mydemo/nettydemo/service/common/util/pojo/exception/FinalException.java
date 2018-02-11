package com.lxx.mydemo.nettydemo.service.common.util.pojo.exception;


import com.lxx.mydemo.nettydemo.service.common.util.pojo.CodeMessage;

/**
 * @author miao.yang susing@gmail.com
 * @date 14-3-21.
 */
@SuppressWarnings("serial")
public class FinalException extends RuntimeException implements CodeMessage {

    private final int code;

    private Object data;

    public FinalException(int code) {
        this.code = code;
    }

    public FinalException(int code, String message) {
        super(message);
        this.code = code;
    }

    public FinalException(int code, String message, Throwable cause) {
        super(message, cause);
        this.code = code;
    }

    public FinalException(int code, Throwable cause) {
        super(cause);
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
}
