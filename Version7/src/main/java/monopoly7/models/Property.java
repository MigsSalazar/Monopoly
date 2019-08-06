package monopoly7.models;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import com.google.gson.annotations.Expose;

import lombok.Getter;
import monopoly7.event.PropertyChangeEvent;
import monopoly7.event.PropertyChangeEvent.ChangeCode;
import monopoly7.event.PropertyChangeListener;

public abstract class Property {
	
	@Expose @Getter private String name = "";
	@Expose @Getter private String owner = "";
	@Expose @Getter private int position = 0;
	@Expose @Getter private int cost = 0;
	@Expose @Getter private int grade = 0;
	@Expose @Getter private boolean mortgaged = false;
	
	private transient List<PropertyChangeListener> listeners = new ArrayList<PropertyChangeListener>();
	
	public static final Comparator<Property> POSITION_ORDER = (Property p1, Property p2) -> (p1.getPosition() - p2.getPosition());
	public static final Comparator<Property> NAME_ORDER = (Property p1, Property p2) -> (p1.getName().compareTo(p2.getName()));
	
	public abstract int getRent();
	
	public void incGrade( int inc ){
		grade += inc;
		fireChange("grade increased", ChangeCode.GRADE, grade - inc, grade);
	}
	
	public void decGrade( int dec ){
		grade -= dec;
		fireChange("grade decreased", ChangeCode.GRADE, grade + dec, grade);
	}
	
	public void setGrade( int gr ){
		if( grade != gr ){
			int old = grade;
			grade = gr;
			fireChange( "grade changed", ChangeCode.GRADE, old, grade );
		}
	}
	
	public void setMortgaged( boolean mort ){
		if( mortgaged != mort ){
			mortgaged = mort;
			fireChange( "mortgaged changed", ChangeCode.MORTGAGE, !mortgaged, mortgaged );
		}
	}
	
	public void setOwner( String owner ){
		String old = this.owner;
		this.owner = owner;
		fireChange("owner changed", ChangeCode.OWNER, old, this.owner );
	}
	
	public void upgrade( int up ){
		grade += up;
		fireChange( "property upgraded", ChangeCode.GRADE, grade - up, grade );
	}
	
	public void downgrade( int down ){
		grade -= down;
		fireChange( "property downgraded", ChangeCode.GRADE, grade + down, grade );
	}
	
	public int getWorth(){
		//I use the method calls to the instance variables to make mocking and tests easier 
		return isMortgaged() ? (int)(getCost()/2) : getCost();
	}
	
	public int getLiquidWorth(){
		//I use the method calls to the instance variables to make mocking and tests easier
		return isMortgaged() ? 0 : (int)(getCost()/2);
	}
	
	private void fireChange(String msg, PropertyChangeEvent.ChangeCode code, Object ov, Object nv){
		PropertyChangeEvent pce = new PropertyChangeEvent(this, msg, code, ov, nv);
		listeners.forEach( (PropertyChangeListener l) -> l.propertyStateChanged(pce) );
	}
	
	public void addPropertyChangeListener( PropertyChangeListener pcl ){
		listeners.add(pcl);
	}
	
	public void removePropertyChangeListener( PropertyChangeListener pcl ){
		listeners.remove(pcl);
	}

}
