package system;

import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class RankMap {
	public static HashMap<String, Double> sort(HashMap<String, Double> documentMap) {

        List<Map.Entry<String, Double> > list = 
        		new LinkedList<Map.Entry<String, Double> >(documentMap.entrySet());
 
        Collections.sort(list, (i1, i2) -> i1.getValue().compareTo(i2.getValue()));
        Collections.reverse(list);
 
        HashMap<String, Double> temp = new LinkedHashMap<String, Double>();
        for (Map.Entry<String, Double> aa : list) {
            temp.put(aa.getKey(), aa.getValue());
        }
        
        return temp;
	}
}
