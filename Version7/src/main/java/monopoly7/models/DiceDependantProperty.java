package monopoly7.models;

import java.util.Collection;

import javax.security.auth.RefreshFailedException;

import lombok.extern.flogger.Flogger;

@Flogger
public class DiceDependantProperty extends LinkedGradeProperty {
	
	private transient Dice dice = Environment.dice;
	
	@SuppressWarnings("unchecked")
	public DiceDependantProperty( String n, String o, int[] r, int p, int c, int g, boolean m, String h, Collection<? extends LinkedGradeProperty> fellows ){
		super( n, o, r, p, c, g, m, h, (Collection<LinkedGradeProperty>) fellows );
		
	}

	@Override
	public boolean isCurrent(){
		return super.isCurrent() && (dice != null);
	}
	
	@Override
	public void refresh() throws RefreshFailedException{
		super.refresh();
		if( dice == null ){
			if( Environment.dice == null ){
				Environment.refreshDice();
			}
			dice = Environment.dice;
		}
	}
	
	@Override
	public int getRent() {
		if( !isCurrent() ){
			try {
				refresh();
			} catch (RefreshFailedException e) {
				log.atWarning().log("unable to refresh DiceDependentProperty", e);
			}
		}
		return super.getRent() * dice.getLastSum();
	}

}
