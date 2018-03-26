package enamel;

import java.util.ArrayList;

public class NodeInfo {
	ArrayList<Node> currentList;
	int index;
	String label;
	
	public NodeInfo(ArrayList<Node> currentList, int index, String label) {
		this.currentList = currentList;
		this.index = index;
		this.label = label;
	}
	
	@Override
	public String toString() {
		return this.label;
	}
}
