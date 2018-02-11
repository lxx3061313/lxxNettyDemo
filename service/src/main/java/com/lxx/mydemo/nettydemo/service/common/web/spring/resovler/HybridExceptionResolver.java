package com.lxx.mydemo.nettydemo.service.common.web.spring.resovler;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.lxx.mydemo.nettydemo.service.common.util.json.JsonUtil;
import com.lxx.mydemo.nettydemo.service.common.util.pojo.APIResponse;
import com.lxx.mydemo.nettydemo.service.common.util.pojo.BizException;
import com.lxx.mydemo.nettydemo.service.common.web.spring.annotation.JsonBody;
import java.util.Enumeration;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.Ordered;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.multipart.MaxUploadSizeExceededException;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.SimpleMappingExceptionResolver;
import org.springframework.web.servlet.mvc.annotation.ModelAndViewResolver;

/**
 * com.qunar.hotel.qta.order.biz.common.spring.HybridExceptionResolver
 * 异常处理器
 * 仅仅针对异常（是真正的异常）进行处理。不涉及业务
 *
 *
 * 1. 支持JSON @see qunar.api.pojo.json.JsonV2
 * 5. 支持@ResponseBody
 * 6. 支持@JsonBody
 * 7. 支持Assert, Precondition验证
 * 8. 兼容APIResponse
 *
 * 当抛出的异常是IllegalArgumentException || BizException的时候使用
 *
 * 不直接使用公司提供的common-web，因为没办法记500监控
 *
 * @author yushen.ma
 * @version 2014-12-01
 */
public class HybridExceptionResolver extends SimpleMappingExceptionResolver {

    private final static Logger logger = LoggerFactory.getLogger(HybridExceptionResolver.class);

    static Pattern p = Pattern.compile("[\u4e00-\u9fa5]");

    /** THINK 这个方法在已经是默认ExceptionResolver的情况下还有没有意义呢 */
    public HybridExceptionResolver() {
        //set highest precedence
        this.setOrder(Ordered.HIGHEST_PRECEDENCE);
    }

    @Override
    protected ModelAndView doResolveException(HttpServletRequest request,
            HttpServletResponse response, Object _handler, Exception ex) {

        if (ex instanceof HttpRequestMethodNotSupportedException
                || ex instanceof MissingServletRequestParameterException
                || ex instanceof MaxUploadSizeExceededException) {
            return super.doResolveException(request, response, _handler, ex);
        }

        if ( ! (_handler instanceof HandlerMethod) ) {
            // not supported
            return super.doResolveException(request, response, _handler, ex);
        }//End Of If

        HandlerMethod handler = (HandlerMethod) _handler;

        // json view
        if (handler.getMethod().isAnnotationPresent(ResponseBody.class)
                || handler.getMethod().isAnnotationPresent(JsonBody.class)) {
            ErrorJsonGen.flush(handler, request, response, ex);
            // skip other resolver and view render
            return ModelAndViewResolver.UNRESOLVED;
        }

        // to next resolver
        return super.doResolveException(request, response, handler, ex);
    }

    /**
     * 错误信息展示工具
     */
    static class ErrorJsonGen {

        private final static String ERROR_MSG = "系统好忙, 请您稍后再试";

        private final static ObjectMapper objectMapper = JsonUtil.instance();

        private final static int ERROR_SERVER_STATUS = 200;

        public static void flush(HandlerMethod handler,
                                 HttpServletRequest request,
                                 HttpServletResponse response,
                                 final Exception ex) {
            response.setStatus(ERROR_SERVER_STATUS);
            response.setContentType("application/json; charset=UTF-8");

            Object result;
            if(handler.getMethod().isAnnotationPresent(JsonBody.class)
                    || (handler.getMethod().isAnnotationPresent(ResponseBody.class)
                    && handler.getMethod().getReturnType().equals(APIResponse.class))) {
                //兼容APIResponse
                result = APIResponse.error(errorMsg(request, ex));
            } else {//兼容无任何格式
                result = errorMsg(request, ex);
            }

            try {
                response.getOutputStream().write(objectMapper.writeValueAsString(
                        result).getBytes("UTF-8"));
            } catch (Exception e) {
                logger.error("flush exception json", e);
            }
        }

        private static String errorMsg(HttpServletRequest request ,Throwable t) {
            Throwable real = findAnyBizException(t);
            if (real != null) {
                t = real;
            }

            if (t instanceof BizException &&
                    isContainChinese(t.getMessage()) && isReadable(t.getMessage())) {
                logger.warn("已知业务异常", t);
                return t.getMessage();
            } else if (t instanceof IllegalArgumentException
                    && isContainChinese(t.getMessage()) && isReadable(t.getMessage())) {
                logger.warn("已知业务异常", t);
                return t.getMessage();
            }
            logger.error("unexpected exception", t);
            // record 500
            _500Recorder.record500(request, t);
            return ERROR_MSG;
        }

        private static Throwable findAnyBizException(Throwable t) {
            Throwable cause = t.getCause();
            if (cause == t || cause == null) { // 到头了
                return null;
            }
            if (cause instanceof BizException) {
                return cause;
            }
            return findAnyBizException(cause);
        }

        private static boolean isReadable(String message) {
            return !StringUtils.containsAny(message, "exception") &&
                    !StringUtils.containsAny(message, "null") &&
                    !StringUtils.containsAny(message, "exception") &&
                    !StringUtils.containsAny(message, "stack") &&
                    !StringUtils.containsAny(message, "com") &&
                    !StringUtils.containsAny(message, "alibaba");
        }
    }

    public static boolean isContainChinese(String str) {
        if (StringUtils.isEmpty(str)) {
            return false;
        }
        Matcher m = p.matcher(str);
        return m.find();
    }

    /**
     * Http 500 Recorder
     */
    static class _500Recorder {

        /**
         * 1. 500 监控
         * 2. exception日志
         * 3. 请求日志(param & header)
         *
         * @param request 请求
         * @param t 异常
         */
        public static void record500(HttpServletRequest request, Throwable t) {
            //Metrics.meter("http_process_error").get().mark();
            // exception 日志
            logger.warn("http_500", t);
            // 请求日志
            logger.warn("http_500", RequestFormatter.INSTANCE.cookieNParam(request) );
        }

        /**
         * format request to write into log
         */
        private enum RequestFormatter {

            INSTANCE;

            public String cookieNParam(HttpServletRequest request) {
                return "params: " + formatRequestParameters(request) + "\n"
                        + "headers: " + formatRequestHeaders(request);
            }

            private String formatRequestParameters(ServletRequest request) {
                StringBuilder params = new StringBuilder();
                boolean first = true;
                Enumeration<?> inter = request.getParameterNames();
                while (inter.hasMoreElements()) {
                    String name = (String) inter.nextElement();
                    if (first) {
                        first = false;
                    } else {
                        params.append(", ");
                    }
                    params.append(name).append("=");
                    try {
                        for (String value : request.getParameterValues(name)) {
                            if (StringUtils.containsIgnoreCase(name, "password")) {
                                value = "******";
                            }
                            params.append(value);
                        }
                    } catch (Exception ignore) {/** 吃掉 */}
                }

                return params.toString();
            }

            private String formatRequestHeaders(HttpServletRequest request) {
                Enumeration<?> e = request.getHeaderNames();
                StringBuilder params = new StringBuilder();
                boolean first = true;
                if (e != null) {
                    while (e.hasMoreElements()) {
                        Object o = e.nextElement();
                        if (first) {
                            first = false;
                        } else {
                            params.append(", ");
                        }
                        String name = (String) o;
                        params.append(name).append("=").append(request.getHeader(name));
                    }
                }
                return params.toString();
            }//End Of Func formatRequestHeaders
        }//End Of RequestFormatter class
    }//End Of Recorder

}
