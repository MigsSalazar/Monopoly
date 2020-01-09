package monopoly7.pathFinding;

import java.awt.geom.Point2D.Double;
import java.util.List;

import monopoly7.utils.RevolvingList;

public class MonopolyExplorable implements Explorable {

	private RevolvingList<Double> points = new RevolvingList<Double>();
	
	@Override
	/**
	 * The exact specification of this Explorable goes as follows.
	 * Every tile is enumerated 0-39 where Go is 0 and Boardwalk
	 * is 39. The characteristic argument takes in the value of the
	 * dice roll while the point argument holds the number of the
	 * player's current position. This way, the function can return
	 * a list of coordinates that is of size "characteristic" that
	 * starting at the coordinate of position "point". Negative
	 * characteristics are acceptable as well
	 */
	public List<Double> getReachablePoints(double characteristic, double point) {
		return points.subList((int)point, (int)(point + characteristic));
	}

}
