package edu.illinois.masalzr2.models;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class CyclicalPathModel implements ListedPathModel, Serializable{
	

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Override
	public List<int[]> generatePath(int c1, int c2, ListedPath path) {
		if(path == null){
			return new ArrayList<int[]>();
		}
		List<int[]> coords = path.getCoords();
		List<int[]> ret = new ArrayList<int[]>();
		
		int start;
		int end;
		
		if( (c1 < c2 && c1 >= 0) || (c2 < c1 && c2 >=0) ){
			start = c1;
			end = c2;
		}else if( c1 < c2 && c1 < 0 ){
			start = coords.size() + (c1 % coords.size());
			end = start + (c2 - c1);
		}else if( c2 < c1 && c2 < 0){
			start = coords.size() + (c1 % coords.size());
			end = start - (c1 - c2);
		}else{
			start = c1 % coords.size();
			ret.add(coords.get(start));
			return ret;
		}
		
		int mod = c1 < c2 ? 1 : -1;
		
		for(int i = start; i<end; i+=mod){
			int[] copied = coords.get(i%coords.size());
			ret.add( new int[]{ copied[0], copied[1] } );
		}
		
		return ret;
		
	}
}
