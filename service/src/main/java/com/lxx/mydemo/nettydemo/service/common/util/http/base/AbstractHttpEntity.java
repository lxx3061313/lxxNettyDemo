package com.lxx.mydemo.nettydemo.service.common.util.http.base;

import com.google.common.base.Charsets;

/**
 * @author zhenwei.liu
 * @since 2016-12-20
 */
public abstract class AbstractHttpEntity<T> implements HttpEntity<T> {

    protected static final String DEF_CHARSET = Charsets.UTF_8.name();

    private String mimeType;
    private String charset;
    private String contentEncoding;

    public AbstractHttpEntity(String mimeType, String charset) {
        this.mimeType = mimeType;
        this.charset = charset;
    }

    public void setMimeType(String mimeType) {
        this.mimeType = mimeType;
    }

    public void setCharset(String charset) {
        this.charset = charset;
    }

    public void setContentEncoding(String contentEncoding) {
        this.contentEncoding = contentEncoding;
    }

    @Override
    public String getMimeType() {
        return mimeType;
    }

    @Override
    public String getCharset() {
        return charset;
    }

    @Override
    public String getContentEncoding() {
        return contentEncoding;
    }
}
