package monopoly7.pathFinding;

import java.awt.geom.Point2D.Double;
import java.util.List;

public interface Explorable {

	/**
	 * While the return type of this method makes sense, a list of all available
	 * points that can be reached given the arguments, the actual arguments are
	 * intentionally left vague. Instead of using another Point2D.Double to note
	 * the current position of the piece to move, the use of two integers allows
	 * for a programmatic level of abstraction where either integer can be used
	 * for any reason. The intended usage is to use the second "point" argument
	 * as a flattened 2D coordinate where point = height * y + x and the first
	 * argument as an additional characteristic flag to allow additional, meta-
	 * coordinate behavior to take place. However, this is an interface so there's
	 * absolutely nothing stopping anyone from doing anything with this function.
	 * If one desires, both integers can be used as single non-flattened
	 * coordinates, they can both be used as flattened coordinates allowing the
	 * developer to send in two different complete coordinates. They can both
	 * be used as a kind of characteristic flag if coordinates are not used at all.
	 * The characteristic argument can be used to give the Explorable additional
	 * information about it's environment such as the case with board games that
	 * use walls or structures to influence pathing. 
	 * @param characteristic a flag marker to give additional behavioral info.
	 * This argument can be left unused in implementation, and in such cases
	 * just pass in any non null value
	 * @param point a flattened 2D coordinate where point = height * y + x
	 * @return
	 */
	public List<Double> getReachablePoints( double characteristic, double point);
	
}
