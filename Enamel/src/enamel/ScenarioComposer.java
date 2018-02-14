package enamel;

import java.util.ArrayList;

public class ScenarioComposer {
	String text = "";

	ListManager aList;
	ArrayList<String> output = new ArrayList<String>();

	ScenarioComposer() {
		this.aList = null;
	}

	public String returnStringFile(ListManager aList) {
		aList.goHome();
		StringBuilder sb = new StringBuilder();

		boolean skip = false;
		while (skip == false) {
		//for(int j = 0; j < 4; j++) {
		
			for (int u = 0; u < aList.currentList.size(); u++) {
				Node s = aList.currentList.get(u);
				String key = s.getKeyPhrase();
				String data = s.getData();
				String jKey = "";
				String jData = "";
				int x = 0;

				if (key == "#HEAD" | key == "#TAIL" | key == "#BUTTON" | key == "#TEXT") {
					sb.append(data); // assumes it is simply text
					sb.append("\n");
				}

				if (key == "#JUNCTION") {
					for (int i = 0; i < s.buttonsNames.size(); i++) {
						{ // For all the list of nodes in Junction (now itterating through via index rater
							// than name
							if (s.buttonsNames.get(i) != null) {
								jData = s.buttonsNames.get(i);
								jKey = "/~skip-button:";
								sb.append(jKey + jData);
								sb.append("\n");
								x++;
							}
						}
					}
					jKey = "/~user-input";
					sb.append(jKey);
					sb.append("\n");

					// good up to here?

					// Now inside one of the Junctions
					for (int i = 0; i < s.buttons.size(); i++) {
						{ // For all the list of nodes in Junction (now itterating through via index rater
							// than name
							aList.currentList = s.buttons.get(i);
							// Changes to a new branch per itteration
							// Now on a new list
							for (Node t : aList.currentList) // for every node t in the current list(branch)
							{
								jKey = t.getKeyPhrase();
								jData = t.getData();
								if (jKey == "#BUTTON") {
									jKey = "/~";
									jData = t.getData();
									sb.append(jKey + jData);
									sb.append("\n");// append aList name
								} else if (jKey == "#TEXT") {
									sb.append(jData);
									sb.append("\n");// assumes it is simply text
								} else if (jKey == "/~skip:NEXTT") {
									sb.append(jKey);
									sb.append("\n");
								} else {
									sb.append(jKey + jData);
									sb.append("\n");// this code assumes that every node is in the proper order
													// readable
													// by the braile cell
													// and simply places txt as they would be in the array list

								}

								if (jKey == "/~skip:NEXTT" && i == s.buttons.size() - 1) {
									aList.currentList = t.nextList;
									// skip = true;

								}
							}
						}
					}	
				} //end of JUNCTION
				
				
				//test if at the end of data structure
				if((u == aList.currentList.size() - 1)  && (s.getKeyPhrase() != "#JUNCTION")) {
					skip = true;
				}
			}
		}


		text = sb.toString();
		return text;
	}
}
