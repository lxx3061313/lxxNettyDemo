package com.lxx.mydemo.nettydemo.service.common.util.pojo.node;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.lxx.mydemo.nettydemo.service.common.util.json.JsonMapper;
import com.lxx.mydemo.nettydemo.service.common.util.json.MapperBuilder;
import java.io.IOException;
import java.io.Reader;
import java.util.Map.Entry;

/**
 * miao.yang susing@gmail.com 2013年8月26日
 */
public class JacksonSupport {

    private static final JsonMapper mapper = MapperBuilder.create().build();

    public static ValueNode parse(String json) {
        try {
            return mapper.readValue(json, ValueNode.class);
        } catch (Exception e) {
            throw new RuntimeException("jackson parse error :" + json.substring(0, Math.min(100, json.length())), e);
        }
    }

    public static ValueNode parse(Reader json) {
        try {
            return mapper.readValue(json, ValueNode.class);
        } catch (Exception e) {
            throw new RuntimeException("jackson parse error" , e);
        }
    }


    public static <T> T parseJson(String json, Class<T> type) {
        return mapper.readValue(json, type);
    }

    public static String toJson(Object obj) {
        return mapper.writeValueAsString(obj);
    }

    public static class ValueNodeSerializer extends JsonSerializer<ValueNode> {
        @Override
        public void serialize(ValueNode value, JsonGenerator jgen, SerializerProvider provider) throws IOException {
            write(value, jgen);
        }
    }
    public static class ValueNodeDeserializer extends JsonDeserializer<ValueNode> {
        @Override
        public ValueNode deserialize(JsonParser jp, DeserializationContext ctxt) throws IOException {
            return parseToken(jp.getCurrentToken(), jp);
        }
    }

    public static void write(ValueNode node, JsonGenerator gen) throws IOException {
        switch (node.getType()) {
            case bool:
                gen.writeBoolean(node.toBooleanValue());
                break;
            case NULL:
                gen.writeNull();
                break;
            case number:
                writeNumber(node, gen);
                break;
            case string:
                gen.writeString(node.toString());
                break;
            case list:
                gen.writeStartArray();
                for (ValueNode child : node)
                    write(child, gen);
                gen.writeEndArray();
                break;
            case map:
                gen.writeStartObject();
                for (Entry<String, ValueNode> entry : node.entrySet()) {
                    gen.writeFieldName(entry.getKey());
                    write(entry.getValue(), gen);
                }
                gen.writeEndObject();
                break;
        }
    }

    private static void writeNumber(ValueNode node, JsonGenerator gen) throws IOException {
        Number num = node.toNumber();
        if (num instanceof Integer)
            gen.writeNumber(num.intValue());
        else if (num instanceof Long)
            gen.writeNumber(num.longValue());
        else if (num instanceof Double)
            gen.writeNumber(num.doubleValue());
        else
            gen.writeNumber(num.floatValue());
    }

    private static ValueNode parseToken(JsonToken token, JsonParser jp) throws IOException {
        switch (token) {
            case START_OBJECT:
                return parseMap(jp);
            case START_ARRAY:
                return parseArray(jp);
            case VALUE_STRING:
                return new StringNode(jp.getValueAsString());
            case VALUE_NUMBER_FLOAT:
            case VALUE_NUMBER_INT:
                return new NumericNode(jp.getNumberValue());
            case VALUE_NULL:
                return NullNode.getRoot();
            case VALUE_FALSE:
            case VALUE_TRUE:
                return new BooleanValue(jp.getBooleanValue());
            default:
                throw new JsonParseException("意外的的token: " + token, jp.getCurrentLocation());
        }
    }

    private static ValueNode parseArray(JsonParser jp) throws IOException {
        ListNode node = new ListNode();
        JsonToken token = jp.nextToken();
        while (token != JsonToken.END_ARRAY) {
            ValueNode vn = parseToken(token, jp);
            node.add(vn);
            token = jp.nextToken();
        }
        return node;
    }

    private static MapNode parseMap(JsonParser jp) throws IOException {
        MapNode node = new MapNode();
        while (jp.nextToken() != JsonToken.END_OBJECT) {
            String fieldName = jp.getCurrentName();
            ValueNode vn = parseToken(jp.nextToken(), jp);
            node.set(fieldName, vn);
        }
        return node;
    }

}
