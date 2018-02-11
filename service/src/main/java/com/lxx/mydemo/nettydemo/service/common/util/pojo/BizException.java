package com.lxx.mydemo.nettydemo.service.common.util.pojo;

/**
 * @author yong.chen
 * @since 2016-07-01 11:49 AM
 */
public class BizException extends RuntimeException {

    private static final long serialVersionUID = -504955880063536901L;

    public BizException() {
    }

    public BizException(String errorMsg) {
        super(errorMsg);
    }

    public BizException(Throwable cause) {
        super(cause);
    }

    public BizException(Throwable cause, String errorMsg) {
        super(errorMsg, cause);
    }
}
