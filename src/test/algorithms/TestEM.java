package test.algorithms;

import static org.junit.Assert.*;

import java.awt.Color;
import java.util.HashMap;

import org.junit.Test;

import algorithms.EM;
import algorithms.Kmeans;
import data.Cluster;
import data.ClustersAndTheirStatistics;
import data.Data;
import data.DataPoint;
import data.DataStatistics;
import data.NumberOfPointsAndDataDimension;
import data.Parameters;
import distance.measures.L2Norm;

public class TestEM {
	
	Cluster cluster;
	DataPoint [] dataPoints;
	DataPoint center;
	Color color;
	int parentId;
	int rootId;
	EM em;
	
	DataStatistics stats; 
	Data data;
	
	public TestEM()
	{
		dataPoints = new DataPoint[] {new DataPoint(new double []{2,1},new double []{1,2},"intNam","classAtr"),
				new DataPoint(new double []{1,2},new double []{1,2},"intNam","classAtr")};
		center = new DataPoint(new double []{1,2},new double []{1,2},"intNam","classAtr") ;
		color= new Color(255,255,255);
		parentId=0;
		rootId=0;
		cluster = new Cluster(dataPoints,center,color,parentId,rootId);
		em= new EM();
		
		Parameters.setVerbose(false);
		Parameters.setClassAttribute(false);
		
		stats = calculateDataStatistics
				(dataPoints, new NumberOfPointsAndDataDimension(2,2).getDataDimension(), new HashMap<String, Integer>());
		data = new Data(dataPoints,2,2, stats, null);
	}
	
	//jakies parametry z innych testow psuja wynik
	@Test
	public void testRun() {
		EM.setMeasure(new L2Norm());
		ClustersAndTheirStatistics clustersAndTheirStatistics = em.run(0, cluster, 0);
		assertEquals(1, clustersAndTheirStatistics.getClusters().length);
		assertEquals(cluster, clustersAndTheirStatistics.getClusters()[0]);
	}
	
	//normalnie failuje ale po jakis parametrach z innych testow przechodzi
	@Test
	public void testRunSecondBranch() {	
		EM.setMeasure(new L2Norm());
		Parameters.setMaxNumberOfNodes(5);
		ClustersAndTheirStatistics clustersAndTheirStatistics = em.run(0, cluster, 0);
		assertEquals(1, clustersAndTheirStatistics.getClusters().length);
		assertEquals(cluster, clustersAndTheirStatistics.getClusters()[0]);
	}

	@Test
	public void testMakeCluster() {
		Cluster newCluster= em.makeCluster(data, new L2Norm());
		assertEquals(2, newCluster.getNumberOfPoints());
		assertEquals(1.0, newCluster.getPoints()[0].getCoordinate(0), Parameters.getEpsilon());
		assertEquals(0, newCluster.getPoints()[0].getCoordinate(1), Parameters.getEpsilon());
		assertEquals(0, newCluster.getPoints()[1].getCoordinate(0), Parameters.getEpsilon());
		assertEquals(1, newCluster.getPoints()[1].getCoordinate(1), Parameters.getEpsilon());
		
	}

	@Test
	public void testUpdateCenter() {
		assertEquals(null, em.updateCenter(cluster, new L2Norm())  );
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
