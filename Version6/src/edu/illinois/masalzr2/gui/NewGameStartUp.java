package edu.illinois.masalzr2.gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.JTextField;
import javax.swing.SpinnerModel;
import javax.swing.SpinnerNumberModel;
import javax.swing.border.Border;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import edu.illinois.masalzr2.models.MonopolizedToken;


public class NewGameStartUp implements ActionListener, ChangeListener, KeyListener{
	
	private JDialog dialog;
	
	private JFrame parent;

	private List<MonopolizedToken> tokens;

	private JPanel playerNums;
	private JLabel numPrompt;
	private JSpinner numSpin;


	private JPanel playerSpecs;
	private JLabel valid;
	private JPanel[] nameSpinnerPairs;
	private JLabel[] namePrompts;
	private JTextField[] nameFields;
	private ArrayList<IndexJButton> playerIcons;

	private JPanel confirm;
	private JButton ok;
	private JButton cancel;

	private boolean finished = false;
	
	//private List<Integer> iconIds;
	private List<String> names;

	public NewGameStartUp(JFrame p, List<MonopolizedToken> tokens2){
		parent = p;

		tokens = tokens2;

		//iconIds = new ArrayList<Integer>();
		names = new ArrayList<String>();

		developPlayerNums();
		developPlayerSpecs();
		developConfirm();

		//ImageIcon miniMe = new ImageIcon(System.getProperty("user.dir")+"/resources/game-assets/frameicon.png");
		//dialog.setIconImage(miniMe.getImage());
		//ArrayList<Map<String,Integer>> fillme;

	}

	public void beginDialog(){

		dialog = new JDialog(parent, "New Game", true);
		
		BorderLayout bl = new BorderLayout();
		
		bl.setVgap(5);
		dialog.setLayout(bl);
		
		dialog.add(playerNums, BorderLayout.NORTH);
		dialog.add(playerSpecs, BorderLayout.CENTER);
		dialog.add(confirm, BorderLayout.SOUTH);
		dialog.pack();
		//dialog.setResizable(false);
		//dialog.revalidate();
		dialog.setVisible(true);
		
	}
	/*
	public List<Integer> getIconIds(){
		return iconIds;
	}
	*/
	public List<String> getNames(){
		return names;
	}

	private void developPlayerNums(){
		BorderLayout bl = new BorderLayout();
		bl.setVgap(5);
		bl.setHgap(10);
		playerNums = new JPanel(bl);

		numPrompt = new JLabel("Number of players");
		numPrompt.setHorizontalAlignment(JLabel.RIGHT);
		SpinnerModel model = new SpinnerNumberModel(2,2,8,1);
		numSpin = new JSpinner(model);
		numSpin.addChangeListener(this);

		numSpin.setPreferredSize(new Dimension(75,50));

		playerNums.add(numPrompt, BorderLayout.CENTER);
		playerNums.add(numSpin, BorderLayout.EAST);

		playerNums.setPreferredSize(new Dimension(200,30));
		Border buffer = BorderFactory.createEmptyBorder(5, 0, 2, 10);
		playerNums.setBorder(buffer);
	}

	private void developPlayerSpecs(){
		playerSpecs = new JPanel(new GridLayout(9,1));
		Border title = BorderFactory.createTitledBorder("Define your players");
		Border buffer = BorderFactory.createEmptyBorder(0, 8, 0, 8);
		playerSpecs.setBorder(BorderFactory.createCompoundBorder(buffer, title));

		valid = new JLabel("     Valid Names");
		valid.setBackground(Color.GREEN);
		valid.setOpaque(true);
		//valid.setHorizontalTextPosition(JLabel.CENTER);

		nameSpinnerPairs = new JPanel[8];

		namePrompts = new JLabel[8];

		nameFields = new JTextField[8];

		playerIcons = new ArrayList<IndexJButton>();

		for(int i=0; i<8; i++){
			nameSpinnerPairs[i] = new JPanel(new BorderLayout());

			namePrompts[i] = new JLabel("Player "+(i+1)+" name: ");
			nameFields[i] = new JTextField();
			nameFields[i].setColumns(10);
			nameFields[i].addKeyListener(this);;
			//SpinnerListModel model = new SpinnerListModel(pieceSelection);
			tokens.get(i).refreshIcon();
			playerIcons.add(new IndexJButton(tokens.get(i).getPiece()  ,i));
			playerIcons.get(i).setPreferredSize(new Dimension(30,30));
			playerIcons.get(i).addActionListener(this);

			nameSpinnerPairs[i].add(namePrompts[i], BorderLayout.WEST);
			nameSpinnerPairs[i].add(nameFields[i], BorderLayout.CENTER);
			nameSpinnerPairs[i].add(playerIcons.get(i), BorderLayout.EAST);

			nameSpinnerPairs[i].setPreferredSize(new Dimension(375,30));

			if(i > 1){
				nameFields[i].setEnabled(false);
				playerIcons.get(i).setEnabled(false);
			}

			playerSpecs.add(nameSpinnerPairs[i]);
		}
		playerSpecs.add(valid);
		//playerSpecs.setPreferredSize(new Dimension(400,600));

	}

	private void developConfirm(){

		confirm = new JPanel();

		ok = new JButton("Ok");
		ok.setPreferredSize(new Dimension(75,25));
		ok.addActionListener(this);

		cancel = new JButton("Cancel");
		cancel.setPreferredSize(new Dimension(75,25));

		cancel.addActionListener(this);

		confirm.add(ok);
		confirm.add(cancel);

		confirm.setPreferredSize(new Dimension(200,50));
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if(e.getSource().equals(cancel)){
			//iconIds = new ArrayList<Integer>();
			//iconIds.add(0);
			names = new ArrayList<String>();
			names.add("");
			dialog.dispose();
		}else if(playerIcons.contains(e.getSource())){

			IndexJButton selected = (IndexJButton)e.getSource();
			int index = (selected.getIndex()+1)%tokens.size();
			selected.setIcon(tokens.get(index).getPiece());
			selected.setIndex(index);

		}else if(e.getSource().equals(ok)){
			int num = (Integer)numSpin.getValue();
			for(int i=0; i<num; i++){

				String named = nameFields[i].getText();
				if(named.equals("")){
					named = "Unnamed "+i;
				}
				int choice = playerIcons.get(i).getIndex();
				
				String oldDir = tokens.get(i).getPieceDir();
				tokens.get(i).giveIconPath(tokens.get(choice).getPieceDir());
				tokens.get(choice).giveIconPath(oldDir);
				tokens.get(i).refreshIcon();
				tokens.get(choice).refreshIcon();
				
				//iconIds.add(choice);
				names.add(named);
				finished = true;
			}
			dialog.dispose();
		}
	}

	private void enableAllNames(){
		ok.setEnabled(true);
		valid.setText("     Valid Names");
		valid.setBackground(Color.GREEN);
	}

	private void disableInvalidNames(HashMap<String,JTextField> inputs, int enabled, String error){
		valid.setText("     Invalid Names: "+error);
		valid.setBackground(Color.RED);
		for(int i=0; i<enabled; i++){
			if(!nameFields[i].equals(inputs.get(nameFields[i].getText()))){
				//namePrompts[i].setBackground(Color.RED);
				ok.setEnabled(false);
			}
		}
	}

	@Override
	public void stateChanged(ChangeEvent e) {
		if(e.getSource().equals(numSpin)){
			//System.out.println("JSpinner registered a change");
			int num = (Integer)numSpin.getValue();
			for(int i=0; i<8; i++){
				if(num > 0){
					nameFields[i].setEnabled(true);
					playerIcons.get(i).setEnabled(true);
					num--;
				}else{
					nameFields[i].setEnabled(false);
					playerIcons.get(i).setEnabled(false);

				}
			}
		}
	}

	private boolean invalidLetter(String c){
		return c.contains("<") ||
				c.contains(">") ||
				c.contains("\\") ||
				c.contains("/") ||
				c.contains(";") ||
				c.contains(":") ||
				c.contains("\"");
	}

	private void checkNameValidity() {
		HashMap<String, JTextField> inputs = new HashMap<String, JTextField>();
		//System.out.println("old size: "+inputs.size());
		int numEnabled = 0;
		boolean validString = true;
		for(JTextField n : nameFields){
			if(n.isEnabled()){
				if(n.getText().equals("")){
					inputs.put("unnamed:"+numEnabled, n);
				}else{
					inputs.put(n.getText(), n);
				}
				numEnabled++;
			}
			if(invalidLetter(n.getText())){
				validString = false;
				//break;
			}
		}
		//System.out.println("enabled: "+numEnabled+" size: "+inputs.size());
		if(inputs.size() != numEnabled || !validString ){
			String errorMessage = validString? "All names must be unique" : "You are using an invalid character in a name";
			disableInvalidNames(inputs, numEnabled, errorMessage);
		}else if(!ok.isEnabled()){
			enableAllNames();
		}
	}
	
	public boolean isfinished() {
		return finished;
	}

	@Override
	public void keyPressed(KeyEvent e) {

	}

	@Override
	public void keyReleased(KeyEvent e) {
		//System.out.println("textarea change");
		//System.out.println(e.getSource().toString());
		if(e.getSource() instanceof JTextField){

			checkNameValidity();
		}
	}



	@Override
	public void keyTyped(KeyEvent e) {
		//System.out.println("textarea change");
		//System.out.println(e.getSource().toString());
		if(e.getSource() instanceof JTextField){
			checkNameValidity();
		}
	}

	private class IndexJButton extends JButton{

		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;
		private int index;

		public IndexJButton(ImageIcon icon, int i){
			super(icon);

			index = i;
		}

		public int getIndex(){
			return index;
		}

		public void setIndex(int i){
			index = i;
		}
	}



}
