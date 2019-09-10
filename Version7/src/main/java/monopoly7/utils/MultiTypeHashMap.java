package monopoly7.utils;

import java.util.Collection;
import java.util.Map;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.function.BiConsumer;

import lombok.Getter;

public class MultiTypeHashMap{
	
	private Map< Class<?>, Map<String, ?> > master = new HashMap< Class<?>, Map<String, ?> >();
	private int bigness = 0; //why bigness? because size was taken by the method
	
	@SuppressWarnings("unchecked")
	public <T> Map<String, T> keysToType( Class<T> type ){
		return (Map<String, T>) master.get(type);
	} 
	
	public int size() {
		return bigness;
	}

	public boolean isEmpty() {
		return bigness == 0;
	}

	public <T> boolean containsKey( Class<T> t, String key ) {
		if( master.containsKey(t) ){
			return master.get(t).containsKey(key);
		}
		return false;
	}

	public <T> boolean containsValue( Class<T> t, Object value ) {
		if( master.containsKey(t) ){
			return master.get(t).containsValue( value );
		}
		return false;
	}

	public <T> T get( Class<T> t, String key) {
		if( master.containsKey(t) ){
			master.get(t).get(key);
		}
		return null;
	}

	@SuppressWarnings("unchecked")
	public <T> T put(String key, T value) {
		if( !master.containsKey(value.getClass()) ){
			master.put( value.getClass(), new HashMap<String, T>());
		}
		return ((Map<String, T>)(master.get(value.getClass()))).put(key, value);
	}

	public <T> T remove(Class<T> t, String key) {
		if( !master.containsKey(t) ){
			return null;
		}
		@SuppressWarnings("unchecked")
		T ret = (T) master.get(t).remove(key);
		if( master.get(t).isEmpty() ){
			master.remove(t);
		}
		return ret;
	}

	public <T> void putAll(Map<String, T> m) {
		m.forEach(new BiConsumer<String, T>(){
			@Override
			public void accept(String t, T u) {
				put( t, u );
			}
		});
	}

	public void clear() {
		master.clear();
	}

	public Set<Object> keySet() {
		Set<Object> ret = new HashSet<Object>();
		for( Class<?> c : master.keySet() ){
			ret.addAll(master.get(c).keySet());
		}
		return ret;
	}

	public Set<Class<?>> typeSet(){
		return master.keySet();
	}
	
	public Collection<Object> values() {
		List<Object> ret = new LinkedList<Object>();
		for( Class<?> c : master.keySet() ){
			ret.addAll(master.get(c).values());
		}
		return ret;
	}

	@SuppressWarnings("unchecked")
	public <T> Set<MultiTypeHashMapEntry<?>> entrySet() {
		Set<MultiTypeHashMapEntry<?>> ret = new HashSet<MultiTypeHashMapEntry<?>>(); 
		for( Class<?> c : master.keySet() ){
			for( String s : master.get(c).keySet() ){
				ret.add( new MultiTypeHashMapEntry<T>( (Class<T>)c, s, (T) master.get(c).get(s) ) );
			}
		}
		return ret;
	}
	
	class MultiTypeHashMapEntry<T> implements Map.Entry<String, T>{
		@Getter Class<T> type;
		@Getter String key;
		@Getter T value;
		
		public MultiTypeHashMapEntry( Class<T> t, String k, T v ){
			type = t;
			key = k;
			value = v;
		}

		@Override
		public T setValue(T value) {
			T old = this.value;
			this.value = value;
			return old;
		}
		
	}

}
