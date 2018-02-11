package com.lxx.mydemo.nettydemo.service.common.util.http.provider;

import com.ning.http.client.AsyncHttpClient;
import com.ning.http.client.AsyncHttpClientConfig;
import com.ning.http.client.Response;
import com.ning.http.client.multipart.ByteArrayPart;
import com.ning.http.client.multipart.FilePart;
import com.ning.http.client.uri.Uri;
import com.lxx.mydemo.nettydemo.service.common.util.http.base.AsyncHttpResponse;
import com.lxx.mydemo.nettydemo.service.common.util.http.base.BasicStatusLine;
import com.lxx.mydemo.nettydemo.service.common.util.http.base.BinaryHttpEntity;
import com.lxx.mydemo.nettydemo.service.common.util.http.base.ClientConfig;
import com.lxx.mydemo.nettydemo.service.common.util.http.base.FileHttpEntity;
import com.lxx.mydemo.nettydemo.service.common.util.http.base.HttpEntity;
import com.lxx.mydemo.nettydemo.service.common.util.http.base.HttpEntityRequest;
import com.lxx.mydemo.nettydemo.service.common.util.http.base.HttpRequest;
import com.lxx.mydemo.nettydemo.service.common.util.http.base.Method;
import com.lxx.mydemo.nettydemo.service.common.util.http.base.MultipartBinary;
import com.lxx.mydemo.nettydemo.service.common.util.http.base.MultipartData;
import com.lxx.mydemo.nettydemo.service.common.util.http.base.MultipartFile;
import com.lxx.mydemo.nettydemo.service.common.util.http.base.PoolKeyStrategy;
import com.lxx.mydemo.nettydemo.service.common.util.http.base.ProtocolVersion;
import com.lxx.mydemo.nettydemo.service.common.util.http.base.ProxyServer;
import com.lxx.mydemo.nettydemo.service.common.util.http.base.RequestConfig;
import com.lxx.mydemo.nettydemo.service.common.util.http.base.StatusLine;
import com.lxx.mydemo.nettydemo.service.common.util.http.base.StringHttpEntity;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.nio.charset.Charset;
import java.util.Map;

/**
 * @author zhenwei.liu
 * @since 2016-07-18
 */
public class AsyncClientProvider extends AbstractClientProvider<AsyncHttpClient, com.ning.http
        .client.Request, Response, AsyncHttpResponse, AsyncHttpClientConfig> {

    public AsyncClientProvider(ClientConfig config) {
        super(config);
    }

    @Override
    protected AsyncHttpClient createClient(AsyncHttpClientConfig asyncHttpClientConfig) {
        return new AsyncHttpClient(asyncHttpClientConfig);
    }

    @Override
    protected AsyncHttpClientConfig createConfig(ClientConfig source) {
        AsyncHttpClientConfig.Builder builder = new AsyncHttpClientConfig.Builder();
        builder.setConnectTimeout(source.getConnectTimeout());
        builder.setRequestTimeout(source.getRequestTimeout());
        builder.setFollowRedirect(source.isAutoRedirect());
        if (source.isPoolConnections()) {
            builder.setAllowPoolingConnections(true);
            builder.setAllowPoolingSslConnections(true);
            builder.setPooledConnectionIdleTimeout((int) source.getPooledConnectionIdleTimeoutMs());
        }
        builder.setMaxConnections(source.getMaxConnections());
        builder.setMaxConnectionsPerHost(source.getMaxConnectionsPerHost());
        if (source.getSslContext() != null) {
            builder.setSSLContext(source.getSslContext());
        }
        return builder.build();
    }

    @Override
    public com.ning.http.client.Request createRequest(HttpRequest source) {
        Method method = source.getMethod();
        final com.ning.http.client.RequestBuilder requestBuilder = new com.ning.http.client
                .RequestBuilder(method.name());

        if (HttpEntityRequest.class.isAssignableFrom(source.getClass())) {
            HttpEntityRequest pr = (HttpEntityRequest) source;

            HttpEntity entity = pr.getEntity();
            if (entity != null) {

                if (BinaryHttpEntity.class.isAssignableFrom(entity.getClass())) {
                    // binary entity
                    BinaryHttpEntity entity1 = (BinaryHttpEntity) entity;
                    byte[] content = entity1.getContent();
                    if (content != null) {
                        requestBuilder.setBody(content);
                    }

                } else if (StringHttpEntity.class.isAssignableFrom(entity.getClass())) {

                    // string or form data
                    StringHttpEntity entity1 = (StringHttpEntity) entity;
                    String content = entity1.getContent();
                    if (content != null) {
                        try {
                            requestBuilder.setBody(content.getBytes(entity1.getCharset()));
                        } catch (UnsupportedEncodingException e) {
                            // never
                            throw new RuntimeException(e);
                        }
                    }

                } else if (FileHttpEntity.class.isAssignableFrom(entity.getClass())) {

                    // multipart file entity
                    FileHttpEntity entity1 = (FileHttpEntity) entity;

                    Map<String, MultipartData> fileData = entity1.getContent();
                    fileData.forEach((dataKey, multipartData) -> {
                        if (multipartData instanceof MultipartFile) {
                            MultipartFile multipartFile = (MultipartFile) multipartData;
                            requestBuilder.addBodyPart(new FilePart(dataKey, multipartFile
                                    .getData(), multipartFile.getMimeType(), Charset.forName
                                    (multipartFile.getCharset())));

                        } else if (multipartData instanceof MultipartBinary) {

                            MultipartBinary multipartBinary = (MultipartBinary) multipartData;
                            requestBuilder.addBodyPart(new ByteArrayPart(dataKey, multipartBinary
                                    .getData(), multipartBinary.getMimeType(), Charset.forName
                                    (multipartBinary.getCharset()), multipartBinary.getFilename()));

                        }
                    });
                }
            }

        }

        requestBuilder.setUrl(source.getURI().toString());
        source.getAllHeaders().forEach((name, list) -> list.forEach(val -> requestBuilder
                .addHeader(name, val)));

        PoolKeyStrategy poolKeyStrategy = getClientConfig().getPoolKeyStrategy();
        if (poolKeyStrategy != null) {
            requestBuilder.setConnectionPoolKeyStrategy(
                    (uri, ps) -> {
                        ProxyServer proxyServer = ps != null ? new ProxyServer(ps.getHost(), ps
                                .getPort()) : null;
                        return poolKeyStrategy.createPoolKey(URI.create(uri.toUrl()), proxyServer);
                    });
        }

        RequestConfig config = source.getConfig();
        if (config != null) {
            requestBuilder.setRequestTimeout(config.getRequestTimeout());
            ProxyServer proxyServer = config.getProxyServer();
            if (proxyServer != null) {
                requestBuilder.setProxyServer(new com.ning.http.client.ProxyServer(
                        proxyServer.getHost(), proxyServer.getPort()
                ));
            }
        }
        return requestBuilder.build();
    }

    @Override
    public AsyncHttpResponse createResponse(Response source) {
        try {
            Uri uri = source.getUri();
            // todo http 协议版本号拿不到, 写死 1.1
            StatusLine status = new BasicStatusLine(new ProtocolVersion(uri.getScheme(), 1, 1),
                    source.getStatusCode(), source.getStatusText());
            AsyncHttpResponse response = new AsyncHttpResponse(status, source
                    .getResponseBodyAsBytes());
            source.getHeaders().forEach((name, list) -> list.forEach((val) -> response
                    .addHeader(name, val)));
            return response;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void close() throws IOException {
        if (client != null) {
            client.close();
        }
    }
}
