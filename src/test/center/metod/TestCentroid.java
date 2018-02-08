package test.center.metod;

import static org.junit.Assert.*;

import java.nio.file.Paths;

import org.junit.Test;

import center.method.Centroid;
import data.Cluster;
import data.Data;
import data.DataReader;
import data.Parameters;
import distance.measures.L2Norm;
import distance.measures.LOG_GMMBayesMLE;

public class TestCentroid {

	static Data inputData;
	Centroid centroid= new Centroid();
	
	@Test
	public void testMakeCluster() {
		Parameters.setVerbose(true);
		Parameters.setClassAttribute(true);
		inputData = DataReader.read(Paths.get("test4.csv"));
		Cluster cluster = centroid.makeCluster(inputData, new L2Norm());
		assertEquals(0.5, cluster.getCenter().getCoordinate(0),Parameters.getEpsilon());
		assertEquals(0.5, cluster.getCenter().getCoordinate(1),Parameters.getEpsilon());
		assertEquals(2, cluster.getCenter().getNumberOfDimensions());
	}

	@Test
	public void testUpdateCenter() {
		Parameters.setVerbose(true);
		Parameters.setClassAttribute(true);
		inputData = DataReader.read(Paths.get("test3.csv"));
		Cluster cluster = centroid.makeCluster(inputData, new L2Norm());
		Cluster cluster2= centroid.updateCenter(cluster, new  L2Norm());
		assertEquals(cluster2, cluster);
	}

}
