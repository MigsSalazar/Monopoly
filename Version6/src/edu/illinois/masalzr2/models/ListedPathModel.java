package edu.illinois.masalzr2.models;

import java.util.List;


public abstract interface ListedPathModel{
	
	public abstract List<int[]> generatePath(int c1, int c2, ListedPath path);
	
}
