package monopoly7.models;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
	private transient List<PlayerChangeListener> listeners = new ArrayList<PlayerChangeListener>();
	
	public static transient final Comparator<Player> ID_ORDER = (Player o1, Player o2) -> o1.getId() - o2.getId();
	public static transient final Comparator<Player> NAME_ORDER = (Player o1, Player o2) -> o1.getName().compareTo(o2.getName());
	public static transient final Comparator<Player> WEALTH_ORDER = (Player o1, Player o2) -> o1.getWealth() - o2.getWealth();
	
	public void setCash( int c ){
		if( cash != c ){
			int old = cash;
			cash = c;
			fireChange( "cash changed", ChangeCode.CASH, old, cash );
		}
	}
	
	public void setBankrupt( boolean bankruptcy ){
		if( bankrupt != bankruptcy ){
			bankrupt = bankruptcy;
			fireChange("bankrupt changed", ChangeCode.BANKRUPT, !bankrupt, bankrupt);
		}
	}
	
	public void advancePosition( int move ){
		if( move < 0 )
			return;
		position += move;
		fireChange("position increased", ChangeCode.POSITION, position - move, position);
	}
	
	public void retreatPosition( int move ){
		if( move < 0 )
			return;
		position -= move;
		fireChange("position decreased", ChangeCode.POSITION, position + move, position);
	}
	
	public void setPosition( int pos ){
		int old = position;
		position = pos;
		fireChange("position changed", ChangeCode.POSITION, old, position );
	}
	
	public int getWealth(){
		int ret = cash;

		for( Property p : props.values() ){
			ret += p.getWorth();
		}
		
		return ret;
	}
	
	public int getLiquidationWorth(){
		int ret = cash;
		
		for(Property p : props.values()){
			ret += p.getLiquidWorth();
		}
		
		return ret;
		
	}
	
	public boolean ownsProp( Property prop ){
		return ownsProp(prop.getName());
	}
	
	public boolean ownsProp( String prop ){
		return props.containsKey(prop);
	}
	
	public void addPlayerChangeListener( PlayerChangeListener listener ){
		if( !listeners.contains(listener) )
			listeners.add(listener);
	}
	
	public void removePlayerChangeListener( PlayerChangeListener listener ){
		 listeners.remove(listener);
	}
	
	public void addCash( int add ){
		if( add < 0 )
			return;
		cash += add;
		fireChange( "cash added", ChangeCode.CASH, cash - add, cash );
	}
	
	public void subCash( int sub ){
		if( sub < 0 )
			return;
		cash -= sub;
		fireChange( "cash subed", ChangeCode.CASH, cash + sub, cash );
	}
	
	public void addBailout( int bails ){
		if( bails < 0 )
			return;
		this.bails += bails;
		fireChange( "bails added", ChangeCode.BAILOUT, this.bails - bails, this.bails );
	}
	
	public void removeBailout( int bails ){
		if( bails < 0 )
			return;
		this.bails -= bails;
		fireChange( "bails subed", ChangeCode.BAILOUT, this.bails + bails, this.bails );
	}
	
	public void addProperty( Property p ){
		props.put(p.getName(), p);
		p.setOwner(name);
		p.addPropertyChangeListener(this);
		fireChange( "obtained "+p.getName(), ChangeCode.PROPERTIES, null, p );
	}
	
	public void removeProperty( String propName ){
		Property out = props.remove(propName);
		if( out != null ){
			out.setOwner("");
			out.removePropertyChangeListener(this);
			fireChange( "lost "+out.getName(), ChangeCode.PROPERTIES, out, null);
		}
	}
	
	private void fireChange( String msg, PlayerChangeEvent.ChangeCode code, Object ov, Object nv){
		fireChange( new PlayerChangeEvent( this, msg, code, ov, nv) );
	}
	
	private void fireChange( PlayerChangeEvent pce ){
		for( PlayerChangeListener l : listeners ){
			l.playerStateChanged(pce);
		}
	}

	@Override
	public void propertyStateChanged(PropertyChangeEvent pce) {
		fireChange( "property changed", ChangeCode.PROPERTIES, null, null );

	}
	
}
