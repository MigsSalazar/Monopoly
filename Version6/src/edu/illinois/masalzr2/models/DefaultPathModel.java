package edu.illinois.masalzr2.models;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class DefaultPathModel implements ListedPathModel, Serializable {
	
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Override
	public List<int[]> generatePath(int c1, int c2, ListedPath path) {
		if(c1 > -1 && c2 < path.getCoords().size() && c1<c2){
			return path.getCoords().subList(c1, c2);
		}
		return new ArrayList<int[]>();
	}

}
