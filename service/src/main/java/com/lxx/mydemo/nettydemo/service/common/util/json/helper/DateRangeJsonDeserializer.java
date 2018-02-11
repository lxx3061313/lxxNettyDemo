package com.lxx.mydemo.nettydemo.service.common.util.json.helper;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.lxx.mydemo.nettydemo.service.common.util.pojo.DateRange;
import java.io.IOException;
import org.apache.commons.lang3.StringUtils;

/**
 * @author yushen.ma
 * @version 1.0 2016-04-08
 */
public class DateRangeJsonDeserializer extends JsonDeserializer<DateRange> {
    @Override
    public DateRange deserialize(JsonParser p, DeserializationContext ctxt)
            throws IOException, JsonProcessingException {
        String source = p.getText();
        if (StringUtils.isBlank(source)) {return null;}
        if (StringUtils.equalsIgnoreCase("null", source)) {return null;}

        try {
            return DateRange.create(source) ;
        } catch (Throwable t) {
            throw new JsonParseException("cannot parse date range string: " + source, p.getCurrentLocation(), t);
        }
    }
}
