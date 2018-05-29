package edu.illinois.masalzr2.models;

import java.util.ArrayList;

import javax.swing.event.ChangeListener;

public class Street extends Property {
	
	private int grade;
	private int upgradeCost;
	private int[] rent;
	
	public Street(String n, int pos, int pr, String o, boolean m, ArrayList<ChangeListener> listen, int g, int uc, int[] r){
		super(n, pos, pr, o, m, listen);
		grade = g;
		upgradeCost = uc;
		rent = r;
	}
	
	public Street(){
		grade 		= 0;
		rent 		= new int[5];
	}
	
	public int getUpgradeCost(){
		return upgradeCost;
	}
	
	public void setGrade(int g ){
		grade = g;
	}
	
	public int getGrade(){
		return grade;
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
		return 0;
	}
	
	@Override
	public int getLiquidationWorth(){
		return 0;
	}
	
}
