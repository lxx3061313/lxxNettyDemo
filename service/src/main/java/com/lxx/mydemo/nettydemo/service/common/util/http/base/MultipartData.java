package com.lxx.mydemo.nettydemo.service.common.util.http.base;

import com.google.common.base.Charsets;

/**
 * @author zhenwei.liu
 * @since 2016-11-30
 */
public class MultipartData<T> {

    private static final String DEF_CHARSET = Charsets.UTF_8.name();
    private static final String DEF_MIME_TYPE = "application/octet-stream";

    private T data;
    private String mimeType;
    private String charset;

    public MultipartData(T data) {
        this(data, DEF_MIME_TYPE, DEF_CHARSET);
    }

    public MultipartData(T data, String mimeType) {
        this(data, mimeType, DEF_CHARSET);
    }

    public MultipartData(T data, String mimeType, String charset) {
        this.data = data;
        this.mimeType = mimeType;
        this.charset = charset;
    }

    public T getData() {
        return data;
    }

    public MultipartData setData(T data) {
        this.data = data;
        return this;
    }

    public String getMimeType() {
        return mimeType;
    }

    public MultipartData setMimeType(String mimeType) {
        this.mimeType = mimeType;
        return this;
    }

    public String getCharset() {
        return charset;
    }

    public MultipartData setCharset(String charset) {
        this.charset = charset;
        return this;
    }
}
