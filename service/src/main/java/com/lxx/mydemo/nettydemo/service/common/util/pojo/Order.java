package com.lxx.mydemo.nettydemo.service.common.util.pojo;

import java.io.Serializable;
import javax.validation.constraints.NotNull;

/** 
 * 表示所有可定义的排序规则.
 * @author miao.yang susing@gmail.com
 * @version 创建时间：2012-4-17 下午7:16:59 
 */
public class Order implements Serializable{

	private static final long serialVersionUID = -7919046237869173480L;
	
	@NotNull
	private String item;
	private boolean desc= false;

	public Order() {}
	
	public Order(String item, boolean desc) {
		this.item = item;
		this.desc = desc;
	}
	
	public String getItem() {
		return item;
	}
	public void setItem(String item) {
		this.item = item;
	}
	public boolean isDesc() {
		return desc;
	}
	public void setDesc(boolean desc) {
		this.desc = desc;
	}
	public static long getSerialversionuid() {
		return serialVersionUID;
	}

}
