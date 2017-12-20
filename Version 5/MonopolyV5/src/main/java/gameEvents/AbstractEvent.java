package main.java.gameEvents;

import java.awt.event.ActionListener;

import javax.swing.JComponent;

import main.java.gui.EventPanel;

public abstract class AbstractEvent implements ActionListener{
	protected String text;
	protected EventPanel parent;
	protected JComponent[] buttons;
	
	public AbstractEvent(EventPanel p){
		parent = p;
		text = "Default Event";
		System.out.println("Finished at the non text constructor");
	}
	
	public AbstractEvent(EventPanel p, String t){
		parent = p;
		text = t;
		System.out.println("Finished at the text filled constructor");
		//parent.paintEvent(this);
	}
	
	public JComponent[] getComponents(){
		return buttons;
	}
	
	public String getText(){
		return text;
	}
	
	public void setText(String t){
		text = t;
	}
	
	public abstract void defineComponents();

	protected void sync(AbstractEvent ae){
		
		Thread t = new Thread(){
			@Override
			public void run(){
				boolean flag = true;
				while(flag){
					try{
						synchronized(ae){
							ae.wait();
						}
					}catch(InterruptedException e){
						e.printStackTrace();
					}finally{
						flag = false;
					}
				}
			}
		};
		
		t.start();
		/*
		try {
			synchronized(ae){
				System.out.println("Current sync trace: "+ae.toString());
				boolean flag = true;
				
				ae.wait();
				System.out.println("Im still waiting");
			}
		} catch (InterruptedException e) {
			e.printStackTrace();
		}*/
	}
	
	protected void desync(){
		AbstractEvent me = this;
		synchronized(me){
			me.notify();
		}
	}
	
}
