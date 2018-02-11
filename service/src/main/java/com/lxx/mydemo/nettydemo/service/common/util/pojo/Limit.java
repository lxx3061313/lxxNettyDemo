package com.lxx.mydemo.nettydemo.service.common.util.pojo;

import java.io.Serializable;
import javax.validation.constraints.Min;

/** 
 * @author miao.yang susing@gmail.com
 * @version 创建时间：2012-4-12 下午8:21:45 
 * 用于描述查询的区段信息
 * like sql limit 0, 100
 */
public class Limit implements Serializable{

	private static final long serialVersionUID = -3709924425505549153L;
	
	@Min(value = 0)
	private int offset = 0;
	
	@Min(value=1)
	private int length = 0;
	
	public Limit(){}
	
	public Limit(int length){
		this.length = length;
	}

	public Limit(int offset, int length){
		this.offset = offset;
		this.length = length;
	}
	
	public int getOffset() {
		return offset;
	}
	
	public void setOffset(int offset) {
		this.offset = offset;
	}
	
	public int getLength() {
		return length;
	}

	public void setLength(int length) {
		this.length = length;
	}

	@Override
	public String toString() {
		return "limit [" + offset + ", " + length + "]";
	}

	public static final Limit create(int offset, int length){
		return new Limit(offset, length);
	}
	
	public static final Limit create(int length){
		return new Limit(length);
	}
	
}
