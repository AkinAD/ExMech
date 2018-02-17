package enamel;

import java.util.ArrayList;
import java.util.Map.Entry;

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

			for (int u = 0; u < aList.currentList.size(); u++) {

				Node currentNode = aList.currentList.get(u);
				String key = currentNode.getKeyPhrase();
				String data = currentNode.getData();
				String jKey = "";
				String jData = "";
				
				if (key == "#HEAD") {
					sb.append(data);
					sb.append("\n");
					sb.append("\n");
				}

				else if (key == "#TAIL" || key == "#BUTTON" || key == "#TEXT") {
					sb.append(data); // assumes it is simply text
					sb.append("\n");
				}

				else if (key == "#JUNCTION") {
					//create /~skip-buttons for every branch
					for (Entry<Integer,String> entry : currentNode.buttonsNames.entrySet()) {
							if (entry.getValue() != null) {
								jData = entry.getValue();
								jKey = entry.getKey().toString();
								sb.append("/~skip-button:"+jKey+" " + jData);
								sb.append("\n");
							}	
					}
					sb.append("/~user-input");
					sb.append("\n");
					sb.append("\n");

					// Now inside one of the Junctions
					int entrySetCount = 0;
					for (Entry<Integer, String> entry : currentNode.buttonsNames.entrySet()) {
						entrySetCount++;
						int i = entry.getKey();
						//set currentList to the branch so we can iterate through it
						aList.currentList = currentNode.buttons.get(i);
						//for every node in the branch...
						for (int k = 0; k < aList.currentList.size(); k++) {
							Node branchNode = aList.currentList.get(k);
							String bKey = branchNode.getKeyPhrase();
							String bData = branchNode.getData();
							if (bKey == "#BUTTON") {
								sb.append("/~" + bData);
								sb.append("\n");// append aList name
							} else if (bKey == "#TEXT") {
								sb.append(bData);
								sb.append("\n");// assumes it is simply text
							} else if (bKey == "/~skip:NEXTT") {
								sb.append(bKey);
								sb.append("\n");
								sb.append("\n");
							} else if (bKey == "#JUNCTION") {
								//sb.append("!!!!!!!!!made it to a junction somehow?");
								sb.append("\n");
							} else {
								sb.append(bKey+":"+ bData);
								sb.append("\n");
							}
							
							//reach last branch and last node
							if (bKey == "/~skip:NEXTT" && entrySetCount == currentNode.buttonsNames.entrySet().size()) {
								aList.currentList = branchNode.nextList;
								//sb.append("!!!!!!!!!!!!end of branch condition \n");
								break;
								}
						}
					}
				} // end of JUNCTION
				
				else if (key == "/~NEXTT"){
					sb.append(key);
					sb.append("\n");
					sb.append("\n");
				}

				else {
					//generic node data
					sb.append(key+":"+data);
					sb.append("\n");
				}
				
				// test if at the end of data structure (no more nodes left)
				if ((u == aList.currentList.size() - 1) && (currentNode.getKeyPhrase() != "#JUNCTION")) {
					//sb.append("!!!!!!!!!!end of data struct condition \n");
					skip = true;
				}
			}
		}

		text = sb.toString();
		return text;
	}
}
