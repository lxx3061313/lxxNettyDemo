package com.lxx.mydemo.nettydemo.service.common.util.http.base.builder;

import com.lxx.mydemo.nettydemo.service.common.util.http.base.GetRequest;
import java.net.URI;

/**
 * @author zhenwei.liu
 * @since 2016-06-29
 */
public class GetRequestBuilder extends RequestBuilder<GetRequest, GetRequestBuilder> {

    GetRequestBuilder() {
        super(GetRequestBuilder.class);
    }

    @Override
    GetRequest doBuild(URI uri) {
        return new GetRequest(uri);
    }
}
