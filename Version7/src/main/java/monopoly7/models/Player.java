package monopoly7.models;

import lombok.Data;
import lombok.Setter;
import lombok.Getter;

import java.io.Serializable;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import com.google.gson.annotations.Expose;

import lombok.AccessLevel;

@Data
public class Player implements ChangeListener, Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -1374881719360924125L;
	
	public static transient final Comparator<Player> ID_ORDER = new SortById();
	public static transient final Comparator<Player> NAME_ORDER = new SortByName();
	public static transient final Comparator<Player> WEALTH_ORDER = new SortByWealth();

	@Expose
	private String name;
	
	@Expose
	private int id = -1;
	
	@Expose
	private int cash = 1500;
	
	@Setter(AccessLevel.NONE) @Getter(AccessLevel.NONE) @Expose
	private int wealth;

	@Expose
	private int jailFreeCards;

	@Expose
	private boolean bankrupt = true;

	@Expose
	private Map<String, Property> ownedProps;
	
	@Setter(AccessLevel.NONE) @Getter(AccessLevel.NONE) @Expose
	private transient List<ChangeListener> listeners;

	public int getWealth(){
		return 1;
	}
	
	private void fireChange(){
		//System.out.println("Change has been fired in Player");
		if(listeners == null) {
			return;
		}
		ChangeEvent ce = new ChangeEvent(this);
		for(ChangeListener cl : listeners){
			cl.stateChanged(ce);
		}
	}

	@Override
	public void stateChanged(ChangeEvent e) {
		//System.out.println("Player has detected a change from is properties");
		fireChange();
	}
	
	private static class SortByWealth implements Comparator<Player>{
		@Override
		public int compare(Player p1, Player p2) {
			return p1.getWealth() - p2.getWealth();
		}
	}
	
	private static class SortById implements Comparator<Player>{
		@Override
		public int compare(Player o1, Player o2) {
			return o1.getId() - o2.getId();
		}
		
	}
	
	private static class SortByName implements Comparator<Player>{
		@Override
		public int compare(Player p1, Player p2) {
			return p1.getName().compareTo(p2.getName());
		}
	}
	
}
