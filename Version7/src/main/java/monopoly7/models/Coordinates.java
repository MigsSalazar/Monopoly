package monopoly7.models;

import lombok.Getter;
import lombok.Setter;

public class Coordinates{
	
	@Getter @Setter
	private double x;
	@Getter @Setter
	private double y;
	
	public Coordinates(){
		x = 0;
		y = 0;
	}
	
	public Coordinates( double x, double y ){
		this.x = x;
		this.y = y;
	}
	
}