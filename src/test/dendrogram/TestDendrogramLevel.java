package test.dendrogram;

import static org.junit.Assert.*;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;

import org.junit.Test;

import algorithms.Kmeans;
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
		
		Parameters.setVerbose(true);
		Parameters.setClassAttribute(false);
		Parameters.setMaxNumberOfNodes(100);
		Parameters.setNumberOfClusterisationAlgIterations(10);
		Parameters.setNumberOfClusterisationAlgRepeats(5);
		
		dataStatistics = calculateDataStatistics
				(dataPoints, new NumberOfPointsAndDataDimension(2,2).getDataDimension(), new HashMap<String, Integer>());
		
		data = new Data(dataPoints,2,2, dataStatistics, (new HashMap<Integer, String>()) );
		
		dendrogramLevel= new DendrogramLevel(0, dataStatistics);
		
		dendrogramLevel.setDistanceMethod(new Centroid());
		dendrogramLevel.setMeasure(new L2Norm());
		dendrogramLevel.makeRoot(data);
		Parameters.setVerbose(false);
	}
	
	@Test  
	public void testAllClustersDontHaveEnoughPoints() {
		assertEquals(false, dendrogramLevel.allClustersDontHaveEnoughPoints(1));
	}


	@Test
	public void testExpandTree() {
		Cluster.setAlgorithm(new Kmeans());
		Kmeans.setCenterMethod(new Centroid());
		Kmeans.setMeasure(new L2Norm());
		
		DendrogramLevel newLevel= dendrogramLevel.expandTree(1, 0, dataStatistics, 0);
		
		assertEquals(2, newLevel.getClusters().length);
	}

	@Test
	public void testWrite() {
		Parameters.setClassAttribute(true);
		try {
			Files.deleteIfExists( Paths.get("test/testFile.csv") );
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		dendrogramLevel.write(data, new File("test/testFile.csv"), dataStatistics);
		
		assertEquals(true, new File("test/testFile.csv").isFile() );
	}

	@Test
	public void testGetImage() {
		BufferedImage bufferedImage= dendrogramLevel.getImage();
		assertNotEquals(null, bufferedImage);
	
	}

	@Test
	public void testGetMeasure() {
		assertEquals(new L2Norm().getClass(), dendrogramLevel.getMeasure().getClass());
	}

	@Test
	public void testGetStatistic() {
		assertEquals(1.5 , dendrogramLevel.getStatistic(), 0.1);
	}

	@Test
	public void testGetClusters() {
		assertEquals(1, dendrogramLevel.getClusters().length);
	}

	//pierw ClusterisationStatistics i potem powrot do tego 
	@Test
	public void testGetClusterisationStatistics() {
		//Parameters.isClassAttribute() = false wiec clusteryzationstatistic sie nie policzy
		assertEquals(Double.NaN, dendrogramLevel.getClusterisationStatistics().getFlatClusterisationFMeasure(), 0.1 );
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
