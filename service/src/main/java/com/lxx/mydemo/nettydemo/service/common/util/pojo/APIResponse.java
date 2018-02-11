package com.lxx.mydemo.nettydemo.service.common.util.pojo;


import com.fasterxml.jackson.annotation.JsonIgnore;
import java.io.Serializable;
import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

/**
 * @author yushen.ma
 * @version 1.0 2016-04-06
 */
public class APIResponse<T> implements Serializable {

    private static final long serialVersionUID = 5412715938795343087L;

    @JsonIgnore
    private final String ver = "1.0.0";

    private int resCode;

    private String msg;

    private T data;

    private APIResponse() {}

    public APIResponse(int resCode, String msg, T data) {
        this.resCode = resCode;
        this.msg = msg;
        this.data = data;
    }

    public static <T> APIResponse<T> success() {
        APIResponse<T> result = new APIResponse<>();
        result.setResCode(0);
        result.setData(null);
        return result;
    }

    public static <T> APIResponse<T> success(T data) {
        APIResponse<T> result = new APIResponse<>();
        result.setResCode(0);
        result.setData(data);
        return result;
    }

    public static <T> APIResponse<T> error(String message) {
        APIResponse<T> result = new APIResponse<>();
        result.setResCode(10000);
        result.setMsg(message);
        return result;
    }

    public static <T> APIResponse<T> error(int errStatus, String message) {
        APIResponse<T> result = new APIResponse<>();
        result.setResCode(errStatus);
        result.setMsg(message);
        return result;
    }

    public static <T> APIResponse<T> error(int errStatus, String message, T data) {
        APIResponse<T> result = new APIResponse<>();
        result.setResCode(errStatus);
        result.setMsg(message);
        result.setData(data);
        return result;
    }

    @JsonIgnore
    public boolean isOK() {
        return resCode == 0;
    }

    @JsonIgnore
    public boolean isNotOK() {
        return resCode != 0;
    }

    @JsonIgnore
    public String getVer() {
        return ver;
    }

    public int getResCode() {
        return resCode;
    }

    public void setResCode(int resCode) {
        this.resCode = resCode;
    }

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        APIResponse<?> that = (APIResponse<?>) o;

        if (resCode != that.resCode) return false;
        if (ver != null ? !ver.equals(that.ver) : that.ver != null) return false;
        if (msg != null ? !msg.equals(that.msg) : that.msg != null) return false;
        return data != null ? data.equals(that.data) : that.data == null;

    }

    @Override
    public int hashCode() {
        int result = ver != null ? ver.hashCode() : 0;
        result = 31 * result + resCode;
        result = 31 * result + (msg != null ? msg.hashCode() : 0);
        result = 31 * result + (data != null ? data.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return ReflectionToStringBuilder.toString(this, ToStringStyle.SHORT_PREFIX_STYLE);
    }
}
