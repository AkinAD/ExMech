package enamel;

import java.util.ArrayList;

public class ListManager {

	ArrayList<Node> currentList;
	int index;

	int cells;
	int buttons;

	public ListManager() {
	}

	public ListManager(int c, int b) {
		this.cells = c;
		this.buttons = b;
		// constructs default ROOT node given cell and button sizes as data.
		currentList = new ArrayList<Node>();
		Node root = new Node("#HEAD", "cell " + cells + "\nbutton " + buttons);
		currentList.add(root);
		index = 0;
	}

	public void addNext(String k, String d) {
		// Creates node next to current position. SWITCHES TO NODE!
		currentList.add(index + 1, new Node(k, d));
		index++;
		System.out.println(
				"Created Node: " + currentList.get(index).getKeyPhrase() + " " + currentList.get(index).getData());
	}

	/*
	 * Not using this yet because it might add confusion. public void addLast(String
	 * k, String d) { currentList.add(new Node(k,d));
	 * System.out.println("Created Node: " +
	 * currentList.get(currentList.size()-1).getKeyPhrase() + " " +
	 * currentList.get(currentList.size()-1).getData()); }
	 */

	public void remove() {

	}

	public void next() {
		if (index + 1 > currentList.size() - 1) {
			System.out.println("End of List!");
			return;
		}

		if (currentList.get(index + 1).keyPhrase.equals("#JUNCTION")) {
			// redirect to next list
			System.out.println("start junction stuff");
			return;
		}

		index++;
		System.out.println("Switched to Node(next): " + currentList.get(index).getKeyPhrase() + " "
				+ currentList.get(index).getData());
		return;
	}

	public String prev() {
		if (index - 1 < 0) {
			System.out.println("Already at root node!");
			return "END OF LIST!";
		}

		if (currentList.get(index - 1).keyPhrase.equals("#JUNCTION")) {
			// redirect to next list
			return "doing the junction things.";
		}

		index--;
		System.out.println("Switched to Node(prev): " + currentList.get(index).getKeyPhrase() + " "
				+ currentList.get(index).getData());
		return "Switched to Prev Node";
	}

	public String getKeyPhrase() {
		return currentList.get(index).getKeyPhrase();
	}

	public String getData() {
		return currentList.get(index).getData();
	}

	public void createJunction(String[] s) {
		// takes an input of strings for button choices. NULL means button does nothing.
		if (currentList.get(0).getKeyPhrase().equals("#BUTTON")) {
			System.out.println("You cannot create another junction within a button branch!");
			return;
		}

		if (s.length > buttons) {
			throw new IndexOutOfBoundsException("There are more branches than the buttons set for this scenario!");
		}

		System.out.println("Creating junction...");
		ArrayList<ArrayList<Node>> buttons = new ArrayList<ArrayList<Node>>();
		for (String name : s) {
			
			if (name != null) {
				ArrayList<Node> newList = new ArrayList<Node>();
				Node buttonHead = new Node(name, currentList);
				newList.add(buttonHead);
				buttons.add(newList);
			} else if (name == null){
				buttons.add(null);
			}
		}
		currentList.add(index + 1, new Node(buttons));
	}

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
		String[] s = {"apple", "banana",null, "chocolate"};
		
		derp.addNext("/~pause", "3");
		derp.createJunction(s);
		System.out.println(derp.getKeyPhrase());
		derp.next();
	}

}
