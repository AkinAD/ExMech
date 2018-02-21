package enamel;

import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.FocusTraversalPolicy;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.KeyEventDispatcher;
import java.awt.KeyboardFocusManager;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.WindowEvent;
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
import java.util.Vector;

import javax.swing.AbstractAction;
import javax.swing.ActionMap;
import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.InputMap;
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
import javax.swing.JSeparator;
import javax.swing.JTabbedPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.KeyStroke;
import javax.swing.ScrollPaneConstants;
import javax.swing.SwingConstants;
import javax.swing.border.BevelBorder;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;
import javax.swing.text.BadLocationException;

import org.apache.commons.io.FilenameUtils;

public class View {

	JFrame frame;

	JPanel navigationPanel;
	//JTabbedPane tabbedPane_1;
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
	

	/**
	 * @wbp.parser.entryPoint
	 */
	public void init() {

		bevel = new BevelBorder(BevelBorder.RAISED);
		empty = new EmptyBorder(5, 5, 5, 5);

		// Frame
		frame = new JFrame("Treasure Box Braille");
		frame.setBounds(100, 100, 800, 400);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);
		frame.getContentPane().add(new KeyPane());
		

		JButton btnNext = new JButton("\\/");
		btnNext.getAccessibleContext().setAccessibleName("Next");
		btnNext.getAccessibleContext().setAccessibleDescription("Switches to next event in the list. Keyboard shortcut is Down Arrow Key");
		btnNext.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				controller.nextButton();
				//currentNode.setRequestFocusEnabled(true);
			}
		});
		btnNext.setBounds(452, 173, 42, 29);
		frame.getContentPane().add(btnNext);

		JButton btnPrev = new JButton("/\\");
		btnPrev.getAccessibleContext().setAccessibleName("Previous");
		btnPrev.getAccessibleContext().setAccessibleDescription("Switches to previous event in the list. Keyboard shortcut is Up Arrow Key");
		btnPrev.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				controller.prevButton();
			}
		});
		btnPrev.setBounds(452, 139, 42, 29);
		frame.getContentPane().add(btnPrev);

		// Menu
		JMenuBar menuBar = new JMenuBar();
		menuBar.getAccessibleContext().setAccessibleName("Menu Bar");
		menuBar.getAccessibleContext().setAccessibleDescription("Contains options relevant to program");
		frame.setJMenuBar(menuBar);

		JMenu mnFile = new JMenu("File");
		menuBar.add(mnFile);

		JMenuItem mntmClear = new JMenuItem("New");
		mntmClear.setAccelerator(KeyStroke.getKeyStroke('C', Toolkit.getDefaultToolkit().getMenuShortcutKeyMask())); // Shortcut:
																														// Control
																														// +
																														// C
		mntmClear.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				controller.newMenuItem();
			}
		});

		JMenuItem mntmOpen = new JMenuItem("Open");
		// mntmOpen.getAccessibleContext().setAccessibleName("Open");
		mntmOpen.getAccessibleContext()
				.setAccessibleDescription("Imports existing text file for editing in editor text box");
		// textArea.getAccessibleContext().setAccessibleName("Text box");
		// textArea.getAccessibleContext().setAccessibleDescription("Type Text here");
		mntmOpen.setAccelerator(KeyStroke.getKeyStroke('O', Toolkit.getDefaultToolkit().getMenuShortcutKeyMask())); // Shortcut:
																													// Control
																													// +
																													// O
		mntmOpen.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				controller.openMenuItem();
			}
		});
		mnFile.add(mntmOpen);

		JMenuItem mntmSave = new JMenuItem("Save");
		mntmSave.setAccelerator(KeyStroke.getKeyStroke('S', Toolkit.getDefaultToolkit().getMenuShortcutKeyMask())); // Shortcut:
																													// Control
																													// +
																													// S
		mntmSave.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				controller.saveMenuItem();
			}
		});
		mnFile.add(mntmSave);
		mnFile.add(mntmClear);

		JMenu mnSimulate = new JMenu("Simulate");
		menuBar.add(mnSimulate);
		JMenuItem mntmSimulateScenario = new JMenuItem("Simulate Story");
		mntmSimulateScenario.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				controller.simulateScenario();
			}
		});
		mnSimulate.add(mntmSimulateScenario);

		// TabbedPane 1
		navigationPanel = new JPanel();
		navigationPanel.setBounds(10, 29, 442, 289);
		navigationPanel.setFocusable(true);
		navigationPanel.setToolTipText("Current Position ");
		navigationPanel.getAccessibleContext().setAccessibleName("");
		navigationPanel.getAccessibleContext().setAccessibleDescription("Current Position ");
		frame.getContentPane().add(navigationPanel);
		
		Border raisedbevel = BorderFactory.createRaisedBevelBorder();
		Border loweredbevel = BorderFactory.createLoweredBevelBorder();
		Border bevel = BorderFactory.createCompoundBorder(
                raisedbevel, loweredbevel);
		Border matte = BorderFactory.createMatteBorder(1, 5, 1, 1, new Color(200,221,242));
		Border redline = BorderFactory.createLineBorder(new Color(99,130,191));


		//Add a red outline to the frame.
		Border compound = BorderFactory.createCompoundBorder(
		                          redline, matte);
		navigationPanel.setBorder(compound);

		JLabel naviLabel = new JLabel("  Navigation");
		naviLabel.setBackground(new Color(200,221,242));
		naviLabel.setOpaque(true);
		naviLabel.setFont(new Font("", Font.BOLD, 12));
		naviLabel.setBounds(10, 5, 442, 23);
		frame.getContentPane().add(naviLabel);

		// panel_1 = new JPanel();
		// tabbedPane_1.addTab("Scenarios", null, panel_1, null);
		// panel_1.setLayout(null);
		//
		// scrollPane = new JScrollPane();
		// scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		// scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_ALWAYS);
		// scrollPane.setBounds(6, 6, 393, 245);
		// panel_1.add(scrollPane);
		// textArea = new JTextArea();
		// scrollPane.setViewportView(textArea);
		// textArea.setEditable(false);

		//panel_1B = new JPanel();
		//tabbedPane_1.addTab("Navigation", null, panel_1B, null);
		navigationPanel.setLayout(new GridLayout(5, 0));
		// tabbedPane_1.setSelectedIndex(1);

		labeltop = new JLabel();
		labelbottom = new JLabel();
		label = new JLabel[3];
		label[0] = new JLabel();
		label[1] = new JLabel();
		label[2] = new JLabel();
		label[0].setBorder(bevel);

		currentNode = new JLabel();
		//currentNode.setFocusable(true);
		currentNode.setBounds(10, 319, 770, 15);
		currentNode.setForeground(Color.GRAY);
		frame.getContentPane().add(currentNode);

		// TabbedPane 2
		tabbedPane_2 = new JTabbedPane(JTabbedPane.TOP);
		tabbedPane_2.setBounds(494, 5, 280, 313);
		frame.getContentPane().add(tabbedPane_2);
		JPanel panel_2 = new JPanel();
		tabbedPane_2.addTab("Create", null, panel_2, null);
		panel_2.setLayout(null);

		JButton btnBranch = new JButton("User-Input");
		btnBranch.getAccessibleContext().setAccessibleName("User-Input");
		btnBranch.getAccessibleContext().setAccessibleDescription("Creates a new Branch from current list");
		btnBranch.addKeyListener(enter); // Must be added to each button to execute it with the 'ENTER' key
		btnBranch.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				controller.branchItButton();
			}
		});
		btnBranch.setBounds(73, 12, 150, 25);
		panel_2.add(btnBranch);

		JButton btnAdd = new JButton("Text");
		btnAdd.getAccessibleContext().setAccessibleName("Add Text");
		btnAdd.getAccessibleContext().setAccessibleDescription("Input new text");
		btnAdd.addKeyListener(enter); // Must be added to each button to execute it with the 'ENTER' key
		btnAdd.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				controller.addTextButton();
			}
		});
		btnAdd.setBounds(73, 49, 150, 25);
		panel_2.add(btnAdd);

		JButton btnSound = new JButton("Audio Recording");
		btnSound.getAccessibleContext().setAccessibleDescription("Record and import audio files to scenario file");
		btnSound.addKeyListener(enter);
		btnSound.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				controller.soundButton();
			}
		});
		btnSound.setBounds(73, 86, 150, 25);
		panel_2.add(btnSound);

		JButton btnAddPause = new JButton("Pause");
		btnAddPause.getAccessibleContext().setAccessibleDescription("Adds pause for a user specified duration");
		btnAddPause.addKeyListener(enter);
		btnAddPause.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				controller.addPauseButton();
			}
		});

		btnAddPause.setBounds(73, 123, 150, 25);
		panel_2.add(btnAddPause);

		JButton btnPin = new JButton("Set Pins");
		btnPin.getAccessibleContext().setAccessibleName("Set Pins");
		btnPin.getAccessibleContext().setAccessibleDescription("Set the cell pins to alphabet letters.");
		btnPin.addKeyListener(enter); // Must be added to each button to execute it with the 'ENTER' key
		btnPin.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				controller.setPinButton();
			}
		});
		btnPin.setBounds(73, 160, 150, 25);
		panel_2.add(btnPin);

		JButton btnClrPin = new JButton("Clear Pins");
		btnClrPin.getAccessibleContext().setAccessibleName("Clear Pins");
		btnClrPin.getAccessibleContext().setAccessibleDescription("Clear all pins to the lowered position.");
		btnClrPin.addKeyListener(enter); // Must be added to each button to execute it with the 'ENTER' key
		btnClrPin.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				controller.clrPinButton();
			}
		});
		btnClrPin.setBounds(73, 197, 150, 25);
		panel_2.add(btnClrPin);
		
		JButton btnRemove = new JButton("Remove");
		btnRemove.getAccessibleContext().setAccessibleName("Remove");
		btnRemove.getAccessibleContext().setAccessibleDescription("Removes the current node. Can not undo this action.");
		btnRemove.addKeyListener(enter); // Must be added to each button to execute it with the 'ENTER' key
		btnRemove.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				controller.removeButton();
			}
		});
		btnRemove.setBounds(73, 234, 150, 25);
		panel_2.add(btnRemove);

		// //Sample button: Adds "Sample Text" to the text field.
		// JButton btnSample = new JButton("Sample");
		// //btnSample.getAccessibleContext().setAccessibleName("Sample");
		// btnSample.getAccessibleContext().setAccessibleDescription("Adds sample text
		// to editor text box");
		// btnSample.addKeyListener(enter); // Must be added to each button to execute
		// it with the 'ENTER' key
		// btnSample.addActionListener(new ActionListener() {
		// public void actionPerformed(ActionEvent e) {
		// controller.sampleButton();
		// }
		// });
		// btnSample.setBounds(73, 190, 117, 29);
		// panel_2.add(btnSample);

		if (controller != null) {
			controller.initializeList();
		}
		
		
		//Set the focus order when using Tab or Shift+Tab
		Vector<Component> order = new Vector<Component>();
        order.add(navigationPanel);
        order.add(btnNext);
        order.add(btnPrev);
        order.add(tabbedPane_2);
        order.add(btnBranch);
        order.add(btnAdd);
        order.add(btnSound);
        order.add(btnAddPause);
        order.add(btnPin);
        order.add(btnClrPin);
        order.add(btnRemove);
        FocusPolicy newPolicy = new FocusPolicy(order);
        frame.setFocusTraversalPolicy(newPolicy);
        
  
        
	}// end of int()

	public KeyListener enter = new KeyAdapter() {
		@Override
		public void keyTyped(KeyEvent e) {
			if (e.getKeyChar() == KeyEvent.VK_ENTER) {
				((JButton) e.getComponent()).doClick();
			}
		}
	};

	public void close() {
		frame.dispatchEvent(new WindowEvent(frame, WindowEvent.WINDOW_CLOSING));
	}

	public void setController(Controller controller) {
		this.controller = controller;
	}
	

	//Add Key Bindings
	public class KeyPane extends JPanel {
		public KeyPane() {

	        setFocusable(true);

	        InputMap im = getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW);
	        ActionMap am = getActionMap();

	        im.put(KeyStroke.getKeyStroke(KeyEvent.VK_UP, 0), "Up");
	        am.put("Up", new UpAction());
	        
	        im.put(KeyStroke.getKeyStroke(KeyEvent.VK_DOWN, 0), "Down");
	        am.put("Down", new DownAction());

	    }
		
	    protected class UpAction extends AbstractAction {
	        @Override
	        public void actionPerformed(ActionEvent e) {
	            controller.prevButton();
	        }
	    }
	    protected class DownAction extends AbstractAction {
	        @Override
	        public void actionPerformed(ActionEvent e) {
	            controller.nextButton();
	        }
	    }

	}

}
