package monopoly7.models;

import java.util.List;
import java.util.Random;

import com.google.gson.annotations.Expose;

import lombok.Getter;
import monopoly7.event.DiceListener;
import monopoly7.event.DiceRollEvent;

public class Dice {
	@Expose private int sides;
	@Expose @Getter private int lastSum;
	@Expose private int[] lastRoll;
	private Random rando = new Random();
	private List<DiceListener> listeners;
	
	public static Dice generateCoin(){ return new Dice(1,2); }
	public static Dice generate1d6(){ return new Dice(1,6); }
	public static Dice generate2d6(){ return new Dice(2,6); }
	public static Dice generate1d20(){ return new Dice(1,20); }
	/*
	 * So the functionality for the following dice
	 * situation changes between games
	 * some care about both die, some care about the final
	 * number. Some consider a 0 and 00 a 100% while others
	 * consider it a 0%. This dice set warrants it's own
	 * subclass or at least additional functionality.
	public static Dice generatePercentile(){
		return Dice();
	}
	*/
	
	public Dice( int quantity, int sides ){
		this.sides = sides;
		lastRoll = new int[quantity];
		for(int i=0; i<quantity; i++){
			lastRoll[i] = 1;
		}
		lastSum = sides;
	}
	
	public int rollDice(){
		lastSum = 0;
		for(int i=0; i<lastRoll.length; i++){
			lastRoll[i] = rando.nextInt(sides) + 1;
			lastSum += lastRoll[i];
		}
		DiceRollEvent dre = new DiceRollEvent(this);
		listeners.forEach((DiceListener l) -> l.diceRolled(dre) );
		
		return lastSum;
	}
	
	public int[] getLastRoll(){
		int[] ret = new int[lastRoll.length];
		for(int i=0; i<lastRoll.length; i++){
			ret[i] = lastRoll[i];
		}
		return ret;
	}
}
