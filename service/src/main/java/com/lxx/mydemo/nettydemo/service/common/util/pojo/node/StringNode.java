package com.lxx.mydemo.nettydemo.service.common.util.pojo.node;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.Date;

/**
 * miao.yang susing@gmail.com 2013年8月26日
 */
public class StringNode extends AbstractNode implements LeafNode {

    private static final long serialVersionUID = -606620138622554905L;

    private final String text;

    public StringNode(String text) {
        this.text = text;
    }

    @Override
    public Type getType() {
        return Type.string;
    }

    @Override
    public Boolean toBooleanValue() {
        return "1".equals(text) || "true".equalsIgnoreCase(text);
    }

    @Override
    public Date toDateValue() {
        return Timestamp.valueOf(text);
    }

    @Override
    public Number toNumber() {
        return new BigDecimal(text);
    }

    @Override
    public String toString() {
        return text;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((text == null) ? 0 : text.hashCode());
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
        StringNode other = (StringNode) obj;
        if (text == null) {
            if (other.text != null)
                return false;
        } else if (!text.equals(other.text))
            return false;
        return true;
    }

    @Override
    public String toJson() {
        return JacksonSupport.toJson(text);
    }

}
