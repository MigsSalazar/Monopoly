package monopoly7;

import java.awt.Container;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.io.File;

import javax.swing.JFrame;

import monopoly7.gui.Board;

public class Start {
	
	static JFrame temp;
	static Board b;

	public static void main(String[] args) {		
		
		File f = new File("C:\\Users\\Unknown\\Desktop\\monopoly board.jpg");
		
		//Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		//Board b = new Board(f, screenSize.width, screenSize.height);
		b = new Board(f, 500, 500);
		temp = new JFrame();
		Container c = temp.getContentPane();
		c.add(b);
		
		temp.getContentPane().addComponentListener(new ComponentAdapter() {
		    public void componentResized(ComponentEvent componentEvent) {
		    	b.setScales(temp.getContentPane().getWidth(), temp.getContentPane().getHeight());
				temp.repaint();
				//temp.getContentPane().repaint();
		    }
		});
		
		
		temp.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		temp.pack();
		temp.setVisible(true);
	}

}
