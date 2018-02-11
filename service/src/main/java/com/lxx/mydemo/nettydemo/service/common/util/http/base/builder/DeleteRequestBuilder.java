package com.lxx.mydemo.nettydemo.service.common.util.http.base.builder;

import com.lxx.mydemo.nettydemo.service.common.util.http.base.DeleteRequest;
import java.net.URI;

/**
 * @author zhenwei.liu
 * @since 2016-06-29
 */
public class DeleteRequestBuilder extends RequestBuilder<DeleteRequest, DeleteRequestBuilder> {

    DeleteRequestBuilder() {
        super(DeleteRequestBuilder.class);
    }

    @Override
    DeleteRequest doBuild(URI uri) {
        return new DeleteRequest(uri);
    }
}
