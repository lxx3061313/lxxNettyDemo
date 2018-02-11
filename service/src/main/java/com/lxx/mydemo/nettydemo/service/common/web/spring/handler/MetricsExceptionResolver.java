package com.lxx.mydemo.nettydemo.service.common.web.spring.handler;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.core.Ordered;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.SimpleMappingExceptionResolver;

/**
 * Created by zhaohui.yu
 * 6/3/15
 * <p/>
 * 用于监控所有异常
 */
public class MetricsExceptionResolver extends SimpleMappingExceptionResolver {
    public MetricsExceptionResolver() {
        this.setOrder(Ordered.HIGHEST_PRECEDENCE);
    }

    @Override
    protected ModelAndView doResolveException(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) {
        if (ex == null) {return null;}
        //HttpMetricsInterceptor.monitorError(request, ex);
        return null;
    }
}
