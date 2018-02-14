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
	  private File selectedWavFile;
	  AudioFileFormat.Type fileType = AudioFileFormat.Type.WAVE;
	  Boolean imported = false;
	  
	  public SoundRecorder() {
		frmAudio = new JFrame();
		frmAudio.setTitle("Audio Studio");
		frmAudio.setBounds(100, 100, 368, 425);
		frmAudio.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frmAudio.getContentPane().setLayout(null);
	 
	    final JButton capture = new JButton("Capture");
	    final JButton stop = new JButton("Stop");
	    final JButton btnplay = new JButton("Play");
	   	   	    
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
			    });
			  mnFile.add(mntmSave);
			  
	    capture.setEnabled(true);
	    stop.setEnabled(false);
	    btnplay.setEnabled(false);
	    mntmSave.setEnabled(false);
	   
	 
	    ActionListener captureListener = new ActionListener() {
	      public void actionPerformed(ActionEvent e) {
	        capture.setEnabled(false);
	        stop.setEnabled(true);
	        btnplay.setEnabled(false);
	        mntmSave.setEnabled(true);
	        captureAudio();
	        }
	    };
	    capture.addActionListener(captureListener);
	 
	    ActionListener stopListener =  new ActionListener() {
	      public void actionPerformed(ActionEvent e) {
	        capture.setEnabled(true);
	        stop.setEnabled(false);
	        btnplay.setEnabled(true);
	        active = false;
	      }
	    };
	    
	    stop.addActionListener(stopListener);
	 
	    ActionListener playListener = new ActionListener() {
	      public void actionPerformed(ActionEvent e) {
	        playAudio();
	      }
	    };
	    
	   
		
	    btnplay.addActionListener(playListener); 	//LOOK HERE TO ASK USER TO SAVE THEIR FILE OR REPLAY IT 
	    btnplay.setBounds(93, 98, 117, 29);
	    frmAudio.getContentPane().add(btnplay);
	    capture.setBounds(93, 140, 117, 29);
	    frmAudio.getContentPane().add(capture);
	    stop.setBounds(93, 182, 117, 29);
	    frmAudio.getContentPane().add(stop);
	    
	   
	  
	  
	  }
	  // CONSTRUCTOR ENDS HERE
 
	  private void captureAudio()
	  {
	    try
	    {
	      final AudioFormat format = getFormat();
	      DataLine.Info info = new DataLine.Info(
	        TargetDataLine.class, format);
	      final TargetDataLine line = (TargetDataLine)AudioSystem.getLine(info);
	      
	      line.open(format);
	      line.start();
	      
	      Runnable runner = new Runnable() {
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
	          	} 
	          catch (IOException e) {
	            System.err.println("I/O problems: " + e);
	            System.exit(-1);
	          }
	        }
	      };
	      Thread captureThread = new Thread(runner);
	      captureThread.start();
	    } catch (LineUnavailableException e) {
	      System.err.println("Line unavailable: " + e);
	      System.exit(-2);
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
		// ___ above code might be useful for saving it appears twice so far 
		      
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
		          } 
		          catch (IOException e)
		          {
		            System.err.println("I/O problems: " + e);
		            System.exit(-3);
		          }
		        }
		      };
		      
		      Thread playThread = new Thread(executer);
		      playThread.start();
		      
		    }
		    catch (LineUnavailableException e)
		    {
		      System.err.println("Line unavailable: " + e);
		      System.exit(-4);
		    } 
	  	}
		else
		{ //For imported wav files
			  try {
		            AudioInputStream ais = AudioSystem.getAudioInputStream(selectedWavFile);
		            Clip test = AudioSystem.getClip();
		            test.open(ais);
		            test.start();
		            test.drain();
		            test.close();
		        }catch(Exception ex){
		            ex.printStackTrace();
		        }
		}
	  }
	  
	
	  private AudioFormat getFormat() { //defines file format used to record
	    float sampleRate = 16000;
	    int sampleSizeInBits = 8;
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
		 
	      aisS.close();
		  output.close();
		}
	 public void close() {
		 frmAudio.setVisible(false);
			frmAudio.dispose();
		}

	public static void main(String args[])
	  {
		SoundRecorder window = new SoundRecorder();
	    window.frmAudio.setVisible(true);
	  }
	}