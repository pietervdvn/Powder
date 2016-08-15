package grid2;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import csv.CSVEntry;
import csv.SimpleCSVParser;

public class ParseElements {
	
	public static Element[] parseElements(List<String> csv){
		List<Map<String, CSVEntry>> parsed = SimpleCSVParser.parseCSV(csv);
		Element[] els = new Element[parsed.size()];
		
		int id = 1;
		Map<String, Integer> ids = new HashMap<String, Integer>();
		for (Map<String,CSVEntry> map : parsed) {
			String name = map.get("name").stringValue.toUpperCase();
			if(ids.containsKey(name)){
				throw new IllegalArgumentException("Element with duplicate name: "+name);
			}
			ids.put(name, id);
			id++;
		}
		
		for (int i = 0; i < els.length; i++) {
			els[i] = Element.fromCSV(i+1, ids, parsed.get(i));
		}
		
		for (Element e : els) {
			e.validate();
			e.crossValidate(els);
		}
		
		return els;
	}

}
