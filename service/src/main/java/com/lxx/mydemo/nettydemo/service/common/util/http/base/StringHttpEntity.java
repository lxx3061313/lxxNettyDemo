package com.lxx.mydemo.nettydemo.service.common.util.http.base;

/**
 * @author zhenwei.liu
 * @since 2016-12-20
 */
public class StringHttpEntity extends AbstractHttpEntity<String> {

    private static final String MIME_TYPE = "text/html";

    private final String content;

    public StringHttpEntity(String content) {
        super(MIME_TYPE, DEF_CHARSET);
        this.content = content;
    }

    public StringHttpEntity(String mimeType, String charset, String content) {
        super(mimeType, charset);
        this.content = content;
    }

    @Override
    public String getContent() {
        return content;
    }
}
