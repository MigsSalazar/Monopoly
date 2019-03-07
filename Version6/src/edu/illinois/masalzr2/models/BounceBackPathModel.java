package edu.illinois.masalzr2.models;

import java.util.ArrayList;
import java.util.List;

public class BounceBackPathModel implements ListedPathModel {

	@Override
	/*
	 *  The first argument
	 * is the current step in the path, the second is how far along the path
	 * to go. 
	 * The BounceBackPathModel is a cyclical style of path finding
	 * but instead of connecting the beginning and end of a ListedPath,
	 * this model treats the ends as reflective walls. Take the following
	 * example:
	 * Input:
	 * 		c1 = 3
	 * 		c2 = 10
	 * |#|#|#|#|#|#|#|#|
	 *  0 1 2 3 4 5 6 7
	 *  
	 *  The resulting indices are:
	 *  {3,4,5,6,7,6,5,4,3,2}
	 *  
	 *	as it, starting at 3, will increment upwards until it reaches then
	 *	end and *bounces back* towards the start of the list. This behavior
	 *	repeats until the size of the lise equals c2;
	 *
	 *Another example:
	 *		Input:
	 *			c1 = 2
	 *			c2 = 25
	 *	|#|#|#|#|#|#|#|#|#|#|
	 *	 0 1 2 3 4 5 6 7 8 9
	 *
	 *		output:
	 *	{2,3,4,5,6,7,8,9,8,7,6,5,4,3,2,1,0,1,2,3,4,5,6,7,8}
	 *
	 *  
	 * (non-Javadoc)
	 * @see edu.illinois.masalzr2.models.ListedPathModel#generatePath(int, int, edu.illinois.masalzr2.models.ListedPath)
	 * @param c1	the step at which to begin the found path
	 * @param c2	the size of the desired path
	 * @param path	the ListedPath object that contains a finite ordered set of coordinates
	 * @return		an ordered list of coordinates as defined by the path model
	 */
	public List<int[]> generatePath(int c1, int c2, ListedPath path) {
		
		List<int[]> coords = path.findFullPath();
		List<int[]> ret = new ArrayList<int[]>();
		
		int current = c1%coords.size();
		int dir = c2 >= 0 ? 1 : -1;
		
		c2 = Math.abs(c2);
		
		while(c2 >= 0){
			ret.add(coords.get(current));
			
			if(current == 0){
				dir = 1;
			}else if(current == coords.size()-1){
				dir = -1;
			}
			
			c2--;
			current += dir;
			
		}
		
		return ret;
	}

}
