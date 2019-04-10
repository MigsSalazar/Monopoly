package monopoly7.gui;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import javax.imageio.ImageIO;
import javax.swing.*;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.flogger.Flogger;

@Flogger
public class Board extends JPanel{

	/**
	 * 
	 */
	private static final long serialVersionUID = 2069587844100683941L;
	
	private Image unscaledBaseBoard;
	private Map<Integer, Map<Integer,Image>> scaledBaseBoard;
	private Dimension scales;
	@Getter @Setter
	private BufferedRender overlay;
	
	public Board( File base, Dimension scale ){
		super();
		assert base.exists();
		scales = scale;
		overlay = null;
		initializeBaseBoards(base);
	}
	
	public Board( File base, int width, int height ){
		super();
		assert base.exists();
		scales = new Dimension( width, height );
		overlay = null;
		initializeBaseBoards(base);
	}
	
	public void setScales( Dimension s ){
		setScales( s.width, s.height );
	}
	
	public void setScales( int w, int h ){
		w -= ( w % 10 );
		h -= ( h % 10 );
		if( w <= 0 ){
			w = 1;
		}
		if( h <= 0 ){
			h = 1;
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
		if( overlay != null ){
			log.atFiner().atMostEvery(20, TimeUnit.SECONDS).log("overlay is not null");
			g.drawImage(overlay.render(), 0, 0, null);
		}
	}
	
	
}
