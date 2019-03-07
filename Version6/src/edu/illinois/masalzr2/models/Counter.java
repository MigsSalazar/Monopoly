package edu.illinois.masalzr2.models;

import java.io.Serializable;

import com.google.gson.annotations.Expose;

import lombok.Getter;

public class Counter implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	@Getter @Expose
	private int count;
	@Getter @Expose
	private int max;
	@Getter @Expose 
	private int min;
	
	public Counter(){
		max = 0;
		min = 0;
		setCount(0);
	}
	
	public Counter(int c){
		max = 0;
		min = 0;
		setCount(c);
	}
	
	public Counter(int c, int max, int min){
		this.max = max;
		this.min = min;
		setCount(c);
	}
	
	public void setCount(int c) {
		count = c;
		if(max <= min) {
			return;
		}
		if(count > max) {
			count %= max;
		}
		if(count < min) {
			count = min;
		}
	}
	
	public void setMax(int m) {
		max = m;
		setCount(count);
	}
	
	public void setMin(int m) {
		min = m;
		setCount(count);
	}
	
	public int add(int a){
		count += a;
		setCount(count);
		return count;
	}
	
	public int sub(int s){
		return add(s * -1);
	}
	
}
