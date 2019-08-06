package monopoly7.event;

import java.util.HashMap;
import java.util.Map;

import lombok.Getter;

public class PropertyChangeEvent extends EnvironmentChangeEvent {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6024194790367340148L;

	public enum ChangeCode{
		GRADE, OWNER,
		MORTGAGE,
		LISTENER
	}
	
	public static final Map<String, ChangeCode> codes = getCodes();
	
	@Getter
	private ChangeCode status;
	@Getter
	private Object oldValue;
	@Getter
	private Object newValue;

	public PropertyChangeEvent(Object source, String message, ChangeCode code, Object oldVal, Object newVal) {
		super(source, message);
		status = code;
		oldValue = oldVal;
		newValue = newVal;
		
	}
	
	private static Map<String, ChangeCode> getCodes(){
		Map<String, ChangeCode> ret = new HashMap<String, ChangeCode>();
		ret.put("grade", ChangeCode.GRADE);
		ret.put("owner", ChangeCode.OWNER);
		ret.put("mortgage", ChangeCode.MORTGAGE);
		ret.put("listener", ChangeCode.LISTENER);
		return ret;
	}

}
