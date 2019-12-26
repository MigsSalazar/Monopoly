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

public class MultiTypeHashMap {
	
	private Map< Class<?>, Map<String, ?> > master = new HashMap< Class<?>, Map<String, ?> >();
	private Map< String, Class<?>> keyType = new HashMap<>();
	
	public MultiTypeHashMap(){
		master.put(null, new HashMap<String, Object>());
	}
	
	public Class<?> keysToType( String key ){
		return keyType.get(key);
	}
	
	@SuppressWarnings("unchecked")
	public <T> Map<String,T> getMapOfType( Class<T> t ){
		return (Map<String, T>) master.get(t);
	}
	
	public int size() {
		int size = 0;
		for( Class<?> t : master.keySet() ){
			size += master.get(t).size();
		}
		return size;
	}
	
	public boolean isEmpty() {
		return size() == 0;
	}

	public boolean containsKey( Object key ) {
		validKey(key);
		return keyType.containsKey(key);
	}

	public boolean containsValue( Object value ) {
		if( keyType.containsValue(value.getClass() ) ){
			return master.get(value.getClass()).containsValue(value);
		}
		return false;
	}

	public Object get( Object key ) {
		validKey(key);
		return master.get(keyType.get(key)).get(key);
	}
	
	@SuppressWarnings("unchecked")
	public <T> T get( Class<T> t, String key ){
		if( master.containsKey(t) ){
			return (T)master.get(t).get(key);
		}
		return null;
	}
	
	public Object put(String key, Object value) {
		return realPut( key, value );
	}

	@SuppressWarnings("unchecked")
	private <T> T realPut( String key, T value ){
		if( !master.containsKey(value.getClass()) ){
			master.put( value.getClass(), new HashMap<String, T>());
			keyType.put(key, value.getClass());
		}
		if( keyType.containsKey(key) && !keyType.get(key).equals( value.getClass() ) ){
			master.get(keyType.get(key)).remove(key);
			keyType.put(key, value.getClass());
		}
		return ((Map<String,T>)master.get(value.getClass())).put(key,value);
	}

	@SuppressWarnings("unchecked")
	public <T> T remove(Class<T> t, String key) {
		if( !master.containsKey(t) ){
			return null;
		}
		return (T) master.get(t).remove(key);
	}

	public Object remove(Object key){
		validKey(key);
		return master.get(keyType.get(key)).remove(key);
	}
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public void putAll(Map m) {
		m.forEach(new BiConsumer<Object, Object>(){
			@Override
			public void accept(Object t, Object u) {
				if( t instanceof String ){
					put( t.toString(), u );
				}else{
					throw new IllegalArgumentException("Keys must be of type String");
				}
			}
		});
	}
	
	public void clear() {
		master.clear();
	}

	public Set<String> keySet() {
		Set<String> ret = new HashSet<>();
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

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public Set<MultiTypeHashMapEntry<?>> entrySet() {
		Set<MultiTypeHashMapEntry<?>> ret = new HashSet<MultiTypeHashMapEntry<?>>(); 
		for( Class<?> c : master.keySet() ){
			for( String s : master.get(c).keySet() ){
				ret.add( new MultiTypeHashMapEntry( c, s, master.get(c).get(s) ) );
			}
		}
		return ret;
	}
	
	private void validKey(Object key) {
		if( !(key instanceof String ) ){
			throw new IllegalArgumentException("Key must be of type String");
		}
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
