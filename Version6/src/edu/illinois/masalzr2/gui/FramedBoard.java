package edu.illinois.masalzr2.gui;

import java.awt.GraphicsConfiguration;
import java.awt.HeadlessException;

import javax.swing.JFrame;

/**
 * A testing wrapper class that opens a JFrame that holds a game board,
 * Never used within the game.
 * @author Miguel Salazar
 *
 */
public class FramedBoard extends JFrame {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Board board;

	public FramedBoard(Board b) throws HeadlessException {
		super();
		board = b;
		this.getContentPane().add(board.getBoard());
		this.pack();
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}

	public FramedBoard(GraphicsConfiguration gc, Board b) {
		super(gc);
		board = b;
		this.getContentPane().add(board.getBoard());
		this.pack();
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}

	public FramedBoard(String title, Board b) throws HeadlessException {
		super(title);
		board = b;
		this.getContentPane().add(board.getBoard());
		this.pack();
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}

	public FramedBoard(String title, GraphicsConfiguration gc, Board b) {
		super(title, gc);
		board = b;
		this.getContentPane().add(board.getBoard());
		this.pack();
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}
	
	public Board getBoard(){
		return board;
	}

}
