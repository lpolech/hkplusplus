package test.distance.measures;

import static org.junit.Assert.*;

import java.awt.Color;

import org.junit.Test;

import Jama.Matrix;
import data.Cluster;
import data.DataPoint;
import data.Parameters;
import distance.measures.GMMBayesMLE;
import distance.measures.L2Norm;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

public class TestGMMBayesMLE {
	Cluster cluster;
	
	DataPoint [] dataPoints;
	DataPoint center;
	Color color;
	int parentId;
	int rootId;
	GMMBayesMLE gmmBayesMLE;
	
	
	public TestGMMBayesMLE () {
	
		dataPoints = new DataPoint[] {new DataPoint(new double []{2,1},new double []{1,2},"intNam","classAtr"),
				new DataPoint(new double []{1,2},new double []{1,2},"intNam","classAtr")};
		center = new DataPoint(new double []{1,2},new double []{1,2},"intNam","classAtr") ;
		color= new Color(255,255,255);
		parentId=0;
		rootId=0;
		cluster = new Cluster(dataPoints,center,color,parentId,rootId);
		gmmBayesMLE = new GMMBayesMLE();
		
		Parameters.setVerbose(false);
		Parameters.setClassAttribute(false);
	}
	
	//nalezy tak spreparowac metode aby wyznacznik byl bliski zero, ale metoda dzialala, nie wiem jak to zrobic
	@Test
	public void testDistance() {
		Matrix covariance = new Matrix(new double[][] {{1.1 ,1},{1,1}});
		//Matrix covariance = new Matrix(new double[][] {{0 ,0},{0,0}});
		
		
		cluster.setCovariance(covariance);
		DataPoint newPoint = new DataPoint(new double []{1. ,1},new double []{1,1},"intNam","classAtr");
		DataPoint.setNumberOfDimensions(2);
		
		//najlepszy test to to nie jest, ale nie wiem jak sensownie przetestowac odleglosci
		assertEquals(0, gmmBayesMLE.distance(cluster, newPoint), 0.02 );
	}

	@Test(expected = NotImplementedException.class) 
	public void testUpdateCenter() {
		DataPoint.setNumberOfDimensions(2);
		double [] centroid=gmmBayesMLE.updateCenter(dataPoints);
		assertEquals(2, centroid.length);
		assertArrayEquals(new double [] {1.5,1.5}, centroid, Parameters.getEpsilon());
	}

	
	@Test
	public void testCalculateClusterisationStatistic() {
		Matrix covariance = new Matrix(new double[][] {{1.1 ,1},{1,1}});
		cluster.setCovariance(covariance);
		DataPoint.setNumberOfDimensions(2);
		
		
		double clusterisationStatistic = gmmBayesMLE.calculateClusterisationStatistic(new Cluster[] {cluster});
		//najlepszy test to to nie jest, ale nie wiem jak przetestowac odleglosci
		assertEquals( Double.NEGATIVE_INFINITY, clusterisationStatistic, 0.1);
		fail("Fix needed");
	}

	@Test
	public void testPrintClusterisationStatisticName() {
		assertEquals("Data Log. Likelihood",  gmmBayesMLE.printClusterisationStatisticName());
	}

	@Test
	public void testCompareStatistics() {
		assertEquals(0, gmmBayesMLE.compareStatistics(0, 0));
		assertEquals(1, gmmBayesMLE.compareStatistics(1, 0));
		assertEquals(-1, gmmBayesMLE.compareStatistics(0, 1));
	}

	@Test
	public void testGetTheWorstMeasureValue() {
		assertEquals((-1)*Double.MAX_VALUE,  gmmBayesMLE.getTheWorstMeasureValue(), Parameters.getEpsilon());
	}

}
