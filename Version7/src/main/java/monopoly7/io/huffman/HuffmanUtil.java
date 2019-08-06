package monopoly7.io.huffman;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Stack;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import lombok.extern.flogger.Flogger;
import monopoly7.io.BinaryIOUtil;

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
@Flogger
public class HuffmanUtil {
	/**
	 * Determines if the project is running from a non-packaged maven project
	 * and updates the user.dir appropriately such that resources can be used 
	 */
	static{
		String FILESEP = System.getProperty("file.separator");
		String userDevDir = System.getProperty("user.dir")+FILESEP+"src"+FILESEP+"main"+FILESEP;
		File f = new File( userDevDir );
		if( f.isDirectory() ){
			//System.out.println("userDevDir exists");
			log.atWarning().log("Developer mode discovered. Resetting user.dir");
			System.setProperty("user.dir", userDevDir);
			System.setProperty("user.env", "development");
		}else{
			System.setProperty("user.env", "production");
		}
	}
	
	final static long BIGLONG = -9223372036854775808L;
	final static int BIGINT = -2147483648;
	final static short BIGSHORT = -32768;
	final static byte BIGBYTE = -128;
	
	/*
	 * ===========================================================================================
	 * ==========================HUFFMAN CREATION AND WRITE OUT METHODS===========================
	 * ===========================================================================================
	 * PROCESS
	 * 1. Count the characters (countChars)
	 * 2. Generate Huffman Tree (generateHuffman)
	 *    A. Sort the characters by frequency (mergeSortMap)
	 *    B. Build the tree
	 * 3. Find the path to each character in the tree (mapCharPaths)
	 * 4. Write the headers to the output stream (writeOutHeaders)
	 *    A. Make a topography header (treeTopographyToBinary)
	 * 5. Write out the string as it's binary paths (WriteOutBinary)
	 */
	
	public static boolean writeOutObjectToHuffman( Object o, String dir ){
		//create json string out of test object
		Gson gson = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().setPrettyPrinting().create();
		String out = gson.toJson(o).replaceAll("\n", "");
		
		//Calculate weights of each char
		Map<Character, Integer> charWeights = countChars(out);
		//parse into a huffman tree
		Node root = generateHuffman( charWeights );
		//get all the binary paths of the chars
		Map<Character, LinkedList<Integer>> paths = mapCharPaths( root );
		
		//print out the huffman tree. use for debugging/console output
		if( System.getProperty("user.env").equals("development") ){
			try{ root.printTree(); }
			catch(IOException ioe){ ioe.printStackTrace(); }
		}
		
		try {
			FileOutputStream fos = new FileOutputStream( new File( dir ) );
			writeOutHeaders(fos, charWeights, root, paths);
			
			//write out all the paths of tree without padding
			writeOutBinary(out, paths, fos);
			
			fos.close();
		} catch (FileNotFoundException e) {
			log.atSevere().withCause(e);
			return false;
		} catch (IOException e) {
			log.atSevere().withCause(e);
			return false;
		}
		
		return true;
	}
	
	/**
	 * Counts the occurances of each character in the string
	 * Note: the \n char is explicitly ignored
	 * @param jsoned
	 * @return
	 */
	private static Map<Character, Integer> countChars( String jsoned ){
		char[] charArr = jsoned.toCharArray();
		Map<Character, Integer> ret = new HashMap<Character, Integer>();
		
		for( Character c : charArr )
			if( c != '\n' )
				if( !ret.containsKey(c) )
					ret.put(c, jsoned.length() - jsoned.replace(""+c, "").length() );
		return ret;
	}

	/**
	 * Converts a string into a huffman tree with included weights
	 * @param out String to be converted
	 * @return root node of the huffman tree
	 */
	private static Node generateHuffman( Map<Character, Integer> mappings ){
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
	 * Merge sort method that sorts the list of characters based on their occurences in the string
	 * @param charMap
	 * @param sortMe
	 * @return
	 */
	private static List<Character> mergeSortMap( Map<Character, Integer> charMap, List<Character> sortMe ){
		if( sortMe.isEmpty() )
			return null;
		else if( sortMe.size() == 1 )
			return sortMe;
		
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
		
		if( !list1.isEmpty() )
			ret.addAll(list1);
		if( !list2.isEmpty() )
			ret.addAll(list2);
		
		return ret;
	}
	
	/**
	 * Takes the ordered list and builds a huffman tree using the 
	 * passed nodes as leaf nodes and creates synthetic nodes for the
	 * parent/root nodes
	 * @param forest
	 */
	private static void buildTree( LinkedList<Node> forest ){
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
	
	private static Map<Character, LinkedList<Integer>> mapCharPaths( Node root ){
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


	private static void writeOutHeaders(FileOutputStream fos, Map<Character, Integer> charWeights, Node root,
		Map<Character, LinkedList<Integer>> paths) throws IOException {
		//figure out how many bytes this will cost
		int lengthInBytes = 0, neededBits;
		byte extraBits = 0;
		//Calculating the total byte size
		for( Character c : charWeights.keySet() ){
			neededBits = charWeights.get(c) * paths.get(c).size();
			lengthInBytes += (int)(neededBits/8);
			extraBits += (byte)(neededBits % 8);
			lengthInBytes += (int)(extraBits / 8);
			extraBits %= 8;
		}
		//parse headers
		byte[] treeTopography = treeTopographyToBinary(root);
			
		//Write out how large the header will be
		BinaryIOUtil.writeOutInt(treeTopography.length, fos);
		//Write out the header
		fos.write(treeTopography);
		
		//write out the size of the paths
		BinaryIOUtil.writeOutInt(lengthInBytes, fos);
		fos.write(extraBits);
		
		//flush for safety
		fos.flush();
	}
	
	private static byte[] treeTopographyToBinary( Node cur ){
		ByteArrayOutputStream boas = new ByteArrayOutputStream();
		if( cur.left == null && cur.right == null ){
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
				boas.write(treeTopographyToBinary(cur.left));
				boas.write('\0');
				boas.write('R');
				boas.write(treeTopographyToBinary(cur.right));
			}catch( IOException ioe ){
				ioe.printStackTrace();
			}
		}
		return boas.toByteArray();
	}

	private static void writeOutBinary(String out, Map<Character, LinkedList<Integer>> paths, FileOutputStream fos) throws IOException{
		log.atInfo().log("beginning writeJsonOut");
		LinkedList<Integer> curPath;
		byte byteAdj = BIGBYTE;
		byte writtenByte = 0; 
		
		for( Character c : out.toCharArray() ){
			if( c == '\n' ){
				continue;
			}
			log.atFinest().log("current char="+c+".");
			curPath = paths.get(c);
			for( Integer i : curPath ){
				if( byteAdj == 0 ){
					log.atFinest().log("writtenByte:"+writtenByte);
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
	/*
	 * ===========================================================================================
	 * ============================HUFFMAN PARSING AND READ IN METHODS============================
	 * ===========================================================================================
	 * 1. read in topography as number of bytes which is stored as an int
	 * 2. read in topography until topography is done (headerParser)
	 * 3. read in the size, as a number of bytes, of the json file which is stored as a long
	 * 4. read in the number of dangling bits which is stored as a byte
	 * 5. read in the path until reach the end of the size (readInBody)
	 */

	public static <T> T readInHuffmanToObject(String dir, Class<T> t) {
		try {
			FileInputStream fis = new FileInputStream( new File(dir) );
			int topographyLength = BinaryIOUtil.readInInt(fis);
			Node readRoot = headerParser(fis, topographyLength);
			
			if( System.getProperty("user.env").equals("development"))
				readRoot.printTree();
			
			int dataSize = BinaryIOUtil.readInInt( fis );
			byte dangleBits = (byte)fis.read();
			long fullDataSize = (dataSize * 8) + dangleBits;
			
			byte[] pathData = new byte[dataSize + 1];
			fis.read(pathData);
			fis.close();
			
			return new GsonBuilder().create().fromJson(readInBody(readRoot, fullDataSize, pathData), t );
		} catch (FileNotFoundException e1) {
			e1.printStackTrace();
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		return null;
	}
	
	public static Node headerParser( FileInputStream fis, int headerSize ) throws IOException{
		fis.read(new byte[2]); //this is a trash read to grab the sentinel
		
		//Create a root node
		Node root = new Node("", 0);
		
		//traversal setup
		Stack<Node> stack = new Stack<Node>();
		stack.push(root);
		
		for( int i=2; i < headerSize; i += 4 ){
			//The node is full if the right node is not null
			while( stack.peek().right != null )
				stack.pop();
			
			//get the top of the stack
			Node cur = stack.peek();
			
			//Find the header, either L or R
			char header = BinaryIOUtil.readInChar( fis );
			//Find the character
			char c = BinaryIOUtil.readInChar( fis );
			
			Node n = new Node( c=='\0' ? "" : ""+c, 0 );
			
			if( c == '\0' )
				stack.push(n);
			
			if( header == 'L' )
				cur.left = n;
			else
				cur.right = n;
		}
		return root;
	}

	private static String readInBody(Node readRoot, long fullDataSize, byte[] pathData) {
		byte byteAdj = BIGBYTE;
		int byteIdx = 0;
		Node cur = readRoot;			
		StringBuilder sb = new StringBuilder();
		
		for( long i=0; i<=fullDataSize; i++ ){
			if( byteAdj == 0 ){
				byteAdj = BIGBYTE;
				byteIdx++;
			}
			if( !cur.name.equals("") ){
				sb.append(cur.name);
				cur = readRoot;
			}
			cur = ( (pathData[byteIdx] & byteAdj) == 0 ) ? cur.left : cur.right;
			byteAdj = (byte)((byteAdj >> 1) & 127); //&& 127 is to make sure we don't bit shift any negatives
		}
		return sb.toString();
	}

}
