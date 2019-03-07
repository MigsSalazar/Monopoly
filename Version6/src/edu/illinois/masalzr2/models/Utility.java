package edu.illinois.masalzr2.models;

import java.io.Serializable;
import java.util.ArrayList;

import javax.swing.event.ChangeListener;

import com.google.gson.annotations.Expose;

import lombok.Getter;
import lombok.Setter;

public class Utility extends Property implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	@Getter @Setter @Expose 
	private Counter utilityOwned;
	@Getter @Setter 
	private Dice gameDice;
	
	public Utility(String n, int pos, int pr, String o, boolean m, int c, ArrayList<ChangeListener> listen){
		super(n, pos, pr, o, m, c, listen);
	}
	
	public Utility(String n, int pos, int pr, String o, boolean m, int c, ArrayList<ChangeListener> listen, Counter u){
		super(n, pos, pr, o, m, c, listen);
		utilityOwned = u;
	}
	
	@Override
	public void setOwner(String p) {

		if(owner == null || owner.equals("")) {
			utilityOwned.add(1);
		}
		super.setOwner(p);
	}
	
	@Override
	public int getRent(){
		int mod = (utilityOwned.getCount()==1)?4:10;
		return mod * gameDice.getLastRoll();
	}
	
}
