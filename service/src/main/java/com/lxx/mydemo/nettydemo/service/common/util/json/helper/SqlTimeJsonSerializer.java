package com.lxx.mydemo.nettydemo.service.common.util.json.helper;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import java.io.IOException;
import java.sql.Time;
import org.apache.commons.lang3.time.FastDateFormat;

/**
 * java.sql.Date序列化
 * @author ken.yang created on 14-6-9 下午6:36
 * @version $Id$
 */
public class SqlTimeJsonSerializer extends JsonSerializer<Time> {

    public static final FastDateFormat DATE_FORMAT = FastDateFormat.getInstance(SqlTimeJsonDeserializer.DATE_PATTERN);


    @Override
    public void serialize(Time date, JsonGenerator jgen, SerializerProvider provider) throws IOException {
        jgen.writeString(date != null ? DATE_FORMAT.format(date) : "null");
    }
}
