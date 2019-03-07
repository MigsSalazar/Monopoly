package edu.illinois.masalzr2.gui;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Hashtable;
import java.util.HashMap;

import javax.swing.ImageIcon;

import com.google.gson.annotations.Expose;

import lombok.extern.log4j.*;

@Log4j2
public class StickerBook implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	@Expose private List<String> stickers;
	@Expose private Map<Integer, Map<Integer, Map<Integer, Integer>>> pages;
	transient private List<ImageIcon> coloredIn;
	@Expose private boolean dirtyColors = true;
	
	@Expose private int width;
	@Expose private int height;
	
	@Expose private String texture;

	
	public StickerBook(String t, int w, int h) {
		texture = t;
		width = w;
		height = h;
		makePages(width, height);
		stickers = new ArrayList<String>();
		coloredIn = new ArrayList<ImageIcon>();
	}
	
	public StickerBook(String t, int w, int h, ArrayList<String> st) {
		texture = t;
		width = w;
		height = h;
		makePages(width,height);
		
		stickers = st;
		
		coloredIn = new ArrayList<ImageIcon>();
		
		for(String s : stickers){
			coloredIn.add(new ImageIcon(s));
		}
		
	}
	
	public StickerBook(String t, int w, int h, ArrayList<String> st, Map<Integer, Map<Integer, Map<Integer, Integer>>> pa){
		texture = t;
		stickers = st;
		pages = pa;
		width = w;
		
		height = h;
		coloredIn = new ArrayList<ImageIcon>();
		
		//refreshColoredIn();
	}
	
	public List<ImageIcon> stackStickersAt(int x, int y){
		refreshColoredIn();
		
		List<ImageIcon> stacked = new ArrayList<ImageIcon>();
		
		if(pageDepthAt(x,y) > 0){
			Map<Integer,Integer> tile = pages.get(x).get(y);
			List<Integer> order = new ArrayList<Integer>(tile.keySet());
			order.sort(new Comparator<Integer>(){
				@Override
				public int compare(Integer o1, Integer o2) {
					return o1.intValue() - o2.intValue();
				}
			});
			for(int i=0; i<order.size(); i++){
				stacked.add( coloredIn.get(tile.get(i).intValue()) );
			}
		}
		
		return stacked;
	}
	
	public void placeSticker(int x, int y, int index){
		if(index < 0 || index >= stickers.size())
			return;
		
		Map<Integer, Integer> current = pages.get(x).get(y);
		current.put(current.size(), index);
	}
	
	public void addSticker(String location){
		if(!stickers.contains(location)){
			stickers.add(location);
			dirtyColors = true;
		}
	}
	
	public void removeSticker(String name){
		if(stickers.remove(name)){
			dirtyColors = true;
		}
	}
	
	public void removeSticker(int index){
		if(index < stickers.size()){
			stickers.remove(index);
			dirtyColors = true;
		}
	}
	
	public void placeStickerAtPage(int x, int y, int index, int pageNum){
		if(index < 0 || index >= stickers.size())
			return;
		pages.get(x).get(y).put(pageNum, index);
	}
	
	public int pageDepthAt(int x, int y){
		if(pages.containsKey(x) ){
			if(pages.get(x).containsKey(y)){
				return pages.get(x).get(y).size();
			}
		}
		return 0;
	}
	
	public List<String> getStickerFileLocation(){
		return stickers;
	}
	
	public List<String> listStickerLocationsAt(int x, int y){
		List<String> locs = new ArrayList<String>();
		for(int s : pages.get(x).get(y).values()){
			locs.add( stickers.get(s) );
		}
		
		return locs;
	}
	
	public ImageIcon getColoredStickerAt(int index){
		return coloredIn.get(index);
	}
	
	public ImageIcon[] getPaintedIcons(){
		refreshColoredIn();
		ImageIcon[] icons = new ImageIcon[coloredIn.size()];
		return coloredIn.subList(0, coloredIn.size()).toArray(icons);
	}
	
	public List<Integer> listStickerIndicesAt(int x, int y){
		return new ArrayList<Integer>(pages.get(x).get(y).values());
	}
	
	public int getIconIndex(ImageIcon icon){
		return getIconIndex(icon.getDescription());
	}
	
	public int getIconIndex(String location){
		return stickers.indexOf(location);
	}
	
	private void refreshColoredIn(){
		log.debug("StickerBook: refreshColoredIn: Method was called: dirtyColors={} coloredIn={}", dirtyColors, coloredIn);
		//LogMate.LOG.flush();
		if(!dirtyColors && coloredIn != null){
			return;
		}
		coloredIn = new ArrayList<ImageIcon>();
		//String sep = System.getProperty("file.separator");
		//String homeDir = System.getProperty("user.dir") + sep + "textures";
		//System.getProperty("user.dir") + sep + "textures" + sep + texture + sep;
		String sep = System.getProperty("file.separator");
		for(int i=0; i<stickers.size(); i++){
			coloredIn.add(new ImageIcon( System.getProperty("user.dir") + sep + "textures" + sep + texture + sep + stickers.get(i)) );
			//System.out.println(homeDir+stickers.get(i));
		}
		dirtyColors = false;
		log.debug("StickerBook: refreshColoredIn: End of method: coloredIn={}", coloredIn);
	}
	
	private void makePages(int w, int h) {
		pages = new Hashtable<Integer, Map<Integer, Map<Integer, Integer>>>(w, 1.0f);
		for(int x = 0; x < w; x++){
			Map<Integer, Map<Integer, Integer>> current = new Hashtable<Integer, Map<Integer, Integer>>(h, 1.0f);
			pages.put(x, current);
			for(int y = 0; y<h; y++){
				Map<Integer, Integer> square = new HashMap<Integer, Integer>();
				current.put(y, square);
			}
		}
	}
	
}
