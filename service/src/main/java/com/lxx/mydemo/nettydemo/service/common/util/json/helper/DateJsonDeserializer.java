package com.lxx.mydemo.nettydemo.service.common.util.json.helper;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import java.io.IOException;
import java.text.ParseException;
import java.util.Date;
import org.apache.commons.lang3.time.DateUtils;

/**
 * @author yushen.ma
 * @version 1.0 2016-04-08
 */
public class DateJsonDeserializer extends JsonDeserializer<Date> {

    public static final String DATE_PATTERN = "yyyy-MM-dd HH:mm:ss";

    @Override
    public Date deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) throws IOException,
            JsonProcessingException {
        String date = jsonParser.getText();
        if (date != null && !date.isEmpty()) {
            try {
                return DateUtils.parseDate(date, DATE_PATTERN);
            } catch (ParseException e) {
                throw new JsonParseException("cannot parse date string: " + date, jsonParser.getCurrentLocation(), e);
            }
        }
        return null;
    }
}
