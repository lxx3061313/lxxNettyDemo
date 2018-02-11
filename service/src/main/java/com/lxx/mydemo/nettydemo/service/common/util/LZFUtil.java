package com.lxx.mydemo.nettydemo.service.common.util;

import com.ning.compress.lzf.LZFDecoder;
import com.ning.compress.lzf.LZFEncoder;

/**
 * @author yushen.ma
 * @version 2016-04-08
 */
public class LZFUtil {

    public static byte[] decompress(byte[] data) {
        try {
            return LZFDecoder.decode(data);
        } catch (Throwable t) {
            throw UnsafeUtil.throwException(t);
        }
    }

    public static byte[] compress(byte[] data) {
        return LZFEncoder.encode(data);
    }

}
