package enamel;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
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
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.KeyStroke;
import javax.swing.ScrollPaneConstants;
import javax.swing.SwingUtilities;
import javax.swing.border.BevelBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.filechooser.FileSystemView;
import javax.swing.text.BadLocationException;


public class NavigationDemo {
	
	protected JFrame frame;	
	protected JPanel panel;
	protected JTextArea textArea;
	protected File selectedFile;
	BevelBorder bevel;
	EmptyBorder empty;
	
	int position = 0;	//position of the selected label (0, 1 or 2)
	int listSize = 0;	//size of array list
	int elemIndex = 1;	//index of selected element in list (starts with 1)
	
	public NavigationDemo() {
		
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
		
		listSize = derp.currentList.size();	//listSize set to the size of current list
		
		frame = new JFrame("DEMO");
		frame.setBounds(100, 100, 800, 400);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);
		
		JTabbedPane tabbedPane = new JTabbedPane(JTabbedPane.TOP);
		tabbedPane.setBounds(19, 31, 426, 212);
		frame.getContentPane().add(tabbedPane);
		
		JPanel panel = new JPanel();
		tabbedPane.addTab("Scenarios", null, panel, null);
		panel.setLayout(new GridLayout(3, 0));

	    JTabbedPane tabbedPane_2 = new JTabbedPane(JTabbedPane.TOP);
		tabbedPane_2.setBounds(479, 31, 301, 303);
		frame.getContentPane().add(tabbedPane_2);
		
		JPanel panel_2 = new JPanel();
		tabbedPane_2.addTab("Buttons", null, panel_2, null);
		panel_2.setLayout(null);
		
		JTabbedPane tabbedPane_3 = new JTabbedPane(JTabbedPane.TOP);
		tabbedPane_3.setBounds(19, 238, 426, 121);
		frame.getContentPane().add(tabbedPane_3);
		
//		JPanel panel_3 = new JPanel();		//Fuck radio buttons
//		tabbedPane_3.addTab("Branches", null, panel_3, null);
//		panel_3.setLayout(null);
//		
//		JRadioButton rdbtnUno = new JRadioButton(list2.get(0));
//		rdbtnUno.hide();
//		rdbtnUno.setBounds(6, 24, 108, 23);
//		panel_3.add(rdbtnUno);
//		
//		JRadioButton rdbtnDos = new JRadioButton(list3.get(0));
//		rdbtnDos.hide();
//		rdbtnDos.setBounds(149, 24, 108, 23);
//		panel_3.add(rdbtnDos);
//		
//		JRadioButton rdbtnTres = new JRadioButton(list4.get(0));
//		rdbtnTres.hide();
//		rdbtnTres.setBounds(291, 24, 108, 23);
//		panel_3.add(rdbtnTres);
		
	    bevel = new BevelBorder(BevelBorder.RAISED);

		JLabel label[] = new JLabel[3];		//INITIALIZE 3 LABELS. 
	    label[0] = new JLabel();
	    label[1] = new JLabel();
	    label[2] = new JLabel();
	    
	    label[0].setBorder(bevel);		//add border to first label
        
	    //Assigning data to the first 3 labels.Got rid of the mouse shit even though i liked it...
	    for (int i = 0; i < label.length; i++) {
	    	label[i].setText(derp.getData());
	        label[i].setHorizontalAlignment(JLabel.CENTER);
	        panel.add(label[i]);
	    	derp.next();
	      }
	    
	    derp.prev();
		derp.prev();
		derp.prev();
		//POSITION IS NOW AT ROOT
	    
		//Random code things happening 
	    JButton btnNext = new JButton("Next");
	    btnNext.addActionListener(new ActionListener() {
		public void actionPerformed(ActionEvent e) {
			for (int k = 0; k < label.length; k++) {
		        if (label[k].getBorder() == bevel) {
		        	position = k;
		        }
		      }
			if (position < label.length - 1) {
				for (int k = 0; k < label.length; k++) {
			        label[k].setBorder(empty);
			      }
				position++;
				label[position].setBorder(bevel);
				elemIndex++;
        	    derp.next();
				String text = label[position].getText();
        	    System.out.println("Selected Node: " + text + ", Positon: " + position + ", Data Type: " + derp.getData() + ", Index: " + elemIndex);
			} else {
				if (listSize - 1 > label.length) {
			    	derp.prev();
			    	derp.prev();
				    for (int i = 0; i < label.length; i++) {
				    	derp.next();
				    	label[i].setText(derp.getData());
				      }
					listSize--;
					elemIndex++;
					String text = label[position].getText();
	        	    System.out.println("Selected Node: " + text + ", Positon: " + position + ", Data Type: " + derp.getData() + ", Index: " + elemIndex);
				} else {
					System.out.println("Listed has ended");
				}
			}
		}
	});
		btnNext.setBounds(73, 10, 117, 29);
		panel_2.add(btnNext);
		
		//Random code things happening 
		JButton btnPrev = new JButton("Previous");
		btnPrev.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				for (int k = 0; k < label.length; k++) {
			        if (label[k].getBorder() == bevel) {
			        	position = k;
			        }
			      }
				if (position > 0) {
					for (int k = 0; k < label.length; k++) {
				        label[k].setBorder(empty);
				      }
					position--;
					label[position].setBorder(bevel);
					elemIndex--;
	        	    derp.prev();
					String text = label[position].getText();
	        	    System.out.println("Selected Node: " + text + ", Positon: " + position + ", Data Type: " + derp.getData() + ", Index: " + elemIndex);
				} else {
					if (position == 0 && elemIndex > 1) {
				    	derp.next();
				    	derp.next();
					    for (int i = label.length - 1; i >= 0; i--) {
					    	derp.prev();
					    	label[i].setText(derp.getData());
					      }
					    listSize++;
					    elemIndex--;
						String text = label[position].getText();
		        	    System.out.println("Selected Node: " + text + ", Positon: " + position + ", Data Type: " + derp.getData() + ", Index: " + elemIndex);
					} else {
						System.out.println("Already at root");
					}
				}
			}
		});
		btnPrev.setBounds(73, 40, 117, 29);
		panel_2.add(btnPrev);
	}
	
	public static void main(String[] args) {
		NavigationDemo window = new NavigationDemo();
		window.frame.setVisible(true);
	}
}
