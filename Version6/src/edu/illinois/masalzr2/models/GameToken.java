package edu.illinois.masalzr2.models;

import java.awt.MediaTracker;
import java.io.File;
import java.io.Serializable;
import java.util.Comparator;

import javax.swing.ImageIcon;

import com.google.gson.annotations.Expose;

import lombok.Getter;
import lombok.Setter;


public class GameToken implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	@Getter @Setter 
	private transient ImageIcon piece;
	@Getter @Setter @Expose 
	private int team;
	@Getter @Setter @Expose
	private String pieceDir;
	
	public static final transient Comparator<GameToken> TEAM_ORDER = new SortByTeam();
	public static final transient Comparator<GameToken> ICON_ORDER = new SortByIcon();
	
	public GameToken(int t, String dir){
		
		team = t;
		
		piece = new ImageIcon();
		
		giveIconPath(dir);
		
	}
	
	public boolean giveIconPath(String dir){
		piece = new ImageIcon(dir);
		pieceDir = dir;
		return piece.getImageLoadStatus() == MediaTracker.COMPLETE;
	}
	
	public void refreshIcon(){
		refreshIcon(System.getProperty("user.dir"));
	}
	
	public void refreshIcon(String parentDir){
		File f = new File(parentDir + File.separator + "textures" + File.separator + pieceDir);
		//System.out.println(parentDir + File.separator + "textures" + File.separator + pieceDir);
		if(f.exists()){
			piece = new ImageIcon(f.getAbsolutePath());
		}else{
			piece = new ImageIcon(pieceDir);
		}
		
	}
	
	private static class SortByTeam implements Comparator<GameToken>{
		public int compare(GameToken g0, GameToken g1) {
			return g0.getTeam() - g1.getTeam();
		}
	}
	
	private static class SortByIcon implements Comparator<GameToken>{
		public int compare(GameToken o0, GameToken o1) {
			return o0.pieceDir.compareTo(o1.pieceDir);
		}
	}
	
}
