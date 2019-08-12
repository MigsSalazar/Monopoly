package monopoly7.controllers;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.geom.Point2D.Double;
import java.util.concurrent.LinkedBlockingQueue;

import javax.swing.Timer;

import lombok.extern.flogger.Flogger;
import monopoly7.event.PlayerChangeEvent;
import monopoly7.event.PlayerChangeListener;
import monopoly7.gui.Sticker;
import monopoly7.gui.StickerPage;
import monopoly7.models.Environment;
import monopoly7.models.Player;
import monopoly7.models.RelativeDimension;
import monopoly7.pathFinding.MonopolyExplorable;
import monopoly7.utils.UoroborosList;

@Flogger
public class PlayerPieceController implements PlayerChangeListener, ActionListener{
	
	
	private StickerPage stickerPage;
	private MonopolyExplorable path;
	private String stickerName;
	private Sticker sticker;
	
	private Timer stepper;
	private LinkedBlockingQueue<Double> mover;
	
	public PlayerPieceController( StickerPage page, Sticker image, RelativeDimension rd, Double point, MonopolyExplorable path ){
		stickerPage = page;
		sticker = image;
		stickerName = stickerPage.addSticker(sticker, point, rd);
		this.path = path;
		mover = new LinkedBlockingQueue<Double>();
		stepper = new Timer(1000, this);
		log.atInfo().log("PlayerPieceController created with sticker name %s at page %s", stickerName, stickerPage);
	}
	
	@Override
	public void playerStateChanged(PlayerChangeEvent pce) {
		if( pce.getSource() instanceof Player && pce.getStatus() == PlayerChangeEvent.ChangeCode.POSITION ){
			int diceRoll = (int)pce.getNewValue() - (int)pce.getOldValue();
			log.atInfo().log("Player %s has moved by %d", ((Player)pce.getSource()).getName(), diceRoll );
			UoroborosList<Double> move = (UoroborosList<Double>)path.getReachablePoints(diceRoll, (int)pce.getOldValue());
			if( Environment.currentGame.isFancyMove() ){
				log.atFiner().log("fancy moved enabled. adding all doubles from start to finish");
				for( Double d : move ){
					mover.add(d);
				}
			}else{
				log.atFiner().log("fancy move disabled. only adding last double");
				mover.add(move.get(-1));
			}
			stepper.setDelay(1000/mover.size());
		}
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if( !mover.isEmpty() ){
			log.atFinest().log("moving sticker %s", stickerName);
			stickerPage.moveSticker(stickerName, mover.poll());
		}
	}
	
	

}
