package com.lxx.mydemo.nettydemo.service.common.util.http.base;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.apache.http.NameValuePair;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.message.BasicNameValuePair;

/**
 * @author zhenwei.liu
 * @since 2016-12-20
 */
public class UrlEncodedFormHttpEntity extends StringHttpEntity {

    private static final String MIME_TYPE = "application/x-www-form-urlencoded";

    public UrlEncodedFormHttpEntity(Map<String, String> formContent, String charset) {
        super(URLEncodedUtils.format(toNameValuePairList(formContent), charset));
        setMimeType(MIME_TYPE);
    }

    private static List<NameValuePair> toNameValuePairList(Map<String, String> formContent) {
        return formContent.entrySet().stream().map(e -> new BasicNameValuePair(e.getKey(), e
                .getValue())).collect(Collectors.toList());
    }
}
