package enamel;

import java.util.ArrayList;
import java.util.HashMap;

public class Node {
	String keyPhrase;
	String data;
	String label;

	ArrayList<Node> nextList;
	ArrayList<Node> prevList;
	ArrayList<ArrayList<Node>> buttons;
	HashMap<Integer, String> buttonsNames;

	Node() {

	}

	Node(String k, String d) {
		// Constructor for basic node. Takes keyPhrase and Data
		this.keyPhrase = k;
		this.data = d;
		
		this.label = k + " " + d;
		
		//set labels
		switch(k) {
		case "#HEAD":
			this.label = "LOCATION: Main Story Line";
			break;
		case "#TEXT":
			this.label ="TEXT: " + "\""+ d +"\"";
			break;
		case "/~sound":
			this.label = "EVENT: Play sound:  " + d;
			break;
		case "/~disp-clear-cell":
			this.label = "EVENT: Clears the following braille characters:  " + d;
			break;
		case "/~disp-cell-pins":
			this.label = "EVENT: Set braille character  " +"1"+ " to " + "a";
			break;
		case "/~pause":
			this.label = "EVENT: Pause for " + d + " seconds";
			break;
		case "/~reset-pins":
			this.label = "EVENT: Reset all braille characters";
			break;
		case "/~disp-string":
			this.label = "EVENT: Set braille characters to: " + d;
			break;
		case "/~disp-cell-clear":
			this.label = "EVENT: Clear all braille characters.";
			break;
		}
	}

	public static Node junction(ArrayList<ArrayList<Node>> buttons, HashMap<Integer, String> buttonsNames,
			ArrayList<Node> nextt) {
		// Constructor for junction. Takes a list of button names.
		Node a = new Node();
		a.keyPhrase = "#JUNCTION";
		a.data = "Question (choose an answer)";
		a.label = "USER-INPUT: (Choose an answer to continue).";
		a.buttons = buttons;
		a.buttonsNames = buttonsNames;
		a.nextList = nextt;
		// System.out.println("Created new Junction!");
		return a;

	}

	public static Node button(String name, ArrayList<Node> prev) {
		Node a = new Node();
		// Constructor for BUTTON head.
		a.keyPhrase = "#BUTTON";
		a.data = name;
		a.label ="LOCATION:  " + name;
		a.prevList = prev;
		System.out.println("-new Button: " + name);
		return a;
	}

	public static Node buttonNextt(ArrayList<Node> prev) {
		Node a = new Node();
		// Constructor for BUTTON head.
		a.keyPhrase = "/~NEXTT";
		a.data = "Nextt";
		a.label ="LOCATION: Main Story Line. (Press previous to go back to USER-INPUT)";
		a.prevList = prev;
		System.out.println("-new Button: " + "/~NEXTT");
		return a;
	}

	public static Node tail(ArrayList<Node> next) {
		Node a = new Node();
		// Constructor for NEXT head.
		a.keyPhrase = "/~skip:NEXTT";
		a.data = "Skip to main branch";
		a.label = "LOCATION: End of Answer.   (Press next again to return to the main story line.)";
		a.nextList = next;
		return a;
	}

	public void setKeyPhrase(String s) {
		this.keyPhrase = s;
	}

	public void setData(String s) {
		this.data = s;
	}

	public String getKeyPhrase() {
		return this.keyPhrase;
	}

	public String getData() {
		return this.data;
	}
	public String getLabel() {
		return this.label;
	}

	public ArrayList<ArrayList<Node>> getButtons() {
		return buttons;
	}
}
