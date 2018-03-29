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
		
		dataStatistics=new DataStatistics(new double [] {1,1}, new double [] {2,2}, new double[] {1,1},
					2, new HashMap<String, Integer>(), null, 0);
		
		
		data = new Data(dataPoints,2,2, dataStatistics, (new HashMap<Integer, String>()) );
		
		dendrogramLevel= new DendrogramLevel(0, dataStatistics);
		
		DendrogramLevel.setDistanceMethod(new Centroid());
		DendrogramLevel.setMeasure(new L2Norm());
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
 
	@Test
	public void testGetClusterisationStatistics() {
		assertEquals(Double.NaN, dendrogramLevel.getClusterisationStatistics().getFlatClusterisationFMeasure(), 0.1 );
	}
	
}
