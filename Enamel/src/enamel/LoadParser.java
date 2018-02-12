package enamel;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class LoadParser {

	BufferedReader bufferedReader;
	String line;
	
	LoadParser(){
		bufferedReader = null;
		line = "";
	}
	
	public ListManager fromText(String scenarioFile) {
		ListManager result = null;
		int cells = -1;
		int buttons = -1;
		
		try {
			//read the text file
			FileReader fileReader = new FileReader(scenarioFile);
			bufferedReader = new BufferedReader(fileReader);
			
			System.out.println("## LOADING SCENARIO FILE ##");
			System.out.println("Loading: '" + scenarioFile+ "'");
			
			//Get cells and buttons sizes from the first two lines.
			for (int i = 0; i < 2; i++) {
					nextLine();
					//split the line into fields: keyPhrase, data
					String[] lineSplit = line.split(" ", 2);		
					String first = lineSplit[0];
					String rest = lineSplit[1];
					//Set cell and button values
					if (first.equals("Cell")) {
						cells = Integer.parseInt(rest);
					} else if (first.equals("Button")) {
						buttons = Integer.parseInt(rest);
						break;
					} else {
						throw new IllegalArgumentException("loading failed: does not contain 'cells' or 'buttons'");
					}
					
				
			}
			//Create new ListManager using the extracted cell and button numbers.
			result = new ListManager(cells, buttons);
			
			//Add nodes:
			while ((nextLine()) != null) {
				line.trim();
				//Handle keyPhrases
				if(line.startsWith("/~")) {
					String[] lineSplit = line.split(":", 2);
					if(lineSplit.length > 1) {
						String first = lineSplit[0];
						String rest = lineSplit[1];
						
						//handles cases for JUNCTIONS
						if(line.startsWith("/~skip-button")) {
							junction(result);
						} else {
						
						//Add generic keyPhrase & data node.
						result.addNext(first, rest);
						}
					}else {
						//do stuff with labels here! Possibly remove this!
						result.addNext(line, "THIS IS A LABEL!!!!!!!!!!!!!!!!!!!!!");
					}
				}else if(!line.isEmpty() && !line.equals(" ")){
					//add line of text (no keyPhrase detected)
					result.addNext("#TEXT", line.trim());
				}
			}
			
			
			fileReader.close();
			
			
			
			System.out.println("## FINISHED LOADING SCENARIO FILE ##");
			
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return result;
	}
	
	private void junction(ListManager result) {
		//Handles cases where a junction is required
		ArrayList<String> buttonList = new ArrayList<String>();
		//Get all the button names and set them in an array list.
		while (line.startsWith("/~skip-button")) {
			String[] lineSplit = line.split(":", 2);
			String[] values = lineSplit[1].split(" ");
			if (values.length > 1) {
				int button = Integer.parseInt(values[0]);
				String buttonName = values[1];
				buttonList.add(button, buttonName);
			} else {
				throw new IllegalArgumentException("loading failed: skip-button does not contain enough variables.");
			}
			nextLine();
			line.trim();
		}
		if (!line.startsWith("/~user-input")) {
			throw new IllegalArgumentException("loading failed: expected /~user-input after skip buttons!");
		}

		// create the junction with the above buttonNames
		result.createJunction(buttonList);
		int juncIndex = result.index;
		ArrayList<Node> juncList = result.currentList;
		ArrayList<Node> juncNext = result.getNextList();

			//Add nodes under each of the 'branches' under the Junction
			while ((nextLine()) != null && !line.trim().equals(" ")) {
				if (line.startsWith("/~NEXTT")) {
					//Case for loading nodes is done. Set current position to NEXTT node. exit.
					result.currentList = juncNext;
					result.index = 0;
					System.out.println("Switched to NEXTT path...");
					break;
				}
				
				//Switch to the relevant 'branch' to add nodes.
				if (line.startsWith("/~")) {
					String buttonName = line.substring(2, line.length()).trim();
					int index = buttonList.indexOf(buttonName);
					if (index < 0) {
						throw new IllegalArgumentException(
								"loading failed: start of branch does not match list of buttons!");
					}

					// Navigate to list after verifying it exists
					result.junctionGoto(index);

					// Read text until you reach /~skip:NEXTT
					while ((nextLine()) != null) {
						line.trim();
						
						//Add nodes until /~skip is reached.
						if (line.startsWith("/~")) {
							String[] lineSplit = line.split(":", 2);
							if (lineSplit.length > 1) {
								String first = lineSplit[0];
								String rest = lineSplit[1];
								
								// break if /~skip shows up
								if (line.startsWith("/~skip:")) {
									result.currentList = juncList;
									result.index = juncIndex;
									break;
								}
								
								//Add generic keyPhrase & data node.
								result.addNext(first, rest);
							
								
							}
						} else if (!line.isEmpty() && !line.equals(" ")) {
							//add line of text (no keyPhrase detected)
							result.addNext("#TEXT", line);
						}
					}

				
				
				
			}
			
		}

		System.out.println("END OF JUNCTION CREATION: " + buttonList.toString());


	}

	private String nextLine() {
		//made this into a method to clean up the code a bit.
		try {
			line = bufferedReader.readLine();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return line;
	}
	
	
	
	/* #############################################################################
	 * TESTING // Delete later.
	 * #############################################################################*/
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		LoadParser parser = new LoadParser();
		ListManager derp = parser.fromText("FactoryScenarios/Scenario_3.txt");
		
		System.out.println();
		System.out.println("##### TESTING #####");
		System.out.println();
		
		derp.goHome();
		derp.next();
		derp.next();
		derp.next();
		derp.next();
		derp.next();
		derp.next();
		derp.next();
		derp.junctionGoto(1);
		derp.next();
		derp.next();
		derp.next();
		derp.next();
		derp.next();
		derp.next();
		derp.next();
		derp.next();
		derp.junctionGoto(1);
		derp.next();
		derp.next();
		derp.next();
		derp.next();
		derp.next();
		derp.next();
		derp.next();
		derp.next();
		derp.junctionGoto(1);
		derp.next();
		derp.next();
		derp.next();
		derp.next();
		derp.next();
		derp.next();
		derp.next();
		derp.next();
		derp.next();
	}
}
