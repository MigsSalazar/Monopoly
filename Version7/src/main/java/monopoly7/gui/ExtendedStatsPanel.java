package monopoly7.gui;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import monopoly7.event.PlayerChangeEvent;
import monopoly7.event.PlayerChangeListener;
import monopoly7.models.Player;
import monopoly7.models.Property;

public class ExtendedStatsPanel extends StatsPanel implements PlayerChangeListener {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6160162439567376233L;
	
	private JLabel bails;
	private PropPanel propPanel;
	
	public ExtendedStatsPanel( Player p, ImageIcon i ){
		super(p,i);
		
		bails = new JLabel( bailString( p.getBails() ) );
		propPanel = new PropPanel( p.getProps() );
		
		this.add(bails);
		this.add(propPanel);
		p.addPlayerChangeListener(this);
	}
	
	
	public StatsPanel createdStatsPanel(){
		return new StatsPanel( following, icon );
	}
	
	private String bailString( int in ){
		return "Bail cards: " + in;
	}
	
	@Override
	public void playerStateChanged(PlayerChangeEvent pce) {
		
		switch(pce.getStatus()){
			case BAILOUT:
				bails.setText(bailString((int)pce.getNewValue()));
				break;
			case PROPERTIES:
				if( pce.getMessage().contains("obtained") ){
					propPanel.addProperty((Property)  pce.getNewValue());
				}else if( pce.getMessage().contains("lost") ){
					propPanel.removeProperty((Property)  pce.getOldValue());
				}else{
					propPanel.changeProperty( (Property)pce.getNewValue() );
				}
				break;
			default: super.playerStateChanged( pce );
		}
		
	}

	class PropPanel extends JPanel{
		/**
		 * 
		 */
		private static final long serialVersionUID = -1399343975262886198L;
		private JLabel header = new JLabel("Properties");
		private JScrollPane scroll;
		private JPanel viewPort = new JPanel( new GridLayout(0, 1) );
		private Map<String, JLabel> propLabels = new HashMap<String, JLabel>();
		private List<Property> orderedProps = new ArrayList<Property>();;
		
		PropPanel(){
			super( new BorderLayout() );
			subConstructor();
		}
		
		PropPanel( Map<String, Property> props ){
			super( new BorderLayout() );
			subConstructor();
			
			orderedProps.addAll(props.values());
			orderedProps.sort(Property.POSITION_ORDER);
			
			for( Property p : orderedProps ){
				JLabel label = createPropLabel(p);
				propLabels.put(p.getName(), label);
				viewPort.add(label);
			}
			
		}
		
		void changeProperty( Property p ){
			JLabel label = propLabels.get(p.getName());
			label.setText(propString(p));
			label.setBackground(p.getDisplayColor());
		}

		void addProperty( Property p ){
			orderedProps.add(p);
			orderedProps.sort(Property.POSITION_ORDER);
			JLabel label = createPropLabel( p );
			propLabels.put(p.getName(), label);
			viewPort.add(label, orderedProps.indexOf(p)+1);
		}
		
		void removeProperty( Property p ){
			viewPort.remove(propLabels.get(p.getName()));
			orderedProps.remove(p);
		}
		
		private void subConstructor() {
			this.add(header, BorderLayout.NORTH);
			scroll = new JScrollPane( viewPort,
										JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
										JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED );
			this.add(scroll, BorderLayout.CENTER);
		}
		
		private JLabel createPropLabel( Property p ) {
			JLabel label = new JLabel( propString(p) );
			label.setBackground(p.getDisplayColor());
			return label;
		}

		private String propString(Property p) {
			return p.getName() + (p.isMortgaged() ? " - mortgaged" : "");
		}
		
	}
	
}
