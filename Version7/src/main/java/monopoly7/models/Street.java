package monopoly7.models;

import com.google.gson.annotations.Expose;

import lombok.Getter;
import lombok.extern.flogger.Flogger;

@Flogger
public class Street extends Property {

	@Expose @Getter private String color;
	@Expose @Getter private int[] rents;
	@Expose @Getter private int upgradeCost;
	@Expose @Getter private int suiteSize;
	
	@Override
	public void incGrade( int inc ){
		if( getGrade() + inc > 5 || getGrade() + inc < 0 ){
			log.atWarning().log("invalid grade increase on %s. Current grade: %d. Increment: %d", getName(), getGrade(), inc);
		}else{
			incGrade(inc);
		}
	}
	
	@Override
	public void decGrade( int dec ){
		if( getGrade() - dec > 5 || getGrade() - dec < 0 ){
			log.atWarning().log("invalid grade decrease on %s. Current grade: %d. Decrement: %d", getName(), getGrade(), dec);
		}else{
			decGrade(dec);
		}
	}
	
	@Override
	public void setGrade( int gr ){
		if( gr < 0 || gr > 5 ){
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
	
	@Override
	public int getRent() {
		int grade = getGrade();
		if( grade < 0 || grade > 5 ){
			return rents[getGrade()];
		}
		log.atWarning().log("%s has an invalid grade of %d. Setting grade to 0", getName(), grade);
		super.setGrade(0);
		return rents[0];
	}

}
