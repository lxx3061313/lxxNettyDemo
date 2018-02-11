package com.lxx.mydemo.nettydemo.service.common.util.json.helper;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import java.io.IOException;
import java.util.Date;
import org.apache.commons.lang3.time.FastDateFormat;

/**
 * @author yushen.ma
 * @version 1.0 2016-04-08
 */
public class DateJsonSerializer extends JsonSerializer<Date> {

    public static final FastDateFormat DATE_FORMAT =
            FastDateFormat.getInstance(DateJsonDeserializer.DATE_PATTERN);

    @Override
    public void serialize(Date date, JsonGenerator jsonGenerator, SerializerProvider serializerProvider)
            throws IOException, JsonProcessingException {
        jsonGenerator.writeString(date != null ? DATE_FORMAT.format(date) : "null");
    }
}
