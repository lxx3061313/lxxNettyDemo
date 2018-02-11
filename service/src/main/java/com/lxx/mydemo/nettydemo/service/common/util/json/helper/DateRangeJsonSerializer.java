package com.lxx.mydemo.nettydemo.service.common.util.json.helper;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.lxx.mydemo.nettydemo.service.common.util.pojo.DateRange;
import java.io.IOException;

/**
 * @author yushen.ma
 * @version 1.0 2016-04-08
 */
public class DateRangeJsonSerializer extends JsonSerializer<DateRange> {

    @Override
    public void serialize(DateRange value, JsonGenerator gen, SerializerProvider serializers) throws IOException, JsonProcessingException {
        gen.writeString(value != null ? value.toString() : "null");
    }

}
