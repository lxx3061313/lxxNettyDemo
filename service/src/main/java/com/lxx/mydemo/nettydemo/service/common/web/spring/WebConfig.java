package com.lxx.mydemo.nettydemo.service.common.web.spring;

import com.lxx.mydemo.nettydemo.service.common.util.json.JsonUtil;
import com.lxx.mydemo.nettydemo.service.common.web.spring.resovler.HybridExceptionResolver;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.nio.charset.Charset;
import java.util.List;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.method.support.HandlerMethodReturnValueHandler;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerAdapter;

/**
 * @author lixiaoxiong
 * @version 2017-10-23
 */
@Configuration
@EnableWebMvc
public class WebConfig extends BaseWebConfig {
    @Bean
    public HybridExceptionResolver hybridExceptionResolver() {
        return new HybridExceptionResolver();
    }

//    @Bean
//    public MappedInterceptor mCodeTrace() {
//        return new MappedInterceptor(null, new MTraceInfoInterceptor());
//    }

    @Bean
    @Override
    public RequestMappingHandlerAdapter requestMappingHandlerAdapter() {
        RequestMappingHandlerAdapter adapter = super.requestMappingHandlerAdapter();

        // Hacking
        List<HandlerMethodReturnValueHandler> returnValueHandlers = getDefaultReturnValueHandlers(adapter);
        returnValueHandlers.add(0, this.getMyMethodProcessor());
        adapter.setReturnValueHandlers(returnValueHandlers);

        // 替换 mapper
        // 这里有更简单的办法,但是由于需要注入这个HybridMethodProcessor,所以提前调用了requestMappingHandlerAdaptor
        List<HttpMessageConverter<?>> _messageConverters = adapter.getMessageConverters();
        _messageConverters.stream()
                .filter(item -> item instanceof MappingJackson2HttpMessageConverter)
                .forEach(item -> {
                    MappingJackson2HttpMessageConverter item1 = (MappingJackson2HttpMessageConverter) item;
                    item1.setObjectMapper(JsonUtil.instance());
                });

        // 解决string convert 里面默认参数是iso的办法, 直接干他!!!!
        _messageConverters.stream()
                .filter(item -> item instanceof StringHttpMessageConverter)
                .forEach(item -> {
                    try {
                        Field defaultCharset = item.getClass().getField("defaultCharset");
                        defaultCharset.setAccessible(true);
                        defaultCharset.set(item, Charset.forName("UTF-8"));
                    } catch (Throwable ignore) { }
                });

        return adapter;
    }


    @SuppressWarnings("unchecked")
    private List<HandlerMethodReturnValueHandler> getDefaultReturnValueHandlers(RequestMappingHandlerAdapter adapter) {
        try {
            Method method = RequestMappingHandlerAdapter.class.getDeclaredMethod("getDefaultReturnValueHandlers");
            method.setAccessible(true);
            return (List<HandlerMethodReturnValueHandler>) method.invoke(adapter);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    protected HandlerMethodReturnValueHandler getMyMethodProcessor() {
        return new MyMethodProcessor();
    }
}
