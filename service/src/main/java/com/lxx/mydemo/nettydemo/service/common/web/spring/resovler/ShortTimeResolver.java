package com.lxx.mydemo.nettydemo.service.common.web.spring.resovler;

import com.lxx.mydemo.nettydemo.service.common.util.pojo.ShortTime;
import org.apache.commons.lang3.StringUtils;
import org.springframework.core.MethodParameter;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

/**
 * @author yushen.ma
 * @version 2016-07-11
 */
public class ShortTimeResolver  implements HandlerMethodArgumentResolver {

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.getParameterType().equals(ShortTime.class);
    }

    @Override
    public Object resolveArgument(MethodParameter parameter,
                                  ModelAndViewContainer mavContainer,
                                  NativeWebRequest webRequest,
                                  WebDataBinderFactory binderFactory) throws Exception {
        String parameterName = parameter.getParameterName();
        String target = webRequest.getParameter(parameterName);
        if (StringUtils.isBlank(target)) {
            return null;
        }
        return ShortTime.create(target);
    }
}
