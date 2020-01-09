package monopoly7.models;

import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import com.google.gson.annotations.Expose;

import lombok.Getter;
import monopoly7.event.PlayerChangeEvent;
import monopoly7.event.PlayerChangeEvent.ChangeCode;
import monopoly7.event.PlayerChangeListener;
import monopoly7.event.PropertyChangeEvent;
import monopoly7.event.PropertyChangeListener;

public class Player implements PropertyChangeListener {
	
	//NON TRANSIENT INSTANCE VARIBLES 
	@Expose @Getter private String name = "";
	@Expose @Getter private int id = 0;
	@Expose @Getter private int position = 0;
	@Expose @Getter private int cash = 0;
	@Expose @Getter private int bails = 0;
	@Expose @Getter private boolean bankrupt = false;
	@Getter private Map<String, Property> props = new HashMap<String, Property>();

	//TRANSIENT INSTANCE VARIABLES
	private transient Set<PlayerChangeListener> listeners = new HashSet<PlayerChangeListener>();
	
	public static transient final Comparator<Player> ID_ORDER = (Player o1, Player o2) -> o1.getId() - o2.getId();
	public static transient final Comparator<Player> NAME_ORDER = (Player o1, Player o2) -> o1.getName().compareTo(o2.getName());
	public static transient final Comparator<Player> WEALTH_ORDER = (Player o1, Player o2) -> o1.getWealth() - o2.getWealth();
	
	/**
	 * NOT RECOMMENDED FOR PRACTICAL USE
	 * Intended use is to create basic Player objects for testing behavior
	 */
	public Player(){ /*Place holder constructor to allow for a default constructor*/ }
	
	/**
	 * Proper constructor for typical use. Creates a fully fleshed object with values
	 * @param n 	Name of the player
	 * @param i 	ID of the player. Recommended to be the player's number in the turn order
	 * @param p 	Position of the player relative to the START square
	 * @param c 	Current cash on hand for the player
	 * @param b 	Number of Get out of Jail Free cards currently in possession of the player
	 * @param r 	Bankruptcy status.
	 */
	public Player( String n, int i, int p, int c, int b, boolean r ){
		name = n;
		id = i;
		position = p;
		cash = c;
		bails = b;
		bankrupt = r;
	}
	
	/**
	 * Sets the cash on hand value of the player and triggers fireChange if value
	 * is different from the current value
	 * @param c 	Cash value to set the player's cash to
	 */
	public void setCash( int c ){
		if( cash != c ){
			int old = cash;
			cash = c;
			fireChange( "cash changed", ChangeCode.CASH, old, cash );
		}
	}
	
	/**
	 * Sets the bankruptcy status of the player and triggers fireChange if value
	 * is different from the value prior to the method call
	 * @param bankruptcy 	TRUE: the players is bankrupt and has lost the game
	 * 						</br>
	 * 						FASLE: the player is NOT bankrupt and still playing 
	 */
	public void setBankrupt( boolean bankruptcy ){
		if( bankrupt != bankruptcy ){
			bankrupt = bankruptcy;
			fireChange("bankrupt changed", ChangeCode.BANKRUPT, !bankrupt, bankrupt);
		}
	}
	
	/**
	 * Increases the player's position by the passed in amount
	 * @param move 	Increases the player's position by passed in amount. Any number less than 0 will be ignored
	 */
	public void advancePosition( int move ){
		if( move < 0 )
			return;
		position += move;
		fireChange("position increased", ChangeCode.POSITION, position - move, position);
	}
	
	/**
	 * Decreases the player's position by the passed in amount
	 * @param move 	Decreases the player's position by the passed in amount. Any number less than 0 will be ignored
	 */
	public void retreatPosition( int move ){
		if( move < 0 )
			return;
		position -= move;
		fireChange("position decreased", ChangeCode.POSITION, position + move, position);
	}
	
	/**
	 * Hard codes the player's position relative to the START square
	 * @param pos 	Value to set the player's position to.
	 */
	public void setPosition( int pos ){
		int old = position;
		position = pos;
		fireChange("position changed", ChangeCode.POSITION, old, position );
	}
	
	/**
	 * Calculates and returns the total taxable wealth of the player
	 * @return 	Total taxable worth of the player
	 */
	public int getWealth(){
		int ret = cash;

		for( Property p : props.values() ){
			ret += p.getWorth();
		}
		
		return ret;
	}
	
	/**
	 * Calculates and returns the total liquidizable worth of the player and
	 * all of their assets. Or, in other words, calculates how much cash the
	 * player has if they were to liquidize all available assets possible.
	 * @return 	The total possible obtainable cash if the player were to liquidize
	 */
	public int getLiquidationWorth(){
		int ret = cash;
		
		for(Property p : props.values()){
			ret += p.getLiquidWorth();
		}
		
		return ret;
		
	}
	
	/**
	 * Takes a property and confirm whether or not this player owns that property
	 * @param prop 	Property to remove
	 * @return true if the players owns the property, false otherwise
	 */
	public boolean ownsProp( Property prop ){
		return ownsProp(prop.getName());
	}
	
	/**
	 * Takes a property's name and confirm whether or not this player owns that property
	 * @param prop 	Property to remove
	 * @return true if the players owns the property, false otherwise
	 */
	public boolean ownsProp( String prop ){
		return props.containsKey(prop);
	}
	
	/**
	 * Includes the provided listener. Including a listener that is already present
	 * in the set of listeners does not create duplicates
	 * @param listener
	 */
	public void addPlayerChangeListener( PlayerChangeListener listener ){
		listeners.add(listener);
	}
	
	/**
	 * Removes the passed in listener of the set. You can "remove" a listener that
	 * is not currently listening to the object
	 * @param listener 	The listener to remove
	 */
	public void removePlayerChangeListener( PlayerChangeListener listener ){
		 listeners.remove(listener);
	}
	
	/**
	 * Adds the passed in value to the current cash on hand value
	 * @param add 	Cash to be added
	 */
	public void addCash( int add ){
		if( add < 0 )
			return;
		cash += add;
		fireChange( "cash added", ChangeCode.CASH, cash - add, cash );
	}
	
	/**
	 * Subtracts the passed in value from the current cash on hand value
	 * @param sub 	Cash to be subtracted
	 */
	public void subCash( int sub ){
		if( sub < 0 )
			return;
		cash -= sub;
		fireChange( "cash subed", ChangeCode.CASH, cash + sub, cash );
	}
	
	/**
	 * Adds the passed in value to the current number of Get out of Jail Free cards
	 * @param add 	Number of Get out of Jail Free cards to add
	 */
	public void addBailout( int bails ){
		if( bails < 0 )
			return;
		this.bails += bails;
		fireChange( "bails added", ChangeCode.BAILOUT, this.bails - bails, this.bails );
	}
	
	/**
	 * Subtracts the passed in value from the current number of Get out of Jail Free cards
	 * @param add 	Number of Get out of Jail Free cards to subtract
	 */
	public void removeBailout( int bails ){
		if( bails < 0 )
			return;
		this.bails -= bails;
		fireChange( "bails subed", ChangeCode.BAILOUT, this.bails + bails, this.bails );
	}
	
	/**
	 * Marks the property as owned by this player by setting the property's owner,
	 * adding the player object to the property's set of change listeners,
	 * storing the property as one that the player owns, and triggers a change to listeners
	 * of the player object
	 * @param p 	Property to set the player object as owner
	 */
	public void addProperty( Property p ){
		props.put(p.getName(), p);
		p.setOwner(name);
		p.addPropertyChangeListener(this);
		fireChange( "obtained "+p.getName(), ChangeCode.PROPERTIES, null, p );
	}
	
	/**
	 * Removes the passed in property from the set of properties owned by the
	 * player object. If no such property is owned by the player, then no changes are made
	 * and no listeners are alerted of a change
	 * @param prop 	The property object to remove
	 * @return 	true: the property was owned by the player and then removed
	 * 			</br>
	 *  		false: the property was not owned by the player and no changes were made
	 */
	public boolean removeProperty( Property prop ){
		return removeProperty(prop.getName());
	}
	
	/**
	 * Removes the property of the passed in name from the set of properties owned by the
	 * player object. If no such property is owned by the player, then no changes are made
	 * and no listeners are alerted of a change
	 * @param propName 	Name of the property gotten by the Property.getName() method
	 * @return 	true: the property was owned by the player and then removed
	 * 			</br>
	 *  		false: the property was not owned by the player and no changes were made
	 */
	public boolean removeProperty( String propName ){
		Property out = props.remove(propName);
		if( out != null ){
			out.setOwner("");
			out.removePropertyChangeListener(this);
			fireChange( "lost "+out.getName(), ChangeCode.PROPERTIES, out, null);
			return true;
		}
		return false;
	}
	
	/**
	 * Verbosely accepts a PlayerChangeEvent by it's arguments and alerts all listeners
	 * of a change to the player object
	 * @param msg 	A detailed message of the change that occurred. All methods that use fireChange should use a unique message
	 * @param code 	The respective ChangeEvent enum of the field that has been altered
	 * @param ov 	The old value of the field
	 * @param nv 	The new value of the field
	 */
	private void fireChange( String msg, PlayerChangeEvent.ChangeCode code, Object ov, Object nv){
		fireChange( new PlayerChangeEvent( this, msg, code, ov, nv) );
	}
	
	/**
	 * Alerts all listeners with the passed in PlayerChangeEvent. <b>Note: There are no measures to ensure correctness
	 * or coherence of the passed in PlayerChangeEvent so it is up to the developer to ensure the object makes sense as
	 * a change that can happen
	 * @param pce 	Event description passed on to all listeners 
	 */
	private void fireChange( PlayerChangeEvent pce ){
		for( PlayerChangeListener l : listeners ){
			l.playerStateChanged(pce);
		}
	}
	
	@Override
	public void propertyStateChanged(PropertyChangeEvent pce) {
		if( pce.getStatus() == PropertyChangeEvent.ChangeCode.OWNER
				&& pce.getOldValue().equals(this.getName())
				&& !pce.getNewValue().equals(this.getName())){
			Property p = (Property)pce.getSource();
			removeProperty(p.getName());
		}else{
			fireChange( "property changed", ChangeCode.PROPERTIES, pce.getSource(), pce.getSource() );
		}
	}
}
