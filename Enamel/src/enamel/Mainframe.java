package enamel;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.EventQueue;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
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
import java.util.Iterator;
import java.util.Map.Entry;
import java.util.Scanner;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComponent;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.KeyStroke;
import javax.swing.ScrollPaneConstants;
import javax.swing.SwingUtilities;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.filechooser.FileSystemView;
import javax.swing.text.BadLocationException;

import org.apache.commons.io.FilenameUtils;

public class Mainframe {
	
	public JFrame frmAuthoringApp;
	private JTextArea textArea;
	private File selectedFile;
	private JLabel currentNode;
	
	public Mainframe() {
		frmAuthoringApp = new JFrame();
		frmAuthoringApp.setTitle("Authoring App");
		frmAuthoringApp.setBounds(100, 100, 470, 300);
		frmAuthoringApp.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frmAuthoringApp.getContentPane().setLayout(null);
		
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_ALWAYS);
		scrollPane.setBounds(6, 20, 320, 220);
		frmAuthoringApp.getContentPane().add(scrollPane);
		
		textArea = new JTextArea();
		scrollPane.setViewportView(textArea);
		textArea.setEditable(false);
		
		
		//Generating basic graph manually to test...
		ListManager derp = new ListManager(3, 6);
		derp.addNext("#TEXT", "this is under the root.");
		derp.addNext("#TEXT", "one");
		derp.addNext("#TEXT", "two");
		derp.addNext("#TEXT", "three");
		derp.addNext("#TEXT", "four");
		derp.addNext("/~pause:", "5"); //Akin
		derp.prev();
		derp.prev();
		derp.prev();
		derp.prev();
		derp.prev();
		HashMap<Integer, String> stuff = new HashMap<Integer, String>();
		stuff.put(1,"apple");
		stuff.put(2,"banana");
		stuff.put(3,"chocolate");

		
		currentNode = new JLabel("Current Position: " + derp.getKeyPhrase() + " "+ '"' + derp.getData() + '"');
		currentNode.setBounds(10, 0, 500, 15);
		frmAuthoringApp.getContentPane().add(currentNode);
		
		ScenarioComposer Compose = new ScenarioComposer(); //Akin
		
		JButton btnPrev = new JButton("Previous");
		btnPrev.getAccessibleContext().setAccessibleName("Previous");
		btnPrev.getAccessibleContext().setAccessibleDescription("Navigates current list back to previous node");
		btnPrev.addKeyListener(enter);
		btnPrev.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				derp.prev();
				
				currentNode.setText("Current Position: " + derp.getKeyPhrase() + " "+ '"' + derp.getData() + '"');
			}
		});
		btnPrev.setBounds(327, 60, 117, 29);
		frmAuthoringApp.getContentPane().add(btnPrev);
		
		JButton btnNext = new JButton("Next");
		btnNext.getAccessibleContext().setAccessibleName("Next");
		btnNext.getAccessibleContext().setAccessibleDescription("Navigates current list back to next node");
		btnNext.addKeyListener(enter);	// Must be added to each button to execute it with the 'ENTER' key
		btnNext.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(derp.getKeyPhrase().equals("#JUNCTION")) {
				chooseButton(derp);
				}else {
				derp.next();
				currentNode.setText("Current Position: " + derp.getKeyPhrase() + " "+ '"' + derp.getData() + '"');
				}
			}
		});
		btnNext.setBounds(327, 90, 117, 29);
		frmAuthoringApp.getContentPane().add(btnNext);
		
		
		JButton btnBranch = new JButton("Branch it!");
		btnBranch.getAccessibleContext().setAccessibleName("Branch");
		btnBranch.getAccessibleContext().setAccessibleDescription("Creates a new Branch from current list");
		btnBranch.addKeyListener(enter);	// Must be added to each button to execute it with the 'ENTER' key
		btnBranch.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(derp.index == derp.currentList.size() - 1) {
					createJunction(derp);
				}else{
					System.out.println("ERROR: Can only create junction at the end of list!");
				}
			}
		});
		btnBranch.setBounds(327, 140, 117, 29);
		frmAuthoringApp.getContentPane().add(btnBranch);
		
		
		JButton btnAdd = new JButton("Add Text");
		//btnAdd.getAccessibleContext().setAccessibleName("Add Text");
		btnAdd.getAccessibleContext().setAccessibleDescription("Input new text into editor text box");
		btnAdd.addKeyListener(enter);	// Must be added to each button to execute it with the 'ENTER' key
		btnAdd.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(textArea.getText().isEmpty()) {
					JOptionPane.showMessageDialog(frmAuthoringApp,
						    "Type something in the text box before add.",
						    "Inane error",
						    JOptionPane.ERROR_MESSAGE);
					return;
				}
				derp.addNext("#TEXT", textArea.getText());
				currentNode.setText("Current Position: " + derp.getKeyPhrase() + " "+ '"' + derp.getData() + '"');
			}
		});
		btnAdd.setBounds(327, 170, 117, 29);
		frmAuthoringApp.getContentPane().add(btnAdd);
		
		
		//Sample button: Adds "Sample Text" to the text field.
		JButton btnSample = new JButton("Sample");
		//btnSample.getAccessibleContext().setAccessibleName("Sample");
		btnSample.getAccessibleContext().setAccessibleDescription("Adds sample text to editor text  box");
		btnSample.addKeyListener(enter);	// Must be added to each button to execute it with the 'ENTER' key
		btnSample.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (textArea.getText().equals("")) {
					textArea.append("Sample Text");
				} else {
					int caretOffset;
					int lineNumber;
					int startOffset;
					int endOffset;
					try {
						caretOffset = textArea.getCaretPosition();
						lineNumber = textArea.getLineOfOffset(caretOffset);
						startOffset = textArea.getLineStartOffset(lineNumber);
						endOffset = textArea.getLineEndOffset(lineNumber);
						if (startOffset == endOffset) {
							textArea.replaceRange("Sample Text", startOffset, endOffset);
						} else {
							textArea.append("\n" + "Sample Text");
						}
					} catch (BadLocationException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
				}
			}
		});
		btnSample.setBounds(327, 19, 117, 29);
		frmAuthoringApp.getContentPane().add(btnSample);
		
		JMenuBar menuBar = new JMenuBar();
		menuBar.getAccessibleContext().setAccessibleName("Menu Bar");
		menuBar.getAccessibleContext().setAccessibleDescription("Contains options relevant to program");
		frmAuthoringApp.setJMenuBar(menuBar);
		
		JMenu mnFile = new JMenu("File");
		menuBar.add(mnFile);
		
		JMenuItem mntmOpen = new JMenuItem("Open");
		//mntmOpen.getAccessibleContext().setAccessibleName("Open");
		mntmOpen.getAccessibleContext().setAccessibleDescription("Imports existing text file for editing in editor text box");
		textArea.getAccessibleContext().setAccessibleName("Text box");
		textArea.getAccessibleContext().setAccessibleDescription("Type Text here");
		mntmOpen.setAccelerator(KeyStroke.getKeyStroke('O', Toolkit.getDefaultToolkit ().getMenuShortcutKeyMask()));	// Shortcut: Control + O
		mntmOpen.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				textArea.setText("");
				JFileChooser open = new JFileChooser("FactoryScenarios/");
				int retrunVal = open.showOpenDialog(frmAuthoringApp);
				if (retrunVal == JFileChooser.APPROVE_OPTION) {
					try {
						Scanner sc = new Scanner(new FileReader(open.getSelectedFile().getPath()));
						selectedFile = open.getSelectedFile();
						while (sc.hasNext()) {
							textArea.append(sc.nextLine() + "\n");
						}
					} catch (FileNotFoundException e1) {
						JOptionPane.showMessageDialog(null, e);
					}
				}
			}
		});
		mnFile.add(mntmOpen);
		
		JMenuItem mntmClear = new JMenuItem("Clear");
		mntmClear.setAccelerator(KeyStroke.getKeyStroke('C', Toolkit.getDefaultToolkit ().getMenuShortcutKeyMask()));	// Shortcut: Control + C
		mntmClear.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				textArea.setText("");
			}
		});
		
		JMenuItem mntmSave = new JMenuItem("Save");
		mntmSave.setAccelerator(KeyStroke.getKeyStroke('S', Toolkit.getDefaultToolkit ().getMenuShortcutKeyMask()));	// Shortcut: Control + S
		mntmSave.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				JFileChooser save = new JFileChooser("FactoryScenarios/");
				int retrunVal = save.showSaveDialog(frmAuthoringApp);
				if (retrunVal == JFileChooser.APPROVE_OPTION) {
					try {	
						File file = save.getSelectedFile(); 
						
						if (FilenameUtils.getExtension(file.getName()).equalsIgnoreCase("txt")) {
						    // filename is OK as-is
						} else {
						    file = new File(file.toString() + ".txt");  // append .txt if "foo.jpg.txt" is OK
						    file = new File(file.getParentFile(), FilenameUtils.getBaseName(file.getName())+".txt"); // ALTERNATIVELY: remove the extension (if any) and replace it with ".xml"
						BufferedWriter bf = new BufferedWriter(new FileWriter(file.getPath()));
						bf.write(Compose.returnStringFile(derp)); //Akin
						bf.close();
						}
					} catch (IOException e1) {
						e1.printStackTrace();
					}
				}
			}
		});
		mnFile.add(mntmSave);
		mnFile.add(mntmClear);
	}
	
	public void createJunction(ListManager derp) {
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
		currentNode.setText("Current Position: " + derp.getKeyPhrase() + " " + '"' + derp.getData() + '"');
		// navigate
		chooseButton(derp);
	}

	public void chooseButton(ListManager derp) {
		if (derp.getKeyPhrase().equals("#JUNCTION")) {
			// Get the buttonNames and create a dialog box to choose where to navigate to
			ArrayList<String> buttons = derp.getNode().buttonsNames;
			int i = JOptionPane.showOptionDialog(null, "Choose which branch to go to:", "Choose Button", JOptionPane.PLAIN_MESSAGE, 0, null,
					buttons.toArray(), buttons.toArray()[0]);
			// Goto the selected branch based on the button press
			String s = buttons.toArray()[i].toString();
			derp.junctionGoto(derp.junctionSearch(s));
			currentNode.setText("Current Position: " + derp.getKeyPhrase() + " " + '"' + derp.getData() + '"');
			return;

		}
	}
	
	public KeyListener enter = new KeyAdapter() {
		@Override
		public void keyTyped(KeyEvent e) {
			if (e.getKeyChar() == KeyEvent.VK_ENTER) {
				((JButton) e.getComponent()).doClick();
			}
		}
	};

	public File getSelectedFile() {
		return selectedFile;
	}

	public void close() {
		frmAuthoringApp.setVisible(false);
		frmAuthoringApp.dispose();
	}
	
	
	public static void main(String[] args) {
		Mainframe window = new Mainframe();
		window.frmAuthoringApp.setVisible(true);
	}
}
