package edu.illinois.masalzr2.gui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import edu.illinois.masalzr2.controllers.Environment;
import edu.illinois.masalzr2.models.Counter;
import edu.illinois.masalzr2.models.Player;
import edu.illinois.masalzr2.models.Street;
import edu.illinois.masalzr2.models.Suite;

public class UpgradeManager extends JDialog implements ActionListener, ItemListener{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Environment gameVars;
	private Player player;
	private List<Street> props;
	private Map<String,Suite> suites;
	private boolean hasMonopoly;
	private Suite currSuite;

	private JPanel selection;
	private JButton question;
	private JComboBox<String> colors;
	private JButton confirm;
	
	
	private JPanel alteration;
	private JPanel[] propDisplay;
	private JLabel[] propNames;
	private JPanel[] textButtons;
	private JLabel[] propGrade;
	private int[] grades;
	private ArrayList<JButton> add;
	private ArrayList<JButton> sub;
	private JLabel improper = new JLabel("No data to show");
	
	private JPanel status;
	private JLabel totalCost;
	private int total;
	private JLabel upgradeCost;
	private int addCost;
	private int added;
	private JLabel downgradeCost;
	private int subCost;
	private int subed;
	
	private Counter housecount;
	private Counter hotelcount;
	
	//TODO make 3 JLabels for the total costs, the total upgrade costs, and the total downgrade costs within a third JPanel
	
	
	public UpgradeManager(Environment gv, Player pl) {
		super(gv.getFrame(), "Upgrade Manager", true);
		gameVars = gv;
		housecount = gameVars.getHouseCount();
		hotelcount = gameVars.getHotelCount();
		player = pl;
		suites = gameVars.getSuites();
		
		developTop();
		developMiddle();
		developBottom();
		
	}

	public void beginManager() {
		if(!hasMonopoly) {
			JOptionPane.showMessageDialog(gameVars.getFrame(), "You do have no Monopolized any suites.\nThis manager will not begin until you do.");
			this.dispose();
			return;
		}
		try{
			BorderLayout bl = new BorderLayout();
			bl.setHgap(5);
			bl.setVgap(5);
			this.setLayout(bl);
			this.add(selection, BorderLayout.NORTH);
			this.add(alteration, BorderLayout.CENTER);
			this.add(status, BorderLayout.SOUTH);
			
			this.pack();
			this.setIconImage(gameVars.getFrame().getIconImage());
			this.setResizable(false);
			this.setVisible(true);
			this.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		}catch(Exception e){
			e.printStackTrace();
			JOptionPane.showMessageDialog(gameVars.getFrame(), "Cannot start Upgrade Manager");
			this.dispose();
		}
	}
	
	private void developBottom(){
		status = new JPanel(new BorderLayout());
		total = 0;
		addCost = 0;
		subCost = 0;
		
		added = 0;
		subed = 0;
		
		totalCost = new JLabel();
		totalCost.setHorizontalAlignment(JLabel.CENTER);
		
		upgradeCost = new JLabel();
		upgradeCost.setHorizontalAlignment(JLabel.CENTER);
		
		downgradeCost = new JLabel();
		downgradeCost.setHorizontalAlignment(JLabel.CENTER);
		if(hasMonopoly){
			updateCosts();
		}
		
		
		status.add(totalCost, BorderLayout.WEST);
		status.add(upgradeCost, BorderLayout.CENTER);
		status.add(downgradeCost, BorderLayout.EAST);
	}
	
	private void developMiddle(){
		if(hasMonopoly){
			currSuite = suites.get(colors.getSelectedItem());
			List<Street> suiteProps = currSuite.sortedByPosition();
			
			GridLayout grid = new GridLayout(3,1);
			grid.setHgap(5);
			//grid.setVgap(5);
			
			alteration = new JPanel(grid);
			propDisplay = new JPanel[3];
			propNames = new JLabel[3];
			
			propGrade = new JLabel[3];
			grades = new int[3];
			add = new ArrayList<JButton>();
			sub = new ArrayList<JButton>();
			textButtons = new JPanel[3];
			
			for(int i=0; i<3; i++){
				propDisplay[i] = new JPanel(new BorderLayout());
				
				propNames[i] = new JLabel();
				
				propGrade[i] = new JLabel();
				
				add.add( new JButton("+") );
				add.get(i).addActionListener(this);
				
				sub.add( new JButton("-") );
				sub.get(i).addActionListener(this);
				
				textButtons[i] = new JPanel(new GridLayout(1,3));
			}
			
			fillNames(suiteProps);
			fillGrades(suiteProps);
			completeAddSub(suiteProps);
			populateTextButtons();
			populatePropDisplay();
			
			alteration.add(propDisplay[0]);
			alteration.add(propDisplay[1]);
			alteration.add(propDisplay[2]);
			
			alteration.setPreferredSize(new Dimension(300,90));
			
		}else{
			alteration = new JPanel();
			alteration.add(improper);
			propDisplay = null;
			propNames = null;
			textButtons = null;
			propGrade = null;
			add = null;
			sub = null;
			
		}
	}
	
	private void developTop(){
		selection = new JPanel(new BorderLayout());
		
		confirm = new JButton("Ok");
		confirm.addActionListener(this);
		
		ArrayList<String> heldSuites = new ArrayList<String>();
		
		for(Suite s : suites.values()){
			if(s.isMonopolized(player) && s.isBuildable()){
				heldSuites.add(s.getColorName());
			}
		}
		if(heldSuites.size() > 0){
			colors = new JComboBox<String>(heldSuites.toArray(new String[heldSuites.size()]));
			hasMonopoly = true;
		}else{
			colors = new JComboBox<String>();
			colors.addItem("You own no monopolies");
			hasMonopoly = false;
		}
		//colors = new JComboBox<String>(heldSuites.toArray(new String[heldSuites.size()]));
		colors.addItemListener(this);
		
		question = new JButton("?");
		question.addActionListener(this);
		
		selection.add(confirm,BorderLayout.WEST);
		selection.add(colors, BorderLayout.CENTER);
		selection.add(question, BorderLayout.EAST);
		
		selection.setPreferredSize(new Dimension(300,30));
		
	}

	private void updateCosts(){
		props = currSuite.sortedByPosition();
		Street property;
		added = 0;
		subed = 0;
		for(int i=0; i<props.size(); i++){
			property = props.get(i);
			if(grades[i] > property.getGrade()){
				added += grades[i] - property.getGrade();
			}
			if(grades[i] < property.getGrade()){
				subed += property.getGrade() - grades[i];
			}
		}
		addCost = added * props.get(0).getUpgradeCost();
		subCost = subed * (props.get(0).getUpgradeCost() / 2 );
		total = addCost - subCost;
		
		totalCost.setText("Total Costs: $"+total);
		upgradeCost.setText("Upgrades: $"+addCost);
		downgradeCost.setText("Downgrades: $"+subCost);
	}
	
	private void fillNames(List<Street> props){
		
		for(int i=0; i<props.size(); i++){
			propNames[i].setText(props.get(i).getName());
			propNames[i].setHorizontalAlignment(JLabel.CENTER);
		}
	}
	
	private void fillGrades(List<Street> props){
		
		for(int i=0; i<props.size(); i++){
			grades[i] = props.get(i).getGrade();
			propGrade[i].setText(""+grades[i]);
		}
	}
	
	private void setTextButtonsToDefault(){
		for(int i=0; i<3; i++){
			propNames[i].setText("");
			propGrade[i].setText("");
			add.get(i).setEnabled(false);
			add.get(i).setToolTipText("No Property");
			sub.get(i).setEnabled(false);
			sub.get(i).setToolTipText("No Property");
			
		}
		
	}
	
	
	private int smallestGrade(){
		int smallest = grades[0];
		for(int i=0; i<currSuite.sortedByPosition().size(); i++){
			if(grades[i] < smallest){
				smallest = grades[i];
			}
		}
		return smallest;
	}
	
	private int largestGrade(){
		int largest = grades[0];
		for(int i=0; i<currSuite.sortedByPosition().size(); i++){
			if(grades[i] > largest){
				largest = grades[i];
			}
		}
		return largest;
	}
	
	private void completeAddSub(List<Street> props){
		//Reseting all buttons to a default state
		
		for(int i=0; i<3; i++){
			add.get(i).setEnabled(false);
			sub.get(i).setEnabled(false);
		}
		
		int smallGrade = smallestGrade();
		int largeGrade = largestGrade();
		
		//Populating buttons with a tooltip and situational enabled status
		for(int i=0; i<props.size(); i++){
			Street current = props.get(i);
			int tooltipRent = current.getUpgradeCost();
			
			add.get(i).setToolTipText(""+tooltipRent);
			
			
			
			//System.out.println("add check: current.getGrade - suite's smallest grade: "+(grades[i] - currSuite.smallestGrade()));
			if(grades[i] - smallGrade < 1 && grades[i] < 5 && ampleSupply()){
				add.get(i).setEnabled(true);
			}else{
				add.get(i).setEnabled(false);
			}
			
			sub.get(i).setToolTipText(""+((int)tooltipRent/2));
			//System.out.println("sub check: suite's largest grade - current.getGrade: "+(grades[i] - currSuite.smallestGrade()));
			if(largeGrade-grades[i] < 1 && grades[i] > 0){
				sub.get(i).setEnabled(true);
			}else{
				sub.get(i).setEnabled(false);
			}
			
		}
	}
	
	private void populateTextButtons(){
		JPanel current;
		for(int i=0; i<3; i++){
			current = textButtons[i];
			
			current.add(add.get(i));
			current.add(propGrade[i]);
			current.add(sub.get(i));
			current.setPreferredSize(new Dimension(150,100));
		}
	}
	
	private void populatePropDisplay(){
		for(int i=0; i<3; i++){
			propDisplay[i].add(propNames[i], BorderLayout.CENTER);
			propDisplay[i].add(textButtons[i], BorderLayout.EAST);
		}
		
	}
	
	private void setGradeChange(){
		
		if(total >= player.getCash()){
			JOptionPane.showMessageDialog(this, "You cannot afford the upgrades you have requested"
											+ "\nwithout going bankrupt!");
			return;
		}
		String output = "For "+added+ " upgrades"
				+ "\nand "+subed+ " downgrades:"
				+ "\nUpgrade costs:    $"+addCost
				+ "\nDowngrade costst: $"+subCost
				+ "\nTotal owed:       $"+total;
		int choice = JOptionPane.showConfirmDialog(this, output, "Confirm Transaction", JOptionPane.YES_NO_OPTION);
		if(choice == JOptionPane.YES_OPTION){
			for(int i=0; i<props.size(); i++){
				props.get(i).setGrade(grades[i]);
				if(grades[i] == 5){
					hotelcount.add(1);
				}else{
					int num = grades[i] - props.get(i).getGrade();
					housecount.add(num);
				}
			}
			player.subCash(total);
			gameVars.paintHousing();
		}
		
	}
	
	private boolean ampleSupply(){
		boolean retval = true;
		int combinedHouse = 0;
		int combinedHotel = 0;
		props = currSuite.sortedByName();
		for(int i=0; i<props.size(); i++){
			Street p = props.get(i);
			if(grades[i] == 5 && p.getGrade() < 5){
				combinedHouse -= p.getGrade();
				combinedHotel++;
			}else if(grades[i] < 5 && p.getGrade() == 5){
				combinedHotel--;
				combinedHouse += grades[i];
			}else if(grades[i] < 5 && p.getGrade() < 5 && grades[i] != p.getGrade()){
				combinedHouse += grades[i] - p.getGrade();
			}
		}
		
		if(housecount.getCount() + combinedHouse > housecount.getMax()){
			retval = false;
		}
		if(hotelcount.getCount() + combinedHotel > hotelcount.getMax()){
			retval = false;
		}
		
		return retval;
	}
	
	private void buttonPush(JButton as){
		if(add.contains(as)){
			//Discover the acted upon button
			int addIndex = add.indexOf(as);
			//make sure the button cannot be spammed (easily)
			add.get(addIndex).setEnabled(false);
			
			//Increment grades
			grades[addIndex]++;
			
			//set the grade's text
			propGrade[addIndex].setText(""+ grades[addIndex] );
			
			//update enabled status and tool-tips
			completeAddSub(currSuite.sortedByPosition());
			
		}else if(sub.contains(as)){
			
			int subIndex = sub.indexOf(as);
			
			sub.get(subIndex).setEnabled(false);
			
			grades[subIndex]--;
			
			propGrade[subIndex].setText(""+ grades[subIndex] );
			
			completeAddSub(currSuite.sortedByPosition());
			
		}
		updateCosts();
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		
		if(add.contains(e.getSource()) || sub.contains(e.getSource())){
			buttonPush((JButton)e.getSource());
		}else if(e.getSource().equals(confirm)){
			setGradeChange();
		}else if(e.getSource().equals(question)){
			JOptionPane.showMessageDialog(this, "UPGRADE MANAGER"
											+"\n====================="
											+"\nHere you can select the suite/monopoly you want"
											+"\nto upgrade with houses or hotels. The price to"
											+"\npurchase or sell houses or hotels can be gotten"
											+"\nby hovering over the + or - buttons. You cannot"
											+"\nhave a housing disparity greater than 1 in any"
											+"\nsuite regardless if upgrading or downgrading."
											+"\nChanging to another suite/color of properties"
											+"\nwithout clicking the Ok button WILL SCRAP ANY"
											+"\nCHANGES MADE. ALL TRANSACTIONS ARE FINAL."
											+"\nTo leave, simply x out of the manager.");
		}
		
	}

	@Override
	public void itemStateChanged(ItemEvent e) {
		/*
		 * private GameVariables gameVars;
		 * private Player player;
		 * private Map<String,Property> props;
		 * private Map<String,Suite> suites;
		 * private boolean hasMonopoly;
		 * private Suite currSuite;
		 */
		
		String nextSuite = (String)colors.getSelectedItem();
		if(nextSuite.equals("You own no monopolies")) {
			return;
		}
		//currSuite = suites.get(nextSuite);
		if(!currSuite.getColorName().equals(suites.get(nextSuite).getColorName())){
			currSuite = suites.get(nextSuite);
			List<Street> temp = currSuite.sortedByPosition();
			setTextButtonsToDefault();
			fillNames(temp);
			fillGrades(temp);
			completeAddSub(temp);
		}
	}
	
	
	
}
