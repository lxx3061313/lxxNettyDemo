package com.lxx.mydemo.nettydemo.service.common.util;

import java.util.Collections;
import java.util.List;
import java.util.function.Consumer;
import org.apache.commons.collections.CollectionUtils;

/**
 * @author lixiaoxiong
 * @version 2016-12-07
 */
public class PageUtil<T> {
    private int offset=0;
    private int limit=20;
    private List<T> sourceList;

    public PageUtil(List<T> sourceList, int limit, int offset) {
        this.sourceList = sourceList;
        this.limit = limit;
        this.offset = offset;
    }

    public boolean hasNext() {
        return offset < sourceList.size();
    }

    public List<T> next(int offset, int limit) {
        if(CollectionUtils.isEmpty(sourceList)){
            return Collections.EMPTY_LIST;
        }
        if (offset >= sourceList.size()) {
            throw new ArrayIndexOutOfBoundsException("out of bound, size: " +
                    sourceList.size() + ", offset: " + offset);
        }
        if (offset + limit > sourceList.size()) {
            return sourceList.subList(offset, sourceList.size());
        }
        return sourceList.subList(offset, offset + limit);
    }

    public List<T> next(int limit) {
        List<T> resultList = next(offset, limit);
        offset += limit;
        return resultList;
    }

    public List<T> next() {
        return next(limit);
    }

    public void foreach(Consumer<List<T>> consumer){
        while (this.hasNext()){
            consumer.accept(this.next());
        }
    }

    public int getOffset() {
        return offset;
    }

    public void setOffset(int offset) {
        this.offset = offset;
    }

    public int getLimit() {
        return limit;
    }

    public void setLimit(int limit) {
        this.limit = limit;
    }

    public List<T> getSourceList() {
        return sourceList;
    }

    public void setSourceList(List<T> sourceList) {
        this.sourceList = sourceList;
    }

    public static Builder createBuilder(){
        return new Builder();
    }

    public static class Builder<T>{
        private int currentPage;
        private int pageSize;
        private List<T> sourceList;

        public Builder on(List<T> source){
            this.sourceList = source;
            return this;
        }

        public Builder currentPage(int currentPage){
            this.currentPage = currentPage;
            return this;
        }

        public Builder pageSize(int pageSize){
            this.pageSize = pageSize;
            return this;
        }

        public PageUtil build(){
            int tmp = (currentPage - 1) * pageSize;
            tmp =  tmp < 0 ? 0 : tmp;
            return new PageUtil(sourceList, pageSize, tmp);
        }

    }
}
