package com.lxx.mydemo.nettydemo.service.common.util.http.base;

import com.google.common.base.Charsets;
import com.google.common.net.HttpHeaders;
import java.nio.charset.Charset;
import org.apache.http.entity.ContentType;

public class AsyncHttpResponse extends AbstractHttpMessage implements HttpResponse {

    private static final Charset DEF_CHARSET = Charsets.UTF_8;

    private StatusLine statusLine;
    private byte[] byteBody;

    public AsyncHttpResponse(StatusLine statusLine, byte[] byteBody) {
        this.statusLine = statusLine;
        this.byteBody = byteBody;
    }

    @Override
    public StatusLine getStatusLine() {
        return statusLine;
    }

    public String getBodyAsString() {
        String contentType = getFirstHeader(HttpHeaders.CONTENT_TYPE);
        Charset c = DEF_CHARSET;
        if (contentType != null) {
            try {
                Charset charset = ContentType.parse(contentType).getCharset();
                if (charset != null) c = charset;
            } catch (Exception e) {
            }
        }
        return new String(getBodyAsBytes(), c);
    }

    public byte[] getBodyAsBytes() {
        return byteBody;
    }

    @Override
    public ProtocolVersion getProtocolVersion() {
        return null;
    }
}
