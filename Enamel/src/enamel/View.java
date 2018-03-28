package enamel;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.InputEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.WindowEvent;
import java.util.Vector;

import javax.swing.AbstractAction;
import javax.swing.ActionMap;
import javax.swing.BorderFactory;
import javax.swing.InputMap;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JEditorPane;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTabbedPane;
import javax.swing.JTextArea;
import javax.swing.JTree;
import javax.swing.KeyStroke;
import javax.swing.SwingConstants;
import javax.swing.border.BevelBorder;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;

import javax.swing.tree.TreeSelectionModel;

public class View {

	JFrame frame;

	JPanel navigationPanel;
	
	//JTabbedPane tabbedPane_1;
	JTabbedPane tabbedPane_2;
	JTabbedPane tabbedPane_3;
	JPanel panel_1;
	JPanel panel_1B;
	JPanel panel_create;
	JPanel panel_braille;
	JPanel panel_audio;
	JPanel panel_;
	JLabel label[];
	JLabel labeltop;
	JLabel labelbottom;
	JTextArea textArea;
	JScrollPane scrollPane;
	JMenuBar menuBar;

	JLabel currentNode;

	BevelBorder bevel;
	Border matte;
	Border compound1;
	
	EmptyBorder empty;

	Controller controller;
	
	JPanel jtreePanel;
    JTree tree;
    JScrollPane treeView;
    DefaultMutableTreeNode top;
    DefaultTreeModel model;

	/**
	 * @wbp.parser.entryPoint
	 */
	public void init() {

		bevel = new BevelBorder(BevelBorder.RAISED);
		matte = BorderFactory.createMatteBorder(3, 5, 3, 5, new Color(200,221,242));
		compound1 = BorderFactory.createCompoundBorder(
                bevel, matte);
		empty = new EmptyBorder(5, 5, 5, 5);

		// Frame
		frame = new JFrame("Treasure Box Braille");
		frame.setBounds(100, 100, 1213, 557);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);
		frame.getContentPane().add(new KeyPane());

		

		JButton btnNext = new JButton("\\/");
		btnNext.getAccessibleContext().setAccessibleName("Next");
		btnNext.getAccessibleContext().setAccessibleDescription("Keyboard shortcut is Control + N. Switches to next event in the list.");
		btnNext.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				controller.nextButton();
				//currentNode.setRequestFocusEnabled(true);
			}
		});
		btnNext.setBounds(792, 255, 42, 65);
		frame.getContentPane().add(btnNext);

		JButton btnPrev = new JButton("/\\");
		btnPrev.getAccessibleContext().setAccessibleName("Previous");
		btnPrev.getAccessibleContext().setAccessibleDescription("Keyboard shortcut is Control + P. Switches to previous event in the list.");
		btnPrev.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				controller.prevButton();
			}
		});
		btnPrev.setBounds(792, 183, 42, 65);
		frame.getContentPane().add(btnPrev);

		// Menu
		menuBar = new JMenuBar();
		menuBar.getAccessibleContext().setAccessibleName("Menu Bar");
		menuBar.getAccessibleContext().setAccessibleDescription("Contains options relevant to program");
		frame.setJMenuBar(menuBar);

		JMenu mnFile = new JMenu("File");
		menuBar.add(mnFile);

		JMenuItem mntmClear = new JMenuItem("New");
		mntmClear.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_N, ActionEvent.CTRL_MASK+ActionEvent.SHIFT_MASK)); // Shortcut:
																													 // Control
																												     // +
																													 // N
		mntmClear.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				controller.newMenuItem();
			}
		});
		mnFile.add(mntmClear);

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
		

		JMenu mnSimulate = new JMenu("Simulate");
		menuBar.add(mnSimulate);
		JMenuItem mntmSimulateScenario = new JMenuItem("Simulate Story");
		mntmSimulateScenario.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S, ActionEvent.CTRL_MASK+ActionEvent.SHIFT_MASK));
		mntmSimulateScenario.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				controller.simulateScenario();
			}
		});
		mnSimulate.add(mntmSimulateScenario);

		
		initJTree();
		// TabbedPane 1
		navigationPanel = new JPanel();
		//navigationPanel.setBounds(289, 29, 493, 456);
		navigationPanel.setMinimumSize(new Dimension(500, 50));
		navigationPanel.setFocusable(true);
		navigationPanel.setToolTipText("Current Position ");
		navigationPanel.getAccessibleContext().setAccessibleName("Navigation");
		navigationPanel.getAccessibleContext().setAccessibleDescription("Current Position ");
		//frame.getContentPane().add(navigationPanel);
		
		//Add split pane.
		JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
		splitPane.setBounds(21, 29, 761, 456);
        splitPane.setLeftComponent(treeView);
        splitPane.setRightComponent(navigationPanel);
        frame.getContentPane().add(splitPane);
        
		Border raisedbevel = BorderFactory.createRaisedBevelBorder();
		Border loweredbevel = BorderFactory.createLoweredBevelBorder();
		Border bevel = BorderFactory.createCompoundBorder(
                raisedbevel, loweredbevel);
		Border matte1 = BorderFactory.createMatteBorder(1, 5, 1, 1, new Color(200,221,242));
		Border redline = BorderFactory.createLineBorder(new Color(99,130,191));


		//Add a red outline to the frame.
		Border compound = BorderFactory.createCompoundBorder(
		                          redline, matte1);
		navigationPanel.setBorder(compound);

		JLabel naviLabel = new JLabel("  Navigation");
		naviLabel.setBackground(new Color(200,221,242));
		naviLabel.setOpaque(true);
		naviLabel.setFont(new Font("", Font.BOLD, 12));
		naviLabel.setBounds(21, 5, 761, 23);
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

		labeltop = new JLabel("", SwingConstants.CENTER);
		labelbottom = new JLabel("", SwingConstants.CENTER);
		label = new JLabel[3];
		label[0] = new JLabel("", SwingConstants.CENTER);
		label[1] = new JLabel("", SwingConstants.CENTER);
		label[2] = new JLabel("", SwingConstants.CENTER);
		label[0].setBorder(bevel);

		currentNode = new JLabel();
		//currentNode.setFocusable(true);
		currentNode.setBounds(10, 469, 777, 15);
		currentNode.setForeground(Color.GRAY);
		currentNode.setFocusable(false);
		currentNode.getAccessibleContext().setAccessibleDescription("");
		currentNode.getAccessibleContext().setAccessibleName("");
		//frame.getContentPane().add(currentNode);

		// TabbedPane 2
		tabbedPane_2 = new JTabbedPane(JTabbedPane.TOP);
		tabbedPane_2.setBounds(844, 6, 339, 481);
		tabbedPane_2.getAccessibleContext().setAccessibleDescription("Create");
		tabbedPane_2.getAccessibleContext().setAccessibleName("Create");
		frame.getContentPane().add(tabbedPane_2);
		JPanel pnlCreate = new JPanel();
		pnlCreate.setLayout(null);
		tabbedPane_2.addTab("Create Events", null, pnlCreate, null);
		
		JPanel panel_braille = new JPanel();
		panel_braille.setLayout(null);
		//tabbedPane_2.addTab("Create Braille", null, panel_braille, null);

		JPanel panel_audio = new JPanel();
		panel_audio.setLayout(null);
		//tabbedPane_2.addTab("Create Audio", null, panel_audio, null);

		

		JButton btnBranch = new JButton("Add User-Input");
		btnBranch.getAccessibleContext().setAccessibleName("Add User-Input");
		btnBranch.getAccessibleContext().setAccessibleDescription("Creates a new Branch from current list");
		btnBranch.setToolTipText("Creates a new Branch from current list");
		btnBranch.addKeyListener(enter); // Must be added to each button to execute it with the 'ENTER' key
		btnBranch.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				controller.branchItButton();
			}
		});
		btnBranch.setBounds(12, 63, 150, 25);
		pnlCreate.add(btnBranch);

		JButton btnAdd = new JButton("Add Text");
		btnAdd.getAccessibleContext().setAccessibleName("Add Text");
		btnAdd.getAccessibleContext().setAccessibleDescription("Input new text");
		btnAdd.setToolTipText("Input new text");
		btnAdd.addKeyListener(enter); // Must be added to each button to execute it with the 'ENTER' key
		btnAdd.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				controller.addTextButton();
			}
		});
		btnAdd.setBounds(12, 13, 150, 25);
		pnlCreate.add(btnAdd);
		
		JButton btnResetBtns = new JButton("Reset Buttons");
		btnResetBtns.getAccessibleContext().setAccessibleName("Reset Buttons");
		btnResetBtns.getAccessibleContext().setAccessibleDescription("Reset Buttons so that when you press one of the buttons, nothing will occur.");
		btnResetBtns.setToolTipText("Reset Buttons so that when you press one of the buttons, nothing will occur.");
		btnResetBtns.addKeyListener(enter); // Must be added to each button to execute it with the 'ENTER' key
		btnResetBtns.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				controller.ResetAllButtons();
			}
		});
		btnResetBtns.setBounds(12, 300, 150, 25);
		pnlCreate.add(btnResetBtns);
		
		JButton btnSound = new JButton("Add Sound");
		btnSound.getAccessibleContext().setAccessibleDescription("Add sound from an existing sound file.");
		btnSound.setToolTipText("Add sound from an existing sound file.");
		btnSound.addKeyListener(enter);
		btnSound.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				controller.soundButton();
			}
		});
		btnSound.setBounds(12, 88, 150, 25);
		pnlCreate.add(btnSound);

		JButton btnRecordSound = new JButton("Audio Recording");
		btnRecordSound.getAccessibleContext().setAccessibleDescription("Record and import audio files to scenario file");
		btnRecordSound.setToolTipText("Add sound from an existing sound file.");
		btnRecordSound.addKeyListener(enter);
		btnRecordSound.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				controller.recordSoundButton();
			}
		});
		btnRecordSound.setBounds(12, 113, 150, 25);
		pnlCreate.add(btnRecordSound);

		JButton btnAddPause = new JButton("Add Pause");
		btnAddPause.getAccessibleContext().setAccessibleDescription("Adds pause for a specified duration");
		btnAddPause.setToolTipText("Adds pause for a specified duration");
		btnAddPause.addKeyListener(enter);
		btnAddPause.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				controller.addPauseButton();
			}
		});

		btnAddPause.setBounds(12, 38, 150, 25);
		pnlCreate.add(btnAddPause);

		JButton btnPin = new JButton("Set Braille Letter");
		btnPin.getAccessibleContext().setAccessibleName("Set Braile Pins");
		btnPin.getAccessibleContext().setAccessibleDescription("Set the cell pins to an 8 character sequence.");
		btnPin.setToolTipText("Set the cell pins to an 8 character sequence.");
		btnPin.addKeyListener(enter); // Must be added to each button to execute it with the 'ENTER' key
		btnPin.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				controller.setPinButton();
			}
		});
		btnPin.setBounds(12, 162, 150, 25);
		pnlCreate.add(btnPin);
		
		JButton btnDispString = new JButton("Set Braille Word");
		btnDispString.getAccessibleContext().setAccessibleName("Set Braille Word");
		btnDispString.getAccessibleContext().setAccessibleDescription("Set the braille characters to display a word.");
		btnDispString.setToolTipText("Set the braille characters to display a word.");
		btnDispString.addKeyListener(enter); // Must be added to each button to execute it with the 'ENTER' key
		btnDispString.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				controller.setDispStringButton();
			}
		});
		btnDispString.setBounds(12, 187, 150, 25);
		pnlCreate.add(btnDispString);

		JButton btnClrCell = new JButton("Clear a Braille Cell");
		btnClrCell.getAccessibleContext().setAccessibleName("Clear Braille Character");
		btnClrCell.getAccessibleContext().setAccessibleDescription("Clear all pins to the lowered position.");
		btnClrCell.setToolTipText("Clear all pins to the lowered position.");
		btnClrCell.addKeyListener(enter); // Must be added to each button to execute it with the 'ENTER' key
		btnClrCell.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				controller.clearACell();
			}
		});
		btnClrCell.setBounds(12, 212, 150, 25);
		pnlCreate.add(btnClrCell);
		
		JButton btnClrPin = new JButton("Clear a Pin on a Cell");
		btnClrPin.getAccessibleContext().setAccessibleName("Clear a pin on a Cell");
		btnClrPin.getAccessibleContext().setAccessibleDescription("Sets one pin on one braille cell to the lowered position.");
		btnClrPin.setToolTipText("Sets one pin on one braille cell to the lowered position.");
		btnClrPin.addKeyListener(enter); // Must be added to each button to execute it with the 'ENTER' key
		btnClrPin.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				controller.lowerOnePin();
			}
		});
		btnClrPin.setBounds(12, 262, 150, 25);
		pnlCreate.add(btnClrPin);
		
		JButton btnRepeat = new JButton("Repeat Instructions");
		btnRepeat.getAccessibleContext().setAccessibleName("Repeat Instructions");
		btnRepeat.getAccessibleContext().setAccessibleDescription("Allows you to set text, and a button that when  the button is pressed repeats "
				+ "a set of instructions to the user until button is reassigned.");
		btnRepeat.setToolTipText("Allows you to set text, and a button that when  the button is pressed repeats \"\n" + 
				"				+ \"a set of instructions to the user until button is reassigned.");
		btnRepeat.addKeyListener(enter); // Must be added to each button to execute it with the 'ENTER' key
		btnRepeat.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				controller.setRepeatButton();
			}
		});
		btnRepeat.setBounds(12, 357, 150, 25);
		pnlCreate.add(btnRepeat);
		
		JButton btnClrAllCells = new JButton("Clear All Cells");
		btnRepeat.getAccessibleContext().setAccessibleName("Clear all Braille Cells");
		btnRepeat.getAccessibleContext().setAccessibleDescription("Lowers all the pins for all braille cells.");
		btnRepeat.setToolTipText("Lowers all the pins for all braille cells.");
		btnRepeat.addKeyListener(enter); // Must be added to each button to execute it with the 'ENTER' key
		btnRepeat.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				controller.clearAllCells();;
			}
		});
		btnClrAllCells.setBounds(12, 237, 150, 25);
		pnlCreate.add(btnClrAllCells);
		
		
		JButton btnRemove = new JButton("Remove");
		btnRemove.getAccessibleContext().setAccessibleName("Remove");
		btnRemove.getAccessibleContext().setAccessibleDescription("Removes the current node. Can not undo this action.");
		btnRemove.setToolTipText("Removes the current node. Can not undo this action.");
		btnRemove.addKeyListener(enter); // Must be added to each button to execute it with the 'ENTER' key
		btnRemove.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				controller.removeButton();
			}
		});
		btnRemove.setBounds(12, 413, 150, 25);
		pnlCreate.add(btnRemove);
		
		JPopupMenu popupMenu = new JPopupMenu();
		popupMenu.setSize(183, 41);
		popupMenu.setLocation(100, 0);
		 
		JMenuItem menuItemCreateSpringProject = new JMenuItem("Spring Project");
		popupMenu.add(menuItemCreateSpringProject);
		 
		JMenuItem menuItemCreateHibernateProject = new JMenuItem("Hibernate Project");
		popupMenu.add(menuItemCreateHibernateProject);
		 
		JMenuItem menuItemCreateStrutsProject = new JMenuItem("Struts Project");
		popupMenu.add(menuItemCreateStrutsProject);
		
//		JPopupMenu popupMenu1 = createDropDownMenu(); // created above
//		 
//		ImageIcon icon = new ImageIcon(getClass().getResource("/net/codejava/swing/images/new.gif"));
//		 
//		JButton dropDownButton = DropDownButtonFactory.createDropDownButton(icon, popupMenu1);
//
		pnlCreate.add(popupMenu);

		

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
        order.add(btnAdd);
        order.add(btnAddPause);
        order.add(btnBranch);
        order.add(btnSound);
        order.add(btnRecordSound);
        order.add(btnPin);
        order.add(btnDispString);
        order.add(btnClrCell);
        order.add(btnClrAllCells);
        order.add(btnClrPin);
       
        order.add(btnResetBtns);
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

	        im.put(KeyStroke.getKeyStroke(KeyEvent.VK_P, InputEvent.CTRL_MASK), "Up");
	        am.put("Up", new UpAction());
	        
	        im.put(KeyStroke.getKeyStroke(KeyEvent.VK_N, InputEvent.CTRL_MASK), "Down");
	        am.put("Down", new DownAction());
	        
	        im.put(KeyStroke.getKeyStroke(KeyEvent.VK_ALT, 0, true), "Alt");
	        am.put("Alt", new AltAction());

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
	    protected class AltAction extends AbstractAction {
	        @Override
	        public void actionPerformed(ActionEvent e) {
	            menuBar.getMenu(0).doClick();
	        }
	    }

	}
	
	
	//JTree stuff
	public void initJTree() {
		//Create the nodes.
        top = new DefaultMutableTreeNode("Beginning of Story");
        
		model = new DefaultTreeModel(top);
        top = (DefaultMutableTreeNode)model.getRoot();
        top.add(new DefaultMutableTreeNode("did not update"));
        model.reload(top);
        
        

        //Create a tree that allows one selection at a time.
        tree = new JTree(model);
        tree.getSelectionModel().setSelectionMode
                (TreeSelectionModel.SINGLE_TREE_SELECTION);

        

        //Create the scroll pane and add the tree to it. 
        treeView = new JScrollPane(tree);
        Dimension minimumSize = new Dimension(200, 50);
        treeView.setMinimumSize(minimumSize);


	}
}
