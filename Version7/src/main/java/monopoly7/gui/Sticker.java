package monopoly7.gui;

import java.util.Map;
import java.util.HashMap;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.image.BufferedImage;

import lombok.Getter;
import monopoly7.io.ImageUtil;

public class Sticker extends BufferedRender{
	
	public static final int ITALIC = Font.ITALIC;
	public static final int BOLD = Font.BOLD;
	
	private Map<Integer, Map<Integer, Image>> renderCache = new HashMap<Integer, Map<Integer, Image>>();
	
	private Image picture;
	@Getter 
	private String picDir;
	@Getter 
	private String text;
	@Getter 
	private Font font;
	@Getter
	private Color fillColor;
	@Getter
	private int border;
	@Getter
	private int borderSize;
	@Getter
	private Color borderColor;
	@Getter
	private int width;
	@Getter
	private int height;
	
	public Sticker( String p, String t, Font f, Color fill, int b, int bSize, Color bColor, int w, int h ){
		picDir = p;
		text = t;
		picture = ImageUtil.openImage(p);
		font = f;
		fillColor = fill;
		border = b;
		borderSize = bSize;
		borderColor = bColor;
		width = w;
		height = h;
	}
	
	public Sticker( String... p ){
		StringBuilder sb = new StringBuilder();
		for( String s : p ){
			sb.append(ImageUtil.FILESEP).append(s);
		}
		picDir = sb.toString();
		text = "";
		picture = ImageUtil.openImage(picDir);
		font = new Font("", 0, 0);
		fillColor = null;
		border = 0;
		borderColor = Color.BLACK;
		width = picture.getWidth(null);
		height = picture.getHeight(null);
	}
	
	public void setPicDir( String pd){
		picDir = pd;
		dirty = true;
	}
	
	public void setText( String t ){
		text = t;
	}
	
	public void setFont( Font f ){
		font = f;
		dirty = true;
	}
	
	public void setFillColor( Color fc ){
		if( !fc.equals(fillColor) ){
			fillColor = fc;
			dirty = true;
		}
	}
	
	public void setBorder( int b ){
		border = b;
		dirty = true;
	}
	
	public void setBorderSize( int bs ){
		borderSize = bs;
		dirty = true;
	}
	
	public void setBorderColor( Color bc ){
		if( !bc.equals(borderColor) ){
			borderColor = bc;
			dirty = true;
		}
	}
	
	public void setWidth( int w ){
		width = w;
		dirty = true;
	}
	
	public void setHeight( int h ){
		height = h;
		dirty = true;
	}
	
	@Override
	public Image render(){
		if( isRenderNeeded() ){
			if( !renderCache.containsKey(width) ){
				renderCache.put(width, new HashMap<Integer, Image>());
			}
			if( !renderCache.get(width).containsKey(height) ){
				renderCache.get(width).put(height, picture.getScaledInstance(width, height, Image.SCALE_AREA_AVERAGING));
			}
			lastRender = new BufferedImage( width, height, BufferedImage.TYPE_INT_ARGB );
			Graphics graphics = lastRender.getGraphics();
			
			if( fillColor != null ){
				System.out.println("fill color is not null");
				graphics.setColor(fillColor);
				graphics.fillRect(0, 0, width, height);
			}
			
			graphics.drawImage(renderCache.get(width).get(height), 0, 0, null);
			
			if( border > 0 ){
				graphics.setColor(borderColor);
				//top border
				if( border % 2 == 0 )
					graphics.fillRect(0, 0, width, borderSize);
				//west border
				if( border % 3 == 0 )
					graphics.fillRect(0, 0, borderSize, height);
				//bottom border
				if( border % 5 == 0 )
					graphics.fillRect(0, height-borderSize, width, borderSize);
				//east border
				if( border % 7 == 0 ) 
					graphics.fillRect(width-borderSize, 0, borderSize, height);
			}
			if( !text.equalsIgnoreCase("") ){
				graphics.setFont(font);
				graphics.drawString(text, 0, height/2);
			}
			dirty = false;
		}
		return lastRender;
	}
	
}
