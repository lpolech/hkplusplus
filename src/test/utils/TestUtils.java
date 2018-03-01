package test.utils;

import static org.junit.Assert.*;

import java.awt.Color;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import org.junit.Test;

import data.Parameters;
import utils.Utils;

public class TestUtils {

	@Test
	public void testIsDoubleExtremelyNearZero() {
		assertEquals(false, Utils.isDoubleExtremelyNearZero(1));
		assertEquals(true, Utils.isDoubleExtremelyNearZero(0));
	}

	@Test
	public void testRoundToUpper() {
		assertEquals(2, Utils.roundToUpper(1.5));
		assertEquals(1, Utils.roundToUpper(1.4));
	}

	@Test
	public void testGetStringOfColor() {
		assertEquals("No Color", Utils.getStringOfColor(null));
		assertEquals("0;0;0", Utils.getStringOfColor(Color.black));
		assertEquals("255;255;255", Utils.getStringOfColor(Color.white));
	}

	@Test
	public void testGetNextId() {
		Utils.resetId();
		assertEquals(0, Utils.getNextId());
		assertEquals(1, Utils.getNextId());
		assertEquals(2, Utils.getNextId());
		assertNotEquals(2, Utils.getNextId());
	}

	@Test
	public void testCreateCsvFileIfNotExists() throws IOException {
		assertEquals( "testcsv.csv", Utils.createCsvFileIfNotExists("testcsv.csv").toString());
		Files.deleteIfExists( Paths.get("testcsv.csv") );
		assertEquals( "testcsv.csv", Utils.createCsvFileIfNotExists("testcsv.csv").toString());
	}


	@Test
	public void testGetTimerReport() throws InterruptedException {
		Utils.startTimer();
		Thread.sleep(1001);
		Utils.stopTimer();
		assertEquals(1, Utils.getTimerSeconds(), Parameters.getEpsilon() );
		assertEquals(0.016666666666666666 , Utils.getTimerInMinutes(), Parameters.getEpsilon() );
		assertEquals(2.777777777777778E-4 , Utils.getTimerInHours(), Parameters.getEpsilon() );
		
		String oczekiwanyReport  = ("Elapsed time: \t" +  Double.toString(1) + "s.\n"
				+ "\t\t" + Double.toString(0.016666666666666666) + "min.\n"
				+ "\t\t" + Double.toString(2.777777777777778E-4) + "h.");
		
		assertEquals(oczekiwanyReport, Utils.getTimerReport());
		
		
		Utils.startTimer();
		Thread.sleep(999);
		Utils.stopTimer();
		assertEquals(0, Utils.getTimerSeconds(), Parameters.getEpsilon() );
		assertEquals(0, Utils.getTimerInMinutes(), Parameters.getEpsilon() );
		assertEquals(0, Utils.getTimerInHours(), Parameters.getEpsilon() );
	}

	@Test
	public void testGetTimerSeconds() {
		Utils.startTimer();
		Utils.stopTimer();
		assertEquals(0, Utils.getTimerSeconds(), Parameters.getEpsilon() );
	}

	@Test
	public void testGetTimerInMinutes() {
		Utils.startTimer();
		Utils.stopTimer();
		assertEquals(0.0 , Utils.getTimerInMinutes(), Parameters.getEpsilon() );
	}

	@Test
	public void testGetTimerInHours() {
		Utils.startTimer();
		Utils.stopTimer();
		assertEquals(0.0 , Utils.getTimerInHours(), Parameters.getEpsilon() );
	}

	@Test
	public void testGetDiagonalMatrix() {
		Jama.Matrix mat= new Jama.Matrix (2,2,0.0);
		Jama.Matrix mat2 = Utils.getDiagonalMatrix(mat);
		boolean czyTakieSame=true;
		
		for(int i = 0; i < mat.getColumnDimension(); i++)
		{
			for(int j = 0; j < mat.getRowDimension(); j++)
			{
				if(mat.get(i, j) != mat2.get(i, j) )
				{
					czyTakieSame=false;
				}
			}
		}
		assertEquals(true, czyTakieSame);
		
		mat= new Jama.Matrix (2,2,1);
		mat2 = Utils.getDiagonalMatrix(mat);
		czyTakieSame=true;
		
		for(int i = 0; i < mat.getColumnDimension(); i++)
		{
			for(int j = 0; j < mat.getRowDimension(); j++)
			{
				if(mat.get(i, j) != mat2.get(i, j) )
				{
					czyTakieSame=false;
				}
			}
		}
		assertEquals(false, czyTakieSame);
	}

}
