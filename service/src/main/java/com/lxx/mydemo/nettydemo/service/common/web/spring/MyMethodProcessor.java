package com.lxx.mydemo.nettydemo.service.common.web.spring;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.base.Charsets;
import com.google.common.base.Strings;
import com.lxx.mydemo.nettydemo.service.common.util.RequestUtil;
import com.lxx.mydemo.nettydemo.service.common.util.UnsafeUtil;
import com.lxx.mydemo.nettydemo.service.common.util.json.DataJsonV1;
import com.lxx.mydemo.nettydemo.service.common.util.json.ErrorJsonV1;
import com.lxx.mydemo.nettydemo.service.common.util.json.JsonUtil;
import com.lxx.mydemo.nettydemo.service.common.util.json.JsonV2;
import com.lxx.mydemo.nettydemo.service.common.util.pojo.APIResponse;
import com.lxx.mydemo.nettydemo.service.common.util.pojo.CodeMessage;
import com.lxx.mydemo.nettydemo.service.common.web.spring.annotation.JsonBody;
import com.lxx.mydemo.nettydemo.service.common.web.spring.annotation.Redirect;
import com.lxx.mydemo.nettydemo.service.common.web.spring.handler.JsonBodyMethodProcessor;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.io.Writer;
import java.lang.reflect.Method;
import java.net.URLEncoder;
import java.util.regex.Pattern;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * 用于接管Json标注的方法. MTS中使用APIResponse
 */
final class MyMethodProcessor extends JsonBodyMethodProcessor {

    final private static Logger logger = LoggerFactory.getLogger(MyMethodProcessor.class);

    @Override
    protected void write(Object returnValue, Method method, HttpServletRequest request, HttpServletResponse response) {
        write0(returnValue, method, request, response);
    }

    private void write0(Object value, Method method, HttpServletRequest request, HttpServletResponse response) {

        JsonBody meta = method.getAnnotation(JsonBody.class);
        Redirect redirect = method.getAnnotation(Redirect.class);

        // always work
        String callback = Strings.emptyToNull(request.getParameter(meta.callback()));

        try {
            if (redirect == null) {
                response(callback, buildValue(value, meta.version()), JsonUtil.instance(), response);
            } else if (callback == null) {
                response.getWriter().write("parameter '" + meta.callback() + "' required");
            } else if (!RequestUtil.match(redirect.hosts(), callback)) {
                response.getWriter().write("forbidden to redirect to " + callback);
            } else {
                redirect(callback, buildValue(value, meta.version()), redirect, JsonUtil.instance(), response);
            }
        } catch (IOException e) {
            logger.warn("response write error", e);
            throw UnsafeUtil.throwException(e);
        }
    }

    private void response(String callback, Object value, ObjectMapper om, HttpServletResponse response) throws IOException {
        if (callback != null) {
            response.setContentType("application/javascript; charset=UTF-8");
            // call setContentType before getWriter(), or else UTF-8 doesn't work
            Writer writer = response.getWriter();
            writer.write(callback);
            writer.write('(');
            om.writeValue(writer, value);
            writer.write(')');
        } else {
            response.setContentType("application/json; charset=UTF-8");
            om.writeValue(response.getWriter(), value);
        }
    }

    private void redirect(String callback, Object value, Redirect rmeta, ObjectMapper om, HttpServletResponse response) throws IOException {

        String data = om.writeValueAsString(value);
        String token = RequestUtil.rtoken(data, rmeta.secret());
        String sb = callback + (callback.contains("?") ? '&' : '?') + rmeta.dataKey() + '=' + encode(data) +
                '&' + rmeta.tokenKey() + '=' + token;
        // ?r=$data
        // &cbt=$token
        response.sendRedirect(sb);
    }


    private static final Pattern encode = Pattern.compile("\\+");

    private static String encode(String r) throws UnsupportedEncodingException {
        // + => %20
        return encode.matcher(URLEncoder.encode(r, Charsets.UTF_8.name())).replaceAll("%20");
    }

    private static Object buildValue(Object value, JsonBody.Version version) {

        if (value instanceof JsonV2 || value instanceof APIResponse
                || value instanceof DataJsonV1 || value instanceof ErrorJsonV1) {
            return value;
        }

        switch (version) {
            case v1:
            case v2:
                return makeDefault(value);
            default:
                throw new RuntimeException("Bad json version, " + version);
        }
    }

    private static Object makeDefault(Object value) {
        if (value instanceof CodeMessage) {
            final CodeMessage msg = (CodeMessage) value;
            // avoid flushing cause/stackTrace/localizedMessage
            return new APIResponse<>(msg.getStatus(), msg.getMessage(), msg.getData());
        }

        if (value instanceof Throwable) {
            final Throwable e = (Throwable) value;
            return new APIResponse<>(99999, e.getMessage(), null);
        }

        return new APIResponse<>(0, null, value);
    }

}
