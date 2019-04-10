package monopoly7.gui;

import java.awt.Image;

public abstract class BufferedRender {
	
	protected Image lastRender = null;
	protected boolean dirty = true;
	
	public void unvalidate(){
		dirty = true;
	}

	public boolean isRenderNeeded(){
		return dirty || lastRender == null;
	}
	
	public abstract Image render();
	
}
