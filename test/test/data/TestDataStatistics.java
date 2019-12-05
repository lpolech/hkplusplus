package test.data;

import static org.junit.Assert.*;

import java.util.HashMap;

import org.junit.Test;

import data.DataStatistics;
import data.Parameters;

public class TestDataStatistics {

	double[] minValues;
	double[] maxValues;
	DataStatistics dataStatistics;
	
	public TestDataStatistics() {
		minValues=new double [] {1,2};
		maxValues=new double [] {1,2};
		dataStatistics= new DataStatistics(minValues,maxValues, new double []{1,1}, 2, new HashMap<String, Integer>(), new int[] {1,1}, 0);
	}
	

	@Test
	public void testGetMinValues() {
		assertArrayEquals(minValues, dataStatistics.getMinValues(),Parameters.getEpsilon());
	}

	@Test
	public void testSetMinValues() {
		double[] newMinValues=new double [] {2,3};
		dataStatistics.setMinValues(newMinValues);
		assertArrayEquals(newMinValues, dataStatistics.getMinValues(),Parameters.getEpsilon());
	}

	@Test
	public void testGetMaxValues() {
		assertArrayEquals(maxValues, dataStatistics.getMaxValues(),Parameters.getEpsilon());
	}

	@Test
	public void testSetMaxValues() {
		double[] newMaxValues=new double [] {2,3};
		dataStatistics.setMaxValues(newMaxValues);
		assertArrayEquals(newMaxValues, dataStatistics.getMaxValues(),Parameters.getEpsilon());
	}

	@Test
	public void testGetEachClassNumberOfInstanceWithInheritance() {
		assertArrayEquals(new int[] {1,1}, dataStatistics.getEachClassNumberOfInstanceWithInheritance());
	}

	@Test
	public void testSetEachClassNumberOfInstanceWithInheritance() {
		int [] newEachClassNumberOfInstanceWithInheritance= new int [] {2,2};
		dataStatistics.setEachClassNumberOfInstanceWithInheritance(newEachClassNumberOfInstanceWithInheritance);
		assertArrayEquals(newEachClassNumberOfInstanceWithInheritance, dataStatistics.getEachClassNumberOfInstanceWithInheritance());
	}

	@Test
	public void testGetClassNameAndItsId() {
		assertEquals(new HashMap<String, Integer>(), dataStatistics.getClassNameAndItsId());
	}

	@Test
	public void testSetClassNameAndItsId() {
		HashMap<String, Integer> newClassNameAndItsId=new HashMap<String, Integer>();
		newClassNameAndItsId.put("klucz", 1);
		dataStatistics.setClassNameAndItsId(newClassNameAndItsId);
		assertEquals(newClassNameAndItsId, dataStatistics.getClassNameAndItsId());
	}

	@Test
	public void testGetDatasetLength() {
		assertEquals(2, dataStatistics.getDatasetLength());
	}

	@Test
	public void testSetDatasetLength() {
		dataStatistics.setDatasetLength(5);
		assertEquals(5, dataStatistics.getDatasetLength());
	}

	@Test
	public void testGetNumberOfNoisePoints() {
		assertEquals(0, dataStatistics.getNumberOfNoisePoints());
	}

	@Test
	public void testSetNumberOfNoisePoints() {
		dataStatistics.setNumberOfNoisePoints(5);
		assertEquals(5, dataStatistics.getNumberOfNoisePoints());
	}

	@Test
	public void testGetEachDimNormalisationInterval() {
		assertArrayEquals(new double[] {1,1}, dataStatistics.getEachDimNormalisationInterval(),Parameters.getEpsilon());
	}

	@Test
	public void testSetEachDimNormalisationInterval() {
		double [] newEachDimNormalisationInterval= new double [] {2,2};
		dataStatistics.setEachDimNormalisationInterval(newEachDimNormalisationInterval);
		assertArrayEquals(newEachDimNormalisationInterval, dataStatistics.getEachDimNormalisationInterval(),Parameters.getEpsilon());
	}

}
