package com.lxx.mydemo.nettydemo.service.common.util.json;

/**
 * Created by zhaohui.yu
 * 15/9/9
 */
public class JsonWriterFactory {
    public static final JsonWriter WRITER;

    private JsonWriterFactory() {
    }

    static {
        WRITER = create();
    }

    private static JsonWriter create() {
        try {
            return new JacksonWriter();
        } catch (Throwable e) {
            return new ToStringJsonWriter();
        }
    }
}
