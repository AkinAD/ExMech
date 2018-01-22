package enamel;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.filechooser.FileSystemView;

public class Mainframe {
	
	static JFrame Main_Frame;
	File selectedFile;
	
	public Mainframe() {
		
		//This is the main dialogue that shows when you hit run. 

		Main_Frame = new JFrame("Scenario Picker"); //Akin made  change here to test nvda
		Main_Frame.setVisible(true);
		Main_Frame.setSize(500, 200);

		JPanel Main_Panel = new JPanel();
		Main_Panel.getAccessibleContext().setAccessibleDescription("Scenario file picker");
		JButton File_Dialog = new JButton("Read File Name");

		File_Dialog.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				fileDialog();
			}
		});
		
		
		Main_Panel.add(File_Dialog);
		
		Main_Frame.add(Main_Panel);
		Main_Frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	
	}
	
	public void fileDialog() {

		//This panel runs when you hit the "Read File Name" button
		
		JFileChooser File_Chooser = new JFileChooser("FactoryScenarios/");
		File_Chooser.setDialogTitle("VoiceOver Demo");
		File_Chooser.getAccessibleContext().setAccessibleDescription("Displays list of system files");
		File_Chooser.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);

		int returnVal = File_Chooser.showDialog(null, "Read Name");
		
		if (returnVal == JFileChooser.APPROVE_OPTION) {
			System.out.println("File chosen was: " + File_Chooser.getSelectedFile().getName());
			selectedFile = File_Chooser.getSelectedFile();
			
			
			
			//To get the name of the file for voice over, use this "File_Chooser.getSelectedFile().getName()"
		}
	
	}
	
	public File getSelectedFile() {
		return selectedFile;
	}
	
	public void close() {
		Main_Frame.setVisible(false);
		Main_Frame.dispose();
	}

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		Mainframe obj = new Mainframe();

	}

}
