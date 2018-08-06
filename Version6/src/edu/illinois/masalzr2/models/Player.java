package edu.illinois.masalzr2.models;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;

import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import com.google.gson.annotations.Expose;

import lombok.Getter;
import lombok.Setter;


public class Player implements ChangeListener, Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	@Getter @Setter @Expose private String name;
	@Getter @Setter @Expose private int id;
	@Getter @Setter @Expose private int cash;
	@Expose private Counter position;
	@Getter @Setter @Expose private int jailCard;
	@Getter @Setter @Expose private boolean bankrupt;
	@Getter @Setter private HashMap<String, Property> props;
	@Getter @Setter private ArrayList<ChangeListener> listeners;
	
	public Player(String n, int i, int c, int p, int j, boolean b, HashMap<String, Property> pr, ArrayList<ChangeListener> listen){
		name = n;
		id = i;
		cash = c;
		position = new Counter(0,40,0);
		jailCard = j;
		bankrupt = b;
		props = pr;
		listeners = listen;
	}
	
	public Player(int c){
		name = "";
		id = -1;
		position = new Counter(0,40,0);
		cash = c;
		jailCard = 0;
		props = new HashMap<String, Property>();
		listeners = new ArrayList<ChangeListener>();
	}
	
	public void addCash(int a){
		cash += a;
		fireChange();
	}
	
	public void subCash(int s){
		cash -= s;
		fireChange();
	}
	
	public int getPosition(){
		return position.getCount();
	}
	
	public void setPosition(int p){
		position.setCount(p);
		fireChange();
	}
	
	public int addPosition(int p){
		position.add(p);
		fireChange();
		return position.getCount();
	}
	
	public void addJailCard(int a){
		jailCard += a;
		fireChange();
	}
	
	public void addOneJailCard(){
		addJailCard(1);
	}
	
	public void subJailCard(int s){
		addJailCard(-1 * s);
	}
	
	public void subOneJailCard(){
		addJailCard(-1);
	}
	
	public boolean ownsProp(Property p){
		return props.containsValue(p);
	}
	
	public void addProp(Property p){
		propsExist();
		props.put(p.getName(), p);
		p.addListener(this);
		fireChange();
	}
	
	public void addProperties(Collection<Property> inProps){
		propsExist();
		for(Property pr : inProps){
			props.put(pr.getName(), pr);
			pr.addListener(this);
		}
		fireChange();
	}
	
	
	public void removeProp(Property p){
		removeProp(p.getName());
		p.removeListener(this);
		fireChange();
	}
	
	private void removeProp(String p){
		propsExist();
		props.remove(p);
		fireChange();
	}
	
	public int getWealth(){
		propsExist();
		
		int ret = cash;
		
		for(Property p : props.values()){
			ret += p.getWorth();
		}
		
		return ret;
	}
	
	public int getLiquidationWorth(){
		int ret = cash;
		
		propsExist();
		
		for(Property p : props.values()){
			ret += p.getLiquidationWorth();
		}
		
		return ret;
		
	}
	
	private void propsExist(){
		if(props == null){
			props = new HashMap<String, Property>();
		}
	}
	
	public void addChangeListener(ChangeListener cl){
		if(listeners == null) {
			listeners = new ArrayList<ChangeListener>();
		}
		if(!listeners.contains(cl)){
			listeners.add(cl);
		}
	}
	
	public void removeListener(ChangeListener ce) {
		listeners.remove(ce);
	}
	
	private void fireChange(){
		//System.out.println("Change has been fired in Player");
		if(listeners == null) {
			return;
		}
		ChangeEvent ce = new ChangeEvent(this);
		for(ChangeListener cl : listeners){
			cl.stateChanged(new ChangeEvent(ce));
		}
	}

	@Override
	public void stateChanged(ChangeEvent e) {
		//System.out.println("Player has detected a change from is properties");
		fireChange();
	}
	
}
