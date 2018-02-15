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
		dataStats=calculateDataStatistics
				(dataPoints, new NumberOfPointsAndDataDimension(2,2).getDataDimension(), new HashMap<String, Integer>());
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
	
	//znow korzystam z tego podczas testow
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
