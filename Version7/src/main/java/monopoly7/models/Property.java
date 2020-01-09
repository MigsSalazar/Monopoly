package monopoly7.models;

import java.awt.Color;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Set;

import javax.security.auth.RefreshFailedException;
import javax.security.auth.Refreshable;

import com.google.gson.annotations.Expose;

import lombok.Getter;
import monopoly7.event.PropertyChangeEvent;
import monopoly7.event.PropertyChangeEvent.ChangeCode;
import monopoly7.event.PropertyChangeListener;

public class Property implements Refreshable{
	
	@Expose @Getter private String name = "";
	@Expose @Getter private String owner = "";
	@Expose @Getter private int[] rents;
	@Expose @Getter private int position = 0;
	@Expose @Getter private int cost = 0;
	@Expose @Getter private int grade = 0;
	@Expose @Getter private boolean mortgaged = false;
	@Expose @Getter private String hexColor = "0xFFFFFF";
	@Getter private transient Color displayColor = Color.decode(hexColor);
	
	private transient Set<PropertyChangeListener> listeners = new HashSet<PropertyChangeListener>();
	
	public static final Comparator<Property> POSITION_ORDER = (Property p1, Property p2) -> (p1.getPosition() - p2.getPosition());
	public static final Comparator<Property> NAME_ORDER = (Property p1, Property p2) -> (p1.getName().compareTo(p2.getName()));
	
	/**
	 * NOT RECOMMENDED FOR REGULAR USE
	 * Creates an empty object preferred for testing behavior rather than values
	 */
	public Property(){
		
	}
	
	/**
	 * Proper constructor to use when creating a Property object
	 * @param n 	Name of the property
	 * @param o 	Owner
	 * @param r 	int[] of INCREASING rent values. Can be of size 1 or 0
	 * @param p 	Position of property relative to that START square
	 * @param c 	Cost to purchase the property
	 * @param g 	Current grade of the property
	 * @param m 	Boolean storing whether or not the property is mortgaged
	 * @param h 	Color to use as a highlighting for this property. Useful when creating color coded information displays
	 */
	public Property( String n, String o, int[] r, int p, int c, int g, boolean m, String h ){
		name = n;
		owner = o;
		rents = r;
		position = p;
		cost = c;
		grade = g;
		mortgaged = m;
		hexColor = h;
		displayColor = Color.decode(hexColor);
	}
	
	/**
	 * Returns the rent as a function of the grade
	 * @return	Current cost of rent
	 */
	public int getRent(){
		return getRents()[getGrade()];
	}
	
	/**
	 * Increases the grade of the property.
	 * @param inc 	Value to be added to the property's grade
	 */
	public void incGrade( int inc ){
		if( inc == 0 ){
			return;
		}
		grade += inc;
		fireChange("grade increased", ChangeCode.GRADE, grade - inc, grade);
	}
	
	/**
	 * Decreases the grade of the property
	 * @param dec 	Value to be subtracted from the property's grade
	 */
	public void decGrade( int dec ){
		if( dec == 0 ){
			return;
		}
		grade -= dec;
		fireChange("grade decreased", ChangeCode.GRADE, grade + dec, grade);
	}
	
	/**
	 * Hard sets the grade of the property
	 * @param gr 	The value you wish the grade to be set to
	 */
	public void setGrade( int gr ){
		if( grade != gr ){
			int old = grade;
			grade = gr;
			fireChange( "grade changed", ChangeCode.GRADE, old, grade );
		}
	}
	
	/**
	 * Sets the boolean value of whether or not the property is mortgaged
	 * @param mort
	 */
	public void setMortgaged( boolean mort ){
		if( mortgaged != mort ){
			mortgaged = mort;
			fireChange( "mortgaged changed", ChangeCode.MORTGAGE, !mortgaged, mortgaged );
		}
	}
	
	/**
	 * Sets the owner of this property
	 * @param owner
	 */
	public void setOwner( String owner ){
		if( !owner.equals(getOwner()) ){
			String old = this.owner;
			this.owner = owner;
			fireChange("owner changed", ChangeCode.OWNER, old, this.owner );
		}
	}
	
	/**
	 * Used to find the taxable value of the property.
	 * For a basic property, the worth of the property, if it is not mortgaged, is equal to the price to purchase.
	 * If the property is mortgaged, then it is worth half the price to purchase. This value is used when calculating
	 * a player's worth for income tax
	 * @return 	The taxable value of the property
	 */
	public int getWorth(){
		return isMortgaged() ? (int)(getCost()/2) : getCost();
	}
	
	/**
	 * Used to find the instant cash value of a property.
	 * For a basic property, the liquidizable worth of the property, if not mortgaged, is calculated
	 * by taking half of it's purchase price. If the property is mortgaged, then the liquidizable value.
	 * The liquid worth is used when calculating if a player has enough liquidizable assets to pay for
	 * any outstanding debts.
	 * is zero 
	 * @return 	The liquidizable value of the property
	 */
	public int getLiquidWorth(){
		//I use the method calls to the instance variables to make mocking and tests easier
		return isMortgaged() ? 0 : (int)(getCost()/2);
	}
	
	/**
	 * Takes in the necessary parameters to build a PropertyChangeEvent object and then fires
	 * a PropertyStateChanged event for all listeners
	 * @param msg	Specifics of event
	 * @param code 	PropertyChangeEvent.ChangeEvent enum describing what field has been changed
	 * @param ov 	The old value of changed field
	 * @param nv 	The new value of the changed field
	 */
	private void fireChange(String msg, PropertyChangeEvent.ChangeCode code, Object ov, Object nv){
		PropertyChangeEvent pce = new PropertyChangeEvent(this, msg, code, ov, nv);
		listeners.forEach( (PropertyChangeListener l) -> l.propertyStateChanged(pce) );
	}
	
	/**
	 * Adds the passed PropertyChangeListener to the list of listeners that must be notified
	 * in the event that the property is changed.
	 * @param pcl 	Property Listener that must be made aware of changes to the property
	 */
	public void addPropertyChangeListener( PropertyChangeListener pcl ){
		listeners.add(pcl);
	}
	
	/**
	 * Removes the passed PropertyChangeListener. The passed in object does
	 * not have to already exist in the set of listeners.
	 * @param pcl 	The listener to remove from the mailing list
	 */
	public void removePropertyChangeListener( PropertyChangeListener pcl ){
		listeners.remove(pcl);
	}

	/**
	 * Overrides the isCurrent method from the Refreshable interface.
	 * While the intended use case for this interface is to work with security
	 * keys, it's vague enough that I truly don't care about it's intended use.
	 * The use case in this instance is to ensure that the displayColor field of the Property
	 * object is up to date with the hexColor string field as well as ensuring the Set of
	 * listeners as been initialized. Why include these at all? Because these two fields 
	 * are explicitly and intentionally transient fields to prevent infinite recursive 
	 * serialization when creating JSON strings.
	 */
	@Override
	public boolean isCurrent() {
		if( listeners != null && displayColor != null ){
			if( displayColor.equals( Color.decode(hexColor)) ){
				return true;
			}
		}
		return false;
	}

	/**
	 * Overrides the refresh method from the Refreshable interface.
	 * While the intended use case for this interface is to work with security
	 * keys, it's vague enough that I truly don't care about it's intended use.
	 * The use case of this method for this class is to initialize the listeners
	 * collection of the Property object, if not initialized already, and to
	 * update the displayColor field if it contains an out-dated color.
	 */
	@Override
	public void refresh() throws RefreshFailedException {
		if( listeners == null ){
			listeners = new HashSet<PropertyChangeListener>();
		}
		if( displayColor == null || !displayColor.equals(Color.decode(hexColor))){
			displayColor = Color.decode(hexColor);
		}
	}

}
