package monopoly7.controllers;

import java.util.HashMap;
import java.util.Map;

import com.google.gson.annotations.Expose;

import lombok.Getter;
import lombok.Setter;

public class Environment {
	
	@Expose @Getter @Setter
	private Map<String, Object> players = new HashMap<String,Object>();
	
	@Expose @Getter @Setter
	private Map<String, Object> properties = new HashMap<String,Object>();
	
	@Expose @Getter @Setter
	private Map<String, Object> cards = new HashMap<String,Object>();
	
	@Expose @Getter @Setter
	private Map<String, Object> suites = new HashMap<String,Object>();
	
	@Expose @Getter @Setter
	private Map<String, Object> idontknowMoreStuff = new HashMap<String,Object>();
	
	public static Environment generateTestEnvironment() {
		Environment e = new Environment();
		for( int i=0; i<8; i++ ){
			e.getPlayers().put("player"+i, "player"+i);
		}
		
		for( int i=0; i<35; i++ ){
			e.getProperties().put("property"+i, "property"+i);
		}
		
		for( int i=0; i<8; i++ ){
			e.getSuites().put("suite"+i, "suite"+i);
		}
		
		for( int i=0; i<40; i++ ){
			e.getCards().put("card"+i, "card"+i);
		}
		
		e.getIdontknowMoreStuff().put("lol", "hi");
		return e;
	}
	
	@Override
	public boolean equals( Object o ){
		if( o instanceof Environment ){
			Environment compare = (Environment) o;
			boolean ret = (players.equals(compare.getPlayers()));
			ret &= properties.equals(compare.getProperties());
			ret &= cards.equals(compare.getCards());
			ret &= suites.equals(compare.getSuites());
			ret &= idontknowMoreStuff.equals(compare.getIdontknowMoreStuff());
			return ret;
		}else{
			return false;
		}
	}

}
