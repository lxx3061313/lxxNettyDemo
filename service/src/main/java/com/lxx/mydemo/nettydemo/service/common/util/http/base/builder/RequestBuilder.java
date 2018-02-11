package com.lxx.mydemo.nettydemo.service.common.util.http.base.builder;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;
import com.lxx.mydemo.nettydemo.service.common.util.http.base.BasicHttpRequest;
import com.lxx.mydemo.nettydemo.service.common.util.http.base.RequestConfig;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import org.apache.http.client.utils.URIBuilder;

public abstract class RequestBuilder<R extends BasicHttpRequest, T extends RequestBuilder> {

    protected Class<T> derived;
    protected URI uri;
    protected Multimap<String, String> headers;
    protected Map<String, String> urlParams;
    protected RequestConfig config;

    protected RequestBuilder(Class<T> derived) {
        this.derived = derived;
    }

    public static GetRequestBuilder createGet(String uri) {
        return createGet(URI.create(uri));
    }

    public static GetRequestBuilder createGet(URI uri) {
        GetRequestBuilder builder = new GetRequestBuilder();
        builder.uri = uri;
        return builder;
    }

    public static DeleteRequestBuilder createDelete(String uri) {
        return createDelete(URI.create(uri));
    }

    public static DeleteRequestBuilder createDelete(URI uri) {
        DeleteRequestBuilder builder = new DeleteRequestBuilder();
        builder.uri = uri;
        return builder;
    }

    public static PostRequestBuilder createPost(String uri) {
        return createPost(URI.create(uri));
    }

    public static PostRequestBuilder createPost(URI uri) {
        PostRequestBuilder builder = new PostRequestBuilder();
        builder.uri = uri;
        return builder;
    }

    public static PutRequestBuilder createPut(String uri) {
        return createPut(URI.create(uri));
    }

    public static PutRequestBuilder createPut(URI uri) {
        PutRequestBuilder builder = new PutRequestBuilder();
        builder.uri = uri;
        return builder;
    }

    public T addHeader(String key, String val) {
        if (headers == null) {
            headers = ArrayListMultimap.create();
        }
        headers.put(key, val);
        return derived.cast(this);
    }

    public T setRequestConfig(RequestConfig config) {
        this.config = config;
        return derived.cast(this);
    }

    public T addUrlParam(String key, String value) {
        if (urlParams == null) {
            urlParams = new HashMap<>();
        }
        urlParams.put(key, value);
        return derived.cast(this);
    }

    public T clearUrlParam() {
        this.urlParams = null;
        return derived.cast(this);
    }

    public R build() {

        URIBuilder uriBuilder = new URIBuilder(uri);

        // params
        if (urlParams != null) {
            urlParams.forEach(uriBuilder::addParameter);
        }

        R request;
        try {
            request = doBuild(uriBuilder.build());
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }

        // config
        request.setConfig(config);

        // headers
        if (headers != null) {
            for (String name : headers.keySet()) {
                Collection<String> vals = headers.get(name);
                for (String val : vals) {
                    request.addHeader(name, val);
                }
            }
        }

        return request;
    }

    abstract R doBuild(URI uri);
}
