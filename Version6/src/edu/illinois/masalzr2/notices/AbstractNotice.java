package edu.illinois.masalzr2.notices;

import java.awt.event.ActionListener;
import java.io.Serializable;

import javax.swing.JComponent;

import lombok.extern.log4j.Log4j2;

@Log4j2
public abstract class AbstractNotice implements ActionListener, Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	protected String text;
	protected JComponent[] actions;
	protected ListListener listener;
	
	public AbstractNotice(ListListener ppl){
		text = "This is the default notice";
		listener = ppl;
		//defineActions();
	}
	
	public AbstractNotice(String t, ListListener ppl){
		text = t;
		listener = ppl;
		//defineActions();
	}
	
	protected abstract void defineActions();
	
	public String getText(){
		return text;
	}
	
	public void setText(String t){
		text = t;
	}
	
	public JComponent[] getActions(){
		return actions;
	}
	
	public void setListListener(ListListener ppl){
		listener = ppl;
	}
	
	protected void noticePushPop(AbstractNotice an) {
		log.debug("{}: {}", this.getClass().getName(), text);
		log.debug("{}: noticePushPop: Pushing new {} onto listener's queue and poping self", this.getClass().getName(), an.getClass().getName());
		listener.pushMe(new ListEvent(an));
		listener.popMe(new ListEvent(this));
	}
	
}
