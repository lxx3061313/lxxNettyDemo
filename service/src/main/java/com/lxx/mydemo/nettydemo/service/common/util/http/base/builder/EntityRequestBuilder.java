package com.lxx.mydemo.nettydemo.service.common.util.http.base.builder;

import com.google.common.base.Charsets;
import com.lxx.mydemo.nettydemo.service.common.util.http.base.BinaryHttpEntity;
import com.lxx.mydemo.nettydemo.service.common.util.http.base.FileHttpEntity;
import com.lxx.mydemo.nettydemo.service.common.util.http.base.HttpEntity;
import com.lxx.mydemo.nettydemo.service.common.util.http.base.HttpEntityRequest;
import com.lxx.mydemo.nettydemo.service.common.util.http.base.MultipartData;
import com.lxx.mydemo.nettydemo.service.common.util.http.base.StringHttpEntity;
import com.lxx.mydemo.nettydemo.service.common.util.http.base.UrlEncodedFormHttpEntity;
import java.net.URI;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.Map;

/**
 * @author zhenwei.liu
 * @since 2016-06-29
 */
public abstract class EntityRequestBuilder<R extends HttpEntityRequest, T extends
        EntityRequestBuilder> extends RequestBuilder<R, T> {

    private Map<String, MultipartData> fileData;
    private byte[] body;
    private String bodyString;
    private Map<String, String> formData;
    private static final Charset DEF_CHARSET = Charsets.UTF_8;

    protected EntityRequestBuilder(Class<T> derived) {
        super(derived);
    }

    public T setBody(byte[] body) {
        this.resetNonBodyData();
        this.body = body;
        return derived.cast(this);
    }

    public T setBody(String body) {
        this.resetNonBodyData();
        this.bodyString = body;
        return derived.cast(this);
    }

    public T setBody(String body, Charset charset) {
        this.resetNonBodyData();
        this.body = body.getBytes(charset);
        return derived.cast(this);
    }

    public T addFormData(String key, String val) {
        this.resetNonFormData();
        if (formData == null) {
            formData = new HashMap<>();
        }
        formData.put(key, val);
        return derived.cast(this);
    }

    public T addFormData(Map<String, String> formData) {
        this.resetNonFormData();
        if (this.formData == null) {
            this.formData = new HashMap<>();
        }
        this.formData.putAll(formData);
        return derived.cast(this);
    }

    /**
     * 用作文件上传
     */
    public T addFileData(String key, MultipartData file) {
        this.resetNonFileData();
        if (fileData == null) {
            fileData = new HashMap<>();
        }
        fileData.put(key, file);
        return derived.cast(this);
    }

    public void resetNonBodyData() {
        this.formData = null;
        this.fileData = null;
    }

    public void resetNonFormData() {
        this.body = null;
        this.fileData = null;
    }

    public void resetNonFileData() {
        this.formData = null;
        this.body = null;
    }

    @Override
    R doBuild(URI uri) {

        HttpEntity entity = null;

        if (body != null) {
            entity = new BinaryHttpEntity(body);
        } else if (bodyString != null) {
            entity = new StringHttpEntity(bodyString);
        } else if (fileData != null && fileData.size() > 0) {
            entity = new FileHttpEntity();
            for (Map.Entry<String, MultipartData> entry : fileData.entrySet()) {
                ((FileHttpEntity) entity).addFilePart(entry.getKey(), entry.getValue());
            }
        } else if (formData != null && formData.size() > 0) {
            entity = new UrlEncodedFormHttpEntity(formData, DEF_CHARSET.name());
        }

        return buildEntityRequest(uri, entity);
    }

    abstract R buildEntityRequest(URI uri, HttpEntity entity);
}
