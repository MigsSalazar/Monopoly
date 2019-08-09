package monopoly7.gui;

import java.awt.Image;

import lombok.Getter;
import lombok.Setter;

public abstract class BufferedRender {
	
	@Getter @Setter
	private Image lastRender = null;
	
	@Setter
	private boolean dirty = true;
	
	public void invalidate(){
		dirty = true;
	}
	
	public boolean isDirty(){
		return dirty || lastRender == null;
	}
	
	public abstract Image render();
	
}
