package enamel;

	import javax.swing.*;
	import java.awt.*;
	import java.awt.event.*;
	import java.io.*;
import java.util.Scanner;

import javax.sound.sampled.*;	
	import org.apache.commons.io.FilenameUtils;
	 
	public class SoundRecorder extends JFrame {
	 
		protected boolean active;
		public JFrame frmAudio;
		ByteArrayOutputStream output;
		
		AudioInputStream audioIS;
		static File selectedWavFile;
		Clip aClip;
		
		File exportFile;
		AudioFileFormat.Type fileType = AudioFileFormat.Type.WAVE;
		Boolean imported = false;
		Boolean written = false;
		int option;
		static Controller controller;
		// Icons used for buttons
			//ImageIcon iconRecord = new ImageIcon(getClass().getResource("/progResources/Record.png"));
		//private	ImageIcon iconStop = new ImageIcon(getClass().getResource("/progResources/Stop.png"));
		//private	ImageIcon iconPlay = new ImageIcon(getClass().getResource("/progResources/Play.png"));
	  
	  public SoundRecorder(Controller c)
	  {
		controller = c;
		frmAudio = new JFrame();
		frmAudio.setTitle("Audio Studio");
		frmAudio.setBounds(100, 100, 310, 239);
		frmAudio.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
		frmAudio.getContentPane().setLayout(null);
	 
	    final JButton btnCapture = new JButton("Capture");
	    btnCapture.setIcon(new ImageIcon("C:\\Users\\Akin\\git\\ExMech\\Enamel\\progResources\\Record.ico"));
	    final JButton btnStop = new JButton("Stop");
	    final JButton btnPlay = new JButton("Play");
	    btnPlay.setIcon(new ImageIcon("C:\\Users\\Akin\\git\\ExMech\\Enamel\\progResources\\Play.ico"));
	    
	    //capture.setIcon(iconRecord);
	    //stop.setIcon(iconStop);
	    //btnplay.setIcon(iconPlay);
	   	   	    
		 JMenuBar menuBar = new JMenuBar();
		frmAudio.setJMenuBar(menuBar);
			
		JMenu mnFile = new JMenu("File");
		menuBar.add(mnFile);
			
		JMenuItem mntmOpen = new JMenuItem("Open");
		mntmOpen.addActionListener(new ActionListener() {
		public void actionPerformed(ActionEvent e) {
		JFileChooser open = new JFileChooser("FactoryScenarios/AudioFiles/");
		int retrunVal = open.showOpenDialog(frmAudio);
		if (retrunVal == JFileChooser.APPROVE_OPTION) 
		{
			try 
			{
			Scanner sc = new Scanner(new FileReader(open.getSelectedFile().getPath()));
			selectedWavFile = new File(open.getSelectedFile().getPath());
			imported= true;
			playAudio();
			} 
			catch (FileNotFoundException e1) 
				{
				JOptionPane.showMessageDialog(null, e);
				}
		}
				}
			});
			mnFile.add(mntmOpen);
			
			JMenuItem mntmSave = new JMenuItem("Save");
			  mntmSave.addActionListener( new ActionListener() {
			      public void actionPerformed(ActionEvent e) {
			    	fileChooser();
			      }
			    });
			  mnFile.add(mntmSave);
			  
	    btnCapture.setEnabled(true);
	    btnStop.setEnabled(false);
	    btnPlay.setEnabled(false);
	    mntmSave.setEnabled(false);
	   
	 
	    ActionListener captureListener = new ActionListener() {
	      public void actionPerformed(ActionEvent e) {
	        btnCapture.setEnabled(false);
	        btnStop.setEnabled(true);
	        btnPlay.setEnabled(false);
	        mntmSave.setEnabled(true);
	        captureAudio();
	        }
	    };
	    btnCapture.addActionListener(captureListener);
	 
	    ActionListener stopListener =  new ActionListener() {
	      public void actionPerformed(ActionEvent e) {
	        btnCapture.setEnabled(true);
	        btnStop.setEnabled(false);
	        btnPlay.setEnabled(true);
	        active = false;
	      }
	    };
	    
	    btnStop.addActionListener(stopListener);
	 
	    ActionListener playListener = new ActionListener() {
	      public void actionPerformed(ActionEvent e) {
	        playAudio();
	      }
	    };   
		
	    btnPlay.addActionListener(playListener); 
	    btnPlay.setBounds(88, 0, 117, 29);
	    frmAudio.getContentPane().add(btnPlay);
	    btnCapture.setBounds(88, 37, 117, 29);
	    frmAudio.getContentPane().add(btnCapture);
	    btnStop.setBounds(88, 81, 117, 29);
	    frmAudio.getContentPane().add(btnStop);
	    
	   
         
       // aClip.loop(Clip.LOOP_CONTINUOUSLY);
	  
	  }
	  // CONSTRUCTOR ENDS HERE
 
	  private void captureAudio()
	  {
		  imported = false;
	    try
	    { 
	    	final AudioFormat format = getFormat();
	      DataLine.Info info = new DataLine.Info(TargetDataLine.class, format);
	       
	       TargetDataLine line = (TargetDataLine) AudioSystem.getLine(info);
	      
	      line.open();
	      System.out.println("Recording Started...");
	      line.start();
	      
	      Runnable runner = new Runnable() 
	      {
	    	  int bufferSize = (int)format.getSampleRate()* format.getFrameSize();
	    	  byte buffer[] = new byte[bufferSize];
	  
	        public void run() {
	          output = new ByteArrayOutputStream(); //sim1
	          active = true;
	          try {
	        	  while (active) 
		            {
		              int count =  line.read(buffer, 0, buffer.length); //sim2
			              if (count > 0)
			              {
			                output.write(buffer, 0, count); //sim3
			              }
		            }
	           
	            output.close(); //diff
	            line.stop();
	            line.close();
	          	} 
	          catch (IOException e) {
	            System.err.println("I/O problems: " + e);
	            System.exit(-1);
	          }
	        }
	      };
	      
	    	      Thread captureThread = new Thread(runner);
	      captureThread.start();

	    } 
	       
	    
	    catch (LineUnavailableException e) {
	      System.err.println("Line unavailable: " + e);
	      this.setVisible(false);
	    }
	  }
	 //plays audio without saving file
	  private void playAudio() 
	  { 
		  if(imported == false)
		  {
			try 
		    {
		      byte audio[] = output.toByteArray(); //Sim3 but for saving. come here 
		      InputStream input =  new ByteArrayInputStream(audio); //Sim4
		      final AudioFormat format = getFormat();
		      final AudioInputStream ais = new AudioInputStream(input, format, audio.length / format.getFrameSize());
		     //Above code is diff, AIS is used instead of ByteArrayInputStream
		      DataLine.Info info = new DataLine.Info(SourceDataLine.class, format);
		      final SourceDataLine line = (SourceDataLine) AudioSystem.getLine(info);
		     
		      line.open(format);
		      line.start();
	
		      
		      Runnable executer = new Runnable() 
		      {
		        int bufferSize = (int) format.getSampleRate() * format.getFrameSize();
		        byte buffer[] = new byte[bufferSize];
		  
		        public void run()
		        {
		          try 
		          {
		            int count;
		            while ((count = ais.read(buffer, 0, buffer.length)) != -1) {
		              if (count > 0)
		              {
		                line.write(buffer, 0, count);
		              }
		            }
		            line.drain();
		            line.close();
		            option = optionbox("Are you satisfied with your recording?", "Save Recording?");
		            if(option == 0)
		            {   
		            	fileChooser();
		            	try {
							Thread.sleep(1000);
						} catch (InterruptedException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
		            	option = optionbox("Would you like to export recording to your scenario file?", "Export?");
		            	 if (option == 0)		            		 
		            	 {	
		            		 Controller.AudioFile = exportFile.getName();
		            		 frmAudio.setVisible(false);     	 	
		            		 
		         	    //Do something
		            	 }
		            	 else 
		            	 {	 
		            		 option = optionbox("Would you like to make a new recording?", "New recording?");
			            	 if (option == 0)
			            	 {
			         	    //Do nothing honestly
			            	 }
			            	 else 
			            	 {	 
			            		 infoBox("Exiting..","Program Exiting");
			            		 frmAudio.setVisible(false);
			            	 }
		            	 }
		            }
		            else
		            {
		            	option = optionbox("Would you like to make a new recording?", "New recording?");
		            	 if (option == 0)
		            	 {
		         	    //Do nothing honestly
		            	 }
		            	 else 
		            	 {	 
		            		 infoBox("Exiting..","Program Exiting");
		            		 frmAudio.setVisible(false);
		            	 }
		            }
		            
		          } 
		          catch (IOException e)
		          {
		            System.err.println("I/O problems: " + e);
		            frmAudio.setVisible(false);
		          }
		        }
		      };
		      
		      Thread playThread = new Thread(executer);
		      playThread.start();
		      
		    }
		    catch (LineUnavailableException e)
		    {
		      System.err.println("Line unavailable: " + e);
		      frmAudio.setVisible(false);
		    } 
			
		  }
		else
		{ 	if(imported) {
			try {
				audioIS = AudioSystem.getAudioInputStream(selectedWavFile);
			} catch (UnsupportedAudioFileException | IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
	         
	        // create clip reference
	        try {
				aClip = AudioSystem.getClip();
			} catch (LineUnavailableException e1) {
				e1.printStackTrace();
			}
	         
	        // open audioInputStream to the clip
	        try {
				aClip.open(audioIS);
				}
	        catch (LineUnavailableException e1) {
				e1.printStackTrace();
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		   }
		}
		  //For imported wav files
			 aClip.start();
//			 if(!aClip.isActive())
//			 {
//				 if (option == 0)		            		 
//            	 {	
//            		 Controller.AudioFile = selectedWavFile.getName();
//            		 frmAudio.setVisible(false);     	 	
//            		 
//         	    //Do something
//            	 }
//            	 else 
//            	 {	 
//            		 option = optionbox("Would you like to make a new recording?", "New recording?");
//	            	 if (option == 0)
//	            	 {
//	         	    //Do nothing honestly
//	            	 }
//	            	 else 
//	            	 {	 
//	            		 infoBox("Exiting..","Program Exiting");
//	            		 frmAudio.setVisible(false);
//	            	 }
//            	 }
//			 }
		
	  }
	  
	
	  private AudioFormat getFormat() { //defines file format used to record
		  float sampleRate = 44100;
		    int sampleSizeInBits = 16;
		    int channels = 2;
		    boolean signed = true;
		    boolean bigEndian = true;
		    return new AudioFormat(sampleRate, 
		      sampleSizeInBits, channels, signed, bigEndian);
		    
	  }
	  
	 public void save(File wavFile) throws IOException 
	  {	  byte audio[] = output.toByteArray(); //Sim3 but for saving. come here 
	      InputStream input =  new ByteArrayInputStream(audio); //Sim4
	      final AudioFormat format = getFormat();
	      final AudioInputStream aisS = new AudioInputStream(input, format, audio.length / format.getFrameSize());
	      
	      AudioSystem.write(aisS, AudioFileFormat.Type.WAVE, wavFile);
	      exportFile = wavFile;
	      written = true;
	      aisS.close();
		  output.close();
		}
	 public void fileChooser() {
		 JFileChooser wavSave = new JFileChooser("FactoryScenarios/AudioFiles/");
   	  int returnVal = wavSave.showSaveDialog(frmAudio);
   	  if(returnVal == JFileChooser.APPROVE_OPTION)
   	  {
   		  try
   		  {
   			  File file = wavSave.getSelectedFile();
   			  if (FilenameUtils.getExtension(file.getName()).equalsIgnoreCase("wav")) {
					    // filename is OK as-is
					} else {
					    file = new File(file.toString() + ".wav");  // append .wav if "meh.jpg.txt" is OK
					    file = new File(file.getParentFile(), FilenameUtils.getBaseName(file.getName())+".wav");
					    
					   
					}
   			 save(file);
   		  }
   		  catch(IOException e1)
   		  {
   			  e1.printStackTrace();
   		  }
      
     }
	 }
	 public String getFile() {
		 if (exportFile != null) {
			 System.out.println(exportFile.getName());
			 return exportFile.getName();
		 }
		 else {
			 return null;
		 }
	 }
	 public static int optionbox(String infoMessage, String titleBar)
	 {	 int dialogButton = JOptionPane.YES_NO_OPTION;
		 int dialogResult = JOptionPane.showConfirmDialog(null, infoMessage,"InfoBox: " + titleBar,dialogButton);
		 
		 if(dialogResult == JOptionPane.YES_OPTION)
		 {
		  return 0;
		 }
		 else {
		 return 1;
	    }
	 }
	 public static void infoBox(String infoMessage, String titleBar) {
		    JOptionPane.showMessageDialog(null, infoMessage, "InfoBox: " + titleBar, JOptionPane.INFORMATION_MESSAGE);
		  }
	 
	 public void close() {
		 frmAudio.setVisible(false);
			frmAudio.dispose();
		}

	
	public static void main(String args[])
	  {
		SoundRecorder window = new SoundRecorder(controller);
	    window.frmAudio.setVisible(true);
	  }
	}