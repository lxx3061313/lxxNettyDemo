package com.lxx.mydemo.nettydemo.service.common.util.pojo.node;


/**
 * miao.yang susing@gmail.com 2013年8月26日
 */
public class BooleanValue extends AbstractNode implements LeafNode {

    private static final long serialVersionUID = 2016854980051214673L;

    private final boolean value;

    public BooleanValue(boolean value) {
        this.value = value;
    }

    @Override
    public Type getType() {
        return Type.bool;
    }

    @Override
    public Boolean toBooleanValue() {
        return value;
    }


    @Override
    public Number toNumber() {
        return value ? 1 : 0;
    }


    @Override
    public String toJson() {
        return Boolean.toString(value);
    }

    @Override
    public String toString() {
        return Boolean.toString(value);
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + (value ? 1231 : 1237);
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        BooleanValue other = (BooleanValue) obj;
        return value == other.value;
    }

}
