package com.lxx.mydemo.nettydemo.service.common.util.pojo.node;

import java.util.Date;

/**
 * miao.yang susing@gmail.com 2013年8月26日
 */
public class NumericNode extends AbstractNode implements LeafNode {

    private static final long serialVersionUID = -4314109649333671292L;

    private final Number value;

    public NumericNode(Number value) {
        this.value = value;
    }

    @Override
    public Type getType() {
        return Type.number;
    }

    @Override
    public Boolean toBooleanValue() {
        return value.intValue() == 1;
    }

    @Override
    public Date toDateValue() {
        return new Date(value.longValue());
    }

    @Override
    public Number toNumber() {
        return value;
    }


    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((value == null) ? 0 : value.hashCode());
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
        NumericNode other = (NumericNode) obj;
        if (value == null) {
            if (other.value != null)
                return false;
        } else if (!value.equals(other.value))
            return false;
        return true;
    }

    @Override
    public String toJson() {
        return value.toString();
    }

    @Override
    public String toString() {
        return value.toString();
    }
}
