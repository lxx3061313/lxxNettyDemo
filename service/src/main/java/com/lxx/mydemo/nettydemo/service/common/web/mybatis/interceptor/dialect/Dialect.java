package com.lxx.mydemo.nettydemo.service.common.web.mybatis.interceptor.dialect;

/**
 * @author lixiaoxiong
 * @version 2016-07-01
 */
public interface Dialect {
    /**
     * 拼接SQL文‘limit’及'offset'数据
     *
     * @param sql 原始SQL
     * @param offset 偏移量
     * @param limit 获取记录数
     * @return 拼接完成的SQL
     */
    String getLimitString(String sql, int offset, int limit);
}
