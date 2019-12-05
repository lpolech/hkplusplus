package test.data;

import static org.junit.Assert.assertEquals;

import java.awt.Color;

import org.junit.Test;

import data.ColorPalette;

public class TestColorPalette {

	ColorPalette collorPallete;

	public TestColorPalette() {
		collorPallete = new ColorPalette();
		System.out.println("tes");
	}

	@Test
	public void testResetColorNumber() {
		ColorPalette.resetColorNumber();
		assertEquals(new Color(68, 255, 0), ColorPalette.getNextColor());
		ColorPalette.resetColorNumber();
		assertEquals(new Color(68, 255, 0), ColorPalette.getNextColor());
	}

	@Test
	public void testGetNextColor() {
		ColorPalette.resetColorNumber();
		assertEquals(new Color(68, 255, 0), ColorPalette.getNextColor());
		assertEquals(new Color(230, 0, 153), ColorPalette.getNextColor());
		for (int i = 0; i < 98; i++) {
			ColorPalette.getNextColor();
		}
		assertEquals(new Color(68, 255, 0), ColorPalette.getNextColor());
		assertEquals(new Color(230, 0, 153), ColorPalette.getNextColor());
	}

}
