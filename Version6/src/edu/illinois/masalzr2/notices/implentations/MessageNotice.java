package edu.illinois.masalzr2.notices.implentations;

import java.awt.event.ActionEvent;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JComponent;

import edu.illinois.masalzr2.models.Player;
import edu.illinois.masalzr2.notices.AbstractNotice;
import edu.illinois.masalzr2.notices.ListEvent;
import edu.illinois.masalzr2.notices.ListListener;
import edu.illinois.masalzr2.models.Property;

public class MessageNotice extends AbstractNotice{
	
	public MessageNotice(String t, ListListener ppl){
		super(t, ppl);
	} 
	
	@Override
	protected void defineActions() {
		actions = new JComponent[1];
		actions[0] = new JButton("OK");
		((JButton)actions[0]).addActionListener(this);
		
	}
	
	@Override
	public void actionPerformed(ActionEvent e){
		listener.popMe(new ListEvent(this));
	}
	
}
