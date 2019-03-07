package edu.illinois.masalzr2.models;

import java.io.Serializable;
import java.util.Random;

import com.google.gson.annotations.Expose;

import lombok.Getter;
import lombok.Setter;

public class Dice implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	@Getter @Setter @Expose 
	private int sides;
	@Getter @Setter @Expose 
	private int[] dice;
	@Getter @Setter @Expose 
	private int lastRoll;
	
	public Dice(){
		sides = 6;
		dice = new int[1];
		lastRoll = 0;
	}
	
	public Dice(int s){
		sides = s;
		dice = new int[1];
		lastRoll = 0;
	}
	
	public Dice(int s, int d){
		sides = s;
		dice = new int[d];
		lastRoll = 0;
	}
	
	public int roll(){
		Random rand = new Random();
		lastRoll = 0;
		for(int i=0; i<dice.length; i++){
			dice[i] = rand.nextInt(sides) + 1;
			lastRoll += dice[i];
		}
		return lastRoll;
	}
	
	public int diceNum(){
		return dice.length;
	}
	
	
}
