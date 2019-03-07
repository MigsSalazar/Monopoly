package edu.illinois.masalzr2.models;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;


import com.google.gson.annotations.Expose;

import lombok.Getter;
import lombok.Setter;

public class ListedPath implements Serializable, Router{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	@Expose @Getter @Setter
	protected List<int[]> coords;

	
	@Getter @Setter @Expose 
	private int step;

	@Getter @Setter @Expose
	private ListedPathModel model = new DefaultPathModel();
	
	
	public ListedPath(){
		coords = new ArrayList<int[]>();
		
		coords.add(new int[]{0,0});
		
		step = 0;
	}
	
	public ListedPath(int[] x, int[] y){
		coords = new ArrayList<int[]>();
		for(int i=0; i<x.length && i<y.length; i++){
			coords.add(new int[]{x[i],y[i]});
		}
		
		step = 0;
	}
	
	public ListedPath(List<int[]> c){
		coords = c;
		step = 0;
	}
	
	public ListedPath(int[] x, int[] y, int s){
		coords = new ArrayList<int[]>();
		for(int i=0; i<x.length && i<y.length; i++){
			coords.add(new int[]{x[i],y[i]});
		}
		
		step = s;
	}
	
	public ListedPath(List<int[]> c, int s){
		coords = c;
		step = s;
	}
	
	
	public void moveStep(int m){
		step = (m + step)%coords.size();
	}
	
	public int[] currentCoords(){
		return coords.get(step);
	}
	
	public int[] findStepAt(int step){
		return coords.get(step%coords.size());
	}

	public List<int[]> findPathTo(int end){
		return findPath(step, end);
	}
	
	public List<int[]> findFullPath(){
		return findPath(0, coords.size());
	}
	
	public void setListedPathModel(ListedPathModel m){
		model = m;
	}
	
	@Override
	public List<int[]> findPath(int c1, int c2) {
		
		if(model == null){
			model = new DefaultPathModel();
		}
		
		return model.generatePath(c1, c2, this);
		
	}
	
	
	
}
