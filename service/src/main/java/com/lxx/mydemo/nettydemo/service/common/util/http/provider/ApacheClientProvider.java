package com.lxx.mydemo.nettydemo.service.common.util.http.provider;

import static com.lxx.mydemo.nettydemo.service.common.util.http.base.Method.GET;
import static com.lxx.mydemo.nettydemo.service.common.util.http.base.Method.POST;

import com.google.common.base.Objects;
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
import com.lxx.mydemo.nettydemo.service.common.util.http.base.ProtocolVersion;
import com.lxx.mydemo.nettydemo.service.common.util.http.base.ProxyServer;
import com.lxx.mydemo.nettydemo.service.common.util.http.base.StringHttpEntity;
import com.lxx.mydemo.nettydemo.service.common.util.http.base.SyncHttpResponse;
import java.io.IOException;
import java.net.URI;
import java.util.Collection;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import org.apache.http.Header;
import org.apache.http.HttpHost;
import org.apache.http.StatusLine;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.config.RequestConfig.Builder;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpEntityEnclosingRequestBase;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.config.Registry;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.conn.socket.ConnectionSocketFactory;
import org.apache.http.conn.socket.PlainConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.entity.ByteArrayEntity;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.entity.mime.content.ByteArrayBody;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.conn.BasicHttpClientConnectionManager;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;

/**
 * @author zhenwei.liu
 * @since 2016-07-18
 */
public class ApacheClientProvider extends AbstractClientProvider<CloseableHttpClient,
        HttpRequestBase, CloseableHttpResponse, SyncHttpResponse, RequestConfig> {

    public ApacheClientProvider(ClientConfig config) {
        super(config);
    }

    @Override
    protected CloseableHttpClient createClient(RequestConfig requestConfig) {
        try {
            HttpClientBuilder httpClientBuilder = HttpClientBuilder.create();
            httpClientBuilder.setDefaultRequestConfig(requestConfig);

            Registry<ConnectionSocketFactory> registry = null;

            ClientConfig config = getClientConfig();
            if (config.getSslContext() != null) {
                SSLConnectionSocketFactory factory = new SSLConnectionSocketFactory(config
                        .getSslContext());
                registry = RegistryBuilder.<ConnectionSocketFactory>create()
                        .register("http", PlainConnectionSocketFactory.getSocketFactory())
                        .register("https", factory)
                        .build();
            }

            if (config.isPoolConnections()) {
                long pooledTimeout = config.getPooledConnectionIdleTimeoutMs();
                TimeUnit timeUnit = TimeUnit.MILLISECONDS;
                PoolingHttpClientConnectionManager connManager = //
                        registry == null ? //
                                new PoolingHttpClientConnectionManager(pooledTimeout, timeUnit) : //
                                new PoolingHttpClientConnectionManager(registry, null, null,
                                        null, pooledTimeout, timeUnit);
                connManager.setMaxTotal(config.getMaxConnections());
                connManager.setDefaultMaxPerRoute(config.getMaxConnectionsPerHost());

                httpClientBuilder.setConnectionManager(connManager);
            } else {
                BasicHttpClientConnectionManager connManger = registry == null ? new
                        BasicHttpClientConnectionManager() : new BasicHttpClientConnectionManager
                        (registry);
                httpClientBuilder.setConnectionManager(connManger);
            }

            // httpClientBuilder.addInterceptorFirst(BMTracerRequestInterceptor.INSTANCE);
            // httpClientBuilder.addInterceptorFirst(BMTracerResponseInterceptor.INSTANCE);
            return httpClientBuilder.build();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public HttpRequestBase createRequest(HttpRequest source) {
        HttpRequestBase request;

        URI uri = source.getURI();
        Method method = source.getMethod();
        switch (method) {
            case GET: {
                request = new HttpGet(uri);
                break;
            }
            case DELETE: {
                request = new HttpDelete(uri);
                break;
            }
            case POST:
            case PUT: {

                if (Objects.equal(method, POST)) {
                    request = new HttpPost(uri);
                } else {
                    request = new HttpPut(uri);
                }

                HttpEntityRequest pr = (HttpEntityRequest) source;
                HttpEntityEnclosingRequestBase entityRequest = (HttpEntityEnclosingRequestBase) request;

                HttpEntity entity = pr.getEntity();
                if (entity != null) {

                    ContentType contentType = adaptContentType(entity);

                    if (BinaryHttpEntity.class.isAssignableFrom(entity.getClass())) {
                        // binary entity

                        BinaryHttpEntity entity1 = (BinaryHttpEntity) entity;
                        byte[] content = entity1.getContent();
                        if (content != null) {
                            entityRequest.setEntity(new ByteArrayEntity(content,
                                    contentType));
                        }

                    } else if (StringHttpEntity.class.isAssignableFrom(entity.getClass())) {

                        // string or form data
                        StringHttpEntity entity1 = (StringHttpEntity) entity;
                        String content = entity1.getContent();
                        if (content != null) {
                            entityRequest.setEntity(new StringEntity(content,
                                    contentType));
                        }

                    } else if (FileHttpEntity.class.isAssignableFrom(entity.getClass())) {

                        // multipart file entity
                        FileHttpEntity entity1 = (FileHttpEntity) entity;

                        Map<String, MultipartData> fileData = entity1.getContent();
                        MultipartEntityBuilder multipartBuilder = MultipartEntityBuilder.create();
                        multipartBuilder.setMode(HttpMultipartMode.BROWSER_COMPATIBLE);
                        multipartBuilder.setContentType(contentType);
                        fileData.forEach((dataKey, multipartData) -> {
                            ContentType multipartContentType = ContentType.create(multipartData
                                    .getMimeType(), multipartData.getCharset());
                            if (multipartData instanceof MultipartFile) {

                                multipartBuilder.addPart(dataKey, new FileBody(((MultipartFile)
                                        multipartData).getData(), multipartContentType));
                            } else if (multipartData instanceof MultipartBinary) {

                                MultipartBinary binaryData = (MultipartBinary) multipartData;
                                multipartBuilder.addPart(dataKey, new ByteArrayBody(binaryData
                                        .getData(), multipartContentType, binaryData.getFilename
                                        ()));
                            }
                        });
                        entityRequest.setEntity(multipartBuilder.build());
                    }
                }
                break;
            }
            default: {
                throw new IllegalArgumentException("不支持的请求类型 " + method.name());
            }
        }

        Map<String, Collection<String>> allHeaders = source.getAllHeaders();
        if (allHeaders != null) {
            for (Map.Entry<String, Collection<String>> entry : allHeaders.entrySet()) {
                String name = entry.getKey();
                for (String val : entry.getValue()) {
                    request.addHeader(name, val);
                }
            }
        }

        if (source.getConfig() != null) {
            request.setConfig(createSyncRequestConfig(source.getConfig()));
        }
        return request;
    }

    private ContentType adaptContentType(HttpEntity entity) {
        return ContentType.create(entity.getMimeType(), entity.getCharset());
    }

    @Override
    public SyncHttpResponse createResponse(CloseableHttpResponse source) {
        StatusLine statusLine = source.getStatusLine();
        try {
            org.apache.http.ProtocolVersion pv = statusLine.getProtocolVersion();
            SyncHttpResponse response = new SyncHttpResponse(source.getEntity()
                    .getContent(), new BasicStatusLine(
                    new ProtocolVersion(pv.getProtocol(), pv.getMajor(), pv.getMinor()), statusLine
                    .getStatusCode(), statusLine.getReasonPhrase()));
            for (Header header : source.getAllHeaders()) {
                response.addHeader(header.getName(), header.getValue());
            }
            return response;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    protected RequestConfig createConfig(ClientConfig source) {
        return RequestConfig.custom()
                .setConnectTimeout(source.getConnectTimeout())
                .setSocketTimeout(source.getRequestTimeout()) // 此处需要注意, httpclient 的
                // socketTimeout 指的是两次数据传输的间隔时间, 而不是请求总时间
                .setRedirectsEnabled(source.isAutoRedirect())
                .setContentCompressionEnabled(source.isAutoCompression())
                .build();
    }

    @Override
    public void close() throws IOException {
        if (client != null) {
            client.close();
        }
    }

    private RequestConfig createSyncRequestConfig(
            com.lxx.mydemo.nettydemo.service.common.util.http.base.RequestConfig source) {
        Builder builder = RequestConfig.custom();
        ProxyServer proxyServer = source.getProxyServer();
        if (proxyServer != null) {
            builder.setProxy(new HttpHost(proxyServer.getHost(), proxyServer.getPort()));
        }
        return builder
                .setConnectTimeout(source.getConnectTimeout())
                .setSocketTimeout(source.getRequestTimeout()) // 此处需要注意, httpclient 的
                // socketTimeout 指的是两次数据传输的间隔时间, 而不是请求总时间
                .build();
    }
}
