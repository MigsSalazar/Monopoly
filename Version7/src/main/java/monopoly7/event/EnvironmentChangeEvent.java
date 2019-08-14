package monopoly7.event;

import java.util.EventObject;

import lombok.Getter;

public class EnvironmentChangeEvent extends EventObject {

	/**
	 * 
	 */
	private static final long serialVersionUID = 9218096966660617654L;
	
	@Getter
	private String message;

	public EnvironmentChangeEvent(Object source, String message) {
		super(source);
		this.message = message;
	}
	
	public String toString(){
		return super.toString() + " message:"+message;
	}

}
