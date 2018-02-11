package com.lxx.mydemo.nettydemo.service.common.util;

import java.io.Serializable;
import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

/**
 * @author lixiaoxiong
 * @version 2016-07-02
 */
public class PageRequest {

    private Page page;

    public Page getPage() {
        return page;
    }

    public void setPage(Page page) {
        this.page = page;
    }

    public int getOffset() {
        int tmp = (page.curPage - 1) * page.pageSize;
        return tmp < 0 ? 0 : tmp;
    }

    public int getLimit() {
        return page.pageSize;
    }

    public PageRequest() {
        page = new Page(15, 1);
    }

    public PageRequest(int curPage, int pageSize){
        page = new Page(pageSize, curPage);
    }

    public PageRequest(Page page){
        this.page = page;
    }

    public static class Page implements Serializable {

        private static final long serialVersionUID = -9116229816861557536L;

        int pageSize = 15;

        int curPage = 1;

        public Page() { }

        public Page(int pageSize, int curPage) {
            this.pageSize = pageSize;
            this.curPage = curPage;
        }

        public int getPageSize() {
            return pageSize;
        }

        public void setPageSize(int pageSize) {
            this.pageSize = pageSize;
        }

        public int getCurPage() {
            return curPage;
        }

        public void setCurPage(int curPage) {
            this.curPage = curPage;
        }

    }

    @Override
    public String toString() {
        return ReflectionToStringBuilder.toString(this, ToStringStyle.SHORT_PREFIX_STYLE);
    }
}
