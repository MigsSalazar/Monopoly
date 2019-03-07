package edu.illinois.masalzr2.gui;

import java.awt.Desktop;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.filechooser.FileNameExtensionFilter;

import edu.illinois.masalzr2.Starter;
import edu.illinois.masalzr2.controllers.Environment;
import edu.illinois.masalzr2.io.GameIo;
import lombok.extern.log4j.*;

@Log4j2
public class FrameMenu extends JMenuBar implements ActionListener{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private static String sep = File.separator;
	private Environment gameVars;
	
	private JMenu[] menus;
	private JMenuItem[] options;
	
	
	public FrameMenu(Environment gv) {
		gameVars = gv;
		buildMenuBar();
	}
	
	public void buildMenuBar() {
		menus = new JMenu[2];
		menus[0] = new JMenu("File");
		menus[1] = new JMenu("About");
		
		options = new JMenuItem[7];
		
		options[0] = new JMenuItem("New");
		options[1] = new JMenuItem("Open");
		options[2] = new JMenuItem("Save");
		options[3] = new JMenuItem("Save As...");
		
		options[4] = new JMenuItem("Help");
		options[5] = new JMenuItem("About");
		options[6] = new JMenuItem("GitHub");
		
		for(JMenuItem i : options) {
			i.addActionListener(this);
		}
		
		menus[0].add(options[0]);
		menus[0].add(options[1]);
		menus[0].add(options[2]);
		menus[0].add(options[3]);
		
		menus[1].add(options[4]);
		menus[1].add(options[5]);
		menus[1].add(options[6]);
		
		this.add(menus[0]);
		this.add(menus[1]);
		
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		
		if( e.getSource() instanceof JMenuItem ) {
			JMenuItem source = (JMenuItem) e.getSource();
			
			if( source.equals(options[0]) ) {
				Settings sets = new Settings(gameVars.getFrame(), gameVars.isLimitingTurns(), gameVars.getTurnsLimit());
				sets.start();
				Environment newerGame = GameIo.newGame(sets.getFileDir());
				if(newerGame !=null) {
					newerGame.setTurnsLimit(sets.getTurnLimit());
					newerGame.setLimitingTurns(sets.isTurnsLimited());
					newerGame.setCurrency(sets.getCurrency());
					newerGame.setFancyMoveEnabled(sets.isFancyMoveEnabled());
					if (Starter.gameSetup( gameVars.getFrame(), newerGame))
						gameVars.getFrame().dispose();
					else
						JOptionPane.showMessageDialog(this, "The save file you entered is invalid.\nIt is either out of date, corrupted,\nor is not a save file at all.", "Bad file", JOptionPane.ERROR_MESSAGE);
				}else {
					log.info("FrameMenu: ActionPerformed: Bad file found.");
					JOptionPane.showMessageDialog(this, "The save file you entered is invalid.\nIt is either out of date, corrupted,\nor is not a save file at all.", "Bad file", JOptionPane.ERROR_MESSAGE);
				}
			} else if( source.equals(options[1]) ){
				
				String dir = GameIo.findFile(gameVars.getFrame(), new FileNameExtensionFilter("Monopoly Saves", "mns"), System.getProperty("user.dir")+sep+"saves");
				if(dir==null) {
					return;
				}
				Environment loadedGame = GameIo.produceSavedGame(dir);
				if(loadedGame !=null) {
					loadedGame.buildFrame();
					gameVars.getFrame().dispose();
				}
			} else if( source.equals(options[2]) ) {
				//System.out.println(gameVars.getSaveFile().getPath());
				
				if( gameVars.getSaveFile().getPath().contains( "resources"+sep+"package" ) ) {
					String newName = JOptionPane.showInputDialog(gameVars.getFrame(), "Name your game!");
					gameVars.setSaveFile(new File(System.getProperty("user.dir")+sep+"saves"+sep+newName+".mns" ));
				}
				GameIo.writeOut(gameVars);
				
			} else if( source.equals(options[3]) ) {
				String newName= JOptionPane.showInputDialog(gameVars.getFrame(), "Name your Game!");
				gameVars.setSaveFile(new File(System.getProperty("user.dir")+sep+"saves"+sep+newName+".mns" ));
				GameIo.writeOut(gameVars);
			} else if( source.equals(options[4]) ) {
				Starter.instructionBook(gameVars.getFrame());
			} else if( source.equals(options[5]) ) {
				Starter.about(gameVars.getFrame());
			} else if( source.equals(options[6]) ) {
				JFrame parent = gameVars.getFrame();
				try {
					if(Desktop.isDesktopSupported()) {
						Desktop.getDesktop().browse(new URI("https://github.com/MigsSalazar/Monopoly"));
					}
				} catch (URISyntaxException | IOException e1) {
					JOptionPane.showMessageDialog(parent, "Could no open github file. To access logs, go to:\nhttps://github.com/MigsSalazar/Monopoly");
				}
			}
			
		}
		
	}
	
}
