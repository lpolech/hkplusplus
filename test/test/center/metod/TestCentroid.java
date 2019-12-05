package test.center.metod;

import static org.junit.Assert.assertEquals;

import java.nio.file.Paths;

import org.junit.Test;

import center.method.Centroid;
import data.Cluster;
import data.Data;
import data.DataPoint;
import data.DataReader;
import data.Parameters;
import distance.measures.GMMBayesMLE;
import distance.measures.L2Norm;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

public class TestCentroid {

	Data inputData;
	Centroid centroid;
	Cluster cluster;

	public TestCentroid() {
		centroid = new Centroid();
		Parameters.setVerbose(true);
		Parameters.setClassAttribute(true);
		inputData = DataReader.read(Paths.get("test.csv"));
	}

	@Test
	public void testMakeCluster() {
		cluster = centroid.makeCluster(inputData, new L2Norm());
		assertEquals(0.5, cluster.getCenter().getCoordinate(0), Parameters.getEpsilon());
		assertEquals(0.5, cluster.getCenter().getCoordinate(1), Parameters.getEpsilon());
		cluster.getCenter();
		assertEquals(3, DataPoint.getNumberOfDimensions());
	}

	@Test
	public void testUpdateCenter() {
		Cluster cluster = centroid.makeCluster(inputData, new L2Norm());
		Cluster cluster2 = centroid.updateCenter(cluster, new L2Norm());
		assertEquals(cluster2, cluster);
	}

	@Test(expected = NotImplementedException.class)
	public void testUpdateCenterThrowsException() {
		Cluster cluster = centroid.makeCluster(inputData, new GMMBayesMLE());
		Cluster cluster2 = centroid.updateCenter(cluster, new GMMBayesMLE());
		assertEquals(cluster2, cluster);
	}

}
