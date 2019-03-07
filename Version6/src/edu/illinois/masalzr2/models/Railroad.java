package edu.illinois.masalzr2.models;

import java.io.Serializable;
import java.util.ArrayList;

import javax.swing.event.ChangeListener;

import com.google.gson.annotations.Expose;

import lombok.Getter;
import lombok.Setter;

public class Railroad extends Property implements Serializable{
	
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	@Getter @Setter @Expose 
	private Counter railsOwned;
	
	public Railroad(String n, int pos, int pr, String o, boolean m, int c, ArrayList<ChangeListener> listen){
		super(n, pos, pr, o, m, c, listen);
	}
	
	public Railroad(String n, int pos, int pr, String o, boolean m, int c, ArrayList<ChangeListener> listen, Counter r){
		super(n, pos, pr, o, m, c, listen);
		railsOwned = r;
	}
	
	@Override
	public void setOwner(String p) {

		if(owner == null || owner.equals("")) {
			railsOwned.add(1);
		}
		super.setOwner(p);
	
	}
	
	@Override
	public int getRent(){
		switch(railsOwned.getCount()) {
		case 1: return 25;
		case 2: return 50;
		case 3: return 100;
		case 4: return 200;
		default: return 0;
		}
	}
	
}
