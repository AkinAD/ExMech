package enamel;

import java.util.ArrayList;

public class Node {
	String keyPhrase;
	String data;
	
	ArrayList<Node> nextList;
	ArrayList<Node> prevList;
	ArrayList<ArrayList<Node>> buttons;
	
	Node(){
		
	}
	
	Node(String k, String d){
		//Constructor for basic node. Takes keyPhrase and Data
		this.keyPhrase = k;
		this.data = d;
	}
	
	Node(ArrayList<ArrayList<Node>> buttons, ArrayList<Node> nextt){
		//Constructor for junction. Takes a list of button names.
		this.keyPhrase = "#JUNCTION";
		this.buttons = buttons;
		this.nextList = nextt;
		System.out.println("Created new Junction!");
		
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
	
	public static Node head(ArrayList<Node> next){
		Node a = new Node();
		//Constructor for NEXT head.
		a.keyPhrase = "/~skip:NEXTT";
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
