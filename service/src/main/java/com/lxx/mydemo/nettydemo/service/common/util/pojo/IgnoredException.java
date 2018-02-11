package com.lxx.mydemo.nettydemo.service.common.util.pojo;

/**
 * @author jianghao
 * @since 2016-12-09
 */
public class IgnoredException extends RuntimeException {

    private static final long serialVersionUID = -8937478710852564999L;

    public IgnoredException() {
    }

    public IgnoredException(String errorMsg) {
        super(errorMsg);
    }

    public IgnoredException(Throwable cause) {
        super(cause);
    }

    public IgnoredException(Throwable cause, String errorMsg) {
        super(errorMsg, cause);
    }
}
