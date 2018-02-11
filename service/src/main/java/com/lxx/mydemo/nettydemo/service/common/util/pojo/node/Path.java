package com.lxx.mydemo.nettydemo.service.common.util.pojo.node;

import com.google.common.collect.Lists;
import java.io.Serializable;
import java.util.Collections;
import java.util.List;

/** 
 * miao.yang susing@gmail.com 2013年9月4日
 */
public class Path implements Comparable<Path> ,Serializable{

    private static final long serialVersionUID = -3307689841900403276L;

    private final Path parent;
    private final String name;
    private final int depth;

    private volatile String string = null;

    public static final Path GOD = new Path("$");

    public Path(String name) {
        this.parent = null;
        this.name = name;
        this.depth = 0;
    }

    private Path(Path parent, String name) {
        this.parent = parent;
        this.name = name;
        this.depth = parent.depth + 1;
    }

    public Path getParent() {
        return parent;
    }

    public String getName() {
        return name;
    }

    public List<Path> getBubbles() {
        List<Path> list = Lists.newArrayList();
        for (Path p = this; p != null; p = p.parent) {
            list.add(p);
        }
        Collections.reverse(list);
        return list;
    }

    @Override
    public int hashCode() {
        return toString().hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        return toString().equals(obj.toString());
    }

    public String toString() {
        if (string != null)
            return string;
        StringBuilder sb = new StringBuilder();
        for (Path ln : getBubbles()) {
            if (sb.length() != 0)
                sb.append('.');
            sb.append(PathFormat.convert(ln.name));
        }
        return string = sb.toString();
    }

    public Path append(String name) {
        return new Path(this, name);
    }
    
    public static Path create(Path parent, String name) {
        if (parent == null)
            return new Path(name);
        else
            return parent.append(name);
    }

    public static void main(String[] args) {
        Path node = new Path("a");

        node = node.append("b").append("c").append("d");

        List<Path> list = node.getBubbles();
        list.add(new Path("f"));
        list.add(node.parent.parent.append("a"));

        Collections.sort(list);

        for (Path n : list) {
            System.out.println(n);
        }
    }

    @Override
    public int compareTo(Path o) {
        return toString().compareTo(o.toString());
    }

}
