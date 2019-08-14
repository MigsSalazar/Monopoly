package monopoly7.utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import monopoly7.models.Player;
import monopoly7.models.Property;
import monopoly7.models.MonopolizableProperty;

public class SuiteUtil {
	
	public static int findIntDisparity( int[] values ){
		if(values.length == 0)
			return 0;
		int min = values[0];
		int max = min;
		for( int i : values ){
			if( i < min )
				min = i;
			if( i > max )
				max = i;
		}
		return max - min;
	}
	
	public static int findMonopolyGradeDisparity( List<Property> monopoly ){
		int[] values = new int[monopoly.size()];
		for( int i=0; i<monopoly.size(); i++ ){
			values[i] = monopoly.get(i).getGrade();
		}
		return findIntDisparity(values);
	}
	
	public static Map<String, List<Property>> getPlayerMonopolies( Player player ){
		Map<String, List<Property>> ret = new HashMap<String, List<Property>>();
		Map<String, List<Property>> holder = new HashMap<String, List<Property>>();
		
		for( Property prop : player.getProps().values() ){
			if( !(prop instanceof MonopolizableProperty) )
				continue;
			
			MonopolizableProperty street = (MonopolizableProperty)prop;
			String color = street.getColor();
			if( !holder.containsKey(color) ){
				holder.put(color, new ArrayList<Property>());
			}
			List<Property> tempList = holder.get(color);
			tempList.add(street);
			
			if( tempList.size() == street.getMonopolyLimit() ){
				ret.put(color, tempList);
			}
			
		}
		return ret;
	}
	
}
