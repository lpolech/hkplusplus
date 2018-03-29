package test.data;

import static org.junit.Assert.*;

import org.junit.Test;

import Jama.Matrix;
import algorithms.Algorithm;
import algorithms.EM;
import algorithms.Kmeans;
import center.method.Centroid;
import data.Cluster;
import data.ClustersAndTheirStatistics;
import data.DataPoint;
import data.Parameters;
import distance.measures.L2Norm;

import java.awt.Color;

public class TestCluster {

	Cluster cluster;
	DataPoint [] dataPoints;
	DataPoint center;
	Color color;
	int parentId;
	int rootId;
	
	public TestCluster()
	{
		dataPoints = new DataPoint[] {new DataPoint(new double []{2,1},new double []{1,2},"intNam","classAtr"),
				new DataPoint(new double []{1,2},new double []{1,2},"intNam","classAtr")};
		center = new DataPoint(new double []{1,2},new double []{1,2},"intNam","classAtr") ;
		color= new Color(255,255,255);
		parentId=0;
		rootId=0;
		cluster = new Cluster(dataPoints,center,color,parentId,rootId);
	}
	

	@Test
	public void testGetNumberOfPoints() {
		assertEquals(2, cluster.getNumberOfPoints());
	}

	@Test
	public void testGetPoints() {
		assertArrayEquals(dataPoints, cluster.getPoints());
	}

	@Test
	public void testSetPoints() {
		DataPoint [] newDataPoint=new DataPoint[] {new DataPoint(new double []{2,2},new double []{2,2},"intNam","classAtr"),
				new DataPoint(new double []{2,2},new double []{2,2},"intNam","classAtr")};
		cluster.setPoints(newDataPoint);
		assertArrayEquals(newDataPoint, cluster.getPoints());
	}

	@Test
	public void testGetCenter() {
		assertEquals(center, cluster.getCenter());
	}

	@Test
	public void testSetCenter() {
		DataPoint newCenter = new DataPoint(new double []{2,2},new double []{2,2},"intNam","classAtr") ;
		cluster.setCenter(newCenter);
		assertEquals(newCenter, cluster.getCenter());
	}
	
	@Test
	public void testPerformSplit() {
		Parameters.setVerbose(false);
		Parameters.setClassAttribute(false);
		Parameters.setNumberOfClusterisationAlgIterations(10);
		Kmeans.setCenterMethod(new Centroid());
		Kmeans.setMeasure(new L2Norm());
		
		Cluster.setAlgorithm( new Kmeans());
		
		ClustersAndTheirStatistics clustersAndTheirStatistics= cluster.performSplit(1, 0);
		assertEquals(2, clustersAndTheirStatistics.getClusters().length);
	}
	
	@Test
	public void testGetMaximumValueOfDataPoints() {
		assertEquals(2, cluster.getMaximumValueOfDataPoints(0),Parameters.getEpsilon());
	}

	@Test
	public void testGetMinimumValueOfDataPoints() {
		assertEquals(1, cluster.getMinimumValueOfDataPoints(0),Parameters.getEpsilon());
	}

	@Test
	public void testSetAlgorithm() {
		cluster.setAlgorithm(new Kmeans());
		cluster.setAlgorithm(new EM());
	}

	@Test
	public void testIsStaticCenter() {
		assertEquals(false,cluster.isStaticCenter());
	}

	@Test
	public void testSetStaticCenter() {
		assertEquals(false,cluster.isStaticCenter());
		cluster.setStaticCenter(true);
		assertEquals(true,cluster.isStaticCenter());
	}

	@Test
	public void testGetCovariance() {
		assertEquals(null, cluster.getCovariance());
	}

	@Test
	public void testSetCovariance() {
		Matrix matrix= new Matrix(2, 2);
		cluster.setCovariance(matrix);
		assertEquals(matrix, cluster.getCovariance());
		
	}

	@Test
	public void testGetMixingCoefficient() {
		assertEquals(0, cluster.getMixingCoefficient(),Parameters.getEpsilon());
	}

	@Test
	public void testSetMixingCoefficient() {
		cluster.setMixingCoefficient(5);
		assertEquals(5, cluster.getMixingCoefficient(),Parameters.getEpsilon());
	}

	@Test
	public void testGetColorOnImage() {
		assertEquals(color, cluster.getColorOnImage());
	}

	@Test
	public void testSetColorOnImage() {
		Color newColor= new Color(0,0,0);
		cluster.setColorOnImage(newColor);
		assertEquals(newColor, cluster.getColorOnImage());
	}

	@Test
	public void testGetParentId() {
		assertEquals(parentId, cluster.getParentId());
	}

	@Test
	public void testGetClusterId() {
		assertEquals(rootId, cluster.getClusterId());
	}

	@Test
	public void testToString() {
		Parameters.setClassAttribute(false);
		Parameters.setInstanceName(false);
		DataPoint.setNumberOfDimensions(2);
		
		assertEquals("Center: 1.0;2.0;;Source:;1.0;2.0;\nNum. of Pts: 2\nMixing coef.: 0.0\nStatic: false\n"
				+ "Cov. matrix: null\nColour on img.: java.awt.Color[r=255,g=255,b=255]\n"
				+ "Parent Id: 0\nCluster Id: 0", cluster.toString());
	}

}
