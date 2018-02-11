package com.lxx.mydemo.nettydemo.service.common.util.http.base.builder;

import com.lxx.mydemo.nettydemo.service.common.util.http.base.HttpEntity;
import com.lxx.mydemo.nettydemo.service.common.util.http.base.PostRequest;
import java.net.URI;

/**
 * @author zhenwei.liu
 * @since 2016-12-20
 */
public class PostRequestBuilder extends EntityRequestBuilder<PostRequest, PostRequestBuilder> {

    protected PostRequestBuilder() {
        super(PostRequestBuilder.class);
    }

    @Override
    PostRequest buildEntityRequest(URI uri, HttpEntity entity) {
        return new PostRequest(uri, entity);
    }
}
