package monopoly7.utils;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;

/**
 * My spiciest list ever. I'm certain every Java dev that has ever used Python would love to use
 * a Python list in their code where they need not worry about bounds. This is my version of a
 * Python list named after the ancient symbol for infinity: the Uoroboros which is often and 
 * conveniently depicted using snakes like pythons! However, the weak pun was not the reason for
 * the obscure, if not difficult to spell, name but rather the behavior. I could have gone with
 * CyclicList but that could be easily confused with SinusoidalLsit as their different may not
 * be obvious considering they are both associated with cycles. Uoroboros worked out specifically
 * because continuing beyond the head or tail of the Uoroboros will return you to the opposite
 * side. In contrast, for a sinusoidal graph, once you reach the peak or the valley, you must
 * iterate through every value between the two before returning to the opposite value. The name
 * may be a bit pretentious but it was the most accurate word for the situation I could think of.
 * 
 * @author Miguel Salazar
 *
 * @param <E>
 */
public class UoroborosList<E> extends LinkedList<E> {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -583479706527168363L;

	private int recomputeIndex( int index ){
		if( index < 0 ){
			while( index < 0 ){
				index += size();
			}
		}else if(index > size()){
			index = index % size();
		}
		return index;
	}
	
	@Override
	public Iterator<E> iterator() {
		return new UoroborosIterator( this, 0 );
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
		return new UoroborosListIterator( this, 0 );
	}

	@Override
	public ListIterator<E> listIterator(int index) {
		// TODO Auto-generated method stub
		return new UoroborosListIterator( this, recomputeIndex(index) );
	}

	@Override
	public List<E> subList(int fromIndex, int toIndex) {
		UoroborosList<E> ret = new UoroborosList<E>();
		
		for(int i=fromIndex; i<toIndex; i++){
			ret.add(get(i));
		}
		
		return ret;
	}
	
	class UoroborosIterator implements Iterator<E>{
		
		private UoroborosList<E> iterative;
		private int curIdx;
		
		public UoroborosIterator( UoroborosList<E> list, int idx ){
			iterative = list;
			curIdx = idx;
		}

		@Override
		public boolean hasNext() {
			return true;
		}

		@Override
		public E next() {
			curIdx++;
			return iterative.get(curIdx);
		}
		
	}
	
	class UoroborosListIterator implements ListIterator<E>{
		
		private UoroborosList<E> iterative;
		private int curIdx;
		
		public UoroborosListIterator( UoroborosList<E> list, int idx){
			curIdx = idx;
			iterative = list;
		}

		@Override
		public boolean hasNext() {
			return true;
		}

		@Override
		public E next() {
			curIdx++;
			return iterative.get(curIdx);
		}

		@Override
		public boolean hasPrevious() {
			return true;
		}

		@Override
		public E previous() {
			curIdx--;
			return iterative.get(curIdx);
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

}
