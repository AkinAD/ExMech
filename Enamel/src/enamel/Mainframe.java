package enamel;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Arrays;
import java.util.Scanner;

import javax.swing.JButton;
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
import javax.swing.ScrollPaneConstants;
import javax.swing.SwingUtilities;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.filechooser.FileSystemView;
import javax.swing.text.BadLocationException;

import org.apache.commons.io.FilenameUtils;

public class Mainframe {
	
	public JFrame frmAuthoringApp;
	private JTextArea textArea = new JTextArea();
	private File selectedFile;
	private JLabel currentNode;
	
	public Mainframe() {
		frmAuthoringApp = new JFrame();
		frmAuthoringApp.setTitle("Authoring App");
		frmAuthoringApp.setBounds(100, 100, 470, 300);
		frmAuthoringApp.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frmAuthoringApp.getContentPane().setLayout(null);
		
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_NEVER);
		scrollPane.setBounds(6, 20, 320, 220);
		frmAuthoringApp.getContentPane().add(scrollPane);
		
		scrollPane.setColumnHeaderView(textArea);
		
		
		//Generating basic graph manually to test...
		ListManager derp = new ListManager(3, 6);
		derp.addNext("#TEXT", "this is under the root.");
		derp.addNext("#TEXT", "one");
		derp.addNext("#TEXT", "two");
		derp.addNext("#TEXT", "three");
		derp.addNext("#TEXT", "four");
		derp.prev();
		derp.prev();
		derp.prev();
		derp.prev();
		derp.prev();
		String[] stuff = {"apple", "banana", "chocolate"};

		
		currentNode = new JLabel("Current Position: " + derp.getKeyPhrase() + " "+ '"' + derp.getData() + '"');
		currentNode.setBounds(10, 0, 500, 15);
		frmAuthoringApp.getContentPane().add(currentNode);
		
		JButton btnPrev = new JButton("Previous");
		btnPrev.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				derp.prev();
				
				currentNode.setText("Current Position: " + derp.getKeyPhrase() + " "+ '"' + derp.getData() + '"');
			}
		});
		btnPrev.setBounds(327, 60, 117, 29);
		frmAuthoringApp.getContentPane().add(btnPrev);
		
		JButton btnNext = new JButton("Next");
		btnNext.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				if(derp.getKeyPhrase().equals("#JUNCTION")) {
					String s = (String)JOptionPane.showInputDialog(
							frmAuthoringApp,
					                    "Choose a branch, dont fuck up:\n"
					                    + "\"Computer, please bring me to...\"",
					                    "Customized Dialog",
					                    JOptionPane.PLAIN_MESSAGE,
					                    null,
					                    stuff,
					                    "apple");

					//If a string was returned, say so.
					if ((s != null) && (s.length() > 0)) {
					    derp.junctionGoto(Arrays.asList(stuff).indexOf(s));
					    currentNode.setText("Current Position: " + derp.getKeyPhrase() + " "+ '"' + derp.getData() + '"');
					    return;
					}
				}
				derp.next();
				currentNode.setText("Current Position: " + derp.getKeyPhrase() + " "+ '"' + derp.getData() + '"');
			}
		});
		btnNext.setBounds(327, 90, 117, 29);
		frmAuthoringApp.getContentPane().add(btnNext);
		
		
		JButton btnBranch = new JButton("Branch it!");
		btnBranch.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				derp.createJunction(stuff);
				currentNode.setText("Current Position: " + derp.getKeyPhrase() + " "+ '"' + derp.getData() + '"');
				
				if(derp.getKeyPhrase().equals("#JUNCTION")) {
					String s = (String)JOptionPane.showInputDialog(
							frmAuthoringApp,
					                    "Choose a branch:\n"
					                    + "\"Computer, please bring me to...\"",
					                    "Customized Dialog",
					                    JOptionPane.PLAIN_MESSAGE,
					                    null,
					                    stuff,
					                    "apple");

					//If a string was returned, say so.
					if ((s != null) && (s.length() > 0)) {
					    derp.junctionGoto(Arrays.asList(stuff).indexOf(s));
					    currentNode.setText("Current Position: " + derp.getKeyPhrase() + " "+ '"' + derp.getData() + '"');
					    return;
					}
				}
			}
		});
		btnBranch.setBounds(327, 140, 117, 29);
		frmAuthoringApp.getContentPane().add(btnBranch);
		
		
		JButton btnAdd = new JButton("Add Text");
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
		frmAuthoringApp.setJMenuBar(menuBar);
		
		JMenu mnFile = new JMenu("File");
		menuBar.add(mnFile);
		
		JMenuItem mntmOpen = new JMenuItem("Open");
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
		mntmClear.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				textArea.setText("");
			}
		});
		
		JMenuItem mntmSave = new JMenuItem("Save");
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
						bf.write(textArea.getText());
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
