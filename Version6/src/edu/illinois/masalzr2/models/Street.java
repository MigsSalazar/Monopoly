package edu.illinois.masalzr2.models;

import java.io.Serializable;
import java.util.ArrayList;

import javax.swing.event.ChangeListener;

import com.google.gson.annotations.Expose;

import lombok.Getter;

public class Street extends Property implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	@Getter @Expose 
	private int grade;
	@Getter @Expose 
	private int upgradeCost;
	@Expose 
	private int[] rent;
	
	public Street(String n, int pos, int pr, String o, boolean m, int c, ArrayList<ChangeListener> listen, int g, int uc, int[] r){
		super(n, pos, pr, o, m, c, listen);
		grade = g;
		upgradeCost = uc;
		rent = r;
	}
	
	public Street(){
		grade 		= 0;
		rent 		= new int[5];
	}
	
	public void setGrade(int g ){
		grade = g;
		fireChange();
	}
	
	public int getRent(int num){
		return rent[num];
	}
	
	@Override
	public int getRent(){
		return rent[grade];
	}
	
	@Override
	public int getWorth(){
		//System.out.println("I am "+name+" and my grade is "+grade+" with an upgrade cost of "+upgradeCost);
		return getPrice() + (grade*upgradeCost);
	}
	
	@Override
	public int getLiquidationWorth(){
		return super.getLiquidationWorth() + (grade*upgradeCost);
	}
	
}
