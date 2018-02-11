
package com.lxx.mydemo.nettydemo.service.common.util.json.helper;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.lxx.mydemo.nettydemo.service.common.util.pojo.ShortTime;
import java.io.IOException;

/**
 * @author yushen.ma
 * @version 2016-04-08
 */
public class ShortTimeJsonSerializer extends JsonSerializer<ShortTime> {

    @Override
    public void serialize(ShortTime shortTime, JsonGenerator jsonGenerator, SerializerProvider serializerProvider)
            throws IOException, JsonProcessingException {
        jsonGenerator.writeString(shortTime != null ? shortTime.toString() : "null");
    }

}
