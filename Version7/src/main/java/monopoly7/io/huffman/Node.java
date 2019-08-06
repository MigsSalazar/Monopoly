package monopoly7.io.huffman;

import java.io.IOException;

import lombok.extern.flogger.Flogger;

/**
 * The Huffman Tree Node structure.
 * Contains a public name, weight, left, and right
 * instance variables. The nodes have no functionality
 * to regulate the structure.
 * @author Unknown
 *
 */
@Flogger
public class Node{
	String name = "";
	int weight;
	
	Node left = null;
	Node right = null;
	
	public Node( String n, int w ){
		name = n;
		weight = w;
		left = null;
		right = null;
	}
	
	public Node( String n, int w, Node l, Node r){
		name = n;
		weight = w;
		left = l;
		right = r;
	}
	
    public void printTree() throws IOException {
    	//FileOutputStream os = new FileOutputStream();
    	StringBuilder out = new StringBuilder();
        if (right != null) {
            right.printTree(out, true, "");
        }
        printNodeValue(out);
        if (left != null) {
            left.printTree(out, false, "");
        }
        log.atInfo().log(out.toString());
    }
    private void printNodeValue(StringBuilder out) throws IOException {
        if (name == null) {
            out.append("<null>");
        } else {
            out.append( weight + ": \"" + name + "\"");
        }
        out.append('\n');
    }
    // use string and not stringbuffer on purpose as we need to change the indent at each recursion
    private void printTree(StringBuilder out, boolean isRight, String indent) throws IOException {
        if (right != null) {
            right.printTree(out, true, indent + (isRight ? "        " : " |      "));
        }
        out.append(indent);
        if (isRight) {
            out.append(" /");
        } else {
            out.append(" \\");
        }
        out.append("----- ");
        printNodeValue(out);
        if (left != null) {
            left.printTree(out, false, indent + (isRight ? " |      " : "        "));
        }
    }

}