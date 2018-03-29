package test.distance.measures;

import static org.junit.Assert.*;

import java.awt.Color;

import org.junit.Test;

import Jama.Matrix;
import data.Cluster;
import data.DataPoint;
import data.Parameters;
import distance.measures.LOG_GMMBayesMLE;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

public class TestLOG_GMMBayesMLE {

	Cluster cluster;
	DataPoint [] dataPoints;
	DataPoint center;
	Color color;
	int parentId;
	int rootId;
	LOG_GMMBayesMLE log_GMMBayesMLE;
	
	
	public TestLOG_GMMBayesMLE () {
	
		dataPoints = new DataPoint[] {new DataPoint(new double []{2,1},new double []{1,2},"intNam","classAtr"),
				new DataPoint(new double []{1,2},new double []{1,2},"intNam","classAtr")};
		center = new DataPoint(new double []{1,2},new double []{1,2},"intNam","classAtr") ;
		color= new Color(255,255,255);
		parentId=0;
		rootId=0;
		cluster = new Cluster(dataPoints,center,color,parentId,rootId);
		DataPoint.setNumberOfDimensions(2);
		log_GMMBayesMLE = new LOG_GMMBayesMLE();
		
		Parameters.setVerbose(false);
		Parameters.setClassAttribute(false);
	}
	
	@Test
	public void testDistance() {
		Matrix covariance = new Matrix(new double[][] {{1.1 ,1},{1,1}});
		cluster.setCovariance(covariance);
		DataPoint newPoint = new DataPoint(new double []{1. ,1},new double []{1,1},"intNam","classAtr");
		
		assertEquals(-6.18, log_GMMBayesMLE.distance(cluster, newPoint), 0.1 );
	}

	@Test
	public void testCalculateClusterisationStatistic() {
		Matrix covariance = new Matrix(new double[][] {{1.1 ,1},{1,1}});
		cluster.setCovariance(covariance);
		DataPoint.setNumberOfDimensions(2);
		
		double clusterisationStatistic = log_GMMBayesMLE.calculateClusterisationStatistic(new Cluster[] {cluster});
		assertEquals(-Double.MAX_VALUE,  clusterisationStatistic, 0.1);
		fail("Fix needed");
	}


	@Test(expected = NotImplementedException.class) 
	public void testUpdateCenter() {
		DataPoint.setNumberOfDimensions(2);
		double [] centroid=log_GMMBayesMLE.updateCenter(dataPoints);
		assertEquals(2, centroid.length);
		assertArrayEquals(new double [] {1.5,1.5}, centroid, Parameters.getEpsilon());
	}


	@Test
	public void testPrintClusterisationStatisticName() {
		assertEquals("Data Log. Likelihood",  log_GMMBayesMLE.printClusterisationStatisticName());
	}

	@Test
	public void testCompareStatistics() {
		assertEquals(0, log_GMMBayesMLE.compareStatistics(0, 0));
		assertEquals(1, log_GMMBayesMLE.compareStatistics(1, 0));
		assertEquals(-1, log_GMMBayesMLE.compareStatistics(0, 1));
	}

	@Test
	public void testGetTheWorstMeasureValue() {
		assertEquals((-1)*Double.MAX_VALUE,  log_GMMBayesMLE.getTheWorstMeasureValue(), Parameters.getEpsilon());
	}

}
