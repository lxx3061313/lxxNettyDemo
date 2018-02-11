package com.lxx.mydemo.nettydemo.service.common.util.json.helper;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import java.io.IOException;
import java.sql.Date;
import java.text.ParseException;
import org.apache.commons.lang3.time.DateUtils;

/**
 * java.sql.Date 反序列化
 * @author ken.yang created on 14-6-9 下午6:36
 * @version $Id$
 */
public class SqlDateJsonDeserializer extends JsonDeserializer<Date> {

    public static final String DATE_PATTERN = "yyyy-MM-dd";

    @Override
    public Date deserialize(JsonParser jp, DeserializationContext ctxt) throws IOException {
        String date = jp.getText();
        if (date != null && !date.isEmpty()) {
            try {
                java.util.Date utilDate = DateUtils.parseDate(date, DATE_PATTERN);
                return new Date(utilDate.getTime());
            } catch (ParseException e) {
                throw new JsonParseException("cannot parse date string: " + date, jp.getCurrentLocation(), e);
            }
        }
        return null;
    }
}
