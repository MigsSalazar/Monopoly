package monopoly7.utils;

import java.util.ListIterator;

public class UoroborosListIterator<E> implements ListIterator<E>{
	
	private UoroborosList<E> iterative;
	private int curIdx;
	private int first;
	
	public UoroborosListIterator( UoroborosList<E> list, int idx){
		curIdx = idx;
		first = idx;
		iterative = list;
	}

	@Override
	public boolean hasNext() {
		return curIdx < first + iterative.size();
	}

	@Override
	public E next() {
		E ret = iterative.get(curIdx);
		curIdx++;
		return ret;
	}

	@Override
	public boolean hasPrevious() {
		return curIdx <= first - iterative.size();
	}

	@Override
	public E previous() {
		E ret = iterative.get(curIdx);
		curIdx--;
		return ret;
	}

	@Override
	public int nextIndex() {
		return curIdx + 1;
	}

	@Override
	public int previousIndex() {
		return curIdx - 1;
	}

	@Override
	public void remove() {
		iterative.remove(curIdx);
	}

	@Override
	public void set(E e) {
		iterative.set(curIdx, e);
	}

	@Override
	public void add(E e) {
		iterative.add(curIdx, e);
	}
	
}