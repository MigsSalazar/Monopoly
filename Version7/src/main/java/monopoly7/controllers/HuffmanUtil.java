package monopoly7.controllers;

import java.util.*;

import com.google.gson.*;

public class HuffmanUtil {
	
	public static void main( String[] args ){
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
		
		
		objectToHuffman(e);
		
	}

	public static void objectToHuffman( Object o ){
		Gson gson = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().setPrettyPrinting().create();
		String out = gson.toJson(o);
		//System.out.println(out);
		Map<Character, Integer> mappings = countChars( out );
		
		//printMappings( mappings );
		
		List<Character> unsorted = new LinkedList<Character>( mappings.keySet() );
		List<Character> order = mergeSortMap( mappings, unsorted );
		
		
		System.out.println("================\nORDERED LIST\n================");
		for( Character c : order ){
			System.out.println(c + ": " + mappings.get(c));
		}
		
		List<HuffmanNode> forest = new LinkedList<HuffmanNode>();
		for( Character c : order ){
			forest.add(new HuffmanNode( ""+c, mappings.get(c) ) );
		}
		
		System.out.println("cutting forest");
		
		cutForest(  (LinkedList)forest );
		
		
		//System.out.println("forest size: "+forest.size());
		traverseTree( forest.get(0) );
		
		
		//gson.toJson(src)
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
			String newName = second.name + first.name;
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
		
		HuffmanNode left;
		HuffmanNode right;
		
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
	
}
