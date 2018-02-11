
package com.lxx.mydemo.nettydemo.service.common.util.json.helper;


import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.lxx.mydemo.nettydemo.service.common.util.pojo.ShortDate;
import java.io.IOException;

/**
 * @author yushen.ma
 * @version 1.0 2016-04-08
 */
public class ShortDateJsonSerializer extends JsonSerializer<ShortDate> {

    @Override
    public void serialize(ShortDate shortDate, JsonGenerator jsonGenerator, SerializerProvider serializerProvider)
            throws IOException, JsonProcessingException {
        jsonGenerator.writeString(shortDate != null ? shortDate.toString() : "null");
    }

}
