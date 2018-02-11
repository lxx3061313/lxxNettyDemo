package com.lxx.mydemo.nettydemo.service.common.util.http.base;

/**
 * @author zhenwei.liu
 * @since 2016-12-22
 */
public class MultipartBinary extends MultipartData<byte[]> {

    private String filename;

    public MultipartBinary(byte[] data, String filename) {
        super(data);
        this.filename = filename;
    }

    public MultipartBinary(byte[] data, String mimeType, String filename) {
        this(data, mimeType);
        this.filename = filename;
    }

    public MultipartBinary(byte[] data, String mimeType, String charset, String filename) {
        super(data, mimeType, charset);
        this.filename = filename;
    }

    public String getFilename() {
        return filename;
    }
}
