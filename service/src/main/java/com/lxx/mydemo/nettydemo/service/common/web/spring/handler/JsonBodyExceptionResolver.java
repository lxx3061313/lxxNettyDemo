package com.lxx.mydemo.nettydemo.service.common.web.spring.handler;

import com.google.common.base.Strings;
import com.lxx.mydemo.nettydemo.service.common.web.spring.annotation.JsonBody;
import java.lang.reflect.Method;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.Ordered;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.SimpleMappingExceptionResolver;
import org.springframework.web.servlet.mvc.annotation.ModelAndViewResolver;

/**
 * JsonBody接口异常处理类.
 * 
 * <ul>
 * <li>支持自定义错误编码和消息
 * </ul>
 * 
 * 默认为第一个ExceptionResolver, {@link #doResolveException}方法返回null时会寻求下一个Resolver。
 */
public class JsonBodyExceptionResolver extends SimpleMappingExceptionResolver {

    private static final Logger logger = LoggerFactory.getLogger(JsonBodyExceptionResolver.class);

    public JsonBodyExceptionResolver() {
        this.setOrder(Ordered.HIGHEST_PRECEDENCE);
    }

    @Override
    protected ModelAndView doResolveException(HttpServletRequest request, HttpServletResponse response,
                                              Object _handler, Exception ex) {

        HandlerMethod handler = (HandlerMethod) _handler;
        if (handler == null) {
            // like 'GET' not supported
            return null;
        }

        Method method = handler.getMethod();

        if (method.isAnnotationPresent(JsonBody.class)) {

            String config = super.determineViewName(ex, request);
            Object value = Strings.isNullOrEmpty(config) ? ex : JsonSerializer.parseConfig(config, ex);
            logger.error("mvc error", ex);
            JsonSerializer.write(value, method, request, response);

            // skip other resolver and view render
            return ModelAndViewResolver.UNRESOLVED;
        }

        return null;
    }
}