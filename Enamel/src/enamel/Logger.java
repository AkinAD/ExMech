package enamel;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;

public class Logger {
	
	String filename;
	
	public Logger(String filename) {
		this.filename = filename;
	}
	
	public void countKeyWords() {
		String path = "FactoryScenarios/" + filename;
		List<String> list = Arrays.asList("/~pause", "/~disp-string", "/~repeat", "/~repeat-button", "/~skip-button", "/~user-input", "/~sound", 
												"/~reset-buttons", "/~skip", "/~disp-clearAll", "/~disp-clear-cell", "/~disp-cell-pins", "/~disp-cell-char",
												"/~disp-cell-raise", "/~disp-cell-lower");
	    HashMap<String, Integer> counts = new HashMap<String, Integer>();
	    for (String word : list) {
	        counts.put(word, 0);
	    }

	    try {
			for (String line : Files.readAllLines(Paths.get(path))) {
			    for (String word : line.replaceAll("[!?.,]", "").replaceAll(":", " ").split("\\s+")) {
			        Integer count = counts.get(word);
			        if (count != null) {
			            counts.put(word, count + 1);
			        }
			    }
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	    
	    TreeMap<String, Integer> sortedMap = sortMapByValue(counts);  
		System.out.println(sortedMap);
	       
//	    for (int i = 0; i < list.size(); i++) {
//	    	System.out.print(list.get(i) + ": " + counts.get(list.get(i)) + "\n");
//	    	
//	    }
	    
	}
	
	public static TreeMap<String, Integer> sortMapByValue(HashMap<String, Integer> map){
		Comparator<String> comparator = new ValueComparator(map);
		TreeMap<String, Integer> result = new TreeMap<String, Integer>(comparator);
		result.putAll(map);
		return result;
	}

	public static void main(String[] args) {
		
		Logger log = new Logger("Scenario_1.txt");
		Logger log2 = new Logger("Scenario_2.txt");
		Logger log3 = new Logger("Scenario_3.txt");
		
		log.countKeyWords();
		log2.countKeyWords();
		log3.countKeyWords();


	}

}
