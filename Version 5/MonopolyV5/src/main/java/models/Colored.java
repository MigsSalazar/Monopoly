package main.java.models;

import com.google.gson.annotations.Expose;

public class Colored extends Property {

	
	private Suite suite;
	@Expose private int[] rent;
	@Expose private int grade;
	
	/**
	 * Child to Property class
	 * This Property type is meant to be upgradeable and signified with a color on the board
	 * @param n		String name of the Colored Property
	 * @param o		String name of the owner
	 * @param p		int position of the Colored Property
	 * @param pr	int price  of the Colored Property
	 * @param o		Owner object of the Colored Property. is null if unowned
	 * @param mb	boolean value storing if Colored Property is mortgaged or not
	 * @param s		Suite object of the Colored Property
	 * @param r		array of integers defining the Colored Property's rent values depending on the grade
	 * @param g		int grade of the Colored Property
	 */
	public Colored(String n, String o, int p, int pr, boolean mb, int[] r, int g) {
		super(n, o, p, pr, mb);
		// TODO Auto-generated constructor stub
		
		rent = r;
		grade = g;
		
	}
	
	public void setSuite(Suite s){
		suite = s;
	}
	
	/**
	 * 
	 * @return	containing suite as an int value
	 */
	public Suite getSuite(){
		return suite;
	}
	
	
	/**
	 * 
	 * @return	rent owed determined by property grade
	 */
	public int getRent(){
		return rent[grade];
	}
	
	public int getRentAt(int in){
		return rent[in];
	}
	
	public int getGrade(){
		return grade;
	}
	
	public void setGrade(int g){
		grade = g;
	}
	
	@Override
	public int getWorth(){
		return isMortgaged() ?
						getMortgageValue() : //if Property is mortgaged
						getPrice() + (int)( grade*rent[7] ); //if property is not mortgaged
	}
	
	@Override
	public int getRedeemableWorth(){
		return isMortgaged() ?
				0 : //if Property is mortgaged
				getMortgageValue() + (int)( grade*rent[7]*0.5 ); //if property is not mortgaged
	}

}
