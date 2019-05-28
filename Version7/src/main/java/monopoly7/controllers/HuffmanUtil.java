package monopoly7.controllers;

import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.io.Writer;
import java.util.*;
import java.util.Map.Entry;

import com.google.gson.*;

public class HuffmanUtil {
	
	static File TREEFILE = new File("printed tree.txt");
	static BufferedWriter PRINTTREE;
	
	public static void main( String[] args ){
		/*
		try {
			PRINTTREE = new BufferedWriter(new FileWriter( TREEFILE ));
		} catch (IOException e2) {
			// TODO Auto-generated catch block
			e2.printStackTrace();
		}*/
		
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
		
		e.getIdontknowMoreStuff().put("lol", "penis");
		
		Gson gson = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().setPrettyPrinting().create();
		String out = gson.toJson(e);
		
		System.out.println(out);
		
		HuffmanNode root = objectToHuffman(out);
		
		//speakOut(root);
		
		//BTreePrinter.printNode(root);
		
		ArrayList<Integer> bin = new ArrayList<Integer>(makeBinary( root, out ));

		
		try {
			File fout = new File("tempbin");
			System.out.println(fout.getAbsolutePath());
			ByteArrayOutputStream fos = new ByteArrayOutputStream();
			for( int i : bin ){
				fos.write(i);
			}
			try(OutputStream outputStream = new FileOutputStream("tempbin")) {
			    fos.writeTo(outputStream);
			}
			//fos.
			fos.close();
			//PRINTTREE.close();
		} catch (FileNotFoundException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		
		/*
		int test = -2147483648;
		System.out.println("test="+test);
		for( int i=0; i<31; i++ ){
			test /= 2;
			test &= Integer.MAX_VALUE;
			System.out.println("test="+test);
		}
		*/
		
		
	}
	
	public static LinkedList<Integer> makeBinary( HuffmanNode root, String out ){
		StringBuilder sb = new StringBuilder();
		Map<Character, Integer> steps = new HashMap<Character, Integer>();
		Map<Character, LinkedList<Integer>> outByte = new HashMap<Character, LinkedList<Integer>>();
		
		LinkedList<Integer> retList = new LinkedList<Integer>();
		
		int curByte = 0;
		int bitCount = 0;
		
		//long stepCount = 0;
		
		for( int i=0; i<out.length(); i++ ){
		
			char curChar = out.charAt(i);
			
			if( curChar == '\n' ){
				continue;
			}
			
			//System.out.println("char at "+i+"="+curChar);
			
			if( !outByte.containsKey(curChar) ){
				//System.out.println("searching for char:"+curChar+".");
				outByte.put(curChar, findCharacter( root, steps, curChar ));
			}
			int size = steps.get(curChar);
			
			ArrayList<Integer> path = new ArrayList<Integer>(outByte.get(curChar));
			//int tracker = -2147483648;
			int pathIdx = 0;
			
			for( int j=0, pathShift=0; j<size; j++, pathShift++ ){
				
				if( bitCount >= 31 ){
					retList.add( curByte );
					bitCount = 0;
					curByte = 0;
					sb.append("\n");
				}
				if( pathShift >= 31 ){
					//tracker = -2147483648;
					pathIdx++;
					pathShift = 0;
				}
				int pathed = path.get(pathIdx);
				pathed = ((pathed >> (31 - pathShift)) & 1);
				sb.append(pathed);
				pathed = pathed  << (31 - bitCount);
				System.out.println("pathed post shift="+pathed);
				//int isolateIdx = 31 - pathShift;
				//int isolated = 1 & ( path.get(pathIdx) >> isolateIdx );
				//isolated = isolated << (31 - bitCount);
				curByte = curByte | pathed;
				System.out.println("curByte post or="+curByte);
				bitCount++;
				/*
				int curNum = path.get(pathIdx) >> bitCount;
				int isolater = 1 << ( 30 - bitCount );
				int isolated = curNum & isolater;
				curByte |= isolated;
				bitCount++;
				*/
			}
			
			
		}
		
		if( retList.peekLast() != curByte ){
			curByte = curByte << (31 - bitCount);
			retList.add(curByte);
			System.out.println();
		}
		System.out.println(sb.toString());
		
		return retList;
	}
	
	public static LinkedList<Integer> findCharacter( HuffmanNode root, Map<Character,Integer> steps, Character c ){
		LinkedList<Integer> retList = new LinkedList<Integer>();
		int curByte = 0;
		int bitCount = 0;
		HuffmanNode cur = root;
		int stepCount = 0;
		
		while( !cur.name.equals( "" + c ) ){
			//System.out.println("curname="+cur.name+":left is null="+(cur.left==null)+":right is null="+(cur.right==null));
			stepCount++;
			if( cur.left.name.indexOf(c) > -1 ){
				//curByte &= -2;
				//System.out.println("going left");
				cur = cur.left;
			}else if( cur.right.name.indexOf(c) > -1 ){
				//System.out.println("going right");
				curByte |= 1;
				cur = cur.right;
			}
			curByte = curByte << 1;
			bitCount++;
			if( bitCount >= 31 ){
				retList.add(curByte);
				curByte = 0;
				bitCount = 0;
			}
		}
		//System.out.println("curname="+cur.name+":curByte="+curByte+":stepCount="+stepCount);
		if( bitCount < 31 ){
			/*
			for( int i=bitCount; i<31; i++ ){
				curByte &= -2;
				curByte = curByte << 1;
			}*/
			curByte = curByte << ( 31 - bitCount );
			retList.add(curByte);
		}
		//System.out.println("curByte after shift="+curByte);
		
		steps.put(c, stepCount);
		
		return retList;
	}
	
	
	public static HuffmanNode objectToHuffman( String out ){
		//System.out.println(out);
		Map<Character, Integer> mappings = countChars( out );
		
		//printMappings( mappings );
		
		List<Character> unsorted = new LinkedList<Character>( mappings.keySet() );
		List<Character> order = mergeSortMap( mappings, unsorted );
		
		/*
		System.out.println("================\nORDERED LIST\n================");
		for( Character c : order ){
			System.out.println(c + ": " + mappings.get(c));
		}
		*/
		
		LinkedList<HuffmanNode> forest = new LinkedList<HuffmanNode>();
		for( Character c : order ){
			forest.add(new HuffmanNode( ""+c, mappings.get(c) ) );
		}
		
		//System.out.println("cutting forest");
		
		cutForest( forest );
		
		
		//System.out.println("forest size: "+forest.size());
		//traverseTree( forest.get(0) );
		
		//System.out.println("SPEAKING OUT");
		//speakOut( forest.get(0) );
		
		return forest.get(0);
		//gson.toJson(src)
	}
	
	public static void speakOut( HuffmanNode root ){
		
		System.out.println( root.name );
		
		if( root.left != null ){
			System.out.println( root.name + "-left" );
			speakOut( root.left );
		}
		
		if( root.right != null ){
			System.out.println( root.name + "-right" );
			speakOut( root.right );
		}
		
	}
	
	public static void traverseTree( HuffmanNode root ){
		LinkedList<HuffmanNode> steps = new LinkedList<HuffmanNode>();
		LinkedList<String> bigList = new LinkedList<String>();
		LinkedList<Boolean> leafStat = new LinkedList<Boolean>();
		Set<HuffmanNode> visited = new HashSet<HuffmanNode>();
		
		steps.push(root);
		//visited.add(root);
		
		while( !steps.isEmpty() ){
			HuffmanNode cur = steps.peek();
			if( !visited.contains(cur) ){
				bigList.push(cur.name);
				if( cur.left != null || cur.right != null ){
					leafStat.push(false);
				}else{
					leafStat.push(true);
				}
				visited.add(cur);
			}
			
			if( cur.left != null && !visited.contains(cur.left) ){
				steps.push(cur.left);
				//bigList.push(cur.left.name);
			}else if( cur.right != null && !visited.contains( cur.right ) ){
				steps.push(cur.right);
				//bigList.push(cur.right.name);
			}else{
				steps.pop();
			}
			
		}
		for( int i=0; i<bigList.size(); i++ ){
			System.out.println(bigList.get(i) + ": " + leafStat.get(i));
		}
		
		
	}
	
	public static void cutForest( LinkedList<HuffmanNode> forest ){
		while( forest.size() != 1 ){
			//System.out.println("size of forest: "+forest.size());
			HuffmanNode first = forest.pollFirst();
			HuffmanNode second = forest.pollFirst();
			String newName = first.name + second.name;
			int newWeight = first.weight + second.weight;
			HuffmanNode newRoot = new HuffmanNode( newName, newWeight, first, second );
			int curIdx = 0;
			if( !forest.isEmpty() ){
				while( curIdx < forest.size() && forest.get(curIdx).weight < newRoot.weight ){
					curIdx++;
				}
			}
			forest.add(curIdx, newRoot);
		}
	}
	
	public static void printMappings( Map<Character, Integer> charMap ){
		for( Character c : charMap.keySet() ){
			System.out.println(c + ": " + charMap.get(c));
		}
	}
	
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
	
	public static Map<Character, Integer> countChars( String jsoned ){
		char[] charArr = jsoned.toCharArray();
		Map<Character, Integer> ret = new HashMap<Character, Integer>();
		
		for( Character c : charArr ){
			if( c != '\n' ){
				if( !ret.containsKey(c) ){
					ret.put(c, 0);
				}
				ret.put(c, ret.get(c)+1);
			}
			
		}
		
		return ret;
	}
	
	public static class HuffmanNode{
		String name;
		int weight;
		
		HuffmanNode left = null;
		HuffmanNode right = null;
		
		public HuffmanNode( String n, int w ){
			name = n;
			weight = w;
			left = null;
			right = null;
		}
		
		public HuffmanNode( String n, int w, HuffmanNode l, HuffmanNode r){
			name = n;
			weight = w;
			left = l;
			right = r;
		}
	}
	
	/*
	public static String getHeader( String jsoned ){
		char[] charArr = jsoned.toCharArray();
		Set<Character> uniques = 
		for(){
			
		}
		
		return null;
	}
	*/
	static class BTreePrinter {

	    public static void printNode(HuffmanNode root) {
	        int maxLevel = BTreePrinter.maxLevel(root);

	        printNodeInternal(Collections.singletonList(root), 1, maxLevel);
	    }

	    private static void printNodeInternal(List<HuffmanNode> nodes, int level, int maxLevel) {
	        if (nodes.isEmpty() || BTreePrinter.isAllElementsNull(nodes))
	            return;

	        int floor = maxLevel - level;
	        int endgeLines = (int) Math.pow(2, (Math.max(floor - 1, 0)));
	        int firstSpaces = (int) Math.pow(2, (floor)) - 1;
	        int betweenSpaces = (int) Math.pow(2, (floor + 1)) - 1;

	        BTreePrinter.printWhitespaces(firstSpaces);

	        List<HuffmanNode> newNodes = new ArrayList<HuffmanNode>();
	        for (HuffmanNode node : nodes) {
	            if (node != null) {
	            	try {
						PRINTTREE.write(node.name);
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
	                //System.out.print(node.name);
	                newNodes.add(node.left);
	                newNodes.add(node.right);
	            } else {
	                newNodes.add(null);
	                newNodes.add(null);
	                try {
						PRINTTREE.write(" ");
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
	                //System.out.print(" ");
	            }

	            BTreePrinter.printWhitespaces(betweenSpaces);
	        }
	        try {
				PRINTTREE.newLine();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	        //System.out.println("");

	        for (int i = 1; i <= endgeLines; i++) {
	            for (int j = 0; j < nodes.size(); j++) {
	                BTreePrinter.printWhitespaces(firstSpaces - i);
	                if (nodes.get(j) == null) {
	                    BTreePrinter.printWhitespaces(endgeLines + endgeLines + i + 1);
	                    continue;
	                }

	                if (nodes.get(j).left != null){
	                	try {
	    					PRINTTREE.write("/");
	    				} catch (IOException e) {
	    					// TODO Auto-generated catch block
	    					e.printStackTrace();
	    				}
	                    //System.out.print("/");
	                }else{
	                    BTreePrinter.printWhitespaces(1);
	                }
	                BTreePrinter.printWhitespaces(i + i - 1);

	                if (nodes.get(j).right != null){
	                	try {
	    					PRINTTREE.write("\\");
	    				} catch (IOException e) {
	    					// TODO Auto-generated catch block
	    					e.printStackTrace();
	    				}
	                    //System.out.print("\\");
	                }else{
	                    BTreePrinter.printWhitespaces(1);
	                }
	                BTreePrinter.printWhitespaces(endgeLines + endgeLines - i);
	            }
	            try {
					PRINTTREE.newLine();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
	            //System.out.println("");
	        }

	        printNodeInternal(newNodes, level + 1, maxLevel);
	    }

	    private static void printWhitespaces(int count) {
	        for (int i = 0; i < count; i++){
				try {
					PRINTTREE.write(" ");
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
	        }
	            //System.out.print(" ");
	    }

	    private static int maxLevel(HuffmanNode node) {
	        if (node == null)
	            return 0;

	        return Math.max(BTreePrinter.maxLevel(node.left), BTreePrinter.maxLevel(node.right)) + 1;
	    }

	    private static boolean isAllElementsNull(List<?> list) {
	        for (Object object : list) {
	            if (object != null)
	                return false;
	        }

	        return true;
	    }

	}
}
