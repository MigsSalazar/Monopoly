package monopoly7.utils;

import java.util.ListIterator;

public class SinusoidalListIterator<E> implements ListIterator<E> {
	
	private SinusoidalList<E> iterative;
	private int curIdx;
	private int first;
	private int passes = 0;

	public SinusoidalListIterator( SinusoidalList<E> it, int start ){
		iterative = it;
		curIdx = start;
		first = start;
	}
	
	@Override
	public boolean hasNext() {
		return passes < 2 && (curIdx/iterative.size()) < first;
	}

	@Override
	public E next() {
		E ret = iterative.get(curIdx);
		curIdx++;
		if( curIdx%iterative.size() == first ){
			passes++;
		}
		return ret;
	}

	@Override
	public boolean hasPrevious() {
		return passes > -2  && (curIdx/iterative.size()) > first;
	}

	@Override
	public E previous() {
		E ret = iterative.get(curIdx);
		curIdx--;
		if( (Math.sqrt(curIdx*curIdx)) % iterative.size() == first ){
			passes--;
		}
		return ret;
	}

	@Override
	public int nextIndex() {
		return curIdx+1;
	}

	@Override
	public int previousIndex() {
		return curIdx-1;
	}

	@Override
	public void remove() {
		iterative.remove(curIdx);
	}

	@Override
	public void set(E e) {
		iterative.set((curIdx), e);
	}

	@Override
	public void add(E e) {
		iterative.add(curIdx, e);
	}

}
