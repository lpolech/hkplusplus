package test.dendrogram;

import static org.junit.Assert.*;

import java.awt.Color;
import java.util.HashMap;

import org.junit.Test;

import center.method.Centroid;
import data.Cluster;
import data.Data;
import data.DataPoint;
import data.DataStatistics;
import data.NumberOfPointsAndDataDimension;
import data.Parameters;
import dendrogram.DendrogramLevel;
import distance.measures.L2Norm;
import distance.measures.LOG_GMMBayesMLE;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

public class TestDendrogramLevel {
	
	Cluster cluster;
	DataPoint [] dataPoints;
	DataPoint center;
	Color color;
	int parentId;
	int rootId;
	DendrogramLevel dendrogramLevel;
	DataStatistics dataStatistics; 
	Data data;
	
	public TestDendrogramLevel () {
	
		dataPoints = new DataPoint[] {new DataPoint(new double []{2,1},new double []{1,2},"intNam","classAtr"),
				new DataPoint(new double []{1,2},new double []{1,2},"intNam","classAtr")};
		center = new DataPoint(new double []{1,2},new double []{1,2},"intNam","classAtr") ;
		color= new Color(255,255,255);
		parentId=0;
		rootId=0;
		cluster = new Cluster(dataPoints,center,color,parentId,rootId);
		
		Parameters.setVerbose(false);
		Parameters.setClassAttribute(false);
		
		dataStatistics = calculateDataStatistics
				(dataPoints, new NumberOfPointsAndDataDimension(2,2).getDataDimension(), new HashMap<String, Integer>());
		data = new Data(dataPoints,2,2, dataStatistics, null);
		
		dendrogramLevel= new DendrogramLevel(0, dataStatistics);
	}
	
	@Test  
	public void testAllClustersDontHaveEnoughPoints() {
		dendrogramLevel.setDistanceMethod(new Centroid());
		dendrogramLevel.setMeasure(new L2Norm());
		dendrogramLevel.makeRoot(data);
		assertEquals(false, dendrogramLevel.allClustersDontHaveEnoughPoints(0));
	}
	
	@Test (expected = NullPointerException.class) 
	public void testAllClustersDontHaveEnoughPointsThrowsNullpointerExp() {
		assertEquals(false, dendrogramLevel.allClustersDontHaveEnoughPoints(0));
	}

	@Test
	public void testExpandTree() {
		//wywala bo cluster[] nie zinicjalizowany, a inicjalizuje sie dopiero w 3 linice funkcji a wymagany w 1 
		DendrogramLevel newLevel= dendrogramLevel.expandTree(0, 0, dataStatistics, 0);
	}

	@Test
	public void testMakeRoot() {
		fail("Not yet implemented");
	}

	@Test
	public void testWrite() {
		fail("Not yet implemented");
	}

	@Test
	public void testGetImage() {
		fail("Not yet implemented");
	}

	@Test
	public void testSetDistanceMethod() {
		fail("Not yet implemented");
	}

	@Test
	public void testSetMeasure() {
		fail("Not yet implemented");
	}

	@Test
	public void testGetMeasure() {
		fail("Not yet implemented");
	}

	@Test
	public void testGetStatistic() {
		fail("Not yet implemented");
	}

	@Test
	public void testGetClusters() {
		fail("Not yet implemented");
	}

	@Test
	public void testGetClusterisationStatistics() {
		fail("Not yet implemented");
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
