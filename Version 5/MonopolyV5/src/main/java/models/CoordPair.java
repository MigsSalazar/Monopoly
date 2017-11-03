/**
 * 
 */
package main.java.models;

import com.google.gson.annotations.Expose;

/**
 * @author Unknown
 *
 */
public class CoordPair {

	@Expose private int row;
	@Expose private int col;
	
	public CoordPair(){
		row = 0;
		col = 0;
	}
	
	public CoordPair(int r, int c){
		row = r;
		col = c;
	}
	
	public int getRow(){
		return row;
	}
	
	public int getCol(){
		return col;
	}
	
}
