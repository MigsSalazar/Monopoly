package monopoly7.gui;

import java.awt.Graphics;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import monopoly7.models.Coordinates;
import monopoly7.models.RelativeDimensions;


@RequiredArgsConstructor
public class StickerBook extends BufferedRender{
	
	private List<String> pageOrder = new LinkedList<String>();
	private Map<String, StickerPage> titledPages = new HashMap<String, StickerPage>();
	
	@NonNull @Getter
	private int width;
	@NonNull @Getter
	private int height;
	
	private int counter=0;
	
	public void setWidth( int w ){
		width = w;
		dirty = true;
	}
	
	public void setHeight( int h ){
		height = h;
		dirty = true;
	}
	
	public Map.Entry<String, BufferedRender> removePage(int name){
		if( !titledPages.containsKey(name) ){
			return null;
		}
		int n = pageOrder.indexOf(name);
		dirty = true;
		return new FakeEntry<String,BufferedRender>( pageOrder.remove(n) , titledPages.remove(name) );
	}
	
	public Map.Entry<Integer, BufferedRender> removePage(String name){
		if( !titledPages.containsKey(name) ){
			return null;
		}
		int n = pageOrder.indexOf(name);
		pageOrder.remove(n);
		dirty = true;
		return new FakeEntry<Integer,BufferedRender>( n , titledPages.remove(name) );
	}
	
	public boolean changePageIndex( int oldLevel, int newLevel ){
		if( oldLevel >= pageOrder.size() || oldLevel < 0 ||
			newLevel >= pageOrder.size() || newLevel < 0 ){
			return false;
		}
		String name = pageOrder.remove(oldLevel);
		pageOrder.add(newLevel, name);
		dirty = true;
		return true;
	}
	
	public boolean changePageName( String oldName, String newName ){
		if( !pageOrder.contains(oldName) || !titledPages.containsKey(oldName) ){
			return false;
		}
		if( pageOrder.contains(newName) || titledPages.containsKey(newName) ){
			return false;
		}
		
		StickerPage img = titledPages.remove(oldName);
		titledPages.put(newName, img);
		int index = pageOrder.indexOf(oldName);
		pageOrder.set(index, newName);
		return true;
	}
	
	public boolean resizeStickerAtPage( int level, String stickerName, double w, double h){
		if( level < 0 || level >= pageOrder.size() ){
			return false;
		}
		return resizeStickerAtPage( pageOrder.get(level), stickerName, w, h );
	}
	
	public boolean resizeStickerAtPage( String pageName, String stickerName, double w, double h ){
		return resizeStickerAtPage( pageName, stickerName, new RelativeDimensions(w, h) );
	}
	
	public boolean resizeStickerAtPage( String pageName, String stickerName, RelativeDimensions dim ){
		if( titledPages.containsKey(pageName) ){
			StickerPage page = titledPages.get(pageName);
			if( page.containsSticker(stickerName) ){
				dirty = true;
				return page.resizeSticker(stickerName, dim);
			}
		}
		return false;
	}
	
	public boolean moveStickerAtPage( int level, String stickerName, double x, double y ){
		if( level < 0 || level >= pageOrder.size() ){
			return false;
		}
		return moveStickerAtPage( pageOrder.get(level), stickerName, x, y );
	}
	
	public boolean moveStickerAtPage( String pageName, String stickerName, double x, double y ){
		return moveStickerAtPage( pageName, stickerName, new Coordinates(x,y) );
	}
	
	public boolean moveStickerAtPage( String pageName, String stickerName, Coordinates c ){
		if( titledPages.containsKey(pageName) ){
			StickerPage page = titledPages.get(pageName);
			if( page.containsSticker(stickerName) ){
				dirty = true;
				return page.moveSticker(stickerName, c);
			}
		}
		return false;
	}
	
	public String addStickerToPage( int pageIndex, Sticker s, Coordinates c, RelativeDimensions d ){
		if( pageIndex < 0 || pageIndex >= pageOrder.size() ){
			return "";
		}
		return addStickerToPage( pageOrder.get(pageIndex), s, c, d );
	}
	
	public String addStickerToPage( int pageIndex, Sticker s, double x, double y, double width, double height ){
		if( pageIndex < 0 || pageIndex >= pageOrder.size() ){
			return "";
		}
		return addStickerToPage( pageOrder.get(pageIndex), s, x, y, width, height );
	}
	
	public String addStickerToPage( String pageName, Sticker s, double x, double y, double width, double height ){
		if( !titledPages.containsKey(pageName) ){
			return "";
		}
		return addStickerToPage( pageName, s, new Coordinates(x,y), new RelativeDimensions(width, height) );
	}
	
	public String addStickerToPage( String pageName, Sticker s, Coordinates c, RelativeDimensions d ){
		if( !titledPages.containsKey(pageName) ){
			return "";
		}
		return addStickerToPage( titledPages.get(pageName), s, c, d );
	}
	
	public String addStickerToPage( StickerPage page, Sticker s, Coordinates c, RelativeDimensions d ){
		String ret = page.addSticker(s, c, d);
		if( ret != null && !ret.equals("") ){
			dirty = true;
		}
		return ret;
	}
	
	public boolean pushBackPage( StickerPage br ){
		return addPage( "page"+(counter++), pageOrder.size(), br );
	}
	
	public boolean pushFrontPage( StickerPage br ){
		return addPage( "page"+(counter++), 0, br );
	}
	
	public boolean pushPageAt( int index, StickerPage br ){
		return addPage( "page"+(counter++), index, br );
	}
	
	public boolean addNewPageAt( int index ){
		return addNewPageAt( "page"+(counter++), index );
	}
	
	public boolean addNewPageAt( String name, int index ){
		return addPage( name, index, new StickerPage(width, height) );
	}
	
	public boolean addNewPageToEnd(){
		return addPage( "page"+(counter++), pageOrder.size(), new StickerPage(width,height) );
	}
	
	public boolean addNewPageToEnd( String name ){
		return addPage( name, pageOrder.size(), new StickerPage(width,height) );
	}
	
	public boolean addPage(String name, int index, StickerPage p){
		if( index < 0 || index > titledPages.size() ){
			return false;
		}
		titledPages.put( name, p );
		pageOrder.add(index, name);
		dirty = true;
		return true;
	}
	
	@Override
	public Image render() {
		if( isRenderNeeded() ){
			lastRender = new BufferedImage( width, height, BufferedImage.TYPE_INT_ARGB );
			Graphics g = lastRender.getGraphics();
			for( String s : pageOrder ){
				StickerPage page = titledPages.get(s);
				page.setWidth(width);
				page.setHeight(height);
				g.drawImage( page.render() , 0, 0, null);
			}
			dirty = false;
		}
		return lastRender;
	}
	
	private class FakeEntry <K,V> implements Map.Entry<K, V>{
		
		private K key;
		private V value;
		
		public FakeEntry( K k, V v){
			key = k;
			value = v;
		}
		
		public K getKey() {
			return key;
		}

		public V getValue() {
			return value;
		}

		public V setValue(V value) {
			V old = this.value;
			this.value = value;
			return old;
		}
		
	}
	
}
