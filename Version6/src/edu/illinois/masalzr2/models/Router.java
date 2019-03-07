package edu.illinois.masalzr2.models;

import java.util.List;

public interface Router {
	abstract public List<int[]> findPath(int c1, int c2);
}
