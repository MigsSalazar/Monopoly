package monopoly7.models;

import java.awt.geom.Dimension2D;

import lombok.Getter;
import lombok.Setter;

public class RelativeDimensions extends Dimension2D{
	
	@Getter @Setter
	private double width;
	@Getter @Setter
	private double height;
	
	public RelativeDimensions(){
		width = 0.0;
		height = 0.0;
	}
	
	public RelativeDimensions( double w, double h ){
		width = w;
		height = h;
	}
	
	public RelativeDimensions( RelativeDimensions s ){
		width = s.getWidth();
		height = s.getHeight();
	}
	
	@Override
	public void setSize(double width, double height) {
		this.width = width;
		this.height = height;
	}
	
}