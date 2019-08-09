package monopoly7.gui;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.util.HashMap;
import java.util.Map;

import lombok.Getter;
import lombok.extern.flogger.Flogger;
import monopoly7.io.ImageUtil;

@Flogger
public class Sticker extends BufferedRender{
	
	public static final int ITALIC = Font.ITALIC;
	public static final int BOLD = Font.BOLD;
	
	private Map<Integer, Map<Integer, Image>> renderCache = new HashMap<Integer, Map<Integer, Image>>();
	
	private Image picture;
	@Getter 
	private String[] picDir;
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
	
	public Sticker( String t, Font f, Color fill, int b, int bSize, Color bColor, int w, int h, String... p ){
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
		picDir = p;
		text = "";
		picture = ImageUtil.openImage(picDir);
		font = new Font("", 0, 0);
		fillColor = null;
		border = 0;
		borderColor = Color.BLACK;
		width = picture.getWidth(null);
		height = picture.getHeight(null);
	}
	
	public void setPicDir( String... pd ){
		if( !picDir.equals(pd) ){
			picDir = pd;
			setDirty( true );
		}
	}
	
	public void setText( String t ){
		if( !text.equals(t) ){
			text = t;
			setDirty( true );
		}
	}
	
	public void setFont( Font f ){
		if( !font.equals(f) ){
			font = f;
			setDirty( true );
		}
	}
	
	public void setFillColor( Color fc ){
		if( !fc.equals(fillColor) ){
			fillColor = fc;
			setDirty( true );
		}
	}
	
	public void setBorder( int b ){
		if( border != b ){
			border = b;
			setDirty( true );
		}
	}
	
	public void setBorderSize( int bs ){
		if( borderSize != bs ){
			borderSize = bs;
			setDirty( true );
		}
	}
	
	public void setBorderColor( Color bc ){
		if( !bc.equals(borderColor) ){
			borderColor = bc;
			setDirty( true );
		}
	}
	
	public void setWidth( int w ){
		if( width != w ){
			width = w;
			setDirty( true );
		}
	}
	
	public void setHeight( int h ){
		if( height != h ){
			height = h;
			setDirty( true );
		}
	}
	
	@Override
	public Image render(){
		if( isDirty() ){
			if( !renderCache.containsKey(width) ){
				renderCache.put(width, new HashMap<Integer, Image>());
			}
			if( !renderCache.get(width).containsKey(height) ){
				renderCache.get(width).put(height, picture.getScaledInstance(width, height, Image.SCALE_AREA_AVERAGING));
			}
			
			setLastRender(new BufferedImage( width, height, BufferedImage.TYPE_INT_ARGB ));
			Graphics graphics = getLastRender().getGraphics();
			
			colorInBox(graphics);
			
			graphics.drawImage(renderCache.get(width).get(height), 0, 0, null);
			
			draworder(graphics);
			
			drawText(graphics);
			setDirty( false );
		}
		return getLastRender();
	}

	private void drawText(Graphics graphics) {
		if( !text.equalsIgnoreCase("") ){
			graphics.setFont(font);
			graphics.drawString(text, 0, height/2);
		}
	}

	private void colorInBox(Graphics graphics) {
		if( fillColor != null ){
			log.atFinest().log("sticker fill color is not null");
			graphics.setColor(fillColor);
			graphics.fillRect(0, 0, width, height);
		}
	}

	private void draworder(Graphics graphics) {
		if( border > 0 ){
			graphics.setColor(borderColor);
			switch( pickBorderCase(border) ){
			case 0: graphics.fillRect(0, 0, width, borderSize);
					break;
			case 1: graphics.fillRect(0, 0, borderSize, height);
					break;
			case 2: graphics.fillRect(0, height-borderSize, width, borderSize);
					break;
			case 3: graphics.fillRect(width-borderSize, 0, borderSize, height);
					break;
			default: break;
			}
		}
	}
	
	private int pickBorderCase( int b ){
		return (b % 2 == 0) ? 0 :
					(b % 3 == 0) ? 1 :
					(b % 5 == 0) ? 2 :
					(b % 7 == 0) ? 3 : -1;
	}
	
}
