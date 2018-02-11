package com.lxx.mydemo.nettydemo.service.common.web.spring.handler;

import com.lxx.mydemo.nettydemo.service.common.web.spring.annotation.JsonBody;
import java.lang.reflect.Method;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.core.MethodParameter;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodReturnValueHandler;
import org.springframework.web.method.support.ModelAndViewContainer;

/**
 * 用于接管Json标注的方法.
 *
 * @author miao.yang susing@gmail.com
 * @since 2014-03-15
 */
public class JsonBodyMethodProcessor implements HandlerMethodReturnValueHandler {

    @Override
    public boolean supportsReturnType(MethodParameter returnType) {
        return returnType.getMethodAnnotation(JsonBody.class) != null;
    }

    @Override
    public void handleReturnValue(final Object returnValue, MethodParameter returnType,
                                  ModelAndViewContainer mavContainer, NativeWebRequest webRequest) throws Exception {

        mavContainer.setRequestHandled(true);

        HttpServletRequest request = webRequest.getNativeRequest(HttpServletRequest.class);
        HttpServletResponse response = webRequest.getNativeResponse(HttpServletResponse.class);

        this.write(returnValue, returnType.getMethod(), request, response);
    }

    protected void write(Object returnValue, Method method, HttpServletRequest request, HttpServletResponse response) {
        JsonSerializer.write(returnValue, method, request, response);
    }
}
