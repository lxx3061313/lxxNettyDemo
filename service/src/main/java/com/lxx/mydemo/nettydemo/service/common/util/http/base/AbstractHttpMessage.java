package com.lxx.mydemo.nettydemo.service.common.util.http.base;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;
import java.util.Collection;
import java.util.Map;
import org.apache.commons.collections.CollectionUtils;

/**
 * @author zhenwei.liu
 * @since 2016-12-20
 */
public abstract class AbstractHttpMessage implements HttpMessage {

    private Multimap<String, String> headers = ArrayListMultimap.create();

    @Override
    public String getFirstHeader(String name) {
        Collection<String> col = headers.get(name);
        return CollectionUtils.isNotEmpty(col) ? col.iterator().next() : null;
    }

    @Override
    public Collection<String> getHeader(String name) {
        return headers.get(name);
    }

    @Override
    public Map<String, Collection<String>> getAllHeaders() {
        return headers.asMap();
    }

    @Override
    public void addHeader(String name, String val) {
        headers.put(name, val);
    }

    @Override
    public void setHeader(String name, String val) {
        headers.removeAll(name);
        headers.put(name, val);
    }

    @Override
    public void removeHeader(String name) {
        headers.removeAll(name);
    }
}
