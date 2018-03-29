package test.data;

import static org.junit.Assert.*;

import java.util.HashMap;

import org.junit.Test;

import data.DataNormalisation;
import data.DataPoint;
import data.DataStatistics;
import data.NumberOfPointsAndDataDimension;
import data.Parameters;

public class TestDataNormalisation {

	DataPoint [] dataPoints;
	DataStatistics dataStats;
	
	public TestDataNormalisation() {

		Parameters.setClassAttribute(false);
		
		dataPoints = new DataPoint[] {new DataPoint(new double []{2,1},new double []{2,1},"intNam","classAtr"),
				new DataPoint(new double []{1,2},new double []{1,2},"intNam","classAtr")};
		DataPoint.setNumberOfDimensions(2);
		
		HashMap<String, Integer> map= new HashMap<String, Integer>();
		map.put("classAtr", 0);
		
		dataStats=new DataStatistics(new double [] {1,1}, new double [] {2,2}, new double[] {1,1},
				2, map, new int [] {2}, 0);
	}
	
	@Test
	public void testNormalise() 
	{
		DataPoint [] normalisedData = DataNormalisation.normalise(dataPoints, dataStats);
		assertEquals(1, normalisedData[0].getCoordinates()[0], Parameters.getEpsilon());
		assertEquals(0, normalisedData[0].getCoordinates()[1], Parameters.getEpsilon());
		assertEquals(1, normalisedData[1].getCoordinates()[1], Parameters.getEpsilon());
		assertEquals(0, normalisedData[1].getCoordinates()[0], Parameters.getEpsilon());
	}
	
}
