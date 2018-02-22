package enamel;

import static org.junit.Assert.*;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Random;

import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;
import org.junit.Rule;
import org.junit.rules.Timeout;


public class ListManagerTest {
	
	/**
	 * Tests initialize with different cell and button numbers
	 */
	@Test
	public void test00_init() {
		int expCells;
		int expButtons;
		ListManager t;
		
		Random rng = new Random();
		for (int i = 0; i < 10; i++) {
			expCells = rng.nextInt(20);
			expButtons = rng.nextInt(20);
			t = new ListManager(expCells, expButtons);
			assertEquals(expCells, t.cells);
			assertEquals(expButtons, t.buttons);
		}
	}
	

	/**
	 * Tests adding to the end of the list.
	 */
	@Test
	public void test01_add() {
		
		List<Integer> exp = new ArrayList<>();
		ListManager t = new ListManager(9,9);
		
		Random rng = new Random();
		for (int i = 0; i < 10; i++) {
			String val1 = Integer.toString(rng.nextInt(100));
			String val2 = Integer.toString(rng.nextInt(100));
			t.addNext(val1, val2);
			t.index = t.currentList.size() - 1;
			assertEquals(val1, t.getKeyPhrase());
			assertEquals(val2, t.getData());
		}
	}
	
	/**
	 * Tests getting the size of the list.
	 */
	@Test
	public void test02_size() {
		ListManager t = new ListManager(9,9);
		for (int i = 1; i < 100; i++) {
			assertEquals(i, t.currentList.size());
			t.addNext("","");
		}
	}
	
	/**
	 * Tests the isEmpty method.
	 */
	@Test
	public void test02_index() {
		ListManager t = new ListManager(9,9);
		for (int i = 0; i < 100; i++) {
			assertEquals(i, t.index);
			t.addNext("","");
		}
	}
	
	/**
	 * Tests the getData method
	 * exception.
	 */
	@Test
	public void test04_getData() {
		ArrayList<Node> exp = new ArrayList<>();
		ListManager t = new ListManager(9,9);
		
		Random rng = new Random();
		for (int i = 10; i < 20; i++) {
			try {
				t.getData(i);
				fail(String.format("getData(%d) should have thrown an exception for list: %s", 
						i, exp));
			}
			catch (IndexOutOfBoundsException x) {
				// ok
			}
			catch (Exception x) {
				fail(String.format("getData(%d) threw an unexpected exception of type %s", 
						i, x.getClass().getName()));
			}
		}
	}
	
	/**
	 * Tests the get method; includes testing of the expected
	 * exception.
	 */
	@Test
	public void test05_getKeyPhrase() {
		ArrayList<Node> exp = new ArrayList<>();
		ListManager t = new ListManager(9,9);
		
		Random rng = new Random();
		for (int i = 10; i < 20; i++) {
			try {
				t.getKeyPhrase(i);
				fail(String.format("getKeyPhrase(%d) should have thrown an exception for list: %s", 
						i, exp));
			}
			catch (IndexOutOfBoundsException x) {
				// ok
			}
			catch (Exception x) {
				fail(String.format("getKeyPhrase(%d) threw an unexpected exception of type %s", 
						i, x.getClass().getName()));
			}
		}
	}
	
	@Test
	public void test06_createJunction() {
		ListManager t = new ListManager(9,9);
		HashMap<Integer, String> s = new HashMap<Integer, String>();
		s.put(1, "apple");
		s.put(2, "banana");
		s.put(4, "dog");
		t.createJunction(s);
		t.index = 1;
		assertEquals(t.getNode().buttonsNames,s);
		assertEquals(t.getNode().buttons.get(1).get(0).data,"apple");
		assertEquals(t.getNode().buttons.get(2).get(0).data,"banana");
		assertEquals(t.getNode().buttons.get(3),null);
		assertEquals(t.getNode().buttons.get(4).get(0).data,"dog");
	}
	
	@Test
	public void test07_junctionGoto() {
		ListManager t = new ListManager(9,9);
		HashMap<Integer, String> s = new HashMap<Integer, String>();
		s.put(1, "apple");
		s.put(2, "banana");
		s.put(4, "dog");
		t.createJunction(s);
		t.index = 1;
		
		t.junctionGoto(1);
		assertEquals(t.getNode(),t.currentList.get(0));
		t.prev();
		t.junctionGoto(2);
		assertEquals(t.getNode(),t.currentList.get(0));
		t.prev();
		t.junctionGoto(4);
		assertEquals(t.getNode(),t.currentList.get(0));
	}
	
	@Test
	public void test08_junctionSearch() {
		ListManager t = new ListManager(9,9);
		HashMap<Integer, String> s = new HashMap<Integer, String>();
		s.put(1, "apple");
		s.put(2, "banana");
		s.put(4, "dog");
		t.createJunction(s);
		t.index = 1;
		
		assertEquals(1, t.junctionSearch("apple"));
		assertEquals(2, t.junctionSearch("banana"));
		assertEquals(-1,t.junctionSearch("notHere"));
	}
}
