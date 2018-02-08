package test.data;

import static org.junit.Assert.*;

import org.junit.Test;

import data.DataPoint;
import data.Parameters;

public class TestDataPoint {

	double [] coordinates= {1,2};
	double [] sourceCoordinates= {1,2};
	DataPoint datapoint= new DataPoint(coordinates,sourceCoordinates,"instnam","classAt");
	DataPoint datapoint2= new DataPoint(coordinates,null,"instnam","classAt");
	
	@Test
	public void testDataPoint() {
		assertNotEquals(sourceCoordinates , datapoint.getSourceCoordinates());
		assertEquals(null , datapoint2.getSourceCoordinates());
	}

	@Test
	public void testGetCoordinate() {
		assertEquals(1, datapoint.getCoordinate(0), Parameters.getEpsilon());
		assertEquals(2, datapoint.getCoordinate(1), Parameters.getEpsilon());
		
	}

	@Test
	public void testSetCoordinates() {
		fail("Not yet implemented");
		
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
		fail("Not yet implemented");
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

	//moze powinismy inicjowac w konstuktorze ta wartosc? 
	//choc z drugiej strony wywolywanie iteratorow dla kazdego punktu w duzych zbiorach jest bez sensu
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
		//fail("Not yet implemented");
		//wystarczy ze cokolwiek da
		datapoint.setNumberOfDimensions(2);
		System.out.println(datapoint);
		Parameters.setClassAttribute(true);
		Parameters.setInstanceName(true);
		System.out.println(datapoint);
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
		fail("Not yet implemented");
	}

	@Test
	public void testGetInstanceName() {
		assertEquals("instnam", datapoint.getInstanceName());
	}

	@Test
	public void testSetInstanceName() {
		fail("Not yet implemented");
	}

}
