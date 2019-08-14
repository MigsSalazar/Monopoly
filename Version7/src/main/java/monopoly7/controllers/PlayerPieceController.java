package monopoly7.controllers;

import java.awt.geom.Point2D.Double;

import monopoly7.event.PlayerChangeEvent;
import monopoly7.event.PlayerChangeListener;
import monopoly7.gui.Sticker;
import monopoly7.gui.StickerPage;
import monopoly7.models.RelativeDimension;

public class PlayerPieceController implements PlayerChangeListener{
	
	
	private StickerPage stickerPage;
	private String stickerName;
	private Sticker sticker;
	
	public PlayerPieceController( StickerPage page, Sticker image, RelativeDimension rd, Double point ){
		stickerPage = page;
		sticker = image;
		stickerName = stickerPage.addSticker(sticker, point, rd);
	}
	
	@Override
	public void playerStateChanged(PlayerChangeEvent pce) {
		// TODO Auto-generated method stub
		
	}

}
