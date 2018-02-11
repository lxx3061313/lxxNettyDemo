package com.lxx.mydemo.nettydemo.service.common.web.mybatis.interceptor;

import com.lxx.mydemo.nettydemo.service.common.util.ReflectUtil;
import java.sql.Statement;
import java.util.Properties;
import org.apache.ibatis.executor.resultset.ResultSetHandler;
import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.plugin.Intercepts;
import org.apache.ibatis.plugin.Invocation;
import org.apache.ibatis.plugin.Plugin;
import org.apache.ibatis.plugin.Signature;
import org.apache.ibatis.session.RowBounds;

/**
 * @author lixiaoxiong
 * @version 2016-07-01
 */
@Intercepts({
        @Signature(type = ResultSetHandler.class, method = "handleResultSets", args = { Statement.class }) })
public class ResultSetHandlerInterceptor implements Interceptor {
    private static final RowBounds ROW_BOUNDS = new RowBounds();

    @Override
    public Object intercept(Invocation invocation) throws Throwable {

        ResultSetHandler target = (ResultSetHandler) invocation.getTarget();
        RowBounds rowBounds = (RowBounds) ReflectUtil.getFieldValue(target, "rowBounds");

        if (rowBounds.getLimit() > 0 && rowBounds.getLimit() < RowBounds.NO_ROW_LIMIT) {
            // 清除翻页参数，禁止FastResultSetHandler#skipRows跳过结果集
            ReflectUtil.setFieldValue(target, "rowBounds", ROW_BOUNDS);
        }
        return invocation.proceed();
    }

    @Override
    public Object plugin(Object target) {
        return Plugin.wrap(target, this);
    }

    @Override
    public void setProperties(Properties properties) {
    }
}
