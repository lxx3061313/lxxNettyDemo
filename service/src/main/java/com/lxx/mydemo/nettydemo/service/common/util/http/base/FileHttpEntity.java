package com.lxx.mydemo.nettydemo.service.common.util.http.base;

import com.google.common.collect.Maps;
import java.util.Map;

/**
 * @author zhenwei.liu
 * @since 2016-12-20
 */
public class FileHttpEntity extends AbstractHttpEntity<Map<String, MultipartData>> {

    private static final String MIME_TYPE = "application/octet-stream";

    private final Map<String, MultipartData> fileMap = Maps.newHashMap();

    public FileHttpEntity() {
        super(MIME_TYPE, DEF_CHARSET);
    }

    public FileHttpEntity(String mimeType, String charset) {
        super(mimeType, charset);
    }

    public void addFilePart(String name, MultipartData file) {
        fileMap.put(name, file);
    }

    @Override
    public Map<String, MultipartData> getContent() {
        return fileMap;
    }
}
