package com.lxx.mydemo.nettydemo.service.common.util.http.base;

import java.io.File;

/**
 * @author zhenwei.liu
 * @since 2016-12-22
 */
public class MultipartFile extends MultipartData<File> {

    public MultipartFile(File data) {
        super(data);
    }

    public MultipartFile(File data, String mimeType) {
        super(data, mimeType);
    }

    public MultipartFile(File data, String mimeType, String charset) {
        super(data, mimeType, charset);
    }
}
