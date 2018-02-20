package enamel;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;

import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.Scanner;

import javax.sound.sampled.*;
import org.apache.commons.io.FilenameUtils;

public class SoundRecorder extends JFrame {

	protected boolean active;
	public JFrame frmAudio;
	static File selectedWavFile;
	ByteArrayOutputStream output;
	int option;
	static Controller controller;
	AudioInputStream audioIS;
	File exportFile;
	AudioFileFormat.Type fileType = AudioFileFormat.Type.WAVE;
	Boolean imported = false;
	//Boolean crashControl =  false;
	Clip aClip;

	public SoundRecorder(Controller c) {
		controller = c;
		frmAudio = new JFrame();
		frmAudio.setTitle("Audio Studio");
		frmAudio.setBounds(100, 100, 249, 239);
		frmAudio.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
		frmAudio.getContentPane().setLayout(null);

		final JButton btnCapture = new JButton("Capture");
		final JButton btnStop = new JButton("Stop");
		final JButton btnPlay = new JButton("Play");
		final JToolTip justDaTip = new JToolTip();

		JButton btnExport = new JButton("Export Selected wav File");
		btnExport.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				Controller.setAudioFile(exportFile.getName());
				Controller.appendSound();
				infoBox("Audio File exported! \nExiting!", "Success!");
				frmAudio.setVisible(false);
			}
		});
		btnExport.setEnabled(false);
		btnExport.setBounds(12, 124, 207, 29);
		btnExport.setToolTipText("<html>" + "Click this button only after you import an audio file you would like" + "<br>" + " to add to the scenario file" + "</html>");
		btnExport.getAccessibleContext().setAccessibleName("Export selected wave file");
		btnExport.getAccessibleContext().setAccessibleDescription("Click this button only after you import an audio file you would like to add to the scenario file");
		frmAudio.getContentPane().add(btnExport);

		JMenuBar menuBar = new JMenuBar();
		menuBar.getAccessibleContext().setAccessibleName("Menu Bar");
		menuBar.getAccessibleContext().setAccessibleDescription("Contains operations for using certain actions in application");
		menuBar.setToolTipText("Contains operations for using certain actions in application");
		frmAudio.setJMenuBar(menuBar);

		JMenu mnFile = new JMenu("File");
		mnFile.getAccessibleContext().setAccessibleName("File menu option");
		mnFile.getAccessibleContext().setAccessibleDescription("Click here for a list of other performable action for the application");
		mnFile.setToolTipText("Click here for a list of other performable action for the application");
		menuBar.add(mnFile);

		JMenuItem mntmOpen = new JMenuItem("Open");
		mntmOpen.getAccessibleContext().setAccessibleName("Open, Import File");
		mntmOpen.getAccessibleContext().setAccessibleDescription("Imports pre-exiting audio files into audio studio to be added to scenario File");
		mntmOpen.setToolTipText("Imports pre-exiting audio files into audio studio to be added to scenario File");
		mntmOpen.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				JFileChooser open = new JFileChooser("FactoryScenarios/AudioFiles/");
				open.getAccessibleContext().setAccessibleDescription("Dialog box to choose a file to be imported into Audio Studio");
				open.setToolTipText("Dialog box to choose a file to be imported into Audio Studio");
				FileNameExtensionFilter filter = new FileNameExtensionFilter("WAV Files", "wav");
				open.setFileFilter(filter);
				open.setFileSelectionMode(JFileChooser.FILES_ONLY);
				int retrunVal = open.showOpenDialog(frmAudio);
				if (retrunVal == JFileChooser.APPROVE_OPTION) // error testing
																// here
				{
					try {
						Scanner sc = new Scanner(new FileReader(open.getSelectedFile().getPath()));
						selectedWavFile = new File(open.getSelectedFile().getPath());
						imported = true;
						playAudio();
						btnExport.setEnabled(true);
					} catch (FileNotFoundException e1) {
						JOptionPane.showMessageDialog(null, e);
					}
				}
			}
		});
		mnFile.add(mntmOpen);

		JMenuItem mntmSave = new JMenuItem("Save");
		mntmSave.getAccessibleContext().setAccessibleName("Save File");
		mntmSave.getAccessibleContext().setAccessibleDescription("Saves recorded audio files from audio studio");
		mntmSave.setToolTipText("Saves recorded audio files from audio studio");
		
		mntmSave.addActionListener(new ActionListener() {
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
				btnExport.setEnabled(false);
				captureAudio();
			}
		};
		btnCapture.addActionListener(captureListener);

		ActionListener stopListener = new ActionListener() {
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
		btnPlay.setBounds(12, 0, 207, 29);
		btnPlay.setToolTipText("Plays recorded or imported audio file back to user " );
		btnPlay.getAccessibleContext().setAccessibleName("Play Audio");
		btnPlay.getAccessibleContext().setAccessibleDescription("Plays recorded or imported audio file back to user ");
		
		frmAudio.getContentPane().add(btnPlay);
		btnCapture.setBounds(12, 42, 207, 29);
		btnCapture.setToolTipText("Records user audio" );
		btnCapture.getAccessibleContext().setAccessibleName("Record Audio");
		btnCapture.getAccessibleContext().setAccessibleDescription("Records user audio");

		
		frmAudio.getContentPane().add(btnCapture);
		btnStop.setBounds(12, 84, 207, 29);
		btnStop.setToolTipText("Stops audio recording" );
		btnStop.getAccessibleContext().setAccessibleName("Stop audio Recording");
		frmAudio.getContentPane().add(btnStop);

		// aClip.loop(Clip.LOOP_CONTINUOUSLY);

	}
	// CONSTRUCTOR ENDS HERE

	private void captureAudio() {
		imported = false;
		try {
			final AudioFormat format = getFormat();
			DataLine.Info info = new DataLine.Info(TargetDataLine.class, format);

			TargetDataLine line = (TargetDataLine) AudioSystem.getLine(info);

			line.open();
			System.out.println("Recording Started...");
			line.start();

			Runnable runner = new Runnable() {
				int bufferSize = (int) format.getSampleRate() * format.getFrameSize();
				byte buffer[] = new byte[bufferSize];

				public void run() {
					output = new ByteArrayOutputStream(); // sim1
					active = true;
					try {
						while (active) {
							int count = line.read(buffer, 0, buffer.length); // sim2
							if (count > 0) {
								output.write(buffer, 0, count); // sim3
							}
						}

						output.close(); // diff
						line.stop();
						line.close();
					} catch (IOException e) // error testing here
					{
						System.err.println("I/O problems: " + e);
						System.exit(-1);
					}
				}
			};

			Thread captureThread = new Thread(runner);
			captureThread.start();

		}

		catch (LineUnavailableException e) // error testing here
		{
			System.err.println("Line unavailable: " + e);
			this.setVisible(false);
		}
	}

	// plays audio without saving file
	private void playAudio() {
		if (imported == false) {
			try {
				byte audio[] = output.toByteArray();
				InputStream input = new ByteArrayInputStream(audio);
				final AudioFormat format = getFormat();
				final AudioInputStream ais = new AudioInputStream(input, format, audio.length / format.getFrameSize());
				DataLine.Info info = new DataLine.Info(SourceDataLine.class, format);
				final SourceDataLine line = (SourceDataLine) AudioSystem.getLine(info);

				line.open(format);
				line.start();

				Runnable executer = new Runnable() {
					int bufferSize = (int) format.getSampleRate() * format.getFrameSize();
					byte buffer[] = new byte[bufferSize];

					public void run() {
						try {
							int count;
							while ((count = ais.read(buffer, 0, buffer.length)) != -1) {
								if (count > 0) {
									line.write(buffer, 0, count);
								}
							}
							line.drain();
							line.close();
							option = optionbox("Are you satisfied with your recording?", "Save Recording?");
							
							if (option == 0) {
								fileChooser();
								option = optionbox("Would you like to export recording to your scenario file?",
										"Export?");
								if (option == 0) {
									Controller.setAudioFile(exportFile.getName());
									Controller.appendSound();
									infoBox("Audio File exported! \nExiting!", "Success!");
									frmAudio.setVisible(false);

								} else {
									option = optionbox("Would you like to make a new recording?", "New recording?");
									if (option == 0) {
										// Do nothing honestly
									} else {
										infoBox("Exiting..", "Program Exiting");
										frmAudio.setVisible(false);
									}
								}
							} else {
								option = optionbox("Would you like to make a new recording?", "New recording?");
								if (option == 0) {
									// Do nothing honestly
								} else {
									infoBox("Exiting..", "Program Exiting");
									frmAudio.setVisible(false);
								}
							}

						} catch (IOException e) // error testing here
						{
							System.err.println("I/O problems: " + e);
							frmAudio.setVisible(false);
						}
					}
				};

				Thread playThread = new Thread(executer);
				playThread.start();

			} catch (LineUnavailableException e) // error testing here
			{
				System.err.println("Line unavailable: " + e);
				frmAudio.setVisible(false);
			}

		} else {
			if (imported) {
				try {
					audioIS = AudioSystem.getAudioInputStream(selectedWavFile);
				} catch (UnsupportedAudioFileException | IOException e1) {
					e1.printStackTrace();
				}
				// create clip reference
				try {
					aClip = AudioSystem.getClip();
				} catch (LineUnavailableException e1) // error testing here
				{
					e1.printStackTrace();
				}
				// open audioInputStream to the clip
				try {
					aClip.open(audioIS);
				} catch (LineUnavailableException e1) // error testing here
				{
					e1.printStackTrace();
				} catch (IOException e1) // error testing here
				{
					e1.printStackTrace();
				}
			}
			// For imported wav files
			aClip.start();
			exportFile = selectedWavFile;

		}
	}

	public AudioFormat getFormat() {
		// defines file format used to record
		float sampleRate = 44100;
		int sampleSizeInBits = 16;
		int channels = 2; // this means Stereo, 1 would me mono
		boolean signed = true;
		boolean bigEndian = true;
		return new AudioFormat(sampleRate, sampleSizeInBits, channels, signed, bigEndian);

	}

	public void save(File wavFile) throws IOException // error testing here
	{
		byte audio[] = output.toByteArray();
		InputStream input = new ByteArrayInputStream(audio);
		final AudioFormat format = getFormat();
		final AudioInputStream aisS = new AudioInputStream(input, format, audio.length / format.getFrameSize());

		AudioSystem.write(aisS, AudioFileFormat.Type.WAVE, wavFile);
		exportFile = wavFile; // for Exporting to scenarioFile
		aisS.close();
		output.close();
	}

	public void fileChooser() {
		JFileChooser wavSave = new JFileChooser("FactoryScenarios/AudioFiles/");
		int returnVal = wavSave.showSaveDialog(frmAudio);
		if (returnVal == JFileChooser.APPROVE_OPTION) {
			try {
				File file = wavSave.getSelectedFile();
				if (FilenameUtils.getExtension(file.getName()).equalsIgnoreCase("wav")) {
					// filename is OK as-is, Do nothing
				} else {
					file = new File(file.toString() + ".wav"); // append .wav to
																// end of file
																// name
					file = new File(file.getParentFile(), FilenameUtils.getBaseName(file.getName()) + ".wav");
				}
				save(file); // call the method that actually writes the audio to
							// the given file
			} catch (IOException e1) {
				e1.printStackTrace();
			}

		}
	}

	// public String getFile()
	// {
	// if (exportFile != null) {
	// System.out.println(exportFile.getName());
	// return exportFile.getName();
	// }
	// else {
	// return null;
	// }
	// }
	public static int optionbox(String infoMessage, String titleBar) {
		int dialogButton = JOptionPane.YES_NO_OPTION;
		int dialogResult = JOptionPane.showConfirmDialog(null, infoMessage, "InfoBox: " + titleBar, dialogButton);
		
		if (dialogResult == JOptionPane.YES_OPTION) {
			return 0;
		} else {
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

	public static void main(String args[]) {
		SoundRecorder window = new SoundRecorder(controller);
		window.frmAudio.setVisible(true);
	}
}