package enamel;

import java.awt.GridLayout;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import javax.swing.JCheckBox;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.JTextField;
import javax.swing.SpinnerNumberModel;
import org.apache.commons.io.FilenameUtils;

public class Controller {

	static ListManager derp;
	View view;

	static String AudioFile = null;
	static Boolean sOpen;

	int highlightPosition;

	int cells;
	int buttons;

	public Controller(View view) {

		// default data structure initialized. SET the default cells and buttons
		// here.
		this.derp = new ListManager(10, 10);
		this.view = view;
		this.highlightPosition = 0;
		this.cells = 0;
		this.buttons = 0;
	}

	public void initDefaultList(int cells, int buttons) {
		derp = new ListManager(cells, buttons);
		updateLabels();
	}

	// public void initTestList() {
	// derp = new ListManager(3, 6);
	// derp.addNext("#TEXT", "this is under the root.");
	// derp.addNext("#TEXT", "one");
	// derp.addNext("#TEXT", "two");
	// derp.addNext("/~pause:", "HALT"); // Akin
	//
	// HashMap<Integer,String> stuff = new HashMap<Integer,String>();
	// stuff.put(0,"Apple");
	// stuff.put(1,"Banana");
	// stuff.put(2,"Chocolate");
	//
	// derp.createJunction(stuff);
	// }

	public void initializeList() {
		derp.goHome();
		view.currentNode.setText("Current Position: " + derp.getKeyPhrase() + " " + '"' + derp.getData() + '"');
		view.labeltop.setHorizontalAlignment(JLabel.CENTER);
		view.navigationPanel.add(view.labeltop);
		for (int i = 0; i < view.label.length; i++) {
			view.label[i].setHorizontalAlignment(JLabel.CENTER);
			view.navigationPanel.add(view.label[i]);
		}
		view.labelbottom.setHorizontalAlignment(JLabel.CENTER);
		view.navigationPanel.add(view.labelbottom);
		derp.goHome();
		updateLabels();
	}

	public void welcomeScreen() {
		boolean skip = false;
		while (skip == false) {

			// Custom button text
			Object[] options = { "New Story", "Load Existing", "Quit" };
			JLabel lbl = new JLabel("Welcome to Treasure Box Braille!");
			// lbl.setFocusable(true);
			lbl.addAncestorListener(new RequestFocusListener());
			int n = JOptionPane.showOptionDialog(view.frame, lbl, "Treasure Box Braille",
					JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.DEFAULT_OPTION, null, options, options[0]);

			System.out.println("User selected: " + n);

			if (n == 2 || n == JOptionPane.CLOSED_OPTION) {
				// Close program if 'Quit' or X button
				view.close();
				skip = true;
				break;
			}

			if (n == 0) {
				// Create new story
				JPanel p = new JPanel(new GridLayout(3, 2));
				JLabel msg = new JLabel("Select the number of cells and buttons on your device.");
				msg.addAncestorListener(new RequestFocusListener());
				p.add(msg);
				p.add(new JLabel());
				SpinnerNumberModel model1 = new SpinnerNumberModel();
				model1.setValue(1);
				model1.setMaximum(99);
				model1.setMinimum(1);
				SpinnerNumberModel model2 = new SpinnerNumberModel();
				model2.setValue(1);
				model2.setMaximum(99);
				model2.setMinimum(1);
				JSpinner spinner1 = new JSpinner(model1);
				spinner1.setToolTipText(
						"Cells. Use the up & down arrow keys to set the number of cells for the scenario.");
				spinner1.getAccessibleContext().setAccessibleName("Cells");
				spinner1.getAccessibleContext().setAccessibleDescription(
						"Cells. Use the up & down arrow keys to set the number of cells for the scenario.");
				JSpinner spinner2 = new JSpinner(model2);
				spinner2.setToolTipText(
						"Buttons. Use the up & down arrow keys to set the number of buttons for the scenario.\"");
				spinner2.getAccessibleContext().setAccessibleName("Buttons");
				spinner2.getAccessibleContext().setAccessibleDescription(
						"Buttons. Use the up & down arrow keys to set the number of buttons for the scenario.");
				spinner1.setEditor(new JSpinner.DefaultEditor(spinner1));
				spinner2.setEditor(new JSpinner.DefaultEditor(spinner2));
				JLabel cellsLabel = new JLabel("Cells: ");
				cellsLabel.setLabelFor(spinner1);
				p.add(cellsLabel);
				p.add(spinner1);
				JLabel buttonsLabel = new JLabel("Buttons: ");
				buttonsLabel.setLabelFor(spinner2);
				p.add(buttonsLabel);
				p.add(spinner2);

				
				int result = JOptionPane.showConfirmDialog(null, p, "Create New Story", JOptionPane.PLAIN_MESSAGE);
				System.out.println("User selected2: " + result);
				// return back to welcome screen if X button
				if (result == -1) {
					skip = false;
					// break;
				} else {

					this.cells = Integer.parseInt(spinner1.getValue().toString());
					this.buttons = Integer.parseInt(spinner2.getValue().toString());

					initDefaultList(cells, buttons);
					skip = true;
				}
			}

			if (n == 1) {
				JFileChooser open = new JFileChooser("FactoryScenarios/");
				int retrunVal = open.showOpenDialog(view.frame);
				System.out.println(retrunVal);
				if (retrunVal == JFileChooser.CANCEL_OPTION || retrunVal == -1) {
					skip = false;
					//break;
				}
				if (retrunVal == JFileChooser.APPROVE_OPTION) {
					LoadParser parser = new LoadParser();
					derp = parser.fromText(open.getSelectedFile().getAbsolutePath());
					derp.goHome();
					updateLabels();
					skip = true;
					break;
				}
				
			}

		}
	}

	public void setHighlightPos(int highlightPosition) {
		this.highlightPosition = highlightPosition;
		for (int k = 0; k < view.label.length; k++) {
			view.label[k].setBorder(view.empty);
		}
		view.label[highlightPosition].setBorder(view.compound1);
	}

	public void updateLabels() {
		focusCurrentPosition();
		
		view.currentNode.setText("Current Position: " + derp.getKeyPhrase() + " " + '"' + derp.getData() + '"' + " "
				+ "  Index: " + derp.index + "/" + (listSize() - 1));
		for (int k = 0; k < view.label.length; k++) {
			if (view.label[k].getBorder() == view.bevel) {
				highlightPosition = k;
			}
		}
		if (derp.index == 0) {
			view.labeltop.setText(derp.getLabel(-1));
			view.label[0].setText(derp.getLabel(0));
			view.label[1].setText(derp.getLabel(1));
			view.label[2].setText(derp.getLabel(2));
			view.labelbottom.setText(derp.getLabel(3));
			setHighlightPos(0);
			return;
		}
		if (highlightPosition == 0) {
			view.labeltop.setText(derp.getLabel(-1));
			view.label[0].setText(derp.getLabel());
			view.label[1].setText(derp.getLabel(1));
			view.label[2].setText(derp.getLabel(2));
			view.labelbottom.setText(derp.getLabel(3));
			return;
		}
		if (highlightPosition == 1) {
			view.labeltop.setText(derp.getLabel(-2));
			view.label[0].setText(derp.getLabel(-1));
			view.label[1].setText(derp.getLabel());
			view.label[2].setText(derp.getLabel(1));
			view.labelbottom.setText(derp.getLabel(2));
			return;
		}
		if (highlightPosition == 2) {
			view.labeltop.setText(derp.getLabel(-3));
			view.label[0].setText(derp.getLabel(-2));
			view.label[1].setText(derp.getLabel(-1));
			view.label[2].setText(derp.getLabel(0));
			view.labelbottom.setText(derp.getLabel(1));
			return;
		}
	}

	// public void displayList() {
	// int currentListPos = derp.index;
	// derp.goHome();
	// view.textArea.setText("");
	// for (int i = 0; i < listSize() - 1; i++) {
	// view.textArea.append(derp.getData() + "\n");
	// derp.next();
	// }
	// for (int j = listSize(); j > currentListPos; j--) {
	// derp.prev();
	// }
	// }

	public void newMenuItem() {
		// Create new story
		JPanel p = new JPanel(new GridLayout(3, 2));
		JLabel msg = new JLabel("Select the number of cells and buttons on your device.");
		msg.addAncestorListener(new RequestFocusListener());
		p.add(msg);
		p.add(new JLabel());
		SpinnerNumberModel model1 = new SpinnerNumberModel();
		model1.setValue(1);
		model1.setMaximum(99);
		model1.setMinimum(1);
		SpinnerNumberModel model2 = new SpinnerNumberModel();
		model2.setValue(1);
		model2.setMaximum(99);
		model2.setMinimum(1);
		JSpinner spinner1 = new JSpinner(model1);
		spinner1.setToolTipText(
				"Cells. Use the up & down arrow keys to set the number of cells for the scenario.");
		spinner1.getAccessibleContext().setAccessibleName("Cells");
		spinner1.getAccessibleContext().setAccessibleDescription(
				"Cells. Use the up & down arrow keys to set the number of cells for the scenario.");
		JSpinner spinner2 = new JSpinner(model2);
		spinner2.setToolTipText(
				"Buttons. Use the up & down arrow keys to set the number of buttons for the scenario.\"");
		spinner2.getAccessibleContext().setAccessibleName("Buttons");
		spinner2.getAccessibleContext().setAccessibleDescription(
				"Buttons. Use the up & down arrow keys to set the number of buttons for the scenario.");
		spinner1.setEditor(new JSpinner.DefaultEditor(spinner1));
		spinner2.setEditor(new JSpinner.DefaultEditor(spinner2));
		JLabel cellsLabel = new JLabel("Cells: ");
		cellsLabel.setLabelFor(spinner1);
		p.add(cellsLabel);
		p.add(spinner1);
		JLabel buttonsLabel = new JLabel("Buttons: ");
		buttonsLabel.setLabelFor(spinner2);
		p.add(buttonsLabel);
		p.add(spinner2);

		
		int result = JOptionPane.showConfirmDialog(null, p, "Create New Story", JOptionPane.PLAIN_MESSAGE);
		System.out.println("User selected2: " + result);
		// return back to welcome screen if X button
		if (result == -1) {
			// break;
		} else {

			this.cells = Integer.parseInt(spinner1.getValue().toString());
			this.buttons = Integer.parseInt(spinner2.getValue().toString());

			initDefaultList(cells, buttons);

		}
	}

	public void openMenuItem() {
		JFileChooser open = new JFileChooser("FactoryScenarios/");
		int retrunVal = open.showOpenDialog(view.frame);
		if (retrunVal == JFileChooser.APPROVE_OPTION) {
			LoadParser parser = new LoadParser();
			derp = parser.fromText(open.getSelectedFile().getAbsolutePath());
			derp.goHome();
			updateLabels();
		}
	}

	public void saveMenuItem() {
		JFileChooser save = new JFileChooser("FactoryScenarios/");

		int retrunVal = save.showSaveDialog(view.frame);
		if (retrunVal == JFileChooser.APPROVE_OPTION) {
			try {
				File file = save.getSelectedFile();

				if (FilenameUtils.getExtension(file.getName()).equalsIgnoreCase("txt")) {
					// filename is OK as-is
				} else {
					file = new File(file.toString() + ".txt"); // append .txt if
																// "foo.jpg.txt"
																// is OK
					file = new File(file.getParentFile(), FilenameUtils.getBaseName(file.getName()) + ".txt");
					BufferedWriter bf = new BufferedWriter(new FileWriter(file.getPath(), false));

					ScenarioComposer composer = new ScenarioComposer();
					String result = composer.returnStringFile(derp);
					bf.write(result);
					bf.close();
				}
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		}
	}

	public void nextButton() {
		// check special cases
		if (derp.getKeyPhrase().equals("#JUNCTION")) {
			chooseButton();
			return;
		}
		if (derp.getKeyPhrase().equals("/~skip:NEXTT")) {
			derp.next();
			updateLabels();
			return;
		}

		// check if end of list
		if (derp.index + 1 > derp.currentList.size() - 1) {
			System.out.println("End of list (next node is null).");
			return;
		}
		
		for (int k = 0; k < view.label.length; k++) {
			if (view.label[k].getBorder() == view.bevel) {
				highlightPosition = k;
			}
		}
		if (highlightPosition < view.label.length - 1) {
			highlightPosition++;
			setHighlightPos(highlightPosition);
			// index++;
			derp.next();
			String text = view.label[highlightPosition].getText();
			System.out.println("Positon: " + highlightPosition + ", Data Type: " + derp.getData() + ", Index: "
					+ derp.index + "/" + (listSize() - 1));
			view.currentNode.setText("Current Position: " + derp.getKeyPhrase() + " " + '"' + derp.getData() + '"');
		} else {
			if (listSize() > view.label.length) {
				derp.next();
				updateLabels();

				String text = view.label[highlightPosition].getText();
				System.out.println("Positon: " + highlightPosition + ", Data Type: " + derp.getData() + ", Index: "
						+ derp.index + "/" + (listSize() - 1));
				view.currentNode.setText("Current Position: " + derp.getKeyPhrase() + " " + '"' + derp.getData() + '"');

			}
		}
		updateLabels();
	}

	public void prevButton() {
		// Special cases
		if (derp.getKeyPhrase().equals("#BUTTON")) {
			derp.prev();
			setHighlightPos(2);
			updateLabels();
			return;
		}
		if (derp.getKeyPhrase().equals("/~NEXTT")) {
			derp.prev();
			setHighlightPos(2);
			updateLabels();
			return;
		}

		for (int k = 0; k < view.label.length; k++) {
			if (view.label[k].getBorder() == view.bevel) {
				highlightPosition = k;
			}
		}
		if (highlightPosition > 0) {
			highlightPosition--;
			setHighlightPos(highlightPosition);
			// index--;
			derp.prev();
			String text = view.label[highlightPosition].getText();
			System.out.println("Positon: " + highlightPosition + ", Data Type: " + derp.getData() + ", Index: "
					+ derp.index + "/" + (listSize() - 1));
			view.currentNode.setText("Current Position: " + derp.getKeyPhrase() + " " + '"' + derp.getData() + '"');
		} else {

			derp.prev();
			updateLabels();
			String text = view.label[highlightPosition].getText();
			System.out.println("Positon: " + highlightPosition + ", Data Type: " + derp.getData() + ", Index: "
					+ derp.index + "/" + (listSize() - 1));
			view.currentNode.setText("Current Position: " + derp.getKeyPhrase() + " " + '"' + derp.getData() + '"');

		}
		updateLabels();
	}

	public void branchItButton() {
		if (derp.index == listSize() - 1) {
			HashMap<Integer, String> buttonsNames = new HashMap<Integer, String>();
			JPanel p = new JPanel(new GridLayout(0, 2));
			JLabel text = new JLabel("<html>Type a name for each answer you wish to create. <br> Use the checkbox to enable the answer and type a name.</html>");
			text.addAncestorListener(new RequestFocusListener());
			p.add(text);
			p.add(new JLabel());
			

			// create checkboxes based on number of buttons
			ArrayList<JCheckBox> checkBoxes = new ArrayList<JCheckBox>();
			for (int i = 0; i < derp.buttons; i++) {
				checkBoxes.add(new JCheckBox("Button " + i, false));
			}

			// create textFields based on number of buttons
			ArrayList<JTextField> textFields = new ArrayList<JTextField>();
			for (int i = 0; i < derp.buttons; i++) {
				textFields.add(new JTextField());
			}

			// add items to panel
			for (int i = 0; i < derp.buttons; i++) {
				p.add(checkBoxes.get(i));
				textFields.get(i).setVisible(false);
				p.add(textFields.get(i));
			}

			// add listener to checkBoxes
			for (int i = 0; i < derp.buttons; i++) {
				final int x = i;
				checkBoxes.get(x).addItemListener(new ItemListener() {
					@Override
					public void itemStateChanged(ItemEvent e) {
						if (e.getStateChange() == ItemEvent.SELECTED) {// checkbox
																		// has
																		// been
																		// selected
							textFields.get(x).setVisible(true);
							p.revalidate();
							p.repaint();
						} else {// checkbox has been deselected
							textFields.get(x).setVisible(false);
							p.revalidate();
							p.repaint();
						}
						;
					}
				});
			}

			// show dialog
			boolean skip = false;
			while (skip == false) {
				int result = JOptionPane.showConfirmDialog(null, p, "Create Question", JOptionPane.PLAIN_MESSAGE);
				if (result == JOptionPane.OK_OPTION) {
					// add textFields to buttonsNames
					for (int i = 0; i < derp.buttons; i++) {
						buttonsNames.put(i, textFields.get(i).getText());
						if (!checkBoxes.get(i).isSelected()) {
							buttonsNames.put(i, null);
						}
					}
					// convoluted way of removing null entries, but it works...
					ArrayList<String> poop = new ArrayList<String>();
					poop.add(null);
					buttonsNames.values().removeAll(poop);

					System.out.println(buttonsNames.toString());

					// check if names are unique
					for (String s : buttonsNames.values()) {
						if (s != null && !s.isEmpty()) {
							int count = Collections.frequency(buttonsNames.values(), s);
							// System.out.println("buttonsNames frequency: " +
							// count);
							if (count != 1) {
								buttonsNames.clear();
								JOptionPane.showMessageDialog(p, "Button names must be unique!", "Error",
										JOptionPane.WARNING_MESSAGE);
								break;
							} else {
								// Can now continue after verifications
								skip = true;
							}
						}
					}

				} else {
					System.out.println("User Cancelled createJunction");
					return;
				}
			}
			if (buttonsNames.isEmpty()) {
				JOptionPane.showMessageDialog(p, "Create Junction Failed! buttonsNames is empty!", "Error",
						JOptionPane.WARNING_MESSAGE);
				return;
			}

			derp.createJunction(buttonsNames);
			view.currentNode.setText("Current Position: " + derp.getKeyPhrase() + " " + '"' + derp.getData() + '"');
			// navigate
			chooseButton();
		} else {
			infoBox("Error: You can only add a user input at the end of the list. Move to the end of the list and try again.",
					"Can not add user input here.");
			System.out.println("ERROR: Can only create junction at the end of list!");
		}
	}

	public void chooseButton() {
		if (derp.getKeyPhrase().equals("#JUNCTION")) {
			// Get the buttonNames and create a dialog box to choose where to
			// navigate to
			HashMap<Integer, String> buttons = derp.getNode().buttonsNames;
			int i = JOptionPane.showOptionDialog(null, "Choose which answer you would like to go to:", "Choose User-Input",
					JOptionPane.PLAIN_MESSAGE, 0, null, buttons.values().toArray(), buttons.values().toArray()[0]);
			// Goto the selected branch based on the button press
			if (i != -1) {
				String s = buttons.values().toArray()[i].toString();
				System.out.println(s);
				ArrayList<Node> currentList = derp.currentList;
				derp.junctionGoto(derp.junctionSearch(s));
				derp.getNode().prevList = currentList;
				updateLabels();

				view.currentNode.setText("Current Position: " + derp.getKeyPhrase() + " " + '"' + derp.getData() + '"');
			}
			return;
		}
	}

	public void addTextButton() {
		String text = null;
		text = JOptionPane.showInputDialog(null, "Please enter your text: ", "Add text (Please enter your text) ", -1);
		if (text != null && !text.trim().isEmpty()) {
			derp.addNext("#TEXT", text);
			updateLabels();
		}
	}
	
	public void addResetButtons(){
		derp.addNext("/~reset-buttons", "/~reset-buttons");
		updateLabels();
	}
	public void addRepeat() {
		String text = null;
		text = JOptionPane.showInputDialog(null, "Please enter the text you would like repeated: ", "Add text (Please enter your text) ", -1);
		if (text != null && !text.trim().isEmpty()) {
			derp.addNext("/~repeat", text);
			derp.addNext("/~endrepeat", "");
			updateLabels();
		}
		}
	public void setRepeatButton() {		
		String index = null;
		index = JOptionPane.showInputDialog(null, "What button would you like to set as the repeat button?", "You have " + this.buttons + "buttons "
				+ " that you can set as the repeat button, enter the index of one the one you would like" , -1);
		if (index != null) {
			if (isStringInt(index) && (Integer.parseInt(index) >= 0 && Integer.parseInt(index) <= this.buttons)) {
				addRepeat();				
				derp.addNext("/~repeat-button", String.valueOf(Integer.parseInt(index) -1));
				updateLabels();			

			} else {
				infoBox("Invalid button index! Please enter a valid number", "Invalid!");
			}
		} else {
			infoBox("No Repeat button set", "Exiting");
		}
	}
	public void skipToAplace() {
		// implements the Key phrase: /~skip-button:
		// would need to be jumping to existing junctions
		//would need to check if junctions are valid
		
	}
	public void setDispStringButton() {
		String text = null;
		text = JOptionPane.showInputDialog(null, "Please enter your text: ", "Add text (Please enter your text) ", -1);
		if (text != null && !text.trim().isEmpty()) {
			derp.addNext("/~disp-string", text);
			updateLabels();
		}
	}
	

	public void setPinButton() {
		JPanel p = new JPanel(new GridLayout(2, 2));
		JLabel msg = new JLabel("Select a Braille Character location below:");
		msg.addAncestorListener(new RequestFocusListener());
		p.add(msg);
		p.add(new JLabel());
		SpinnerNumberModel model1 = new SpinnerNumberModel();
		model1.setValue(1);
		model1.setMaximum(derp.cells);
		model1.setMinimum(1);
		JSpinner spinner1 = new JSpinner(model1);
		spinner1.setToolTipText(
				"Braille Location. Use the up & down arrow keys to set the Braille Location.");
		spinner1.getAccessibleContext().setAccessibleName("Cells");
		spinner1.getAccessibleContext().setAccessibleDescription(
				"Braille Location. Use the up & down arrow keys to set the Braille Location.");
		spinner1.setEditor(new JSpinner.DefaultEditor(spinner1));
		JLabel cellsLabel = new JLabel("Braille Location: ");
		cellsLabel.setLabelFor(spinner1);
		p.add(cellsLabel);
		p.add(spinner1);

		
		int result = JOptionPane.showConfirmDialog(null, p, "Select Braille Location", JOptionPane.PLAIN_MESSAGE);
		System.out.println("User selected2: " + result);
		// return back to welcome screen if X button
		
		int location = Integer.parseInt(spinner1.getValue().toString()) - 1;

				String text = null;
				text = JOptionPane.showInputDialog(null, "Please enter a letter or 8-character sequence you would like to display on the braille character: ", "Please enter a letter or 8-character sequence you would like to display on the braille character", -1);
				
				if (text != null) {
					text = text.toLowerCase();
					if (text.length() == 1) {
						switch (text) {
						case "a":
							derp.addNext("/~disp-cell-pins", location + " " + "10000000");
							updateLabels();
							break;
						case "b":
							derp.addNext("/~disp-cell-pins", location + " " + "11000000");
							updateLabels();
							break;
						case "c":
							derp.addNext("/~disp-cell-pins", location + " " + "10100000");
							updateLabels();
							break;
						case "d":
							derp.addNext("/~disp-cell-pins", location + " " + "10011000");
							updateLabels();
							break;
						case "e":
							derp.addNext("/~disp-cell-pins", location + " " + "10001000");
							updateLabels();
							break;
						case "f":
							derp.addNext("/~disp-cell-pins", location + " " + "11010000");
							updateLabels();
							break;
						case "g":
							derp.addNext("/~disp-cell-pins", location + " " + "11011000");
							updateLabels();
							break;
						case "h":
							derp.addNext("/~disp-cell-pins", location + " " + "11001000");
							updateLabels();
							break;
						case "i":
							derp.addNext("/~disp-cell-pins", location + " " + "01010000");
							updateLabels();
							break;
						case "j":
							derp.addNext("/~disp-cell-pins", location + " " + "01011000");
							updateLabels();
							break;
						case "k":
							derp.addNext("/~disp-cell-pins", location + " " + "10100000");
							updateLabels();
							break;
						case "l":
							derp.addNext("/~disp-cell-pins", location + " " + "11100000");
							updateLabels();
							break;
						case "m":
							derp.addNext("/~disp-cell-pins::", location + " " + "10110000");
							updateLabels();
							break;
						case "n":
							derp.addNext("/~disp-cell-pins", location + " " + "10111000");
							updateLabels();
							break;
						case "o":
							derp.addNext("/~disp-cell-pins", location + " " + "10101000");
							updateLabels();
							break;
						case "p":
							derp.addNext("/~disp-cell-pins", location + " " + "11110000");
							updateLabels();
							break;
						case "q":
							derp.addNext("/~disp-cell-pins", location + " " + "11111000");
							updateLabels();
							break;
						case "r":
							derp.addNext("/~disp-cell-pins", location + " " + "11101000");
							updateLabels();
							break;
						case "s":
							derp.addNext("/~disp-cell-pins", location + " " + "01110000");
							updateLabels();
							break;
						case "t":
							derp.addNext("/~disp-cell-pins", location + " " + "01111000");
							updateLabels();
							break;
						case "u":
							derp.addNext("/~disp-cell-pins", location + " " + "10100100");
							updateLabels();
							break;
						case "v":
							derp.addNext("/~disp-cell-pins", location + " " + "11100100");
							updateLabels();
							break;
						case "w":
							derp.addNext("/~disp-cell-pins", location + " " + "01011100");
							updateLabels();
							break;
						case "x":
							derp.addNext("/~disp-cell-pins", location + " " + "10110100");
							updateLabels();
							break;
						case "y":
							derp.addNext("/~disp-cell-pins", location + " " + "10111100");
							updateLabels();
							break;
						case "z":
							derp.addNext("/~disp-cell-pins", location + " " + "10101100");
							updateLabels();
							break;
						case "":
							derp.addNext("/~disp-cell-pins", location + " " + "11111111");
							updateLabels();
							break;
						case " ":
							derp.addNext("/~disp-cell-pins", location + " " + "11111111");
							updateLabels();
							break;
						default:
							infoBox("Invalid character. Please enter a single letter.", "Cancel");
							break;
						}
					} else if (text.length() == 8 && text.matches("^[01]+$")) {
						derp.addNext("/~disp-cell-pins", location + " " + text);
						updateLabels();
					} else {
						infoBox("Invalid Braille character! Please enter a valid number", "Please enter a valid number!");
					}
				
			} else {
				infoBox("Invalid Braille character location number! Please enter a valid number", "Invalid Braille character number! Please enter a valid number");
			}
		
	}

	public void clrPinButton() {
		JPanel p = new JPanel(new GridLayout(2, 2));
		JLabel msg = new JLabel("Select a Braille Character below:");
		msg.addAncestorListener(new RequestFocusListener());
		p.add(msg);
		p.add(new JLabel());
		SpinnerNumberModel model1 = new SpinnerNumberModel();
		model1.setValue(1);
		model1.setMaximum(derp.cells);
		model1.setMinimum(1);
		JSpinner spinner1 = new JSpinner(model1);
		spinner1.setToolTipText(
				"Braille Location. Use the up & down arrow keys to set the Braille Location.");
		spinner1.getAccessibleContext().setAccessibleName("Cells");
		spinner1.getAccessibleContext().setAccessibleDescription(
				"Braille Location. Use the up & down arrow keys to set the Braille Location.");
		spinner1.setEditor(new JSpinner.DefaultEditor(spinner1));
		JLabel cellsLabel = new JLabel("Braille Location: ");
		cellsLabel.setLabelFor(spinner1);
		p.add(cellsLabel);
		p.add(spinner1);

		
		int result = JOptionPane.showConfirmDialog(null, p, "Select Braille Location", JOptionPane.PLAIN_MESSAGE);
		System.out.println("User selected2: " + result);
		// return back to welcome screen if X button
		
		String location = spinner1.getValue().toString();
				derp.addNext("/~disp-cell-clear", location);
				updateLabels();
			
		
	}

	public void removeButton() {
		if (derp.getKeyPhrase() == "#JUNCTION") {
			Object[] options = { "Yes", "Cancel" };
			JLabel str = new JLabel("Removing user input will remove all events associated with it. This action can not be undone. Are you sure you wish to remove the user input?");
			str.addAncestorListener(new RequestFocusListener());
			int n = JOptionPane.showOptionDialog(view.frame, str,
					"Remove User Input", JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE, null, options,
					options[1]);
			if(n == 0) {
				derp.remove();
				updateLabels();
				return;
			}
			return;
		}
		if (derp.getKeyPhrase() == "#BUTTON") {
			Object[] options = { "Yes", "Cancel" };
			JLabel str = new JLabel("Removing this answer choice will remove all events associated with it. This action can not be undone. Are you sure you wish to remove the answer choice?");
			str.addAncestorListener(new RequestFocusListener());
			int n = JOptionPane.showOptionDialog(view.frame, str,
					"Remove Answer", JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE, null, options,
					options[1]);
			if(n == 0) {
				derp.remove();
				updateLabels();
				return;
			}
			return;
		}
		
		if(derp.getKeyPhrase() == "/~NEXTT" || derp.getKeyPhrase() == "/~skip:NEXTT" || derp.getKeyPhrase() == "#HEAD") {
			JOptionPane.showMessageDialog(view.frame,
				    "You cannot remove this!");
		}
		derp.remove();
		updateLabels();

	}

	private int listSize() {
		return derp.currentList.size();
	}
	
	public void soundButton() {
		JFileChooser open = new JFileChooser("FactoryScenarios/AudioFiles/");
		int retrunVal = open.showOpenDialog(view.frame);
		if (retrunVal == JFileChooser.APPROVE_OPTION) {
			derp.addNext("/~sound", open.getSelectedFile().getName());
			updateLabels();
		}
	}

	public void recordSoundButton() {
		SoundRecorder SR = new SoundRecorder(this);
		SR.frmAudio.setVisible(true);
		System.out.println("visible");
		updateLabels();

	}

	public static void setAudioFile(String Au) {
		AudioFile = Au;
		

	}

	public static void appendSound() {
		if (AudioFile != null) {
			System.out.println(AudioFile);
			derp.addNext("/~sound", AudioFile);
			
		}
	}

	public void addPauseButton() {
		String pause = null;
		pause = JOptionPane.showInputDialog(null, "Please enter pause duration: ", "Add a pause ", -1);
		if (pause != null) {
			if (isStringInt(pause)) {
				derp.addNext("/~pause", pause);
				updateLabels();

			} else {
				infoBox("Invalid pause duration! Please enter a valid number", "Invalid!");
			}
		} else {
			infoBox("Pause Cancelled", "Cancel");
		}

	}

	public boolean isStringInt(String s) {
		try {
			Integer.parseInt(s);
			return true;
		} catch (NumberFormatException ex) {
			return false;
		}
	}

	public static int optionbox(String infoMessage, String titleBar) {
		int dialogButton = JOptionPane.YES_NO_OPTION;
		int dialogResult = JOptionPane.showConfirmDialog(null, infoMessage, "InfoBox: " + titleBar, dialogButton);

		if (dialogResult == JOptionPane.YES_OPTION) {
			return 0;
		} else {
			return 1;
		}
	}

	public static void infoBox(String infoMessage, String titleBar) {
		JOptionPane.showMessageDialog(null, infoMessage, titleBar, JOptionPane.INFORMATION_MESSAGE);
	}

	public void sampleButton() {
		derp.addNext("#TEXT", "Sample");
		if (highlightPosition == 0) {
			view.label[0].setText(derp.getData(-1));
			view.label[1].setText(derp.getData());
			view.label[2].setText(derp.getData(1));
			setHighlightPos(1);
			return;
		}
		updateLabels();

		// if (view.textArea.getText().equals("")) {
		// view.textArea.append("Sample Text");
		// } else {
		// int caretOffset;
		// int lineNumber;
		// int startOffset;
		// int endOffset;
		// try {
		// caretOffset = view.textArea.getCaretPosition();
		// lineNumber = view.textArea.getLineOfOffset(caretOffset);
		// startOffset = view.textArea.getLineStartOffset(lineNumber);
		// endOffset = view.textArea.getLineEndOffset(lineNumber);
		// if (startOffset == endOffset) {
		// view.textArea.replaceRange("Sample Text", startOffset, endOffset);
		// } else {
		// view.textArea.append("\n" + "Sample Text");
		// }
		// } catch (BadLocationException e1) {
		// // TODO Auto-generated catch block
		// e1.printStackTrace();
		// }
		// }
	}

	public void simulateScenario() {
		File file = new File("FactoryScenarios/temp_" + 1 + ".txt");

		try {
			file.createNewFile();
			BufferedWriter bf = new BufferedWriter(new FileWriter(file.getPath()));
			ScenarioComposer composer = new ScenarioComposer();
			String result = composer.returnStringFile(derp);
			bf.write(result);
			bf.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		Thread starterCodeThread = new Thread("Starter Code Thread") {
			public void run() {
				ScenarioParser s = new ScenarioParser(true);
				s.setScenarioFile("FactoryScenarios/temp_" + 1 + ".txt");
			}
		};
		starterCodeThread.start();
	}

	
	public void focusCurrentPosition() {
		String currentNodeText = "Current Position is "+ derp.getLabel();
		view.navigationPanel.getAccessibleContext().setAccessibleDescription(currentNodeText);
		view.navigationPanel.requestFocusInWindow();
	}

}
