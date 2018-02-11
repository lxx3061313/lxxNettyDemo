package com.lxx.mydemo.nettydemo.service.common.util.pojo;

/**
 * @author miao.yang susing@gmail.com
 * @date 14-3-21.
 */
public interface CodeMessage {

    public static final int OK = 0;
    public static final int SYSTEM_ERROR = -1;

    int getStatus();

    String getMessage();

    Object getData();

}
