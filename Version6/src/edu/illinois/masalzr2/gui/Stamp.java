/**
 * 
 */
package edu.illinois.masalzr2.gui;

import java.awt.Color;
import java.io.Serializable;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.border.Border;

import com.google.gson.annotations.Expose;

import lombok.Data;

/**
 * A model class used to define simple graphics options to be applied to
 * JLabel or JButtons, both found in the javax.swing package.<br>
 * <br>
 * There are two characteristics that are manipulated by stamps:<br>
 *  - The JLabel or JButton's border<br>
 *  - The JLabel's or JButton's text<br>
 *  <br>
 *  The border is defined by an integer value that is a multiple of some
 *  combination of the first four prime numbers 2,3,5,7 (1 is not considered
 *  a prime). Each prime factor marks a different side of the border to be
 *  included as noted below when multiplied to the border value:
 *  <pre>
 *  
 *              2 - NORTH
 *              _ 
 *  WEST - 7   |_|   3 - EAST
 *  
 *              5 - SOUTH
 * </pre>
 * For example, to create a JLabel or JButton with only a NORTH and SOUTH border,
 * apply a Stamp with a border int value = 2x5 = 10. To include an EAST border,
 * multiply the value by 3 to get 2 x 5 x 3 = 10 x 3 = 30.<br>
 * <br>
 * A value of 1 defines that there is no border.<br>
 * <br>
 * The text characteristics are handled with two values, are char called the
 * engraving and the int called the style. A char is used because, typically
 * in a game of Monopoly, the board tiles are only large enough to hold one
 * character at a time. The style value defines whether or not the engraving
 * is italicized, bolded, or underlined in much the same was as the border
 * value where the primes used are the first three (2, 3, and 5) and define
 * the style in the way below:<br>
 * <br>
 * 	2 - Underline<br>
 *  3 - Italics<br>
 *  5 - Bold<br>
 * <br>
 * A value of 1 defines an engraving of no styles. Generally recommended to
 * use a bold (5) style as is very small on the board and thus can be difficult
 * to see otherwise.<br>
 * <br>
 * FOOT NOTE FOR THE MATHEMATICALLY INTRIGUED<br>
 * A non-sequitur here, this design choice was inspired by the Fundamental
 * Theorem of Arithmetic which states that "every integer greater than 1 either 
 * is prime itself or is the product of a unique combination of prime numbers"
 * which guarantees that each border and style combination is defined if only
 * the above primes are used and are used only once
 * 
 * @author Miguel Salazar
 *
 */
@Data
public class Stamp implements Serializable{
	
	private static final long serialVersionUID = 1L;
	@Expose private char engraving;
	@Expose private int style;
	@Expose private int border;
	
	/**
	 * Default Stamp with no border, engraving, or style
	 */
	public Stamp(){
		engraving = ' ';
		style = 1;
		border = 1;
	}
	
	/**
	 * A Stamp with only a border
	 * Border is validated before being set
	 * @param b int value that is a multiple of 1,2,3, or 5. Defines the border to be applied
	 */
	public Stamp(int b){
		border =1;
		if(validateBorder(b)){
			border = b;
		}
	}
	
	/**
	 * A Stamp with only an engraving
	 * @param e Any char value. Preferably UTF-8 compatible
	 */
	public Stamp(char e){
		engraving = e;
		style = 1;
		border = 1;
		
	}
	
	/**
	 * A fully fleshed out Stamp that applied a border, engraving, and style
	 * @param e Any char value. Preferably UTF-8 compatible
	 * @param s int value that is a multiple of 1,2,3, or 5. Defines the engraving's Italics, Bolded, and Underlined status
	 * @param bint short for Border int, this is the value that defines the border
	 */
	public Stamp(char e, int s, int bint){
		engraving = e;
		border = bint;
		style = s;
		if(!validateStyle(style)) {
			style = 1;
		}
		if(!validateBorder(border)){
			border = 1;
		}
	}
	
	/**
	 * Validates that the style value is some multiple of 1,2,3, or 5
	 * @param s int value style candidate. This STATIC method does not apply the style to any object.
	 * @return boolean - true if valid, false if not
	 */
	public static boolean validateStyle(int s) {
		if(s == 1) {
			return true;
		}else if(s%2 == 0 || s%3 == 0 || s%5 == 0) {
			return true;
		}
		return false;
	}
	
	/**
	 * Validates that the passed border value is some multiple of 1,2,3,5, or 7
	 * @param b int value border candidate. This STATIC method does not apply the style to any object.
	 * @return boolean - true if valid, false if not
	 */
	public static boolean validateBorder(int b){
		if(b == 1){
			return true;
		}else if( (b%2==0 || b%3==0 || b%5==0 || b%7==0) && b<=210 ){
			return true;
		}
			return false;
	}
	
	/**
	 * Engraves the passed JLabel with ONLY the engraving char value and the appropriate stylizations.
	 * Does not apply the border
	 * @param label JLabel - the JLabel to apply the stamp to. 
	 */
	public void engraveLabel(JLabel label){
		String engraved = engraveComponent();
		label.setText(""+engraved);
		if( engraving != ' '){
			label.setText(engraved);
		}
		label.setHorizontalTextPosition(JLabel.CENTER);
		
	}
	
	/**
	 * Engraves the passed JButton with ONLY the engraving char value and the appropriate stylizations.
	 * Does not apply the border
	 * @param button JButton - the JButton to apply the stamp to. 
	 */
	public void engraveButton(JButton button){
		String engraved = engraveComponent();
		button.setText(""+engraved);
		if( engraving != ' '){
			button.setText(engraved);
		}
		button.setHorizontalTextPosition(JButton.CENTER);
	}

	/**
	 * Generates an HTML String with the engraving and stylization tags.
	 * Designed to be used with javax.swing components that utilize HTML
	 * tags for stylization
	 * @return String - HTML string containing the engraving char and stylization tags
	 */
	private String engraveComponent() {
		String engraved = ""+engraving;
		
		italicize(engraved);
		bolden(engraved);
		underlined(engraved);
		engraved = "<html>"+engraved+"</html>";
		
		return engraved;
	}
	
	/**
	 * Returns the passed in String surrounded by HTML underline tags
	 * @param e String to underline
	 * @return String - the passed String.... but underlined....
	 */
	private String underlined(String e){
		if(style%2 == 0){
			e = "<u>"+e+"</u>";
		}
		return e;
	}
	
	/**
	 * Returns the passed in String surrounded by HTML italics tags
	 * @param e String to italicize
	 * @return String - the passed String.... but bolded....
	 */
	private String italicize(String e){
		if(style%3 == 0){
			e = "<i>"+e+"</i>";
		}
		return e;
	}
	
	/**
	 * Returns the passed in String surrounded by HTML bold tags
	 * @param e String to bold
	 * @return String - the passed String.... but bolded....
	 */
	private String bolden(String e){
		if(style%5 == 0){
			e = "<b>"+e+"</b>";
		}
		return e;
	}
	
	/**
	 * Requests a border and applies it to the passed JLabel
	 * @param label JLabel - the JLabel to apply the Stamp object's border
	 */
	public void giveBorder(JLabel label){
		Border given = makeBorder();
		label.setBorder(given);
	}
	
	/**
	 * Requests a border and applies it to the passed JButton
	 * @param button JButton - the JButton to apply the Stamp object's border
	 */
	public void giveBorder(JButton button){
		Border given = makeBorder();
		button.setBorder(given);
	}
	
	/**
	 * A non static method that calls the static {@link #makeBorder(int)}
	 * by passing in the Stamp object's border value
	 * @return Border - the generated MatteBorder based on the border value
	 */
	public Border makeBorder(){
		return makeBorder(border);
	}
	
	/**
	 * Validates, develops, and then returns the border
	 * An empty border is returned when the border value is invalid
	 * @param b int value that defines the border
	 * @return Border - the border object to apply
	 */
	public static Border makeBorder(int b){
		if(validateBorder(b)){
			return BorderFactory.createMatteBorder(hasTop(b), hasLeft(b), hasBottom(b), hasRight(b), Color.BLACK);
		}else{
			return BorderFactory.createMatteBorder(0, 0, 0, 0, Color.BLACK);
		}
	}
	
	/**
	 * Checks value if the border should have a top/NORTH
	 * @param b border value
	 * @return 1 if the border is to be applied. 0 if not
	 */
	private static int hasTop(int b){
		if(b%2 == 0){
			return 1;
		}
		return 0;
	}
	
	/**
	 * Checks value if the border should have a right/EAST
	 * @param b border value
	 * @return 1 if the border is to be applied. 0 if not
	 */
	private static int hasRight(int b){
		if(b%3 == 0){
			return 1;
		}
		return 0;
	}
	
	/**
	 * Checks value if the border should have a bottom.SOUTH
	 * @param b border value
	 * @return 1 if the border is to be applied. 0 if not
	 */
	private static int hasBottom(int b){
		if(b%5 == 0){
			return 1;
		}
		return 0;
	}
	
	/**
	 * Checks value if the border should have a left/WEST
	 * @param b border value
	 * @return 1 if the border is to be applied. 0 if not
	 */
	private static int hasLeft(int b){
		if(b%7 == 0){
			return 1;
		}
		return 0;
	}

	/**
	 * Validates and then updates the border value ONLY IF valid.
	 * The border is otherwise untouched if not valid
	 * @param b the desired value to set border
	 */
	public void setBorder(int b) {
		if(validateBorder(b)){
			border = b;
		}
	}
	
}
