package monopoly7.models;

import java.awt.geom.Point2D;

/**
 * This class is nothing more than a wrapper class for the Point2D.Double class.
 * I got tired of writing that out and thought RelativePoint made more sense.
 * The only different between this class and Point2D.Double is the copy/conversion
 * constructor from Point2D.Double to a RelativePoint. They are otherwise the same
 * class.
 * @author Miguel Salazar
 * @see java.awt.geom.Point2D.Double
 *
 */
public class RelativePoint extends Point2D.Double {

	public RelativePoint(){
		super();
	}
	
	public RelativePoint(double x, double y) {
		super( x, y );
	}
	
	public RelativePoint( Point2D.Double d ){
		super( d.getX(), d.getY() );
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = 1551668287115176503L;

}
