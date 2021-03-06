 

import javax.swing.*;

import java.util.ArrayList;
import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Used by the Trade Manager. This specialized JDialog manages the final confrimation of the trade
 * @author Miguel Salazar
 *
 */
public class TConfirm extends JDialog {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 3596160266999363362L;

	private Runner gameVars;
	
	private Player player1;
	private Player player2;
	private Asset tradeAsset;
	private ArrayList<Asset> offeredAssets;
	private TradeManager tManage;
	
	private JPanel topBlock = new JPanel(new BorderLayout());
	private JLabel label1;
	private JLabel label2;
	private JLabel label3;
	
	private JPanel cenBlock = new JPanel(new BorderLayout());
	private JTextArea offeredTextArea;
	private JScrollPane oScroller;
	
	private JPanel botPanel = new JPanel(new BorderLayout());
	private JButton confirm = new JButton("Confirm");
	private JButton question = new JButton("???");
	private JButton back = new JButton("Back");
	
	/**
	 * Default constructor. Object requires a parent JFrame, a player who wants an asset, a player
	 * who holds the desired asset, the desired asset, an ArrayList of assets that is being offered
	 * and the Trade Manager object that called this JDialog
	 * @param frame	Parent JFrame
	 * @param p1	Player 1: player who wants to make an offer
	 * @param p2	Player 2: player who holds the desired asset
	 * @param ta	The desired asset held by player 2
	 * @param oa	An ArrayList of offered assets held by player 1
	 * @param tm	The Trade Manager object that called this JDialog
	 */
	public TConfirm(Runner gv, Player p1, Player p2, Asset ta, ArrayList<Asset> oa, TradeManager tm){
		super(gv.getFrame(), "Trade Manager - Step 3", true);
		gameVars = gv;
		player1=p1;
		player2=p2;
		tradeAsset = ta;
		offeredAssets = oa;
		tManage = tm;
		
		formTopBlock();
		
		this.add(topBlock, BorderLayout.NORTH);
		
		formCenBlock();
		
		this.add(cenBlock, BorderLayout.CENTER);
		
		formBotPanel();
		
		this.add(botPanel, BorderLayout.SOUTH);
		
	}
	
	/**
	 * Declares and Initializes the top portion of the JDialog and all
	 * children components
	 * Does NOT add the component to the main JDialog
	 */
	public void formTopBlock(){
		label1 = new JLabel(player2.getName()+", your competitor "+player1.getName()+" wants:");
		
		String passedAsset = "";
		if(tradeAsset.getType().equals("prop")){
			passedAsset = tradeAsset.getAsset();
		}else{
			passedAsset = "1 Get out of Jail Free Card";
		}
		
		label2 = new JLabel(passedAsset);
		
		label3 = new JLabel("Which is worth: "+gameVars.getCurrencySymbol()+tradeAsset.getWorth());
		
		topBlock.add(label1, BorderLayout.NORTH);
		topBlock.add(label2, BorderLayout.CENTER);
		topBlock.add(label3, BorderLayout.SOUTH);	
	}
	
	/**
	 * Declares and Initializes the center portion of the JDialog and all
	 * children components
	 * Does NOT add the component to the main JDialog
	 */
	public void formCenBlock(){
		
		JLabel label4 = new JLabel(player1.getName()+" has offered: ");
		
		String passedString = "";
		int worth = 0;
		
		for(Asset a : offeredAssets){
			passedString += a.getAsset()+"\n";
			worth += a.getWorth();
		}
		
		offeredTextArea = new JTextArea(offeredAssets.size(),15);
		offeredTextArea.setText(passedString);
		offeredTextArea.setEditable(false);
		
		oScroller = new JScrollPane(offeredTextArea);
		
		JLabel label5 = new JLabel("Which is worth: "+gameVars.getCurrencySymbol()+worth);
		
		cenBlock.add(label4, BorderLayout.NORTH);
		cenBlock.add(oScroller, BorderLayout.CENTER);
		cenBlock.add(label5, BorderLayout.SOUTH);
		
	}
	
	/**
	 * Declares and Initializes the bottom portion of the JDialog and all
	 * children components
	 * Does NOT add the component to the main JDialog
	 */
	public void formBotPanel(){
		
		confirm.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				tManage.setAgreed(true);
				imDoneThree();
			}
		});
		
		question.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				String step3 = 
					"Here you will see the assets that have been"
				+ "\noffered for trade. At the top, you will see"
				+ "\nthe desired asset owned by the designated"
				+ "\nplayer and on the bottom are the assets you"
				+ "\nhave offered in return. Press confirm to"
				+ "\nfinalize the trade or back to return to the"
				+ "\nprevious step. Closing this window will"
				+ "\ncompletely cancel trade meaning no assets"
				+ "\nwill be lost or gained.";
				post(step3);
			}
		});
		
		back.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				tManage.setStep(1);
				imDoneThree();
			}
		});
		
		botPanel.add(back, BorderLayout.WEST);
		botPanel.add(question, BorderLayout.CENTER);
		botPanel.add(confirm, BorderLayout.EAST);
		
	}
	
	/**
	 * Calls a JOptionPane.showMessageDialog to present the passed String
	 * Used in ActionListeners that cannot call JOptionPanes directly
	 * @param send	String to present to the players
	 */
	public void post(String send){
		JOptionPane.showMessageDialog(gameVars.getFrame(), send);
	}
	
	/**
	 * Disposes itself
	 */
	public void imDoneThree(){
		this.dispose();
	}
	
}
