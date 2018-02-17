package enamel;

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
import java.util.Scanner;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.KeyStroke;
import javax.swing.ScrollPaneConstants;
import javax.swing.border.BevelBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.text.BadLocationException;

import org.apache.commons.io.FilenameUtils;

public class View {

	JFrame frame;

	JTabbedPane tabbedPane_1;
	JTabbedPane tabbedPane_2;
	JTabbedPane tabbedPane_3;
	JPanel panel_1;
	JPanel panel_1B;
	JPanel panel_2;
	JLabel label[];
	JLabel labeltop;
	JLabel labelbottom;
	JTextArea textArea;
	JScrollPane scrollPane;
	
	JLabel currentNode;

	BevelBorder bevel;
	EmptyBorder empty;

	Controller controller;

	public void init() {

		bevel = new BevelBorder(BevelBorder.RAISED);
		empty = new EmptyBorder(5, 5, 5, 5);

		// Frame Properties
		frame = new JFrame("Panda");
		frame.setBounds(100, 100, 800, 400);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);
		
		//Menu
		JMenuBar menuBar = new JMenuBar();
		menuBar.getAccessibleContext().setAccessibleName("Menu Bar");
		menuBar.getAccessibleContext().setAccessibleDescription("Contains options relevant to program");
		frame.setJMenuBar(menuBar);
		
		JMenu mnFile = new JMenu("File");
		menuBar.add(mnFile);
		
		JMenuItem mntmClear = new JMenuItem("New");
		mntmClear.setAccelerator(KeyStroke.getKeyStroke('C', Toolkit.getDefaultToolkit ().getMenuShortcutKeyMask()));	// Shortcut: Control + C
		mntmClear.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				controller.newMenuItem();
			}
		});
		
		JMenuItem mntmOpen = new JMenuItem("Open");
		//mntmOpen.getAccessibleContext().setAccessibleName("Open");
		mntmOpen.getAccessibleContext().setAccessibleDescription("Imports existing text file for editing in editor text box");
		//textArea.getAccessibleContext().setAccessibleName("Text box");
		//textArea.getAccessibleContext().setAccessibleDescription("Type Text here");
		mntmOpen.setAccelerator(KeyStroke.getKeyStroke('O', Toolkit.getDefaultToolkit ().getMenuShortcutKeyMask()));	// Shortcut: Control + O
		mntmOpen.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				controller.openMenuItem();
			}
		});
		mnFile.add(mntmOpen);
		
		JMenuItem mntmSave = new JMenuItem("Save");
		mntmSave.setAccelerator(KeyStroke.getKeyStroke('S', Toolkit.getDefaultToolkit ().getMenuShortcutKeyMask()));	// Shortcut: Control + S
		mntmSave.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				controller.saveMenuItem();
			}
		});
		mnFile.add(mntmSave);
		mnFile.add(mntmClear);

		// TabbedPane 1
		tabbedPane_1 = new JTabbedPane(JTabbedPane.TOP);
		tabbedPane_1.setBounds(19, 31, 426, 303);
		frame.getContentPane().add(tabbedPane_1);
		panel_1 = new JPanel();
		tabbedPane_1.addTab("Scenarios", null, panel_1, null);
		panel_1.setLayout(null);

		scrollPane = new JScrollPane();
		scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_ALWAYS);
		scrollPane.setBounds(6, 6, 393, 245);
		panel_1.add(scrollPane);
		textArea = new JTextArea();
		scrollPane.setViewportView(textArea);
		textArea.setEditable(false);

		panel_1B = new JPanel();
		tabbedPane_1.addTab("Navigate", null, panel_1B, null);
		panel_1B.setLayout(new GridLayout(5, 0));
		tabbedPane_1.setSelectedIndex(1);

		
		labeltop = new JLabel();
		labelbottom = new JLabel();
		label = new JLabel[3];
		label[0] = new JLabel();
		label[1] = new JLabel();
		label[2] = new JLabel();
		label[0].setBorder(bevel);
		
		currentNode = new JLabel();
		currentNode.setBounds(10, 5, 500, 15);
		frame.getContentPane().add(currentNode);

		// TabbedPane 2
		tabbedPane_2 = new JTabbedPane(JTabbedPane.TOP);
		tabbedPane_2.setBounds(479, 31, 301, 303);
		frame.getContentPane().add(tabbedPane_2);
		JPanel panel_2 = new JPanel();
		tabbedPane_2.addTab("Control", null, panel_2, null);
		panel_2.setLayout(null);

		JButton btnNext = new JButton("Next");
		btnNext.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				controller.nextButton();
			}
		});
		btnNext.setBounds(73, 10, 117, 29);
		panel_2.add(btnNext);

		JButton btnPrev = new JButton("Previous");
		btnPrev.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				controller.prevButton();
			}
		});
		btnPrev.setBounds(73, 40, 117, 29);
		panel_2.add(btnPrev);
		
		JButton btnBranch = new JButton("Branch it!");
		btnBranch.getAccessibleContext().setAccessibleName("Branch");
		btnBranch.getAccessibleContext().setAccessibleDescription("Creates a new Branch from current list");
		btnBranch.addKeyListener(enter);	// Must be added to each button to execute it with the 'ENTER' key
		btnBranch.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				controller.branchItButton();
			}
		});
		btnBranch.setBounds(73, 70, 117, 29);
		panel_2.add(btnBranch);
		
		
		JButton btnAdd = new JButton("Add Text");
		//btnAdd.getAccessibleContext().setAccessibleName("Add Text");
		btnAdd.getAccessibleContext().setAccessibleDescription("Input new text into editor text box");
		btnAdd.addKeyListener(enter);	// Must be added to each button to execute it with the 'ENTER' key
		btnAdd.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				controller.addTextButton();
			}
		});
		btnAdd.setBounds(327, 100, 117, 29);
		panel_2.add(btnAdd);
		
		JButton  btnSound = new JButton("Audio Studio");
		btnSound.getAccessibleContext().setAccessibleDescription("Record and import audio files to screnario file");
		btnSound.addKeyListener(enter);
		btnSound.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				controller.soundButton();
			}
		});
		btnSound.setBounds(73, 160, 117, 29);
		panel_2.add(btnSound);
		
		
		JButton btnAddPause = new JButton("Add Pause");
		btnAddPause.getAccessibleContext().setAccessibleDescription("Adds pause for a user specified duration");
		btnAddPause.addKeyListener(enter);
		btnAddPause.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				controller.addPauseButton();
			}
		});
		
		btnAddPause.setBounds(73, 190, 117, 29);
		panel_2.add(btnAddPause);
		
		//Sample button: Adds "Sample Text" to the text field.
		JButton btnSample = new JButton("Sample");
		//btnSample.getAccessibleContext().setAccessibleName("Sample");
		btnSample.getAccessibleContext().setAccessibleDescription("Adds sample text to editor text  box");
		btnSample.addKeyListener(enter);	// Must be added to each button to execute it with the 'ENTER' key
		btnSample.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				controller.sampleButton();
			}
		});
		btnSample.setBounds(73, 130, 117, 29);
		panel_2.add(btnSample);
		
		//Show list
		JButton btnRefresh = new JButton("Refresh List");
		btnRefresh.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				frame.revalidate();
				frame.repaint();
				
				panel_1.revalidate();
				panel_1.repaint();
				
				panel_1B.revalidate();
				panel_1B.repaint();
				
				panel_2.revalidate();
				panel_2.repaint();
			}
		});
		btnRefresh.setBounds(73, 160, 117, 29);
		panel_2.add(btnRefresh);
		
		
		
		if (controller != null) {
			controller.initializeList();
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

	public void close() {
		frame.setVisible(false);
		frame.dispose();
	}

	public void setController(Controller controller) {
		this.controller = controller;
	}

}
