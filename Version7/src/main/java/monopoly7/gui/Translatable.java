package monopoly7.gui;

public interface Translatable {
	
	public Coordinates translate( int p );
	
	public class Coordinates{
		public int x;
		public int y;
		
		public Coordinates(){
			x = 0;
			y = 0;
		}
		
		public Coordinates( int x, int y ){
			this.x = x;
			this.y = y;
		}
		
	}

}
