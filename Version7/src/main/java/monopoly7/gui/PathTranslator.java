package monopoly7.gui;

import monopoly7.models.Coordinates;

public class PathTranslator implements Translatable {

	public static final int PATHTRANSLATOR_NO_LOOP = 0;
	public static final int PATHTRANSLATOR_RESTART_LOOP = 1;
	public static final int PATHTRANSLATOR_BACK_AND_FORTH_LOOP = 2;
	
	private Coordinates[] coords;
	private int pathType;
	private int step;
	
	public PathTranslator( int[] xs, int[] ys, int pt ){
		pathType = pt;
		if( pathType < 0 || pathType > 2 ){
			pathType = 0;
		}
		int length = Math.min(xs.length, ys.length);
		coords = new Coordinates[ length ];
		
		for( int i=0; i<length; i++ ){
			coords[i] = new Coordinates( xs[i], ys[i] );
		}
		
	}
	
	/**
	 * Returns the absolute position from the given step based on the path type
	 * given to the object.
	 * <br>
	 *  - PATHTRANSLATOR_NO_LOOP: If the given step is in the bounds of the list of defined coordinates,
	 * the coordinate stored at that step is returned. Otherwise, if the step is less than 0 or greater
	 * than the length of the coordinate list, then the first and last coordinate respectively are returned
	 * <br>
	 *  - PATHTRANSLATOR_RESTART_LOOP: The path is treated like an infinite loop, restarting the cycle at the
	 * start of the coordinate list once the end is reached and passed
	 * <br>
	 *  - PATHTRANSLATOR_BACK_AND_FORTH_LOOP: The path bounces the step back and forth between the start and end.
	 *  Once the end is reached and surpassed, the path cycles backwards until the start is reached, aat which
	 *  point, the cycle begins to cycle forward again
	 *  
	 * @param p	absolute index of the desired coordinate 
	 * @return	the coordinate found at the index p or at the list's boundaries if p describes an out of bounds index
	 */
	public Coordinates getAbsoluteTranslation( int p ){
		int length = coords.length;
		int pModed = p%length;
		
		switch( pathType ){
		default: //there is no looping to be done on the path
		case 0:  //so bounds are enforced or given
			if( p < 0 || p > length ){
				return coords[0];
			}else if( p < 0){
				return coords[0];
			}
			return coords[p];
		case 1:	//path loops infinitely restarting from the beginning after each end
			return coords[ pModed ];
		case 2: //path loops infinitely going back and forth from start to end and back to start
			int lap = p / length;
			if( lap % 2 == 0 ){
				return coords[ pModed ];
			}
			return coords[ length - pModed ];
		}
	}
	
	/**
	 * 
	 */
	public Coordinates translate(int p) {
		step += p;
		return getAbsoluteTranslation( step );
	}

}
