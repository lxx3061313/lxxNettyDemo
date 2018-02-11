package com.lxx.mydemo.nettydemo.service.common.web.spring.handler;

import static com.lxx.mydemo.nettydemo.service.common.util.pojo.CodeMessage.OK;
import static com.lxx.mydemo.nettydemo.service.common.util.pojo.CodeMessage.SYSTEM_ERROR;
import static com.lxx.mydemo.nettydemo.service.common.util.pojo.Constants.SPLIT_COMMA;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.base.Charsets;
import com.google.common.base.Strings;
import com.google.common.primitives.Ints;
import com.lxx.mydemo.nettydemo.service.common.util.RequestUtil;
import com.lxx.mydemo.nettydemo.service.common.util.json.DataJsonV1;
import com.lxx.mydemo.nettydemo.service.common.util.json.ErrorJsonV1;
import com.lxx.mydemo.nettydemo.service.common.util.json.JsonUtil;
import com.lxx.mydemo.nettydemo.service.common.util.json.JsonV2;
import com.lxx.mydemo.nettydemo.service.common.util.pojo.CodeMessage;
import com.lxx.mydemo.nettydemo.service.common.util.pojo.exception.RuntimeErrorMessage;
import com.lxx.mydemo.nettydemo.service.common.web.spring.annotation.JsonBody;
import com.lxx.mydemo.nettydemo.service.common.web.spring.annotation.JsonBody.Version;
import com.lxx.mydemo.nettydemo.service.common.web.spring.annotation.Redirect;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.io.Writer;
import java.lang.reflect.Method;
import java.net.URLEncoder;
import java.util.Iterator;
import java.util.regex.Pattern;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * JSON接口序列化工具
 */
final class JsonSerializer {

    private static final Logger logger = LoggerFactory.getLogger(JsonSerializer.class);

    static void write(Object value, Method method, HttpServletRequest request, HttpServletResponse response) {

        JsonBody meta = method.getAnnotation(JsonBody.class);
        Redirect rmeta = method.getAnnotation(Redirect.class);

        // always work
        String callback = Strings.emptyToNull(request.getParameter(meta.callback()));

        try {
            if (rmeta == null) {
                response(callback, buildValue(value, meta.version()), JsonUtil.instance(), response);
            } else if (callback == null) {
                response.getWriter().write("parameter '" + meta.callback() + "' required");
            } else if (!RequestUtil.match(rmeta.hosts(), callback)) {
                response.getWriter().write("forbidden to redirect to " + callback);
            } else {
                redirect(callback, buildValue(value, meta.version()), rmeta, JsonUtil.instance(), response);
            }
        } catch (IOException e) {
            logger.warn("response write error", e);
        }
    }

    private static void response(String callback, Object value, ObjectMapper om, HttpServletResponse response) throws IOException {
        if (callback != null) {
            response.setContentType("application/javascript; charset=UTF-8");
            // call setContentType before getWriter(), or else UTF-8 doesn't work
            Writer writer = response.getWriter();
            writer.write(callback);
            writer.write('(');
            om.writeValue(writer, value );
            writer.write(')');
        } else {
            response.setContentType("application/json; charset=UTF-8");
            om.writeValue(response.getWriter(), value);
        }
    }

    private static void redirect(String callback, Object value, Redirect rmeta, ObjectMapper om, HttpServletResponse response) throws IOException {

        String data = om.writeValueAsString(value);
        String token = RequestUtil.rtoken(data, rmeta.secret());
        logger.debug("data={}, secret={}", data, rmeta.secret());

        StringBuilder sb = new StringBuilder(callback);
        // ?r=$data
        sb.append(callback.contains("?") ? '&' : '?').append(rmeta.dataKey()).append('=').append(encode(data));
        // &cbt=$token
        sb.append('&').append(rmeta.tokenKey()).append('=').append(token);
        response.sendRedirect(sb.toString());
    }


    private static final Pattern encode = Pattern.compile("\\+");
    private static final String encode(String r) throws UnsupportedEncodingException {
        // + => %20
        return encode.matcher(URLEncoder.encode(r, Charsets.UTF_8.name())).replaceAll("%20");
    }

    private static Object buildValue(Object value, Version version) {

        if (value instanceof JsonV2 || value instanceof DataJsonV1 || value instanceof ErrorJsonV1) {
            return value;
        }

        switch (version) {
        case v1:
            return makeV1(value);
        case v2:
            return makeV2(value);
        default:
            throw new RuntimeException("Bad json version, " + version);
        }
    }

    protected static Object makeV1(final Object value) {

        if (value instanceof CodeMessage) {
            final CodeMessage msg = (CodeMessage) value;

            if (msg.getStatus() == OK) {
                return new DataJsonV1<Object>(msg.getData());
            }
            return new ErrorJsonV1(msg.getStatus(), msg.getMessage());
        }

        if (value instanceof Throwable) {
            final Throwable e = (Throwable) value;

            return new ErrorJsonV1(SYSTEM_ERROR, e.getMessage());
        }

        return new DataJsonV1<Object>(value);
    }

    protected static Object makeV2(final Object value) {

        if (value instanceof CodeMessage) {
            final CodeMessage msg = (CodeMessage) value;
            // avoid flushing cause/stackTrace/localizedMessage
            return new JsonV2<Object>(msg.getStatus(), msg.getMessage(), msg.getData());
        }

        if (value instanceof Throwable) {
            final Throwable e = (Throwable) value;
            return new JsonV2<Object>(SYSTEM_ERROR, e.getMessage(), null);
        }

        return new JsonV2<Object>(OK, null, value);
    }

    // convert string to message in the format: {status},{message}
    static CodeMessage parseConfig(String config, Exception ex) {

        Iterator<String> i = SPLIT_COMMA.split(config).iterator();
        Integer status = Ints.tryParse(i.next());
        if (status == null) {
            status = SYSTEM_ERROR;
        }

        String message = i.hasNext() ? i.next() : ex.getMessage();

        return new RuntimeErrorMessage(status, message);
    }
}
