package enamel;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

public class LoadParser {

	LoadParser(){
	}
	
	public static ListManager fromText(String scenarioFile) {
		ListManager result = null;
		int cells = -1;
		int buttons = -1;
		
		try {
			FileReader fileReader = new FileReader(scenarioFile);
			BufferedReader bufferedReader = new BufferedReader(fileReader);
			String line;
			
			//Get cells and buttons sizes from the first two lines.
			for (int i = 0; i < 2; i++) {
					line = bufferedReader.readLine();
					String[] lineSplit = line.split(" ", 2);		
					String first = lineSplit[0];
					String rest = lineSplit[1];
					if (first.equals("Cell")) {
						cells = Integer.parseInt(rest);
					} else if (first.equals("Button")) {
						buttons = Integer.parseInt(rest);
						break;
					} else {
						throw new IllegalArgumentException("loading failed: does not contain 'cells' or 'buttons'");
					}
					
				
			}
			result = new ListManager(cells, buttons);
			
			//Add nodes based on key phrases
			while ((line = bufferedReader.readLine()) != null) {
				if(line.startsWith("/~")) {
					String[] lineSplit = line.split(":", 2);
					if(lineSplit.length > 1) {
						String first = lineSplit[0];
						String rest = lineSplit[1];
						result.addNext(first, rest);
					}else {
						result.addNext(line, "THIS IS A LABEL!!!!!!!!!!!!!!!!!!!!!");
					}
				}else if(!line.isEmpty()){
					result.addNext("#TEXT", line);
				}
			}
			
			
			fileReader.close();
			
			
			
			System.out.println("Completed load of scenario file.");
			
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return result;
	}
	
	
	
	
	
	
	/* #############################################################################
	 * TESTING // Delete later.
	 * #############################################################################*/
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		ListManager derp = LoadParser.fromText("FactoryScenarios/Scenario_3.txt");
		
		derp.getData();
	}
}
