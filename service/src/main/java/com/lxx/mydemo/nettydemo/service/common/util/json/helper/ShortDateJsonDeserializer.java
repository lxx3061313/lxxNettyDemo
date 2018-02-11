
package com.lxx.mydemo.nettydemo.service.common.util.json.helper;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.lxx.mydemo.nettydemo.service.common.util.pojo.ShortDate;
import java.io.IOException;

/**
 * @author yushen.ma
 * @version 1.0 2016-04-08
 */
public class ShortDateJsonDeserializer extends JsonDeserializer<ShortDate> {
    @Override
    public ShortDate deserialize(JsonParser jsonParser, DeserializationContext deserializationContext)
            throws IOException, JsonProcessingException {
        String time = jsonParser.getText();
        if (time != null && !time.isEmpty()) {
            return ShortDate.create(time);
        }
        return null;
    }
}
