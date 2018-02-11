package com.lxx.mydemo.nettydemo.service.common.util.http.base;

/**
 * @author zhenwei.liu
 * @since 2016-12-20
 */
public class BinaryHttpEntity extends AbstractHttpEntity<byte[]> {

    private static final String MIME_TYPE = "application/octet-stream";
    private final byte[] content;

    public BinaryHttpEntity(byte[] content) {
        super(MIME_TYPE, DEF_CHARSET);
        this.content = content;
    }

    public BinaryHttpEntity(String mimeType, byte[] content) {
        super(mimeType, DEF_CHARSET);
        this.content = content;
    }

    public BinaryHttpEntity(String mimeType, String charset, byte[] content) {
        super(mimeType, charset);
        this.content = content;
    }

    @Override
    public byte[] getContent() {
        return content;
    }
}
