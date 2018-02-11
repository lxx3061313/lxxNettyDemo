package com.lxx.mydemo.nettydemo.service.common.web.filter;


import com.google.common.base.Joiner;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.lxx.mydemo.nettydemo.service.common.util.UriMatcher;
import com.lxx.mydemo.nettydemo.service.common.util.cache.CachedHttpServletRequestWrapper;
import com.lxx.mydemo.nettydemo.service.common.util.cache.CachedHttpServletResponseWrapper;
import java.io.IOException;
import java.net.URLDecoder;
import java.util.List;
import java.util.Map;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.filter.OncePerRequestFilter;

/**
 * 记录访问数据的过滤器
 * 
 * 先定义一个Logger,该过滤器的日志将会输出到该logger上{@see loggerName}. <br/>
 * 你可以指定哪些URL不进行处理{@see excludeUri},以及在记录日志时对请求与响应的参数做特殊化处理{@see mosaicHandlers}. <br/>
 * 最后将该filter配置到Spring.
 * 
 */
@Service
public class AccessDataLogFilter extends OncePerRequestFilter {
    private static final int MAX_CACHE_LEN = 2 * 1024 * 1024;
    private static final int INIT_CACHE_LEN = 512 * 1024;
    private static final String DEFAULT_LOGGER_NAME = "accessDataLog";
    private String loggerName;
    private String excludeUri;
    private Map<String, MosaicHandler> mosaicHandlers = Maps.newHashMap();
    private static final Logger LOG = LoggerFactory.getLogger(AccessDataLogFilter.class);

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        this.setMCodeQuietly(request);

        if (UriMatcher.match(excludeUri, request.getRequestURI())) {
            filterChain.doFilter(request, response);
            return;
        }
        long start = System.currentTimeMillis();
        CachedHttpServletRequestWrapper httpServletRequestWrapper = new CachedHttpServletRequestWrapper(request,
                INIT_CACHE_LEN, MAX_CACHE_LEN);
        CachedHttpServletResponseWrapper httpServletResponseWrapper = new CachedHttpServletResponseWrapper(response,
                INIT_CACHE_LEN, MAX_CACHE_LEN);
        try {
            filterChain.doFilter(httpServletRequestWrapper, httpServletResponseWrapper);
        } finally {
            long end = System.currentTimeMillis();
            saveLogData(request, httpServletRequestWrapper, httpServletResponseWrapper, end - start);
        }
    }

    private void saveLogData(HttpServletRequest request, CachedHttpServletRequestWrapper httpServletRequestWrapper,
            CachedHttpServletResponseWrapper httpServletResponseWrapper, long time) {
        try {

            // 如果使用了Writer就需要刷新流
            httpServletRequestWrapper.flushStream();
            httpServletResponseWrapper.flushStream();

            byte[] requestData = httpServletRequestWrapper.getCachedStream().getCached();
            byte[] responseData = httpServletResponseWrapper.getCachedStream().getCached();

            String requestString = requestData == null ? StringUtils.EMPTY : new String(requestData);
            String responseString = responseData == null ? StringUtils.EMPTY : new String(responseData);
            String uri = request.getRequestURI();

            // 把关键数据马赛克化，避免敏感数据泄漏，暂不支持模糊匹配
            Map params = request.getParameterMap(); // 注意这里返回的map不能更改，所以需要复制一份
            params = Maps.newHashMap(params);
            MosaicHandler handler = mosaicHandlers.get(uri);
            if (handler != null) {
                params = handler.handleParameters(params);
                requestString = handler.handleRequest(uri, requestString);
                if (!StringUtils.isEmpty(responseString)) {
                    responseString = handler.handleResponse(uri, responseString);
                }
            } else {
                for (String pattern : mosaicHandlers.keySet()) {
                    if (UriMatcher.match(pattern, uri)) {
                        MosaicHandler matched = mosaicHandlers.get(pattern);
                        params = matched.handleParameters(params);
                        requestString = matched.handleRequest(uri, requestString);
                        if (!StringUtils.isEmpty(responseString)) {
                            responseString = matched.handleResponse(uri, responseString);
                        }
                    }
                }
            }

            // 处理请求参数map
            String paramString = StringUtils.EMPTY;
            List<String> pairs = Lists.newArrayList();
            if (MapUtils.isNotEmpty(params)) {
                for (Object name : params.keySet()) {
                    Object value = params.get(name);
                    if (value instanceof String) {
                        pairs.add(name + "=" + StringUtils.trimToEmpty((String) value));
                    } else if (value instanceof String[]) {
                        String[] values = (String[]) value;
                        for (String v : values) {
                            pairs.add(name + "=" + StringUtils.trimToEmpty(v));
                        }
                    } else if (value != null) {
                        pairs.add(name + "=" + value.toString());
                    }
                }
                paramString = Joiner.on("&").join(pairs);
            }

            if (StringUtils.equals(request.getContentType(), MediaType.APPLICATION_FORM_URLENCODED_VALUE)) {
                paramString = URLDecoder.decode(paramString, "UTF-8");
            }

            // 使用logger记录日志
            Logger logger = LoggerFactory
                    .getLogger(StringUtils.isNotEmpty(loggerName) ? loggerName : DEFAULT_LOGGER_NAME);

            StringBuilder buffer = new StringBuilder();
            buffer.append("time=");
            buffer.append(time);
            buffer.append("ms, uri=");
            buffer.append(request.getRequestURI());
            buffer.append(", params=");
            buffer.append(paramString);
            buffer.append(", request=");
            buffer.append(requestString.replaceAll("\n|\r", ""));
            buffer.append(", response=");
            buffer.append(responseString.replaceAll("\n|\r", ""));

            logger.info(buffer.toString());

        } catch (Throwable e) {
            LOG.warn("log request data error", e);
        } finally {
            IOUtils.closeQuietly(httpServletRequestWrapper.getCachedStream());
            IOUtils.closeQuietly(httpServletResponseWrapper.getCachedStream());
        }
    }
    private void setMCodeQuietly(HttpServletRequest request) {
        try {
            if(request != null && !ArrayUtils.isEmpty(request.getCookies())) {
                Cookie[] cookies = request.getCookies();
                for (Cookie cookie : cookies) {
                    if (StringUtils.equals(cookie.getName(), "m_code")) {
                        MDC.put("MCODE", cookie.getValue());
                    }
                }
            }
        } catch (Throwable ignore){}
    }

    public void setLoggerName(String loggerName) {
        this.loggerName = loggerName;
    }

    public void setMosaicHandlers(Map<String, MosaicHandler> mosaicHandlers) {
        this.mosaicHandlers = mosaicHandlers;
    }

    public void setExcludeUri(String excludeUri) {
        this.excludeUri = excludeUri;
    }
}
