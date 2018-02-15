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
	
	DataPoint datapoint;
	DataPoint datapoint2;
	DataPoint [] datapoints;
	
	static DataStatistics stats; 
	static Data data;
	
	public TestData()
	{
		datapoint= new DataPoint(new double[] {0,0},new double[] {-2,-2},"instnam","classAt");
		datapoint2= new DataPoint(new double[] {1,1},new double[] {2,2},"instnam","classAt");
		datapoints=new DataPoint[] {datapoint,datapoint2};
		Parameters.setVerbose(false);
		Parameters.setClassAttribute(false);
		
		stats = calculateDataStatistics
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
	
	static DataStatistics calculateDataStatistics(
			DataPoint[] points, int numberOfDimensions, HashMap<String, Integer> classNameAndItsId) {
		double[] minValues = new double[numberOfDimensions];
		double[] maxValues = new double[numberOfDimensions];
		double[] eachDimNormalisationInterval = new double[numberOfDimensions];
		
		for(int i = 0; i < numberOfDimensions; i++)
		{
			minValues[i] = Double.MAX_VALUE;
			maxValues[i] = Double.MIN_VALUE;
		}
		
		for(DataPoint p: points)
		{
			for(int i = 0; i < numberOfDimensions; i++)
			{
				if(p.getCoordinate(i) < minValues[i])
				{
					minValues[i] = p.getCoordinate(i);
				}
				if(p.getCoordinate(i) > maxValues[i])
				{
					maxValues[i] = p.getCoordinate(i);
				}
			}
		}
		
		int[] eachClassNumberOfInstanceWithInheritance = null;
		int numberOfNoisePoints = 0;
		if(Parameters.isClassAttribute())
		{
			eachClassNumberOfInstanceWithInheritance = new int[classNameAndItsId.size()];
			for(DataPoint p: points)
			{
				if(p.getClassAttribute().contains("Noise"))
				{
					numberOfNoisePoints++;
				}
				else
				{
					String classAttrib = p.getClassAttribute();
					eachClassNumberOfInstanceWithInheritance[classNameAndItsId.get(classAttrib)]++;
					for(String potentialParentClass: classNameAndItsId.keySet())
					{
						if(potentialParentClass.length() < classAttrib.length() 
							&& classAttrib.startsWith(potentialParentClass + basic_hierarchy.common.Constants.HIERARCHY_BRANCH_SEPARATOR))
						{
							eachClassNumberOfInstanceWithInheritance[classNameAndItsId.get(potentialParentClass)]++;
						}
					}
				}
			}
		}
		
		for(int i = 0; i < numberOfDimensions; i++)
		{
			eachDimNormalisationInterval[i] = maxValues[i] - minValues[i];
			if(minValues[i] == maxValues[i])
			{
				System.err.println("DataReader.calculateDataStatistics(..) Warning, found min and max values are equal!"
						+ " This means, that on dimension number " + i + " there is no diferent values.");
			}
		}
		
		return new DataStatistics(minValues, maxValues, eachDimNormalisationInterval, points.length, classNameAndItsId, 
				eachClassNumberOfInstanceWithInheritance, numberOfNoisePoints);
	}
}
