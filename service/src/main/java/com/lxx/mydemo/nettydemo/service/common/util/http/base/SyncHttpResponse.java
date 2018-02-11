package com.lxx.mydemo.nettydemo.service.common.util.http.base;

import com.google.common.base.Charsets;
import com.google.common.net.HttpHeaders;
import java.io.Closeable;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import org.apache.http.entity.ContentType;
import sun.misc.IOUtils;

public class SyncHttpResponse extends AbstractHttpMessage implements HttpResponse, Closeable {

    private static final Charset DEF_CHARSET = Charsets.UTF_8;

    private StatusLine statusLine;
    private InputStream inputStream;
    private byte[] byteBody;

    public SyncHttpResponse(InputStream inputStream, StatusLine statusLine) {
        this.inputStream = inputStream;
        this.statusLine = statusLine;
    }

    @Override
    public StatusLine getStatusLine() {
        return statusLine;
    }

    @Override
    public String getBodyAsString() throws IOException {
        Charset c = DEF_CHARSET;
        String contentType = getFirstHeader(HttpHeaders.CONTENT_TYPE);
        if (contentType != null) {
            try {
                Charset charset = ContentType.parse(contentType).getCharset();
                if (charset != null) c = charset;
            } catch (Exception e) {
            }
        }
        return new String(getBodyAsBytes(), c);
    }

    @Override
    public byte[] getBodyAsBytes() throws IOException {
        try {
            if (byteBody == null) {
                byteBody = IOUtils.readFully(inputStream, -1, true);
            }
            return byteBody;
        } finally {
            close();
        }
    }

    public InputStream getBodyStream() throws IOException {
        return inputStream;
    }

    public void close() throws IOException {
        inputStream.close();
    }

    @Override
    public ProtocolVersion getProtocolVersion() {
        return statusLine.getProtocolVersion();
    }
}
