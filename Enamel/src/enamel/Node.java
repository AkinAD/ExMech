package enamel;

import java.util.ArrayList;

public class Node {
	String keyPhrase;
	String data;
	
	ArrayList<Node> prevList;
	ArrayList<ArrayList<Node>> buttons;
	
	Node(){
		
	}
	
	Node(String k, String d){
		//Constructor for basic node. Takes keyPhrase and Data
		this.keyPhrase = k;
		this.data = d;
	}
	
	Node(ArrayList<ArrayList<Node>> buttons){
		//Constructor for junction. Takes a list of button names.
		this.keyPhrase = "#JUNCTION";
		this.buttons = buttons;
		System.out.println("Created new Junction!");
		
	}
	
	Node(String name, ArrayList<Node> prev){
		//Constructor for BUTTON head.
		this.keyPhrase = "#BUTTON";
		this.data = name;
		this.prevList = prev;
		System.out.println("-new Button: " + name);
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
}
