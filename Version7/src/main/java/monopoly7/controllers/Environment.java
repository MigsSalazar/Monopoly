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

}
