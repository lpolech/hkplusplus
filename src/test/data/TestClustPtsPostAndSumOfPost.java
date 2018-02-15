package test.data;

import static org.junit.Assert.*;

import java.awt.Color;

import org.junit.Test;

import data.ClustPtsPostAndSumOfPost;
import data.Cluster;
import data.DataPoint;
import data.Parameters;

public class TestClustPtsPostAndSumOfPost {
	
	Cluster[] clusterisation;
	double[][] pointsToMixturePosteriories;
	double[] clustersSumOfPosteriories;

	Cluster cluster;
	DataPoint [] dataPoints;
	DataPoint center;
	Color color;
	int parentId;
	int rootId;
	
	ClustPtsPostAndSumOfPost clustPtsPostAndSumOfPost;
	
	public TestClustPtsPostAndSumOfPost() {
		dataPoints = new DataPoint[] {new DataPoint(new double []{2,1},new double []{1,2},"intNam","classAtr"),
				new DataPoint(new double []{1,2},new double []{1,2},"intNam","classAtr")};
		center = new DataPoint(new double []{1,2},new double []{1,2},"intNam","classAtr") ;
		color= new Color(255,255,255);
		parentId=0;
		rootId=0;
		cluster = new Cluster(dataPoints,center,color,parentId,rootId);
		
		clusterisation=new Cluster[] {cluster};
		pointsToMixturePosteriories=null;
		clustersSumOfPosteriories=new double[] {1,2};
		clustPtsPostAndSumOfPost = new ClustPtsPostAndSumOfPost(clusterisation, pointsToMixturePosteriories, clustersSumOfPosteriories);
	}
	
	@Test
	public void testClustPtsPostAndSumOfPost() {
		fail("Not yet implemented");
		//nie wiem czy potrzebujuje tego testu, ale wygenerowal sie z automatu
	}

	@Test
	public void testGetClusterisation() {
		assertArrayEquals(clusterisation, clustPtsPostAndSumOfPost.getClusterisation());
	}

	@Test
	public void testGetPointsToMixturePosteriories() {
		assertArrayEquals(pointsToMixturePosteriories, clustPtsPostAndSumOfPost.getPointsToMixturePosteriories());
	}

	@Test
	public void testGetClustersSumOfPosteriories() {
		assertArrayEquals(clustersSumOfPosteriories, clustPtsPostAndSumOfPost.getClustersSumOfPosteriories(),Parameters.getEpsilon());
	}

}
