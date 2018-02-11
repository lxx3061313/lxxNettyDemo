package com.lxx.mydemo.nettydemo.service.common.util.pojo.node;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

/** 
 * miao.yang susing@gmail.com 2013年8月26日
 */
public class ListNode extends AbstractNode {

    private static final long serialVersionUID = -1457476908248073127L;

    private final List<ValueNode> list = new ArrayList<ValueNode>();
    
    @Override
    public Type getType() {
        return Type.list;
    }

    public void add(ValueNode node) {
        list.add(node);
    }
    
    @Override
    public Iterator<ValueNode> iterator() {
        return list.iterator();
    }

    @Override
    public ValueNode get(int childIndex) {
        return list.get(childIndex);
    }

    public List<ValueNode> getAll() {
        return Collections.unmodifiableList(list);
    }

    @Override
    public int size() {
        return list.size();
    }

    @Override
    public String toJson() {
        return JacksonSupport.toJson(this);
    }

}
