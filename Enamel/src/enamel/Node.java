package enamel;

import java.util.ArrayList;
import java.util.HashMap;

public class Node {
	String keyPhrase;
	String data;
	
	ArrayList<Node> nextList;
	ArrayList<Node> prevList;
	ArrayList<ArrayList<Node>> buttons;
	HashMap<Integer,String> buttonsNames;
	
	Node(){
		
	}
	
	Node(String k, String d){
		//Constructor for basic node. Takes keyPhrase and Data
		this.keyPhrase = k;
		this.data = d;
	}
	
	public static Node junction(ArrayList<ArrayList<Node>> buttons, HashMap<Integer,String> buttonsNames, ArrayList<Node> nextt){
		//Constructor for junction. Takes a list of button names.
		Node a = new Node();
		a.keyPhrase = "#JUNCTION";
		a.data = "<Question (choose an answer)>";
		a.buttons = buttons;
		a.buttonsNames = buttonsNames;
		a.nextList = nextt;
		//System.out.println("Created new Junction!");
		return a;
		
	}
	
	public static Node button(String name, ArrayList<Node> prev){
		Node a = new Node();
		//Constructor for BUTTON head.
		a.keyPhrase = "#BUTTON";
		a.data = name;
		a.prevList = prev;
		System.out.println("-new Button: " + name);
		return a;
	}
	
	public static Node button(String keyPhrase, String name, ArrayList<Node> prev){
		Node a = new Node();
		//Constructor for BUTTON head.
		a.keyPhrase = keyPhrase;
		a.data = name;
		a.prevList = prev;
		System.out.println("-new Button: " + name);
		return a;
	}
	
	public static Node tail(ArrayList<Node> next){
		Node a = new Node();
		//Constructor for NEXT head.
		a.keyPhrase = "/~skip:NEXTT";
		a.data ="<End of branch, press next again to rejoin main branch.>";
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
	
	public ArrayList<ArrayList<Node>> getButtons(){
		return buttons;
	}
}
