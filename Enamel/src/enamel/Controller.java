package enamel;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Arrays;
import java.util.Scanner;

import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.text.BadLocationException;
import org.apache.commons.io.FilenameUtils;


public class Controller {
	
	ListManager list;
	Node node;
	View view;
	
	int highlightPosition;
	int index;
	int listSize;
	
	public Controller(View view) {
		
		this.list = new ListManager();
		this.node = new Node();
		this.view = view;
		
		this.highlightPosition = 0;
		this.index = list.getIndex();
		this.listSize = list.derp.currentList.size();
	}
	
	public void initializeList() {
		displayList();
		list.returnToRoot(list.derp);
		view.currentNode.setText("Current Position: " + list.derp.getKeyPhrase() + " "+ '"' + list.derp.getData() + '"');
		for (int i = 0; i < view.label.length; i++) {
			view.label[i].setText(list.derp.getData());
			view.label[i].setHorizontalAlignment(JLabel.CENTER);
	        view.panel_1B.add(view.label[i]);
	        list.derp.next();
	      }
		list.returnToRoot(list.derp);
	}
	
	public void updateLabels() {
		view.currentNode.setText("Current Position: " + list.derp.getKeyPhrase() + " "+ '"' + list.derp.getData() + '"' + " " + highlightPosition);
		for (int k = 0; k < view.label.length; k++) {
			if (view.label[k].getBorder() == view.bevel) {
				highlightPosition = k;
			}
		}
		if (highlightPosition == 0) {
			for (int i = 0; i < view.label.length; i++) {
				view.label[i].setText(list.derp.getData());
		        list.derp.next();
		      }
			list.derp.prev();
			list.derp.prev();
			list.derp.prev();
		} else if (highlightPosition == 1) {
			for (int i = 1; i < view.label.length; i++) {
				view.label[i].setText(list.derp.getData());
		        list.derp.next();
		      }
			list.derp.prev();
			list.derp.prev();
		} else if (highlightPosition == 2) {
			nextButton();
		}
		
		
	}
	
	public void displayList() {
		int currentListPos = list.derp.index;
		list.returnToRoot(list.derp);
		view.textArea.setText("");
		for (int i = 0; i < list.derp.currentList.size() - 1; i++) {
			view.textArea.append(list.derp.getData() + "\n");
			list.derp.next();
	      }
		for (int j = list.derp.currentList.size(); j > currentListPos; j--) {
			list.derp.prev();
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
			try {
				Scanner sc = new Scanner(new FileReader(open.getSelectedFile().getPath()));
				view.selectedFile = open.getSelectedFile();
				while (sc.hasNext()) {
					view.textArea.append(sc.nextLine() + "\n");
				}
			} catch (FileNotFoundException e) {
				JOptionPane.showMessageDialog(null, e);
			}
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
				    file = new File(file.toString() + ".txt");  // append .txt if "foo.jpg.txt" is OK
				    file = new File(file.getParentFile(), FilenameUtils.getBaseName(file.getName())+".txt"); // ALTERNATIVELY: remove the extension (if any) and replace it with ".xml"
				BufferedWriter bf = new BufferedWriter(new FileWriter(file.getPath()));
				bf.close();
				}
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		}
	}
	
	public void nextButton() {
		for (int k = 0; k < view.label.length; k++) {
			if (view.label[k].getBorder() == view.bevel) {
				highlightPosition = k;
			}
		}
		if (highlightPosition < view.label.length - 1) {
			for (int k = 0; k < view.label.length; k++) {
				view.label[k].setBorder(view.empty);
			}
			highlightPosition++;
			view.label[highlightPosition].setBorder(view.bevel);
			index++;
    	    list.derp.next();
			String text = view.label[highlightPosition].getText();
    	    System.out.println("Positon: " + highlightPosition + ", Data Type: " + list.derp.getData() + ", Index: " + index + " " + listSize);
    		view.currentNode.setText("Current Position: " + list.derp.getKeyPhrase() + " "+ '"' + list.derp.getData() + '"');
		} else {
			if (listSize - 1 > view.label.length) {
		    	list.derp.prev();
		    	list.derp.prev();
			    for (int i = 0; i < view.label.length; i++) {
			    	list.derp.next();
			    	view.label[i].setText(list.derp.getData());
			      }
				listSize--;
				index++;
				String text = view.label[highlightPosition].getText();
        	    System.out.println("Positon: " + highlightPosition + ", Data Type: " + list.derp.getData() + ", Index: " + index + " " + listSize);
        		view.currentNode.setText("Current Position: " + list.derp.getKeyPhrase() + " "+ '"' + list.derp.getData() + '"');
			} else {
				System.out.println("Listed has ended");
			}
		}
	}
	
	public void prevButton() {
		for (int k = 0; k < view.label.length; k++) {
			if (view.label[k].getBorder() == view.bevel) {
				highlightPosition = k;
			}
		}
		if (highlightPosition > 0) {
			for (int k = 0; k < view.label.length; k++) {
				view.label[k].setBorder(view.empty);
			}
			highlightPosition--;
			view.label[highlightPosition].setBorder(view.bevel);
			index--;
    	    list.derp.prev();
			String text = view.label[highlightPosition].getText();
    	    System.out.println("Positon: " + highlightPosition + ", Data Type: " + list.derp.getData() + ", Index: " + index + " " + listSize);
    		view.currentNode.setText("Current Position: " + list.derp.getKeyPhrase() + " "+ '"' + list.derp.getData() + '"');
		} else {
			if (highlightPosition == 0 && index > 0) {
		    	list.derp.next();
		    	list.derp.next();
			    for (int i = view.label.length - 1; i >= 0; i--) {
			    	list.derp.prev();
			    	view.label[i].setText(list.derp.getData());
			      }
			    listSize++;
			    index--;
				String text = view.label[highlightPosition].getText();
        	    System.out.println("Positon: " + highlightPosition + ", Data Type: " + list.derp.getData() + ", Index: " + index + " " + listSize);
        		view.currentNode.setText("Current Position: " + list.derp.getKeyPhrase() + " "+ '"' + list.derp.getData() + '"');
			} else {
				System.out.println("Already at root");
			}
		}
	}
	
	public void branchItButton() {
		if(list.derp.index == list.derp.currentList.size() - 1) {
			view.createJunction(list.derp);
		}else{
			System.out.println("ERROR: Can only create junction at the end of list!");
		}
	}
	
	public void addTextButton() {
		if(view.textArea.getText().isEmpty()) {
			JOptionPane.showMessageDialog(view.frame,
				    "Type something in the text box before add.",
				    "Inane error",
				    JOptionPane.ERROR_MESSAGE);
			return;
		}
		list.derp.addNext("#TEXT", view.textArea.getText());
		view.currentNode.setText("Current Position: " + list.derp.getKeyPhrase() + " "+ '"' + list.derp.getData() + '"');
	}
	
	public void sampleButton() {
		list.derp.addNext("#TEXT", "Sample");
		this.listSize++;
		displayList();
		updateLabels();
		
//		if (view.textArea.getText().equals("")) {
//			view.textArea.append("Sample Text");
//		} else {
//			int caretOffset;
//			int lineNumber;
//			int startOffset;
//			int endOffset;
//			try {
//				caretOffset = view.textArea.getCaretPosition();
//				lineNumber = view.textArea.getLineOfOffset(caretOffset);
//				startOffset = view.textArea.getLineStartOffset(lineNumber);
//				endOffset = view.textArea.getLineEndOffset(lineNumber);
//				if (startOffset == endOffset) {
//					view.textArea.replaceRange("Sample Text", startOffset, endOffset);
//				} else {
//					view.textArea.append("\n" + "Sample Text");
//				}
//			} catch (BadLocationException e1) {
//				// TODO Auto-generated catch block
//				e1.printStackTrace();
//			}
//		}
	}
	
}

