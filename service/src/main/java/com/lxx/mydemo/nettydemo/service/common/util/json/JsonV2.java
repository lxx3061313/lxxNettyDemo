package com.lxx.mydemo.nettydemo.service.common.util.json;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class JsonV2<T> {

    public final int status;
    public final String message;
    public final T data;

    @JsonCreator
    public JsonV2(@JsonProperty("status") int status,
            @JsonProperty("message") String message,
            @JsonProperty("data") T data) {

        this.status = status;
        this.message = message;
        this.data = data;
    }
}
