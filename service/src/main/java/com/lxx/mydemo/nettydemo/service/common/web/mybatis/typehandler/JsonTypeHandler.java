package com.lxx.mydemo.nettydemo.service.common.web.mybatis.typehandler;

import com.lxx.mydemo.nettydemo.service.common.util.json.JsonUtil;
import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import org.apache.ibatis.type.BaseTypeHandler;
import org.apache.ibatis.type.JdbcType;

/**
 * @author chao.zhu
 * @version 2016-07-04
 */
public class JsonTypeHandler<T> extends BaseTypeHandler<T> {
    private Class<T> clazz;

    public JsonTypeHandler(Class<T> clazz) {
        this.clazz = clazz;
    }

    @Override
    public void setNonNullParameter(PreparedStatement ps, int i, T t, JdbcType jdbcType) throws SQLException {
        ps.setString(i, JsonUtil.toJson(t));
    }

    @Override
    public T getNullableResult(ResultSet resultSet, String s) throws SQLException {
        return JsonUtil.of(resultSet.getString(s), clazz);

    }

    @Override
    public T getNullableResult(ResultSet resultSet, int i) throws SQLException {
        return JsonUtil.of(resultSet.getString(i), clazz);
    }

    @Override
    public T getNullableResult(CallableStatement callableStatement, int i) throws SQLException {
        return JsonUtil.of(callableStatement.getString(i), clazz);
    }
}
