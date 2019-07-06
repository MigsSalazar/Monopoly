package monopoly7.controllers;

import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.*;

import com.google.gson.*;

/**
 * THE GREAT HUFFMAN CLASS
 * <br/>
 * So I'm making this to make sure my idiot brain never forgets
 * The format of the huffman file is done in 2 data parts, each with
 * it's own header. The first part contains the huffman tree data structure
 * using the following rules:
 *  - The first 32 bits are used to define how many bytes are taken by the
 *  header pairs are contained within the topological portion of the file
 *  - The following part is the topological portion of the file where each
 *  node is represented as a pair of characters except for the root node which
 *  is a single sentinel null char. Each char is allocated 2 bytes, or 16 bits,
 *  and each character pairing follows has a header char of 'L' or 'R', meaning
 *  the following node is the left or right node, and a node name char that can be null.
 *  If the node name char is null, then the current node is not a leaf node and can
 *  be further built down. If the node name char is not null, then it is a leaf and
 *  no further nodes can descend from that node. NOTE: Java's ByteArray(Out/In)putStream
 *  will trim values down for you so if a char does not occupy all 2 bytes, then it will
 *  be alloted 1 byte in the file. This will break the parsing process as the space
 *  consumption of each character pair is strictly 4 bytes, 2 per character.
 *  - The third part is an additional header defining just how many bits are to be read
 *  as character paths in the tree. This is done to in case the file contains additional
 *  padding at the end since character paths are not guaranteed to be divisible by 8.
 *  The size header for this part is broken up into 2 values, the first, an integer value
 *  occupying 4 bytes (32 bits) of space, defines how many bytes the path data occupies.
 *  The second value, occupying 1 byte (8 bits), defines how many additional bits remain
 *  on the tail end of the file. Any bits beyond in the file are padding bits.
 *  - The fourth and final part is the pathing data. A 0 means take a left turn, a 1
 *  means take a right turn. If the current node does not have a left and/or right node,
 *  the the end of the path has been reached and the we should return to the root node.
 * @author Miguel Salazar
 *
 */
public class HuffmanUtil {
	
	static File TREEFILE = new File("printed tree.txt");
	static BufferedWriter PRINTTREE;
	final static long BIGLONG = -9223372036854775808L;
	final static int BIGINT = -2147483648;
	final static short BIGSHORT = -32768;
	final static byte BIGBYTE = -128;
	
	public static void main( String[] args ){
		/*
		try {
			PRINTTREE = new BufferedWriter(new FileWriter( TREEFILE ));
		} catch (IOException e2) {
			// TODO Auto-generated catch block
			e2.printStackTrace();
		}*/
		
		//STEP 1: WRITING THE HUFFMAN TREE
		//make test object
		Environment e = generateTestEnvironment();
		
		//create json string out of test object
		Gson gson = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().setPrettyPrinting().create();
		String out = gson.toJson(e).replaceAll("\n", "");
		
		//Calculate weights of each char
		Map<Character, Integer> charWeights = countChars(out);
		//parse into a huffman tree
		Node root = stringToHuffman( charWeights );
		
		//print out the huffman tree. use for debugging/console output
		try{
			root.printTree();
		}catch(IOException ioe){
			ioe.printStackTrace();
		}
		
		//get all the binary paths of the chars
		Map<Character, LinkedList<Integer>> paths = mapCharPaths( root );
		
		//figure out how many bytes this will cost
		int lengthInBytes = 0;
		byte extraBits = 0;
		
		int neededBits;
		
		long fullCount = 0;
		
		//Calculating the total byte size
		for( Character c : charWeights.keySet() ){
			neededBits = charWeights.get(c) * paths.get(c).size();
			fullCount += neededBits;
			lengthInBytes += (int)(neededBits/8);
			extraBits += (byte)(neededBits % 8);
			lengthInBytes += (int)(extraBits / 8);
			extraBits %= 8;
		}
		System.out.println("fullCount:"+fullCount);
		//Integer level = Collections.max(lengths.values()); 
		//parse headers
		byte[] treeTopography = parseTreeTopography(root);
		
		//save file dir
		String dir = System.getProperty("user.dir")+System.getProperty("file.separator")+"textOutTree.bin";
		try{
			//System.out.println("chosen dir:"+dir);
			FileOutputStream fos = new FileOutputStream( new File( dir ) );
			
			//Write out how large the header will be
			System.out.println("topography length:"+treeTopography.length);
			writeOutInt(treeTopography.length, fos);
			//fos.write('\n');
			//Write out the header
			fos.write(treeTopography);
			System.out.println("lengths in bytes:"+lengthInBytes);
			System.out.println("extra bits:"+extraBits);
			
			//write out the size of the paths
			writeOutInt(lengthInBytes, fos);
			fos.write(extraBits);
			
			//flush for safety
			fos.flush();
			
			//write out all the paths of tree without padding
			writeJsonOut(out, paths, fos);
			
			fos.close();
		}catch( IOException ioe ){
			ioe.printStackTrace();
		}
		
		//STEP 2: READING THE HUFFMAN TREE
		
		FileInputStream fis;
		try {
			fis = new FileInputStream( new File(dir) );
			//first: read in topography as number of bytes (stored as an int)
			//second: read in topography until topography is done
			//third: read in the size, as a number of bytes, of the json file which is stored as a long
			//fourth: read in the number of dangling bits (stored as a byte)
			//fifth: read in the path until reach the end of the size
			
			int topographyLength = readInInt(fis);
			System.out.println("topography length:"+topographyLength);
			byte[] header = new byte[topographyLength];
			fis.read(header);
			
			//headerReader(header);
			Node readRoot = headerParser(header);
			readRoot.printTree();
			
			int dataSize = readInInt( fis );
			System.out.println("dataSize:"+dataSize);
			
			byte dangleBits = (byte)fis.read();
			
			System.out.println("dangleBits:"+dangleBits);
			
			long fullDataSize = (dataSize * 8) + dangleBits;
			
			System.out.println("fullDataSize"+fullDataSize);

			byte[] pathData = new byte[dataSize + 1];
			
			fis.read(pathData);
			
			fis.close();
			
			byte byteAdj = BIGBYTE;
			int byteIdx = 0;
			Node cur = readRoot;
			StringBuilder sb = new StringBuilder();
			
			for( long i=0; i<=fullDataSize; i++ ){
				//System.out.print("i:"+i+",byteIdx:"+byteIdx+",byte:"+pathData[byteIdx]);
				if( byteAdj == 0 ){
					byteAdj = BIGBYTE;
					byteIdx++;
				}
				if( !cur.name.equals("") ){
					sb.append(cur.name);
					//System.out.println(",letter found:"+cur.name+".");
					cur = readRoot;
				}else{
					//System.out.println();
				}
				
				if( (pathData[byteIdx] & byteAdj) == 0 ){
					cur = cur.left;
				}else{
					cur = cur.right;
				}
				
				byteAdj = (byte)((byteAdj >> 1) & 127);
				
			}
			
			System.out.println("outString:"+sb.toString());
			//System.out.println("exit string");
			String gottenFromRead = sb.toString();
			Environment e2 = gson.fromJson(gottenFromRead, Environment.class);
			System.out.println("e==e2:"+(e.equals(e2)));
			System.out.println("out==gottenFromRead:"+(out.equals(gottenFromRead)));
		} catch (FileNotFoundException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		
	}
	
	private static int readInInt( FileInputStream fis ) throws IOException{
		int ret = 0;
		byte[] bytes = new byte[4];
		fis.read(bytes);
		
		for( int i=0; i<bytes.length; i++ ){
			ret |= ( (((int)bytes[i]) & 255) << (8*(bytes.length - i - 1)));
		}
		
		//ret = (bytes[0] << 24) | (bytes[1] << 16) | (bytes[2] << 8) | bytes[3];
		
		return ret;
	}

	private static Environment generateTestEnvironment() {
		Environment e = new Environment();
		for( int i=0; i<8; i++ ){
			e.getPlayers().put("player"+i, "player"+i);
		}
		
		for( int i=0; i<35; i++ ){
			e.getProperties().put("property"+i, "property"+i);
		}
		
		for( int i=0; i<8; i++ ){
			e.getSuites().put("suite"+i, "suite"+i);
		}
		
		for( int i=0; i<40; i++ ){
			e.getCards().put("card"+i, "card"+i);
		}
		
		e.getIdontknowMoreStuff().put("lol", "hi");
		return e;
	}

	private static void writeOutInt( int data, FileOutputStream fos) throws IOException {
		int padding = 0;
		int pushables = 0;
		
		if( data < 256 && data > 0 ){
			padding = 3;
			pushables = 1;
		}else if( data < 65536 && data > 0 ){
			padding = 2;
			pushables = 2;
		}else{
			pushables = 4;
		}
		
		for( int i=0; i<padding; i++ ){
			fos.write('\0');
		}
		
		for( int i=pushables; i>0; i--){
			int pushing = ((byte)(data >> (8*(i-1)))) & 255;
			System.out.println("i:"+i+",data:"+data+",pushing:"+pushing);
			fos.write( pushing );
		}
	}

	private static void writeJsonOut(String out, Map<Character, LinkedList<Integer>> paths, FileOutputStream fos)
			throws IOException {
		LinkedList<Integer> curPath;
		byte byteAdj = BIGBYTE;
		byte writtenByte = 0; 
		
		for( Character c : out.toCharArray() ){
			if( c == '\n' ){
				continue;
			}
			//System.out.println("current char="+c+".");
			curPath = paths.get(c);
			for( Integer i : curPath ){
				if( byteAdj == 0 ){
					//System.out.println("writtenByte:"+writtenByte);
					fos.write(writtenByte);
					byteAdj = BIGBYTE;
					writtenByte = 0;
				}
				if( i == 1 ){
					writtenByte |= byteAdj;
				}
				byteAdj = (byte)((byteAdj >> 1) & 127);
			}
		}
		fos.write(writtenByte);
	}

	public static void headerReader(byte[] bytes) {
		for( int i=3, j=1; i<bytes.length; i+=2, j++ ){
			//char c = (char)( bytes[i] );
			char c = 0;
			
			int upper = (((int)bytes[i-1] ) & 255 ) << 8;
			int lower = ((int)bytes[i]) & 255;
			
			c = (char)(upper | lower);
			
			if( c == 0 ){
				System.out.print("null,");
			}else{
				System.out.print(c+",");
			}
			
			if( j != 0 && j % 2 == 0 ){
				System.out.println();
			}
		}
	}
	
	public static Node headerParser( byte[] bytes ){
		
		Node root = new Node("", 0);

		Stack<Node> stack = new Stack<Node>();
		stack.push(root);
		
		int i = 5;
		
		while( !stack.isEmpty() && i < bytes.length ){
			
			while( stack.peek().right != null ){
				stack.pop();
			}
			
			Node cur = stack.peek();
			
			int hup = (((int)bytes[i-3] ) & 255 ) << 8;
			int hlow = ((int)bytes[i-2]) & 255;
			char header = (char)(hup | hlow);
			
			int cup = (((int)bytes[i-1] ) & 255 ) << 8;
			int clow = ((int)bytes[i]) & 255;
			char c = (char)(cup | clow);
			
			if( c == '\0' ){
				if( header == 'L' ){
					cur.left = new Node("", 0);
					stack.push(cur.left);
				}else{
					cur.right = new Node("", 0);
					stack.push(cur.right);
				}
			}else{
				if( header == 'L' ){
					cur.left = new Node(""+c, 0);
				}else{
					cur.right = new Node(""+c, 0);
					stack.pop();
				}
			}
			i += 4;
			
		}
		
		return root;
	}
	
	/**
	 * Converts a string into a huffman tree with included weights
	 * @param out String to be converted
	 * @return root node of the huffman tree
	 */
	public static Node stringToHuffman( Map<Character, Integer> mappings ){
		//System.out.println(out);
		
		//printMappings( mappings );
		
		List<Character> unsorted = new LinkedList<Character>( mappings.keySet() );
		List<Character> order = mergeSortMap( mappings, unsorted );
		
		LinkedList<Node> forest = new LinkedList<Node>();
		for( Character c : order ){
			forest.add(new Node( ""+c, mappings.get(c) ) );
		}
		
		buildTree( forest );
		
		return forest.get(0);
	}

	/**
	 * Counts the occurences of each character in the string
	 * Note: the \n char is explicitly ignored
	 * @param jsoned
	 * @return
	 */
	public static Map<Character, Integer> countChars( String jsoned ){
		char[] charArr = jsoned.toCharArray();
		Map<Character, Integer> ret = new HashMap<Character, Integer>();
		
		for( Character c : charArr ){
			if( c != '\n' ){
				if( !ret.containsKey(c) ){
					ret.put(c, jsoned.length() - jsoned.replace(""+c, "").length() );
				}
				//ret.put(c, ret.get(c)+1);
			}
			
		}
		
		return ret;
	}
	
	/**
	 * Merge sort method that sorts the list of characters based on their occurences in the string
	 * @param charMap
	 * @param sortMe
	 * @return
	 */
	public static List<Character> mergeSortMap( Map<Character, Integer> charMap, List<Character> sortMe ){
		if( sortMe.isEmpty() ){
			return null;
		}else if( sortMe.size() == 1 ){
			return sortMe;
		}
		List<Character> list1 = mergeSortMap( charMap, new LinkedList<Character>(sortMe.subList(0, sortMe.size()/2)) );
		List<Character> list2 = mergeSortMap( charMap, new LinkedList<Character>(sortMe.subList(sortMe.size()/2, sortMe.size())) );
		
		List<Character> ret = new LinkedList<Character>();
		
		while( !list1.isEmpty() && !list2.isEmpty() ){
			if( charMap.get(list1.get(0)) < charMap.get(list2.get(0)) ){
				ret.add( list1.get(0) );
				list1.remove(0);
			}else{
				ret.add( list2.get(0) );
				list2.remove(0);
			}
		}
		
		if( !list1.isEmpty() ){
			ret.addAll(list1);
		}
		if( !list2.isEmpty() ){
			ret.addAll(list2);
		}
		
		return ret;
	}
	
	/**
	 * Takes the ordered list and builds a huffman tree using the 
	 * passed nodes as leaf nodes and creates synthetic nodes for the
	 * parent/root nodes
	 * @param forest
	 */
	public static void buildTree( LinkedList<Node> forest ){
		while( forest.size() != 1 ){
			//System.out.println("size of forest: "+forest.size());
			Node first = forest.pollFirst();
			Node second = forest.pollFirst();
			String newName = first.name + second.name;
			int newWeight = first.weight + second.weight;
			Node newRoot = new Node( newName, newWeight, first, second );
			int curIdx = 0;
			if( !forest.isEmpty() ){
				while( curIdx < forest.size() && forest.get(curIdx).weight < newRoot.weight ){
					curIdx++;
				}
			}
			forest.add(curIdx, newRoot);
		}
	}
	
	public static Map<Character, LinkedList<Integer>> mapCharPaths( Node root ){
		Map<Character, LinkedList<Integer>> paths = new HashMap<Character, LinkedList<Integer>>();
		for( Character c : root.name.toCharArray() ){
			LinkedList<Integer> path = new LinkedList<Integer>();
			Node cur = root;
			
			while( !cur.name.equals(""+c) ){
				if( cur.left.name.indexOf(c) != -1 ){
					cur = cur.left;
					path.add(0);
				}else{
					cur = cur.right;
					path.add(1);
				}
			}
			paths.put(c, path );
		}
		return paths;
	}
	

	public static byte[] parseTreeTopography( Node cur ){
		ByteArrayOutputStream boas = new ByteArrayOutputStream();
		//int myPrintOut = (header << 16);
		//String binPrintOut = decimalConversion((long)header);
		//String bpo = decimalConversion((long)myPrintOut);
		if( cur.left == null && cur.right == null ){
			//myPrintOut |= (((int)cur.name.charAt(0)) & 255);
			if( cur.name.charAt(0) > 255 ){
				boas.write(cur.name.charAt(0));
			}else{
				boas.write('\0');
				boas.write(cur.name.charAt(0));
			}
		}else{
			try{
				boas.write('\0');
				boas.write('\0');
				boas.write('\0');
				boas.write('L');
				boas.write(parseTreeTopography(cur.left));
				boas.write('\0');
				boas.write('R');
				boas.write(parseTreeTopography(cur.right));
			}catch( IOException ioe ){
				ioe.printStackTrace();
			}
		}
		return boas.toByteArray();
	}
	
	
    public static String decimalConversion(long numToConvert){
    	String x = "";
    	long adj = BIGLONG;
    	for( int i=0; i<64; i++ ){
    		if( (adj & numToConvert) != 0 ){
    			x = x + "1";
    		}else{
    			x = x + "0";
    		}
    		adj = ((adj >> 1) & Long.MAX_VALUE);
    	}
    	
        return x;
    }
	
	/**
	 * The Huffman Tree Node structure.
	 * Contains a public name, weight, left, and right
	 * instance variables. The nodes have no functionality
	 * to regulate the structure.
	 * @author Unknown
	 *
	 */
	public static class Node{
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
	    	OutputStreamWriter out = new OutputStreamWriter(System.out);
	        if (right != null) {
	            right.printTree(out, true, "");
	        }
	        printNodeValue(out);
	        if (left != null) {
	            left.printTree(out, false, "");
	        }
	        out.flush();
	    }
	    private void printNodeValue(OutputStreamWriter out) throws IOException {
	        if (name == null) {
	            out.write("<null>");
	        } else {
	            out.write( weight + ": \"" + name + "\"");
	        }
	        out.write('\n');
	    }
	    // use string and not stringbuffer on purpose as we need to change the indent at each recursion
	    private void printTree(OutputStreamWriter out, boolean isRight, String indent) throws IOException {
	        if (right != null) {
	            right.printTree(out, true, indent + (isRight ? "        " : " |      "));
	        }
	        out.write(indent);
	        if (isRight) {
	            out.write(" /");
	        } else {
	            out.write(" \\");
	        }
	        out.write("----- ");
	        printNodeValue(out);
	        if (left != null) {
	            left.printTree(out, false, indent + (isRight ? " |      " : "        "));
	        }
	    }

	}
	


}
