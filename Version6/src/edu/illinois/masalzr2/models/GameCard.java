package edu.illinois.masalzr2.models;


import java.io.Serializable;

import com.google.gson.annotations.Expose;

import lombok.Getter;
import lombok.Setter;

/**
 * More like a struct or predefined map than a class or object. 
 * This class performs no special operations and has no knowledge
 * of the impact of any of it's members. I keep this model class
 * as it provides an easy and simple way to parse gamecards for jsons
 * @author Miguel Salazar
 *
 */
public class GameCard implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Getter @Setter @Expose 
	private String text;
	
	@Getter @Setter @Expose 
	private int moneyEarned;
	@Getter @Setter @Expose 
	private boolean globalFunds;
	@Getter @Setter 
	@Expose private int baseMovement;
	
	@Getter @Setter @Expose 
	private boolean getOutOfJail;
	
	@Getter @Setter @Expose 
	private boolean goToJail;
	
	@Getter @Setter @Expose 
	private String findNearest;
	@Getter @Setter @Expose 
	private String findThis;
	
	@Getter @Setter @Expose 
	private boolean propRenovation;
	@Getter @Setter @Expose 
	private int houseCost;
	@Getter @Setter @Expose 
	private int hotelCost;
	
	public GameCard(String t,
					int me, boolean gf,
					int bm,
					boolean oj,
					boolean tj,
					String fn, String ft,
					boolean pr, int house, int hotel){
		text = t;
		moneyEarned = me;
		globalFunds = gf;
		baseMovement = bm;
		getOutOfJail = oj;
		goToJail = tj;
		findNearest = fn;
		findThis = ft;
		propRenovation = pr;
		houseCost = house;
		hotelCost = hotel;
	}

}
