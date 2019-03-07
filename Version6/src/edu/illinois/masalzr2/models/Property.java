package edu.illinois.masalzr2.models;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import com.google.gson.annotations.Expose;

import lombok.Getter;
import lombok.Setter;


public class Property implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	@Getter @Setter @Expose 
	protected String name;
	@Getter @Setter @Expose 
	protected int position;
	@Getter @Setter @Expose 
	protected int price;
	@Getter @Expose 
	protected String owner = "";
	@Getter @Expose 
	protected boolean mortgaged;
	@Getter @Expose 
	protected int color;
	private List<ChangeListener> listeners;
	
	public transient static final Comparator<Property> POSITION_ORDER = new SortByPosition();
	public transient static final Comparator<Property> NAME_ORDER = new SortByName();
	
	public Property(String n, int pos, int pr, String o, boolean m, int c, List<ChangeListener> listen){
		name = n;
		position = pos;
		price = pr;
		owner = o;
		mortgaged = m;
		color = c;
		listeners = listen==null? new ArrayList<ChangeListener>() : listen;
	}

	public Property(){
		name = "";
		position = -1;
		price = 0;
		owner = "";
		mortgaged = false;
		color = 1;
		listeners = new ArrayList<ChangeListener>();
	}
	
	public void setMortgaged(boolean m) {
		mortgaged = m;
		fireChange();
	}
	
	/**
	 * Value of the Property as it relates to taxable income.
	 * Differs from Liquidation Worth in the value goes to your net worth and towards the Property's value
	 * when selling or trading but does not necessarily mean the property can be liquidated further
	 * 
	 * @return		int - property value following the equation:
	 * 				worth = price - mortgage value * (mortgage boolean as 1 or 0)
	 */
	public int getWorth(){
		return isMortgaged() ? mortgageValue() : getPrice() ;
	}
	
	/**
	 * Value of this Property as it relates to liquidation value.
	 * Differs from the Property's Worth in that this value shows the total liquidatable value of the
	 * Property 
	 * @return 		int - value of the Property's liquidizable assets following the equation:
	 * 				liquidation worth = mortgage value * (mortgage boolean as a 1 or 0)
	 */
	public int getLiquidationWorth(){
		return isMortgaged() ? 0 : mortgageValue();
	}
	
	public void setOwner(String o){
		owner = o;
		//System.out.println(name+" was bought buy "+owner);
		fireChange();
	}
	
	public int mortgageValue(){
		return (price/2);
	}
	
	public int getRent(){
		return 0;
	}
	
	public void setColor(int c) {
		color = c;
		fireChange();
	}
	
	public void addListener(ChangeListener ce){
		if(listeners == null) {
			listeners = new ArrayList<ChangeListener>();
		}
		if(!listeners.contains(ce)){
			listeners.add(ce);
		}
	}
	
	public void removeListener(ChangeListener ce) {
		listeners.remove(ce);
	}
	
	protected void fireChange(){
		ChangeEvent ce = new ChangeEvent(this);
		//System.out.println("Change has been fired in property");
		for(ChangeListener cl : listeners){
			cl.stateChanged(ce);
		}
		
	}
	
	private static class SortByPosition implements Comparator<Property>{
		public int compare(Property o0, Property o1) {
			return o0.getPosition() - o1.getPosition();
		}
	}
	
	private static class SortByName implements Comparator<Property>{
		public int compare(Property o0, Property o1) {
			return o0.getName().compareTo(o1.getName());
		}
	}
	
}
