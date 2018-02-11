package com.lxx.mydemo.nettydemo.service.common.util.pojo;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

/** 
 * 通用查询接口的返回信息.  [count , recordList]
 * @author miao.yang susing@gmail.com
 * @version 创建时间：2012-2-16 下午6:57:28 
 */
public final class ResultList<E extends Serializable> implements Iterable<E>, Serializable{

	private static final long serialVersionUID = 2789771757285593580L;

	private List<E> list;
	
	private int count = 0;

	public ResultList(){
		this(new ArrayList<E>());
	}
	public ResultList(List<E> list){
		this(0, list);
	}
	public ResultList(int count){
		this(count, new ArrayList<E>());
	}
	
	public ResultList(int count, List<E> list){
		if(list == null)
			list = Collections.emptyList();
		this.list = list;
		this.count = Math.max(count, list.size());
	}
	
	public void add(E ele){
		list.add(ele);
	}
	
	@Override
	public Iterator<E> iterator() {
		return list.iterator();
	}

	public List<E> getList() {
		return list;
	}

	public void setList(List<E> list) {
		this.list = list;
	}

	public int getCount() {
		return count;
	}

	public void setCount(int count) {
		this.count = count;
	}

}
