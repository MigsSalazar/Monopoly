package monopoly7.gui;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.imageio.ImageIO;
import javax.swing.*;

public class Board extends JPanel{

	/**
	 * 
	 */
	private static final long serialVersionUID = 2069587844100683941L;
	
	private Image unscaledBaseBoard;
	private Map<Integer, Map<Integer,Image>> scaledBaseBoard;
	private Dimension scales;
	
	public Board( File base, Dimension scale ){
		super();
		assert base.exists();
		scales = scale;
		
		initializeBaseBoards(base);
	}
	
	public Board( File base, int width, int height ){
		super();
		assert base.exists();
		scales = new Dimension( width, height );
		
		initializeBaseBoards(base);
	}
	
	public void setScales( Dimension s ){
		setScales( s.width, s.height );
	}
	
	public void setScales( int w, int h ){
		int wRemainder = w % 10;
		int hRemainder = h % 10;
		if( wRemainder != 0 ){
			w -= wRemainder;
		}
		if( hRemainder != 0 ){
			h -= hRemainder;
		}
		scales.setSize(w, h);
	}

	private void initializeBaseBoards(File base) {
		scaledBaseBoard = new HashMap<Integer, Map<Integer, Image>>();
		HashMap<Integer, Image> widthMap = new HashMap<Integer, Image>(); 
		scaledBaseBoard.put(scales.width, widthMap);
		
		try {
			unscaledBaseBoard = ImageIO.read(base);
			widthMap.put(scales.height, getScaledBaseBoard( Image.SCALE_AREA_AVERAGING ) );
			this.setPreferredSize(new Dimension( scales.width, scales.height ));
			//baseBoard.
			this.setOpaque(true);
		} catch (IOException e) {
			e.printStackTrace();
			System.exit(1);
		}
	}

	private Image getScaledBaseBoard( int hint ) {
		int width = scales.width;
		if( !scaledBaseBoard.containsKey(width) ){
			scaledBaseBoard.put(width, new HashMap<Integer, Image>());
		}
		int height = scales.height;
		if( !scaledBaseBoard.get(width).containsKey(height) ){
			scaledBaseBoard.get(width).put(height, unscaledBaseBoard.getScaledInstance(width, height, hint));
		}
		return scaledBaseBoard.get(width).get(height);
	}
	
	@Override
	public void paintComponent( Graphics g ){
		super.paintComponent(g);
		//this.prepareImage(baseBoard, null);
		g.drawImage(getScaledBaseBoard( Image.SCALE_AREA_AVERAGING ), 0, 0, null);
	}
	
	
}
