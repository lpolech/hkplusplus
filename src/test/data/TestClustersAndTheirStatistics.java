package test.data;

import static org.junit.Assert.*;

import java.awt.Color;

import org.junit.Test;

import data.Cluster;
import data.ClustersAndTheirStatistics;
import data.DataPoint;
import data.Parameters;

public class TestClustersAndTheirStatistics {

	Cluster[] clusters;
	double statistic;
	ClustersAndTheirStatistics clustersAndTheirStatistics;
	
	Cluster cluster;
	DataPoint [] dataPoints;
	DataPoint center;
	Color color;
	int parentId;
	int rootId;
	
	public TestClustersAndTheirStatistics() {
		dataPoints = new DataPoint[] {new DataPoint(new double []{2,1},new double []{1,2},"intNam","classAtr"),
				new DataPoint(new double []{1,2},new double []{1,2},"intNam","classAtr")};
		center = new DataPoint(new double []{1,2},new double []{1,2},"intNam","classAtr") ;
		color= new Color(255,255,255);
		parentId=0;
		rootId=0;
		cluster = new Cluster(dataPoints,center,color,parentId,rootId);
		
		clusters=new Cluster[]{cluster}; 
		statistic=8;
		clustersAndTheirStatistics=new ClustersAndTheirStatistics(clusters, statistic, false);
	}
	
	@Test
	public void testClustersAndTheirStatistics() {
		fail("Not yet implemented");
		//nie wiem czy potrzebujuje tego testu, ale wygenerowal sie z automatu
	}

	@Test
	public void testGetClusters() {
		assertArrayEquals(clusters, clustersAndTheirStatistics.getClusters());
	}

	@Test
	public void testGetClusterisationStatistic() {
		assertEquals(statistic, clustersAndTheirStatistics.getClusterisationStatistic(),Parameters.getEpsilon());
	}

}
