package test.algorithms;

import static org.junit.Assert.*;

import java.awt.Color;
import java.util.HashMap;

import org.junit.Test;

import Jama.Matrix;
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
		

		HashMap<String, Integer> map= new HashMap<String, Integer>();
		map.put("classAtr", 0);
		
		stats=new DataStatistics(new double [] {1,1}, new double [] {2,2}, new double[] {1,1},
				2, map, new int [] {2}, 0);
		
		data = new Data(dataPoints,2,2, stats, null);
	}
	
	@Test
	public void testRun() {
		EM.setMeasure(new L2Norm());
		Parameters.setMaxNumberOfNodes(2);
		ClustersAndTheirStatistics clustersAndTheirStatistics = em.run(1, cluster, 2);
		assertEquals(1, clustersAndTheirStatistics.getClusters().length);
		assertEquals(cluster, clustersAndTheirStatistics.getClusters()[0]);
	}
	

	@Test
	public void testRunSecondBranch() {	
		EM.setMeasure(new L2Norm());
		Parameters.setMaxNumberOfNodes(10);
		Parameters.setClusterReestimationBasedOnItsData(true);
		
		Matrix covariance= new Matrix(2, 2);
		cluster.setCovariance(covariance);
		cluster.setStaticCenter(true);
		
		Parameters.setNumberOfClusterisationAlgIterations(10);
		ClustersAndTheirStatistics clustersAndTheirStatistics = em.run(1, cluster, 0);
		assertEquals(1, clustersAndTheirStatistics.getClusters().length);
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
}
