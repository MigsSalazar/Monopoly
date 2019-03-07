package edu.illinois.masalzr2.gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Desktop;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.Map;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSpinner;
import javax.swing.ListSelectionModel;
import javax.swing.SpinnerNumberModel;
import javax.swing.border.TitledBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.core.config.Configurator;

import edu.illinois.masalzr2.Starter;
import edu.illinois.masalzr2.controllers.Environment;
import edu.illinois.masalzr2.io.GameIo;
import edu.illinois.masalzr2.templates.TemplateEnvironment;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.log4j.Log4j2;

/**
 * A (hopefully) intuitive GUI meant for players to define their game rules
 * and settings. Also provides a series of links for further help. Developers
 * should look to this GUI for a GitHub link, instruction guide on how to make
 * textures, and where to then import those textures
 * 
 * @author Miguel Salazar
 *
 */
@Log4j2
public class Settings implements ActionListener, ListSelectionListener {
	
	private String sep = File.separator;

	private JDialog dialog;
	
	private JPanel top;
	
	private JPanel gameSets;
	private JComboBox<String> currency;
	private JCheckBox turnLimitOn;
	private JSpinner turnLimit;
	private SpinnerNumberModel model;
	private JCheckBox fancyMove;
	
	private JPanel documentation;
	private JButton instBook;
	private JButton logs;
	private JButton gitHub;
	
	private JPanel textureSets;
	private JButton textImport;
	private JButton genJson;
	private JButton clean;
	
	private JPanel texturePanel;
	private JScrollPane textureSelect;
	private JList<String> textures;
	private DefaultListModel<String> textModel;
	
	private JPanel botButtons;
	private JCheckBox debug;
	private JButton ok;
	
	private JFrame parent;
	
	private Map<String, File> paths;
	@Getter @Setter private String fileDir = System.getProperty("user.dir") + sep +"resources"+sep+"packages"+sep+"default.mns";
	private boolean limitedTurns;
	private int turnsLimited;
	
	/**
	 * Defines some primitive settings and sets the reference JFrame
	 * @param p JFrame parent. The reference point for the JDialog
	 * @param lt short for limitedTurns, boolean defining if the game has a turn cap for a shorter game or not
	 * @param tl short for turnsLimited, int value defining the number of turns the game is limited to. This
	 * number is ignored when limitedTurns is false
	 */
	public Settings(JFrame p, boolean lt, int tl) {
		parent = p;
		limitedTurns = lt;
		turnsLimited = tl;
		defineDialog();
	}
	
	/**
	 * Packs, formats, and shows the JDialog
	 */
	public void start() {
		dialog.pack();
		dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		dialog.setVisible(true);
	}
	
	/**
	 * Defines components and the layout before adding them to the JDialog
	 */
	private void defineDialog() {
		top = new JPanel();
		top.setLayout(new BoxLayout(top, BoxLayout.PAGE_AXIS));
		
		defineGameSets();
		defineDocumentation();
		defineTextureSets();
		
		top.add(gameSets);
		top.add(documentation);
		top.add(textureSets);
		
		defineTextureSelect();
		
		defineBotButtons();
		
		dialog = new JDialog(parent,"Settings",true);
		dialog.setLayout(new BorderLayout());
		
		dialog.add(top, BorderLayout.NORTH);
		dialog.add(texturePanel, BorderLayout.CENTER);
		dialog.add(botButtons, BorderLayout.SOUTH);
		
	}
	
	private void defineBotButtons(){
		botButtons = new JPanel(new BorderLayout());
		
		ok = new JButton("OK");
		ok.addActionListener(this);
		
		debug = new JCheckBox("Debug Info?");
		debug.addActionListener(this);
		
		botButtons.add(debug, BorderLayout.WEST);
		botButtons.add(ok, BorderLayout.EAST);
	}
	
	/**
	 * Defines the bottom selection box containing all the texture names 
	 */
	private void defineTextureSelect(){
		
		TitledBorder title = BorderFactory.createTitledBorder("Texture Selection");
		
		texturePanel = new JPanel();
		texturePanel.setBorder(title);
		texturePanel.setPreferredSize(new Dimension(300,200));
		texturePanel.setLayout(new BorderLayout());
		
		textModel = new DefaultListModel<String>();
		paths = new HashMap<String, File>();
		
		updateTextures();
		
		textures = new JList<String>(textModel);
		textures.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		textures.setLayoutOrientation(JList.HORIZONTAL_WRAP);
		textures.addListSelectionListener(this);
		
		textureSelect = new JScrollPane(textures);
		//textureSelect.add(textures);
		
		texturePanel.add(textureSelect, BorderLayout.CENTER);
	}

	/**
	 * Searches for packaged mns files in the textures selector. If none are showing,
	 * click "Clean" to include the default texture or import a texture.
	 */
	private void updateTextures() {
		textModel.removeAllElements();
		paths.clear();
		
		File f = new File(System.getProperty("user.dir")+sep+"resources"+sep+"packages"+sep);
		if( !f.exists() ) {
			f.mkdirs();
		}
		//System.out.println(f.getPath());
		File[] files = f.listFiles();
		for(File s : files) {
			if(s.getName().endsWith(".mns")) {
				textModel.addElement(s.getName());
				//System.out.println("textModel has s:"+textModel.contains(s) );
				paths.put(s.getName(), s);
			}
		}
	}
	
	/**
	 * Top panel for game impacting settings besides textures. Included settings are
	 * the currency symbol, the turn limit, and if the turn limit is used
	 */
	private void defineGameSets() {
		TitledBorder gameTitle = BorderFactory.createTitledBorder("Game Settings");
		gameSets = new JPanel();
		gameSets.setLayout(new BoxLayout(gameSets, BoxLayout.LINE_AXIS));
		gameSets.setBorder(gameTitle);
		
		String[] options = {"$","€","£","₱","¥","₿","₨","¤"};
		currency = new JComboBox<String>(options);
		
		turnLimitOn = new JCheckBox("Turn Limit");
		turnLimitOn.setSelected(limitedTurns);
		turnLimitOn.addActionListener(this);
		
		model = new SpinnerNumberModel();
		//model.setMinimum(10);
		model.setValue(turnsLimited);
		turnLimit = new JSpinner(model);
		turnLimit.setEnabled(false);
		
		fancyMove = new JCheckBox("Fancy Move");
		fancyMove.setSelected(true);
		
		gameSets.add(currency);
		gameSets.add(turnLimitOn);
		gameSets.add(turnLimit);
		gameSets.add(fancyMove);
	}
	
	/**
	 * Panel made with developers in mind. Links to the GitHub repo and documentation
	 * as well as opening the logs folders.
	 */
	private void defineDocumentation() {
		TitledBorder docTitle = BorderFactory.createTitledBorder("Documentation");
		
		documentation = new JPanel();
		documentation.setBorder(docTitle);
		documentation.setLayout(new BoxLayout(documentation, BoxLayout.LINE_AXIS));
		
		instBook = new JButton("Guide Book");
		instBook.addActionListener(this);
		
		logs = new JButton("Open logs");
		logs.addActionListener(this);
		
		gitHub = new JButton("GitHub");
		gitHub.addActionListener(this);
		
		documentation.add(instBook);
		documentation.add(logs);
		documentation.add(gitHub);
	}
	
	/**
	 * The texture focused panel used to regenerate the default mns file, generate
	 * json for texture development, and import textures with jsons
	 */
	private void defineTextureSets() {
		TitledBorder textTitle = BorderFactory.createTitledBorder("Import Textures");
		
		textureSets = new JPanel();
		textureSets.setBorder(textTitle);
		textureSets.setLayout(new BoxLayout(textureSets, BoxLayout.LINE_AXIS));
		
		textImport = new JButton("Import");
		textImport.addActionListener(this);
		
		genJson = new JButton("Generate Json");
		genJson.addActionListener(this);
		
		clean = new JButton("Clean");
		clean.addActionListener(this);
		
		textureSets.add(textImport);
		textureSets.add(genJson);
		textureSets.add(clean);	
	}
	
	/**
	 * Returns the selected status directly from the checkbox
	 * @return boolean - true if the game has a turn limit, false if the game can go on indefinitely
	 */
	public boolean isTurnsLimited() {
		return turnLimitOn.isSelected();
	}
	
	/**
	 * Returns the turn cap defined by the Spinner model.
	 * This value will be ignored/unused by the game if the game is not capping turns
	 * @return int - number of turns given to each player
	 */
	public int getTurnLimit() {
		return (Integer)model.getValue();
	}
	
	/**
	 * The currency symbol to be used throughout the game.
	 * Gotten directly from the JComboBox
	 * @return String - the currency symbol
	 */
	public String getCurrency() {
		return (String)currency.getSelectedItem();
	}
	
	/**
	 * Gotten directly from the checkbox, enables or disables the fancy move.
	 * @return true if pieces move one game tile at a time, false if they just teleport to their location
	 */
	public boolean isFancyMoveEnabled() {
		return fancyMove.isSelected();
	}
	
	//Kinda getting border of writing documentation. Find the easter eggs I put in here
	
	/**
	 * This does something different for each component defined within the Dialog. The buttons
	 * are hopefully intuitive and obvious in what they do. The only two of note is the import
	 * which opens a JFileChooser in search of a json to import and the "Generate Json" button
	 * which... well... generates a json file and saves it to default texture folder under
	 * (home.dir)/textures/default as "defaultgmae.json"
	 */
	@Override
	public void actionPerformed(ActionEvent e) {
		Object source = e.getSource();
		//System.out.println(source.toString());
		if( source.equals(turnLimitOn) ) {
			turnLimit.setEnabled(turnLimitOn.isSelected());
		}else if(source.equals(debug)){
			//System.out.println(LoggerConfig.of(log).getName() + " is at level " + LoggerConfig.of(log).getLevel());
			if(debug.isSelected()){
				Configurator.setRootLevel(Level.ALL);
			}else{
				Configurator.setRootLevel(Level.INFO);
			}
			log.info("I am at the info level");
			log.debug("I am at the debug level");
			//log.trace("I am at the trace level");
			log.warn("I am set to warning");
			log.fatal("I am set to fatal");
		}else if(source.equals(textImport)) {
			Environment gv = GameIo.varsFromJson(dialog);
			 if(gv == null) {
				 textImport.setForeground(Color.RED);
				 textImport.setOpaque(true);
			 }else {
				 textImport.setForeground(Color.GREEN);
				 textImport.setOpaque(true);
				 fileDir = gv.getSaveFile().getPath();
				 if( !(new File(fileDir)).exists() ) {
					 fileDir = System.getProperty("user.dir")+sep+fileDir;
				 }
				 updateTextures();
			 }
		}else if(source.equals(clean)) {
			//clean.setForeground(Color.BLACK);
			TemplateEnvironment.produceTemplate();
			clean.setForeground(Color.GREEN);
			clean.setOpaque(true);
			TemplateEnvironment.closeProgressPanel();
		}else if(source.equals(genJson)) {
			if( GameIo.printCleanJson() ) {
				genJson.setForeground(Color.GREEN);
				genJson.setOpaque(true);
			}else {
				genJson.setForeground(Color.RED);
				genJson.setOpaque(true);
			}
		}else if(source.equals(logs)) {
			try {
				if(Desktop.isDesktopSupported()) {
					Desktop.getDesktop().open( new File(System.getProperty("user.dir") + sep + "logs" ) );
				}
			}catch(IOException ioe) {
				JOptionPane.showMessageDialog(dialog, "Could no open Logs file. To access logs, go to:\n"+System.getProperty("user.dir")+sep+"logs");
			}
		}else if(source.equals(gitHub)) {
			try {
				if(Desktop.isDesktopSupported()) {
					Desktop.getDesktop().browse(new URI("https://github.com/MigsSalazar/MonopolyV6"));
				}
			} catch (URISyntaxException | IOException e1) {
				JOptionPane.showMessageDialog(dialog, "Could no open github file. To access the repo, go to:\nhttps://github.com/MigsSalazar/MonopolyV6");
			}
		}else if(source.equals(instBook)) {
			Starter.instructionBook((JFrame)dialog.getParent());
		}else if(source.equals(ok)) {
			dialog.setVisible(false);
		}
	}

	/**
	 * Listens for changes to the selection in the bottom of the GUI. Immediately updates
	 * the texture but does not load the resources to memory
	 */
	@Override
	public void valueChanged(ListSelectionEvent e) {
		Object source= e.getSource();
		if(source.equals(textures)) {
			fileDir = paths.get(textures.getSelectedValue()).getPath();
		}
	}

}
