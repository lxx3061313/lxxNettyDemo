package com.lxx.mydemo.nettydemo.service.common.util.pojo.node;

/**
 * miao.yang susing@gmail.com 2013年8月26日
 */
public final class NullNode extends AbstractNode implements LeafNode {

    private static final long serialVersionUID = -363616870094121511L;

    private final int depth;

    static final NullNode[] nodes = new NullNode[100];
    static {
        for (int i = 0; i < nodes.length; i++)
            nodes[i] = new NullNode(i);
    }

    public static NullNode getRoot() {
        return nodes[0];
    }

    private NullNode(int depth) {
        this.depth = depth;
    }

    @Override
    public Type getType() {
        return Type.NULL;
    }

    @Override
    public ValueNode get(int childIndex) {
        return getNext();
    }

    @Override
    public ValueNode get(String childName) {
        return getNext();
    }

    private ValueNode getNext() {
        int i = depth + 1;
        if (i >= nodes.length)
            throw new RuntimeException("递归深度过大, 可能出现死循环.");
        return nodes[i];
    }

    @Override
    public int hashCode() {
        return 32541;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        return getClass() == obj.getClass();
    }

    @Override
    public String toJson() {
        return "null";
    }

    @Override
    public String toString() {
        return null;
    }
}
