package enamel;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

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
				line.trim();
				
				if(line.startsWith("/~")) {
					String[] lineSplit = line.split(":", 2);
					if(lineSplit.length > 1) {
						String first = lineSplit[0];
						String rest = lineSplit[1];
						
						//handles cases for JUNCTIONS
						if(line.startsWith("/~skip-button")) {
							junction(bufferedReader, line);
						} else {
						
						//Takes keyPhrase and rest as data.
						result.addNext(first, rest);
						}
					}else {
						//do stuff with labels here! Possibly remove this!
						result.addNext(line, "THIS IS A LABEL!!!!!!!!!!!!!!!!!!!!!");
					}
				}else if(!line.isEmpty() && !line.equals(" ")){
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
	
	private static void junction(BufferedReader bufferedReader, String line) {
		try {
			ArrayList<String> buttonList = new ArrayList<String>();
			while (line.startsWith("/~skip-button")) {
				String[] lineSplit = line.split(":", 2);
				String[] values = lineSplit[1].split(" ");
				if (values.length > 1) {
					int button = Integer.parseInt(values[0]);
					String buttonName = values[1];
					buttonList.add(button, buttonName);
				} else {
					throw new IllegalArgumentException(
							"loading failed: skip-button does not contain enough variables.");
				}
				line = bufferedReader.readLine();
				line.trim();
			}
			if(!line.startsWith("/~user-input")) {
				throw new IllegalArgumentException("loading failed: expected /~user-input after skip buttons!");
			}
			
			boolean nextt = false;
			while(nextt == false) {
				while ((line = bufferedReader.readLine()) != null && !line.trim().equals(" ")) {
					if (line.startsWith("/~")) {
						String buttonName = line.substring(2, line.length()).trim();
						int index = buttonList.indexOf(buttonName);
						if (index < 0) {
							throw new IllegalArgumentException(
									"loading failed: start of branch does not match list of buttons!");
						}

					}

				}
				
				if(line.startsWith("/~NEXTT")) {
					nextt = true;
					break;
				}
			}
			
			//set current position to nextt node

			System.out.println("END OF JUNCTION CREATION");
			System.out.println("Buttons are: " + buttonList.toString());

		} catch (IOException e) {
			e.printStackTrace();
		}
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
