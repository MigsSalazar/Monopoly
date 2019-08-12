package monopoly7.utils;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;

public class SinusoidalList<E> extends LinkedList<E> {

	/**
	 * 
	 */
	private static final long serialVersionUID = -3609436102065760690L;

	private int recomputeIndex( int index ){
		int cycle = index / size();
		index %= size();
		if( cycle % 2 == 1 ){
			index *= -1;
			index = size() - index;
		}
		
		return index;
	}
	
	@Override
	public Iterator<E> iterator() {
		return new SinusoidalListIterator<E>( this, 0 );
	}

	@Override
	public E get(int index) {
		return super.get(recomputeIndex(index));
	}

	@Override
	public E set(int index, E element) {
		return super.set(recomputeIndex(index), element);
	}

	@Override
	public void add(int index, E element) {
		super.add(recomputeIndex(index), element);
	}

	@Override
	public E remove(int index) {
		return super.remove(recomputeIndex(index));
		
	}
	
	@Override
	public ListIterator<E> listIterator() {
		return new SinusoidalListIterator<E>( this, 0 );
	}

	@Override
	public ListIterator<E> listIterator(int index) {
		return new SinusoidalListIterator<E>( this, recomputeIndex(index) );
	}

	@Override
	public List<E> subList(int fromIndex, int toIndex) {
		UoroborosList<E> ret = new UoroborosList<E>();
		
		for(int i=fromIndex; i<toIndex; i++){
			ret.add(get(i));
		}
		
		return ret;
	}
}
