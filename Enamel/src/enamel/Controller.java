package enamel;

import java.awt.GridLayout;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Scanner;

import javax.swing.JCheckBox;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.text.BadLocationException;
import org.apache.commons.io.FilenameUtils;

public class Controller {

	ListManager derp;
	Node node;
	View view;

	int highlightPosition;

	public Controller(View view) {

		// default data structure initialized. SET the default cells and buttons here.
		this.derp = new ListManager(10, 10);
		this.node = new Node();
		this.view = view;

		this.highlightPosition = 0;
	}

	public void initTestList() {
		derp = new ListManager(3, 6);
		derp.addNext("#TEXT", "this is under the root.");
		derp.addNext("#TEXT", "one");
		derp.addNext("#TEXT", "two");
		derp.addNext("#TEXT", "three");
		derp.addNext("#TEXT", "four");
		derp.addNext("#TEXT", "five");
		derp.addNext("#TEXT", "six");
		derp.addNext("#TEXT", "seven");
		derp.addNext("/~pause:", "HALT"); // Akin
		
		ArrayList<String> stuff = new ArrayList<String>();
		stuff.add(0,"Apple");
		stuff.add(1,"Banana");
		stuff.add(2,"Chocolate");
		
		derp.createJunction(stuff);
	}

	public void initializeList() {
		displayList();
		derp.goHome();
		view.currentNode.setText("Current Position: " + derp.getKeyPhrase() + " " + '"' + derp.getData() + '"');
		view.labeltop.setHorizontalAlignment(JLabel.CENTER);
		view.panel_1B.add(view.labeltop);
		for (int i = 0; i < view.label.length; i++) {
			view.label[i].setHorizontalAlignment(JLabel.CENTER);
			view.panel_1B.add(view.label[i]);
		}
		view.labelbottom.setHorizontalAlignment(JLabel.CENTER);
		view.panel_1B.add(view.labelbottom);
		derp.goHome();
		updateLabels();
	}
	
	public void setHighlightPos(int highlightPosition) {
		this.highlightPosition = highlightPosition;
		for (int k = 0; k < view.label.length; k++) {
			view.label[k].setBorder(view.empty);
		}
		view.label[highlightPosition].setBorder(view.bevel);
	}

	public void updateLabels() {
		view.currentNode.setText("Current Position: " + derp.getKeyPhrase() + " " + '"' + derp.getData() + '"' + " "
				+ highlightPosition);
		for (int k = 0; k < view.label.length; k++) {
			if (view.label[k].getBorder() == view.bevel) {
				highlightPosition = k;
			}
		}
		if(derp.index==0) {
			view.labeltop.setText(derp.getData(-1));
			view.label[0].setText(derp.getData());
			view.label[1].setText(derp.getData(1));
			view.label[2].setText(derp.getData(2));
			view.labelbottom.setText(derp.getData(3));
			setHighlightPos(0);
			return;
		}
		if (highlightPosition == 0) {
			view.labeltop.setText(derp.getData(-1));
			view.label[0].setText(derp.getData());
			view.label[1].setText(derp.getData(1));
			view.label[2].setText(derp.getData(2));
			view.labelbottom.setText(derp.getData(3));
			return;
		}
		if (highlightPosition == 1) {
			view.labeltop.setText(derp.getData(-2));
			view.label[0].setText(derp.getData(-1));
			view.label[1].setText(derp.getData());
			view.label[2].setText(derp.getData(1));
			view.labelbottom.setText(derp.getData(2));
			return;
		}
		if (highlightPosition == 2) {
			view.labeltop.setText(derp.getData(-3));
			view.label[0].setText(derp.getData(-2));
			view.label[1].setText(derp.getData(-1));
			view.label[2].setText(derp.getData(0));
			view.labelbottom.setText(derp.getData(1));
			return;
		}

	}

	public void displayList() {
		int currentListPos = derp.index;
		derp.goHome();
		view.textArea.setText("");
		for (int i = 0; i < listSize() - 1; i++) {
			view.textArea.append(derp.getData() + "\n");
			derp.next();
		}
		for (int j = listSize(); j > currentListPos; j--) {
			derp.prev();
		}
	}

	public void newMenuItem() {
		view.textArea.setText("");
	}

	public void openMenuItem() {
		view.textArea.setText("");
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
					file = new File(file.toString() + ".txt"); // append .txt if "foo.jpg.txt" is OK
					file = new File(file.getParentFile(), FilenameUtils.getBaseName(file.getName()) + ".txt");
					BufferedWriter bf = new BufferedWriter(new FileWriter(file.getPath()));
					
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
		//check special cases
		if(derp.getKeyPhrase().equals("#JUNCTION")) {
			chooseButton();
			return;
		}
		if(derp.getKeyPhrase().equals("/~skip:NEXTT")) {
			derp.next();
			updateLabels();
			return;
		}
		
		
		//check if end of list
		if(derp.getKeyPhrase(1)==null) {
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
					view.currentNode
							.setText("Current Position: " + derp.getKeyPhrase() + " " + '"' + derp.getData() + '"');
				
			}
		}
	}

	public void prevButton() {
		//Special cases
		if(derp.getKeyPhrase().equals("#BUTTON")) {
			derp.prev();
			setHighlightPos(2);
			updateLabels();
			return;
		}
		if(derp.getKeyPhrase().equals("#BUTTON")) {
			derp.prev();
			setHighlightPos(1);
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
	}

	public void branchItButton() {
		if (derp.index == listSize() - 1) {
			ArrayList<String> buttonsNames = new ArrayList<String>();
			JPanel p = new JPanel(new GridLayout(0, 2));

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
						if (e.getStateChange() == ItemEvent.SELECTED) {// checkbox has been selected
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
				int result = JOptionPane.showConfirmDialog(null, p, "My custom dialog", JOptionPane.PLAIN_MESSAGE);
				if (result == JOptionPane.OK_OPTION) {

					// add textFields to buttonsNames
					for (int i = 0; i < derp.buttons; i++) {
						if (checkBoxes.get(i).isSelected()) {
							buttonsNames.add(i, textFields.get(i).getText());
						}
					}
					// check if names are unique
					for (String s : buttonsNames) {
						int count = Collections.frequency(buttonsNames, s);
						System.out.println(count);
						if (count != 1 || s.isEmpty()) {
							JOptionPane.showMessageDialog(p, "Button names must be unique!", "Error",
									JOptionPane.WARNING_MESSAGE);
							break;
						} else {
							// Can now continue after verifications
							skip = true;
						}
					}

				} else {
					System.out.println("User Cancelled createJunction");
					return;
				}
			}
			// create the junction
			derp.createJunction(buttonsNames);
			view.currentNode.setText("Current Position: " + derp.getKeyPhrase() + " " + '"' + derp.getData() + '"');
			// navigate
			chooseButton();
		} else {
			System.out.println("ERROR: Can only create junction at the end of list!");
		}
	}

	public void chooseButton() {
		if (derp.getKeyPhrase().equals("#JUNCTION")) {
			// Get the buttonNames and create a dialog box to choose where to navigate to
			ArrayList<String> buttons = derp.getNode().buttonsNames;
			int i = JOptionPane.showOptionDialog(null, "Choose which branch to go to:", "Choose Button",
					JOptionPane.PLAIN_MESSAGE, 0, null, buttons.toArray(), buttons.toArray()[0]);
			// Goto the selected branch based on the button press
			String s = buttons.toArray()[i].toString();
			derp.junctionGoto(derp.junctionSearch(s));

			updateLabels();

			view.currentNode.setText("Current Position: " + derp.getKeyPhrase() + " " + '"' + derp.getData() + '"');
			return;
		}
	}

	public void addTextButton() {
		if (view.textArea.getText().isEmpty()) {
			JOptionPane.showMessageDialog(view.frame, "Type something in the text box before add.", "Inane error",
					JOptionPane.ERROR_MESSAGE);
			return;
		}
		derp.addNext("#TEXT", view.textArea.getText());
		view.currentNode.setText("Current Position: " + derp.getKeyPhrase() + " " + '"' + derp.getData() + '"');
	}

	private int listSize() {
		return derp.currentList.size();
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

}
