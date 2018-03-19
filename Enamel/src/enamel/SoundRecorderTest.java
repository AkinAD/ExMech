package enamel;

import static org.junit.Assert.*;
import static org.junit.jupiter.api.Assertions.*;

import java.awt.AWTException;
import java.awt.Robot;
import java.awt.event.InputEvent;

import javax.sound.sampled.AudioFormat;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class SoundRecorderTest {
  SoundRecorder SR;
	@BeforeAll
	static void setUpBeforeClass() throws Exception {
		
	}
	

	@BeforeEach
	void setUp() throws Exception {
	}

	@Test
	void testSoundRecorder() throws AWTException {
		//fail("Not yet implemented"); // TODO
		Robot bot = new Robot();
		bot.mouseMove(12,42);
		bot.mousePress(InputEvent.BUTTON1_MASK);
		//add time between press and release or the input event system may 
		//not think it is a click
		try{Thread.sleep(250);}catch(InterruptedException e){}
		bot.mouseRelease(InputEvent.BUTTON1_MASK);
	}

	@Test
	void testSave() {
		
		//fail("Not yet implemented"); // TODO
	}

//	@Test
//	void testFileChooser() {
//		fail("Not yet implemented"); // TODO
//	}

	@Test
	void testOptionbox() {
	//	fail("Not yet implemented"); // TODO
	}

	@Test
	void testInfoBox() {
		//fail("Not yet implemented"); // TODO
	}

	@Test
	void testClose() {
		//fail("Not yet implemented"); // TODO
	}

	@Test
	void testMain() {
		
		//fail("Not yet implemented"); // TODO
	}

	@Test
	public void testPlayAudio() throws Exception {
		throw new RuntimeException("not yet implemented");
	}


	@Test
	public void testGetFormat() throws Exception {
		AudioFormat EF = new AudioFormat(44100, 16, 2, true, true);
		//AudioFormat GF = SoundRecorder.getFormat();
		assertEquals("Failed: getFormat()", EF, SoundRecorder.getFormat());
		throw new RuntimeException("not yet implemented");
	}


	@Test
	public void testCaptureAudio() throws Exception {
		throw new RuntimeException("not yet implemented");
	}


	@Test
	public void testFileChooser() throws Exception {
		throw new RuntimeException("not yet implemented");
	}

}
