package edu.illinois.masalzr2.models;

public class PositionIndex {
	private int[] x;
	private int[] y;
	
	private int[] specialX;
	private int[] specialY;
	
	private int step;
	
	private boolean locked;
	
	
	
	public PositionIndex(){
		x = new int[1];
		
		y = new int[1];
		x[0] = y[0] = 0;
		
		specialX = new int[1];
		specialY = new int[1];
		
		specialX[0] = specialY[0] = 0;
		
		step = 0;
		
		locked = true;
	}
	
	public PositionIndex(int[] x, int[] y){
		this.x = x;
		this.y = y;
		
		specialX = new int[1];
		specialY = new int[1];
		
		specialX[0] = specialY[0] = 1;
		
		step = 0;
		
		locked = false;
	}
	
	public PositionIndex(int[] x, int[] y, int[] spx, int[] spy){
		this.x = x;
		this.y = y;
		
		specialX = spx;
		specialY = spy;
		
		step = 0;
		
		locked = false;
	}
	
	public boolean isLocked(){
		return locked;
	}
	
	public void setLocked( boolean  l ){
		locked = l;
	}
	
	public int getStep(){
		return step;
	}
	
	public void setStep(int s){
		step = s;
	}
	
	public int[] moveOne(){
		step++;
		return getCoords();
	}
	
	public int[] move(int m){
		step = (step + m) % x.length;
		return getCoords();
	}
	
	public int[] getCoords(){
		int[] retval = new int[2];
		retval[0] = x[step];
		retval[1] = y[step];
		return retval;
	}
	
	public int[] getCoordsAtStep(int s){
		int[] retval = {-1,-1};
		if(s < x.length && s < y.length){
			retval[0] = x[s];
			retval[1] = y[s];
		}
		return retval;
	}
	
	public int[] getSpecialCase(int sp){
		int[] retval = {-1,-1};
		if(sp < specialX.length && sp < specialY.length){
			retval[0] = specialX[sp];
			retval[1] = specialY[sp];
		}
		return retval;
	}
	
}