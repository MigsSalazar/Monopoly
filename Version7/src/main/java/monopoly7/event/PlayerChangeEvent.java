package monopoly7.event;

import java.util.HashMap;
import java.util.Map;

import lombok.Getter;

public class PlayerChangeEvent extends EnvironmentChangeEvent {
	
	/**
	 * I don't like that Eclipse complains about this considering I never use it
	 */
	private static final long serialVersionUID = -3711337672146159053L;
	
	public enum ChangeCode{
		POSITION,
		CASH, WEALTH,
		PROPERTIES,
		BAILOUT,
		BANKRUPT,
		LISTENERS
	}

	@Getter
	private ChangeCode status;
	@Getter
	private Object oldValue;
	@Getter
	private Object newValue;
	public static final Map<String, ChangeCode> codes = getCodes();

	public PlayerChangeEvent(Object source, String message, ChangeCode code, Object oldVal, Object newVal) {
		super(source, message);
		status = code;
		oldValue = oldVal;
		newValue = newVal;
	}
	
	private static Map<String, ChangeCode> getCodes(){
		Map<String, ChangeCode> ret = new HashMap<String, ChangeCode>();
		ret.put("position", ChangeCode.POSITION);
		ret.put("cash", ChangeCode.CASH);
		ret.put("wealth", ChangeCode.PROPERTIES);
		ret.put("property", ChangeCode.WEALTH);
		ret.put("bailout", ChangeCode.BAILOUT);
		ret.put("bankrupt", ChangeCode.BANKRUPT);
		ret.put("listeners", ChangeCode.LISTENERS);
		return ret;
	}

}
