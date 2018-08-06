package edu.illinois.masalzr2.models;

import java.io.Serializable;
import java.util.ArrayList;

import com.google.gson.annotations.Expose;

public class Suite implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private ArrayList<Street> streets;
	@Expose private ArrayList<String> names;
	@Expose private String colorName;
	@Expose private int colorValue;
	
	
	/**
	 * Explicit constructor that takes in 2-3 Street objects to include within the suite.
	 * To define a 2 Street suite, the third parameter must be null.
	 * Once the Street objects are set, they cannot be changed.
	 * The Street objects can be passed through in any order as they will be sorted in order of board position.
	 * @param s0 		First Street object
	 * @param s1		Second Street object
	 * @param s2 		Third Street object
	 * @param c 		String object of the Streets color
	 */
	public Suite(Street s0, Street s1, Street s2, String cn, int cv){
		
		colorName = cn;
		colorValue = cv;
		
		streets = new ArrayList<Street>();
		names = new ArrayList<String>();
		streets.add(s0);
		streets.add(s1);
		
		names.add(s0.getName());
		names.add(s1.getName());
		
		s0.setColor(colorValue);
		s1.setColor(colorValue);
		
		if(s2!=null){
			streets.add(s2);
			names.add(s2.getName());
			s2.setColor(colorValue);
		}
		
		names.sort(String.CASE_INSENSITIVE_ORDER);
		
		streets = sortedByPosition();
		
	}
	
	/**
	 * The String value of the color name. There is no set list of color names so the color can be real
	 * or gibberish.
	 * @return 			String - the passed in value of the Suite name
	 */
	public String getColor(){
		return colorName;
	}
	
	/**
	 * Determines if the passed in player owns all properties in the suite
	 * otherwise known as the playing monopolizing a suite
	 * @param p		The player object of the assumed owner
	 * @return 		boolean: True if the player has monopolized the suite
	 * 						 False if the player has not monopolized the suite
	 */
	public boolean isMonopolized(Player p){
		
		return isMonopolized(p.getName());
		
	}
	
	/**
	 * Takes the name of a player and determines if they currently own all the properties
	 * contained in this suite
	 * @param owner 	The name of the assumed owner as a String
	 * @return 			boolean: True if the player owns all properties
	 * 							 False if the player does not own all of the properties
	 */
	private boolean isMonopolized(String owner){
		boolean retval = true;
		
		for(Property prop : streets){
			retval &= prop.getOwner().equals(owner);
		}
		
		return retval;
	}
	
	/**
	 * Determines if the Street Properties held within the Suite can be upgraded/downgraded
	 * Determinants are:
	 *  - All Properties are owned by the same player
	 *  - All Properties are not mortgaged
	 * @return		boolean: True if properties can be altered
	 * 						 False if properties cannot be altered
	 */
	public boolean isBuildable(){
		boolean retval = true;

		String owner = streets.get(0).getOwner();
		
		if(!isMonopolized(owner)){
			return false;
		}
		
		for(Property prop : streets){
			retval &= !prop.isMortgaged();
		}
		
		return retval;
	}
	
	/**
	 * Returns a new ArrayList that contains all the Street objects in this suite sorted by position
	 * @return 		A new ArrayList of the Streets in the Suite
	 */
	public ArrayList<Street> sortedByPosition(){
		ArrayList<Street> retval = new ArrayList<Street>();
		retval.addAll(streets);
		retval.sort(Property.getPositionComparator());
		return retval;
	}
	
	/**
	 * Returns a new ArrayList that contains all the Street objects in this suite sorted by name
	 * @return 		A new ArrayList of the Streets in the Suite
	 */
	public ArrayList<Street> sortedByName(){
		ArrayList<Street> retval = new ArrayList<Street>();
		retval.addAll(streets);
		retval.sort(Property.getNameComparator());
		return retval;
	}

	/**
	 * Finds the highest grade of all Streets in the Suite
	 * @return 			integer - a value between 0-5
	 */
	public int highestGrade(){
		int retval = 0;
		
		for(Street s : streets){
			retval = retval>s.getGrade() ? retval : s.getGrade();
		}
		
		return retval;
	}
	
	/**
	 * Finds the smallest grade of all the Streets in the Suite
	 * @return 		integer - a value between 0-5
	 */
	public int lowestGrade(){
		int retval = 100;
		
		for(Street s : streets){
			retval = retval<s.getGrade() ? retval : s.getGrade();
		}
		
		return retval;
	}
	
	/**
	 * Compares the highest and lowest grades and returns their difference
	 * @return 		integer - a positive value that notes the greatest difference between the highest and lowest grade in the suite
	 */
	public int gradeDisparity(){
		return highestGrade() - lowestGrade();
	}
	
	/**
	 * Summation of all the grades in the suite
	 * @return 		integer - sum of all grades
	 */
	public int totalGrade(){
		int retval = 0;
		
		for(Street s : streets){
			retval += s.getGrade();
		}
		
		return retval;
	}
	
	
}
