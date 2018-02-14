package enamel;

import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.JFrame;
import javax.swing.JTabbedPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.ScrollPaneConstants;
import javax.swing.JButton;

public class TabbedPaneDemo {

	public JFrame frame;
	public JTextArea textArea;

	public TabbedPaneDemo() {
		frame = new JFrame("DEMO");
		frame.setBounds(100, 100, 650, 400);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);
		
		JTabbedPane tabbedPane_1 = new JTabbedPane(JTabbedPane.TOP);
		tabbedPane_1.setBounds(19, 31, 290, 303);
		frame.getContentPane().add(tabbedPane_1);
		
		JPanel panel_1A = new JPanel();
		tabbedPane_1.addTab("Scenario", null, panel_1A, null);
		panel_1A.setLayout(null);
		
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_ALWAYS);
		scrollPane.setBounds(6, 6, 257, 245);
		panel_1A.add(scrollPane);
		
		textArea = new JTextArea();
		scrollPane.setViewportView(textArea);
		textArea.setEditable(true);
				
		JPanel panel_1B = new JPanel();
		tabbedPane_1.addTab("New tab", null, panel_1B, null);
		panel_1B.setLayout(null);
		
//---------------------------------------------------------------------------------------------------------------------------------------
		
		JTabbedPane tabbedPane_2 = new JTabbedPane(JTabbedPane.TOP);
		tabbedPane_2.setBounds(340, 31, 290, 303);
		frame.getContentPane().add(tabbedPane_2);
		
		JPanel panel_2A = new JPanel();
		tabbedPane_2.addTab("Buttons", null, panel_2A, null);
		panel_2A.setLayout(null);
		
		JButton btnPrev = new JButton("Previous");
		btnPrev.setBounds(73, 10, 117, 29);
		panel_2A.add(btnPrev);
		
		JButton btnNext = new JButton("Next");
		btnNext.setBounds(73, 40, 117, 29);
		panel_2A.add(btnNext);
		
		JButton btnBranch = new JButton("Branch it!");
		btnBranch.setBounds(73, 70, 117, 29);
		panel_2A.add(btnBranch);
		
		JButton btnAdd = new JButton("Add Text");
		btnAdd.setBounds(73, 100, 117, 29);
		panel_2A.add(btnAdd);
		
		JButton btnSample = new JButton("Sample");
		btnSample.setBounds(73, 130, 117, 29);
		panel_2A.add(btnSample);
		
		JPanel panel_2B = new JPanel();
		tabbedPane_2.addTab("New tab", null, panel_2B, null);
		panel_2B.setLayout(null);
	}
	
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					TabbedPaneDemo window = new TabbedPaneDemo();
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

}

