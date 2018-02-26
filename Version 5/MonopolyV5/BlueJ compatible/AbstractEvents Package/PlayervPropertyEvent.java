 

public class PlayervPropertyEvent extends MessageEvent {
	
	public PlayervPropertyEvent(EventPanel p, String t, Player play, Property prop) {
		super(p, t);
		play.addProperty(prop);
		play.subCash(prop.getPrice());
	}
	
	public PlayervPropertyEvent(EventPanel p, String t, Player play, Property prop, int cost) {
		super(p, t);
		play.addProperty(prop);
		play.subCash(cost);
	}

}
