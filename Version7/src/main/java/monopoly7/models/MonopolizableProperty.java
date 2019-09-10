package monopoly7.models;

import com.google.gson.annotations.Expose;

import lombok.Getter;
import lombok.extern.flogger.Flogger;

@Flogger
public class MonopolizableProperty extends Property {

	@Expose @Getter private String color = "";
	@Expose @Getter private int upgradeCost = 0;
	@Expose @Getter private int upgradeLimit = 0;
	@Expose @Getter private int monopolyLimit = 0;
	
	public MonopolizableProperty(){
		super();
	}
	
	public MonopolizableProperty( String n, String o, int[] r, int p, int c, int g, boolean m, String h, String color, int uc, int ul, int ml ){
		super( n, o, r, p, c, g, m, h );
		this.color = color;
		upgradeCost = uc;
		upgradeLimit = ul;
		monopolyLimit = ml;
	}
	
	@Override
	public void incGrade( int inc ){
		if( getGrade() + inc > getUpgradeLimit() || getGrade() + inc < 0 ){
			log.atWarning().log("invalid grade increase on %s. Current grade: %d. Increment: %d", getName(), getGrade(), inc);
		}else{
			incGrade(inc);
		}
	}
	
	@Override
	public void decGrade( int dec ){
		if( getGrade() - dec > getUpgradeLimit() || getGrade() - dec < 0 ){
			log.atWarning().log("invalid grade decrease on %s. Current grade: %d. Decrement: %d", getName(), getGrade(), dec);
		}else{
			decGrade(dec);
		}
	}
	
	@Override
	public void setGrade( int gr ){
		if( gr < 0 || gr > getUpgradeLimit() ){
			log.atWarning().log("invalid grade change on %s of: %d", getName(), gr);
		}else{
			setGrade(gr);
		}
	}
	
	@Override
	public int getLiquidWorth(){
		int ret = super.getLiquidWorth();
		if( !isMortgaged() )
			ret += (getGrade() * getUpgradeCost());
		return ret;
	}

}
