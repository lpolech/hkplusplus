package test.data;

import static org.junit.Assert.*;

import org.junit.Test;


import data.Data;
import data.DataPoint;
import data.DataReader;
import data.DataStatistics;
import data.NumberOfPointsAndDataDimension;
import data.Parameters;

import java.util.HashMap;

public class TestData {
	
	DataPoint datapoint= new DataPoint(new double[] {0,0},new double[] {-2,-2},"instnam","classAt");
	DataPoint datapoint2= new DataPoint(new double[] {1,1},new double[] {2,2},"instnam","classAt");
	DataPoint [] datapoints= {datapoint,datapoint2};
	
	static DataStatistics stats; 
	static Data data;
	
	public TestData()
	{
		Parameters.setVerbose(false);
		Parameters.setClassAttribute(false);
		
		//zamokowac metode, i ustawic spowrotem prywatna
		stats = DataReader.calculateDataStatistics
				(datapoints, new NumberOfPointsAndDataDimension(2,2).getDataDimension(), new HashMap<String, Integer>());
		data = new Data(datapoints,2,2, stats, null);
	}
	
	@Test
	public void testData() {
		//fail("Not yet implemented");
	}

	@Test
	public void testGetDimensionNumberAndItsName() {
		assertEquals(null, data.getDimensionNumberAndItsName());
	}

	@Test
	public void testGetNumberOfDimensions() {
		assertEquals(2, data.getNumberOfDimensions());
	}

	@Test
	public void testGetNumberOfPoints() {
		assertEquals(2, data.getNumberOfPoints());
	}

	@Test
	public void testGetPoints() {
		assertEquals(true,data.getPoints()[0].arePointsPointingTheSame(datapoint));
	}

	@Test
	public void testGetDataStats() {
		assertEquals(stats, data.getDataStats());
	}

}
