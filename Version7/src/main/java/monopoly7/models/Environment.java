package monopoly7.models;

import java.util.HashMap;
import java.util.Map;

import com.google.gson.annotations.Expose;

import lombok.Getter;
import lombok.Setter;

public class Environment {
	
	public static final Dice dice = Dice.generate2d6();
	
	@Expose @Getter @Setter
	private Map<String, Player> players = new HashMap<String,Player>();
	
	@Expose @Getter @Setter
	private Map<String, Property> properties = new HashMap<String,Property>();
	
	@Expose @Getter @Setter
	private Map<String, Object> cards = new HashMap<String,Object>();
	
	@Expose @Getter @Setter
	private Map<String, Object> suites = new HashMap<String,Object>();
	
	@Override
	public boolean equals( Object o ){
		if( o instanceof Environment ){
			Environment compare = (Environment) o;
			boolean ret = (players.equals(compare.getPlayers()));
			ret &= properties.equals(compare.getProperties());
			ret &= cards.equals(compare.getCards());
			ret &= suites.equals(compare.getSuites());
			return ret;
		}else{
			return false;
		}
	}

}
