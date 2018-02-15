package test.data;

import static org.junit.Assert.*;

import org.junit.Test;

import data.DataPoint;
import data.Parameters;

public class TestDataPoint {
	
	double [] coordinates;
	double [] sourceCoordinates ;
	DataPoint datapoint;
	DataPoint datapoint2;
	
	public TestDataPoint ()
	{
		coordinates = new double [] {1,2};
		sourceCoordinates = new double [] {1,2};
		datapoint = new DataPoint(coordinates,sourceCoordinates,"instnam","classAt");
		datapoint2 = new DataPoint(coordinates,null,"instnam","classAt");
	}
	
	@Test
	public void testGetCoordinate() {
		assertEquals(1, datapoint.getCoordinate(0), Parameters.getEpsilon());
		assertEquals(2, datapoint.getCoordinate(1), Parameters.getEpsilon());
	}

	@Test
	public void testSetCoordinates() {
		double [] newCordinates = {3,4};
		datapoint.setCoordinates(newCordinates);
		assertArrayEquals(newCordinates, datapoint.getCoordinates(),Parameters.getEpsilon());
		assertNotEquals(newCordinates, datapoint.getCoordinates());
	}

	@Test
	public void testGetSourceCoordinates() {
		assertArrayEquals(sourceCoordinates, datapoint.getSourceCoordinates(),Parameters.getEpsilon());
	}

	@Test
	public void testGetSourceCoordinate() {
		assertEquals(sourceCoordinates[0], datapoint.getSourceCoordinate(0),Parameters.getEpsilon());
		assertEquals(sourceCoordinates[1], datapoint.getSourceCoordinate(1),Parameters.getEpsilon());
	}

	@Test (expected = java.lang.NullPointerException.class) 
	public void testGetSourceCoordinateThrowExceptionWhenNull() {
		assertEquals(null, datapoint2.getSourceCoordinate(0));
	}
	
	@Test
	public void testSetSourceCoordinates() {
		double [] newSourceCordinates = {3,4};
		datapoint.setSourceCoordinates(newSourceCordinates);
		assertArrayEquals(newSourceCordinates, datapoint.getSourceCoordinates(),Parameters.getEpsilon());
		assertNotEquals(newSourceCordinates, datapoint.getSourceCoordinates());
		datapoint.setSourceCoordinates(null);
		assertEquals(null, datapoint.getSourceCoordinates());
		assertArrayEquals(null, datapoint.getSourceCoordinates(),Parameters.getEpsilon());
	}

	@Test
	public void testSetCoordinate() {
		datapoint.setCoordinate(0, 2);
		assertEquals(2, datapoint.getCoordinate(0), Parameters.getEpsilon());
	}

	@Test
	public void testGetCoordinates() {
		assertEquals(1, datapoint.getCoordinates()[0], Parameters.getEpsilon());
		assertArrayEquals(coordinates, datapoint.getCoordinates(),Parameters.getEpsilon());
	}

	@Test
	public void testGetNumberOfDimensions() {
		assertEquals(2, datapoint.getNumberOfDimensions());
	}

	@Test
	public void testSetNumberOfDimensions() {
		datapoint.setNumberOfDimensions(2);
		assertEquals(2, datapoint.getNumberOfDimensions());
	}

	@Test
	public void testGetMatrix() {
		assertEquals(datapoint.getNumberOfDimensions(), datapoint.getMatrix().getRowDimension());
		assertEquals(1, datapoint.getMatrix().getColumnDimension());
	}

	@Test
	public void testToString() {
		Parameters.setClassAttribute(false);
		Parameters.setInstanceName(false);
		datapoint.setNumberOfDimensions(2);
		//System.out.println(datapoint);
		assertEquals("1.0;2.0;;Source:;1.0;2.0;", datapoint.toString());
		Parameters.setClassAttribute(true);
		Parameters.setInstanceName(true);
		//System.out.println(datapoint);
		assertEquals("Atr: ;classAt;;InstName: ;instnam;;1.0;2.0;;Source:;1.0;2.0;", datapoint.toString());
	}

	@Test
	public void testArePointsPointingTheSame() {
		assertEquals(true, datapoint.arePointsPointingTheSame(datapoint2));
		assertEquals(true, datapoint.arePointsPointingTheSame(datapoint));
		datapoint2.setCoordinate(0, 2);
		assertEquals(false, datapoint.arePointsPointingTheSame(datapoint2));
	}

	@Test
	public void testGetClassAttribute() {
		assertEquals("classAt", datapoint.getClassAttribute());
	}

	@Test
	public void testSetClassAttribute() {
		datapoint.setClassAttribute("ClassAtribute");
		assertEquals("ClassAtribute",datapoint.getClassAttribute());
	}

	@Test
	public void testGetInstanceName() {
		assertEquals("instnam", datapoint.getInstanceName());
	}

	@Test
	public void testSetInstanceName() {
		datapoint.setInstanceName("instanceName");
		assertEquals("instanceName",datapoint.getInstanceName());
	}

}
