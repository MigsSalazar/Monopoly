/**
 * 
 */
package monopoly7.utils;

import java.util.AbstractCollection;
import java.util.ArrayList;
import java.util.Collection;
import java.util.ConcurrentModificationException;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;


/**
 * The intent of this class was to make a Python-esque list class in Java.
 * Much of the code was based off the {@link AbstractList} class esspecially
 * in the sublist class and iterator classes. For that reason, the creators of
 * the {@link AbstractList} class are also accredited as authors for this class.
 * @author	Miguel Salazar
 * @author  Josh Bloch
 * @author  Neal Gafter
 *
 */
public class RevolvingList<E> extends AbstractCollection<E> implements List<E> {

	protected List<E> source;
	protected transient int modCount = 0;
	
	/**
	 * A basic constructor establishing an {@link ArrayList} object as the base
	 * object for this RevolvingList instance. 
	 */
	public RevolvingList(){
		source = new ArrayList<E>();
	}
	
	/**
	 * The RevolvingList class is ultimately a wrapper around any list object
	 * allowing developers to utilize the underlying framework of the Java
	 * utils Lists but in a Python-esque way. This constructor, then, allows
	 * the developer to specify which underlying Java List subclass to use as
	 * its base object. 
	 * @param type
	 */
	public RevolvingList( Class<? extends List<E>> type ){
		try {
			source = type.newInstance();
		} catch (InstantiationException | IllegalAccessException e) {
			throw new IllegalArgumentException("Invalid list type");
		}
	}
	
	/**
	 * A shallow copy of the collection is made out of this constructor,
	 * ensuring that all elements in the collection are available to this
	 * list as well, but ensuring that the constructed RevolvingList is
	 * not also dependent on the original collection. If the collection
	 * also happens to be a list, then the base list takes the same type
	 * as the in passed list. Otherwise, an ArrayList is used instead.
	 * @param c
	 */
	@SuppressWarnings("unchecked")
	public RevolvingList( Collection<E> c ){
		try {
			if( c instanceof List ){
				source = (List<E>)c.getClass().newInstance();
			}else{
				source = new ArrayList<E>( c.size() );
			}
		} catch (InstantiationException | IllegalAccessException e) {
			throw new IllegalArgumentException("Invalid list type");
		}
		source.addAll(c);
	}

	/**
	 * Calculates the appropriate index given any valid integer
	 * @param idx	index desired
	 * @return 	The "revolved" index
	 */
	protected int revolvedIndex( int idx ){
		int size = source.size();
		while( idx < 0 ){
			idx += size;
		}
		return idx % size;
	}

	@Override
	public boolean addAll(int index, Collection<? extends E> c) {
		modCount++;
		return source.addAll(revolvedIndex(index), c);
	}

	@Override
	public E get(int index) {
		return source.get(index);
	}

	@Override
	public E set(int index, E element) {
		return source.set(revolvedIndex(index), element);
	}

	@Override
	public void add(int index, E element) {
		modCount++;
		source.add(revolvedIndex(index), element);
	}

	@Override
	public E remove(int index) {
		modCount++;
		return source.remove(revolvedIndex(index));
	}

	@Override
	public int indexOf(Object o) {
		return source.indexOf(o);
	}

	@Override
	public int lastIndexOf(Object o) {
		return source.lastIndexOf(o);
	}

	@Override
	public ListIterator<E> listIterator() {
		return new RevolvingListIterator<E>( this );
	}

	@Override
	public ListIterator<E> listIterator(int index) {
		return new RevolvingListIterator<E>(this, index);
	}
	
	/**
	 * Returns a revolving sublist that begins at the head, and ends
	 * at the toIndex. However, this sublist will still revolve around
	 * the array's boundaries, but only the sublist's boundaries
	 * @param toIndex
	 * @return
	 */
	public List<E> subList( int toIndex ){
		return new RevolvingSubList<E>(this, 0, revolvedIndex(toIndex));
	}

	@Override
	public List<E> subList(int fromIndex, int toIndex) {
		return new RevolvingSubList<E>(this, revolvedIndex(fromIndex), revolvedIndex(toIndex));
	}

	@Override
	public Iterator<E> iterator() {
		return new RevolvingListIterator<E>(this);
	}

	@Override
	public int size() {
		return source.size();
	}

}

/**
 * 
 * @author Miguel Salazar
 *
 * @param <E>
 */
class RevolvingSubList<E> extends RevolvingList<E>{
	
	private final RevolvingList<E> list;
	private final int offset;
	private int size;
	
	RevolvingSubList( RevolvingList<E> l, int start, int end ){
		list = l;
		offset = start;
		if( end < start ){
			size = start - end;
		}else{
			size = end - start;
		}
		source = list;
	}
	
	@Override
	protected int revolvedIndex(int idx){
		while( idx < size ){
			idx += size;
		}
		return idx % size;
	}

    public E set(int index, E element) {
    	rangeCheck(index);
        checkForComodification();
        return list.set(revolvedIndex(index)+offset, element);
    }

    public E get(int index) {
    	rangeCheck(index);
        checkForComodification();
        return list.get(revolvedIndex(index)+offset);
    }

    public int size() {
        checkForComodification();
        return size;
    }

    public void add(int index, E element) {
    	rangeCheckForAdd(index);
        checkForComodification();
        list.add(revolvedIndex(index)+offset, element);
        this.modCount = list.modCount;
        size++;
    }

    public E remove(int index) {
    	rangeCheck(index);
        checkForComodification();
        E result = list.remove(revolvedIndex(index)+offset);
        this.modCount = list.modCount;
        size--;
        return result;
    }

    public boolean addAll(Collection<? extends E> c) {
        return addAll(size, c);
    }


    public boolean addAll(int index, Collection<? extends E> c) {
    	rangeCheckForAdd(index);
        int cSize = c.size();
        if (cSize==0)
            return false;

        checkForComodification();
        list.addAll(revolvedIndex(offset)+index, c);
        this.modCount = list.modCount;
        size += cSize;
        return true;
    }
    
    public ListIterator<E> listIterator(final int index) {
    	return new RevolvingListIterator<E>( this, index );
    }
    
    private void rangeCheck(int index) {
        if (index < 0 || index >= size)
            throw new IndexOutOfBoundsException(outOfBoundsMsg(index));
    }
    
    private void rangeCheckForAdd(int index) {
        if (index < 0 || index > size)
            throw new IndexOutOfBoundsException(outOfBoundsMsg(index));
    }
    
    private String outOfBoundsMsg(int index) {
        return "Index: "+index+", Size: "+size;
    }
	
    public List<E> subList(int fromIndex, int toIndex) {
    	return new RevolvingSubList<>(this, fromIndex, toIndex);
    }
    
	private void checkForComodification() {
        if (this.modCount != list.modCount)
            throw new ConcurrentModificationException();
    }
}

/**
 * Because of the nature of iterators, the revolving list iterator
 * will return false when calling {@link #hasPrevious} or
 * {@link #hasNext} when the iterator points to the head or tail
 * respectively. However, so long as the underlying list contains
 * at least one element, this iterator will always be able to find
 * a next or previous element. Besides that, this iterator works
 * as any would expect.
 * @author Miguel Salazar
 *
 * @param <E> 	Type of the elements to be utilized
 */
class RevolvingListIterator<E> implements ListIterator<E>{

	protected RevolvingList<E> source;
	private int cursor;
	
	RevolvingListIterator( RevolvingList<E> s ){
		source = s;
		cursor = 0;
	}
	
	RevolvingListIterator( RevolvingList<E> s, int offset ){
		source = s;
		cursor = offset;
	}
	
	public boolean atTail(){
		return (cursor-1) % source.size() == 0;
	}
	
	public boolean atHead(){
		return cursor % source.size() == 0; 
	}
	
	@Override
	public boolean hasNext() {
		return (cursor-1) % source.size() != 0;
	}

	@Override
	public E next() {
		return source.get(++cursor);
	}

	@Override
	public boolean hasPrevious() {
		return cursor % source.size() != 0;
	}

	@Override
	public E previous() {
		return source.get(--cursor);
	}

	@Override
	public int nextIndex() {
		return cursor + 1;
	}

	@Override
	public int previousIndex() {
		return cursor - 1;
	}

	@Override
	public void remove() {
		source.remove(cursor);
	}

	@Override
	public void set(E e) {
		source.set(cursor, e);
	}

	@Override
	public void add(E e) {
		source.add(cursor, e);
	}

}

