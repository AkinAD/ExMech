package enamel;

import java.util.ArrayList;
import java.util.Scanner;

public class ListManager {

	ArrayList<Node> currentList;
	int index;
	int cells;
	int buttons;
	
	/* #############################################################################
	 * CONSTRUCTORS
	 * #############################################################################*/

	public ListManager() {
	}

	public ListManager(int c, int b) {
		//Constructor for List manager given cell and button sizes as data
		this.cells = c;
		this.buttons = b;
		currentList = new ArrayList<Node>();
		Node root = new Node("#HEAD", "cell " + cells + "\nbutton " + buttons);
		currentList.add(root);
		index = 0;
	}

	private Node getNode() {
		return currentList.get(index);
	}
	
	/* #############################################################################
	 * MUTATORS
	 * #############################################################################*/
	
	
	public void addNext(String k, String d) {
		//Cannot AddNext on JUNCTIONS
		if(getNode().getKeyPhrase().equals("#JUNCTION")) {
			System.out.println("Error: Can Not add nodes on JUNCTIONS! You are currently on a JUNCTION.");
			return;
		}
		
		// Creates node next to current position. Index increases to that node!
		currentList.add(index + 1, new Node(k, d));
		index++;
		printString("Created node:");
	}

	public void remove() {
		//Removes node as long as it is not a JUNCTION or BUTTON
		if(!getNode().keyPhrase.equals("#JUNCTION") && !getNode().keyPhrase.equals("#BUTTON")) {
			currentList.remove(index);
		}
	}

	/*
	 * Not using this yet because it might add confusion. 
	 * public void addLast(String k, String d) { currentList.add(new Node(k,d));
	 * System.out.println("Created Node: " +
	 * currentList.get(currentList.size()-1).getKeyPhrase() + " " +
	 * currentList.get(currentList.size()-1).getData()); }
	 */
	
	/* #############################################################################
	 * NAVIGATION
	 * #############################################################################*/
	
	public void next() {
		//Handles branching from JUNCTION
		if (getNode().keyPhrase.equals("#JUNCTION")) {
			junctionInput();
			return;
		}
		//Check index is valid
		if (index + 1 > currentList.size() - 1) {
			System.out.println("End of List!");
			return;
		}
	
		
		
		
		index++;
		
		if(getNode().keyPhrase.equals("/~skip:NEXTT")) {
			currentList = getNode().nextList;
			index = 0;
			return;
		}
		
		if (getNode().keyPhrase.equals("#JUNCTION")) {
			System.out.println("You are currently on a junction, hit next again to choose your path.");
		}
		
		printString("Switched to node(next):");
		return;
	}

	public void prev() {	
		//Handles moving from BUTTON back to JUNCTION
		if (getNode().keyPhrase.equals("#BUTTON")) {
			currentList = getNode().prevList;
			index = currentList.size() - 1;
			printString("Switched from button to node:");
			if (getNode().keyPhrase.equals("#JUNCTION")) {
				System.out.println("You are currently on a junction, hit next again to choose your path.");
			}
			return;
		}
		
		if(getNode().keyPhrase.equals("/~NEXTT")) {
			currentList = getNode().prevList;
			index = currentList.size();
		}
		
		//check if index is valid
		if (index - 1 < 0) {
			System.out.println("Already at root node!");
			return;
		}
		index--;
		printString("Switched to node(prev):");
		
		return;
	}

	public String getKeyPhrase() {
		return getNode().getKeyPhrase();
	}

	public String getData() {
		return getNode().getData();
	}
	
	/* #############################################################################
	 * JUNCTION
	 * #############################################################################*/

	public void createJunction(String[] s) {
		// takes an input of strings for button choices. Order of string array matters! Each element corresponds to a button!
		
		//Only allow JUNCTION creation on the last Node
		if (index != currentList.size() - 1) {
			System.out.println("Can't create JUNCTION unless you are on the last node!");
			
			return;
		}
		
		//Limit JUNCTION creation when inside a button branch.
		if (currentList.get(0).getKeyPhrase().equals("#BUTTON")) {
			System.out.println("You cannot create another junction within a button branch!");
			return;
		}
		
		//Check if there are enough buttons
		if (s.length > buttons) {
			throw new IndexOutOfBoundsException("There are more branches than the buttons set for this scenario!");
		}
		
		//NEXTT creation
		ArrayList<Node> nextt = new ArrayList<Node>();
		Node nexttHead = Node.prev("/~NEXTT", "" , currentList);
		nextt.add(nexttHead);
		

		//JUNCTION Creation.
		System.out.println("Creating junction...");
		ArrayList<ArrayList<Node>> buttons = new ArrayList<ArrayList<Node>>();
		for (String name : s) {
			Node buttonTail = Node.next(nextt);
			if (name != null) {
				ArrayList<Node> newList = new ArrayList<Node>();
				Node buttonHead = Node.prev(name, currentList);
				newList.add(buttonHead);
				newList.add(buttonTail);
				buttons.add(newList);
			} else if (name == null){
				buttons.add(null);
			}
		}
		currentList.add(index + 1, new Node(buttons));
		index++;
		
	}
	
	public void junctionGoto(int n) {
		currentList = getNode().getButtons().get(n);
		index = 0;

		printString("Switched from Junction to node:");
	}
	
	//Choosing branch when inside JUNCTION
	private void junctionInput() {
		//Choose the next branch
		Scanner reader = new Scanner(System.in);  // Reading from System.in
		System.out.println("At a JUNCTION. Enter a button number: ");
		int n = reader.nextInt(); // Scans the next token of the input as an int.
		currentList = getNode().getButtons().get(n);
		index = 0;

		printString("Switched from Junction to node:");
	}
	
	
	
	
	
	
	
	
	
	/* #############################################################################
	 * TESTING STUFF
	 * #############################################################################*/
	
	
	//This is for console testing
	public void printString() {
		System.out.println("CURRENT Node: " + getNode().getKeyPhrase() + " "+ '"' + getNode().getData() + '"'); 
	}
	public void printString(String s) {
		System.out.println(s + " " + getNode().getKeyPhrase() + " "+ '"' + getNode().getData() + '"'); 
	}
	
	//TEMPORARY. Delete this later...
	public static void main(String[] args) {
		ListManager derp = new ListManager(3, 6);
		

		/*
		System.out.println(derp.getKeyPhrase());
		System.out.println(derp.getData());
		derp.addNext("/~sound", "wooop.wav");
		derp.addNext("/~delay", "1");
		derp.addNext("#TEXT", "You enter a mage's castle and wear a wizard's hat.");

		derp.prev();
		derp.prev();
		derp.prev();
		derp.prev();
		derp.prev();
		derp.prev();
		derp.next();
		derp.next();
		derp.next();
		derp.next();

		ArrayList<String> poop = new ArrayList<String>();
		poop.add("one");
		poop.add(null);
		poop.add("three");
		System.out.println(poop);
		*/
		String[] s = {"apple", "banana", "chocolate"};
		
		derp.addNext("#TEXT", "this is under the root.");
		derp.createJunction(s);
		derp.printString();
		derp.next();
		derp.addNext("#TEXT", "This is inside a branch");
		derp.next();
		derp.next();
		derp.printString();
		derp.prev();
		derp.prev();
		derp.next();
		derp.next();
		derp.next();
		derp.next();
	}

}
