package test.distance.measures;

import static org.junit.Assert.*;

import java.awt.Color;
import java.util.HashMap;

import org.junit.Test;

import Jama.Matrix;
import algorithms.EM;
import data.Cluster;
import data.DataPoint;
import data.Parameters;
import distance.measures.GMMBayesMLE;
import distance.measures.L2Norm;

public class TestL2Norm {
	
	Cluster cluster;
	DataPoint [] dataPoints;
	DataPoint center;
	Color color;
	int parentId;
	int rootId;
L2Norm l2Norm;
	
	
	public TestL2Norm() {
	
		dataPoints = new DataPoint[] {new DataPoint(new double []{2,1},new double []{1,2},"intNam","classAtr"),
				new DataPoint(new double []{1,2},new double []{1,2},"intNam","classAtr")};
		center = new DataPoint(new double []{1,2},new double []{1,2},"intNam","classAtr") ;
		color= new Color(255,255,255);
		parentId=0;
		rootId=0;
		cluster = new Cluster(dataPoints,center,color,parentId,rootId);
		l2Norm = new L2Norm();
		
		Parameters.setVerbose(false);
		Parameters.setClassAttribute(false);
	}
	
	@Test
	public void testDistance() {
		Matrix covariance = new Matrix(new double[][] {{1 ,1},{1,1}});
		cluster.setCovariance(covariance);
		DataPoint newPoint = new DataPoint(new double []{1. ,1},new double []{1,1},"intNam","classAtr");
		DataPoint.setNumberOfDimensions(2);
		
		//najlepszy test to to nie jest, ale nie wiem jak przetestowac odleglosci
		assertEquals(1.0, l2Norm.distance(cluster, newPoint), 0.005 );
	}

	@Test
	public void testUpdateCenter() {
		DataPoint.setNumberOfDimensions(2);
		double [] centroid=l2Norm.updateCenter(dataPoints);
		assertEquals(2, centroid.length);
		assertArrayEquals(new double [] {1.5,1.5}, centroid, Parameters.getEpsilon());
	}

	@Test
	public void testCalculateClusterisationStatistic() {
		double clusterisationStatistic = l2Norm.calculateClusterisationStatistic(new Cluster[] {cluster});
		//najlepszy test to to nie jest, ale nie wiem jak przetestowac odleglosci
		assertEquals(1.5, clusterisationStatistic, 0.1);
	}

	@Test
	public void testParticularClusterInterDistance() {
		double particuloarClusterInerDistance = l2Norm.particularClusterInterDistance(cluster);
		//najlepszy test to to nie jest, ale nie wiem jak przetestowac odleglosci
		assertEquals(1.5, particuloarClusterInerDistance, 0.1);
	}

	@Test
	public void testCompareStatistics() {
		assertEquals(0, l2Norm.compareStatistics(0, 0));
		assertEquals(-1, l2Norm.compareStatistics(1, 0));
		assertEquals(1, l2Norm.compareStatistics(0, 1));
	}

	@Test
	public void testPrintClusterisationStatisticName() {
		//metoda super
		assertEquals("Sum of Inter cluster distances",  l2Norm.printClusterisationStatisticName());
	}

	@Test
	public void testGetTheWorstMeasureValue() {
		assertEquals(Double.MAX_VALUE,  l2Norm.getTheWorstMeasureValue(), Parameters.getEpsilon());
	}

}
