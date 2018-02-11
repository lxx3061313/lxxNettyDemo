package com.lxx.mydemo.nettydemo.service.common.util;

import com.google.common.base.Strings;
import java.nio.charset.Charset;
import java.util.Base64;

/**
 * @author lixiaoxiong
 * @version 2018-01-25
 */
public class Base64Util {
    public static String encode(String value) {
        if (Strings.isNullOrEmpty(value)) {
            return null;
        }
        return Base64.getEncoder().encodeToString(value.getBytes(Charset.forName("UTF-8")));
    }

    public static String decode(String enValue) {
        byte[] decode = Base64.getDecoder().decode(enValue);
        return new String(decode, Charset.forName("UTF-8"));
    }
}
