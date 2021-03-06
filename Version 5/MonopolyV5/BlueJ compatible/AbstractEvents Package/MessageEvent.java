 

import java.awt.event.ActionEvent;

import javax.swing.*;


public class MessageEvent extends AbstractEvent {

	public MessageEvent(EventPanel p, String message) {
		super(p);
		text = message;
		defineComponents();
		//parent.paintEvent(this);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if(e.getSource().equals(buttons[0])){
			//System.out.println("message action performed started");
			parent.jumpStartClean();
			desync();
			
			//System.out.println("message action performed over");
		}
	}
	

	@Override
	public void defineComponents() {
		buttons = new JComponent[1];
		buttons[0] = new JButton("OK");
		((JButton)buttons[0]).addActionListener(this);
	}

}
