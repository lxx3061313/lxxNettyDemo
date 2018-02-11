package com.lxx.mydemo.nettydemo.service.common.util;

import com.lxx.mydemo.nettydemo.service.common.util.json.JsonUtil;
import java.io.IOException;
import javax.servlet.ServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author zhangxin
 *         Created on 17/3/12.
 */
public class WebUtil {
    public static Logger logger = LoggerFactory.getLogger(WebUtil.class);

    private static final String enAlgorithmName = "MD5";
    private static int hashIterations = 1024;

    public static void responseJson(Object value, ServletResponse response){
        response.setContentType("application/json; charset=UTF-8");

        try {
            JsonUtil.instance().writeValue(response.getWriter(), value);
        } catch (IOException e) {
            logger.error("ServletResponse返回Json写流失败",e);
            throw UnsafeUtil.throwException(e);
        }
    }

//    public static String md5(String password, String saltSource) {
//        ByteSource salt = new Md5Hash(saltSource);
//        return new SimpleHash(enAlgorithmName, password, salt, hashIterations).toString();
//    }
}
