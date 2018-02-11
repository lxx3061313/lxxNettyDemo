package com.lxx.mydemo.nettydemo.service.common.web.mybatis.interceptor;

import com.lxx.mydemo.nettydemo.service.common.util.ReflectUtil;
import com.lxx.mydemo.nettydemo.service.common.web.mybatis.interceptor.dialect.DefaultDialect;
import com.lxx.mydemo.nettydemo.service.common.web.mybatis.interceptor.dialect.Dialect;
import java.sql.Connection;
import java.util.Properties;
import org.apache.ibatis.executor.statement.BaseStatementHandler;
import org.apache.ibatis.executor.statement.RoutingStatementHandler;
import org.apache.ibatis.executor.statement.StatementHandler;
import org.apache.ibatis.mapping.BoundSql;
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
        @Signature(type = StatementHandler.class, method = "prepare", args = { Connection.class }) })
public class StatementHandlerInterceptor implements Interceptor {

    /** 分页处理程序 */
    private Dialect dialect;

    @Override
    public Object intercept(Invocation invocation) throws Throwable {

        // 获取处理目标
        StatementHandler target = (StatementHandler) invocation.getTarget();
        if (target instanceof RoutingStatementHandler) {
            target = (BaseStatementHandler) ReflectUtil.getFieldValue(target, "delegate");
        }
        RowBounds rowBounds = (RowBounds) ReflectUtil.getFieldValue(target, "rowBounds");

        // 调整查询字符串
        if (rowBounds.getLimit() > 0 && rowBounds.getLimit() < RowBounds.NO_ROW_LIMIT) {
            BoundSql boundSql = target.getBoundSql();
            String sql = boundSql.getSql();

            sql = dialect.getLimitString(sql, rowBounds.getOffset(), rowBounds.getLimit());
            ReflectUtil.setFieldValue(boundSql, "sql", sql);
        }

        // 执行查询处理
        return invocation.proceed();
    }

    @Override
    public Object plugin(Object target) {
        return Plugin.wrap(target, this);
    }

    @Override
    public void setProperties(Properties properties) {

        String dialectClass = properties.getProperty("dialectClass");

        // 初始化物理查询处理程序
        if (dialectClass == null || dialectClass.isEmpty()) {
            dialect = new DefaultDialect();
        } else {
            try {
                dialect = (Dialect) Class.forName(dialectClass).newInstance();
            } catch (Exception e) {
                throw new IllegalArgumentException("Invalid dialect class " + dialectClass, e);
            }
        }
    }
}
