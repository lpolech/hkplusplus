package test.data;

import static org.junit.Assert.*;

import java.awt.Color;

import org.junit.Test;

import data.ColorPalette;



public class TestColorPalette {

	ColorPalette collorPallete;
	
	public TestColorPalette() 
	{
		collorPallete=new ColorPalette();
		System.out.println("tes");
	}
	
	@Test
	public void testResetColorNumber() {
		ColorPalette.resetColorNumber();
		assertEquals(new Color(68,255,0), collorPallete.getNextColor());
		ColorPalette.resetColorNumber();
		assertEquals(new Color(68,255,0), collorPallete.getNextColor());
	}

	@Test
	public void testGetNextColor() {
		ColorPalette.resetColorNumber(); //nie wiem czy potrzebne
		assertEquals(new Color(68,255,0), collorPallete.getNextColor());
		assertEquals(new Color(230,0,153), collorPallete.getNextColor());
		for (int i=0; i<98; i++)
		{
			collorPallete.getNextColor();
		}
		assertEquals(new Color(68,255,0), collorPallete.getNextColor());
		assertEquals(new Color(230,0,153), collorPallete.getNextColor());
	}

}
