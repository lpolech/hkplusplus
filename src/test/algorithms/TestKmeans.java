package test.algorithms;

import static org.junit.Assert.assertEquals;

import java.awt.Color;
import java.util.HashMap;

import org.junit.Test;

import algorithms.Kmeans;
import center.method.Centroid;
import data.Cluster;
import data.ClustersAndTheirStatistics;
import data.Data;
import data.DataPoint;
import data.DataStatistics;
import data.Parameters;
import distance.measures.L2Norm;

public class TestKmeans {
	Cluster cluster;

	DataPoint[] dataPoints;
	DataPoint center;
	Color color;
	int parentId;
	int rootId;
	Kmeans kmeans;

	DataStatistics stats;
	Data data;

	public TestKmeans() {
		dataPoints = new DataPoint[] {
				new DataPoint(new double[] { 2, 1 }, new double[] { 1, 2 }, "intNam", "classAtr"),
				new DataPoint(new double[] { 1, 2 }, new double[] { 1, 2 }, "intNam", "classAtr") };
		center = new DataPoint(new double[] { 1, 2 }, new double[] { 1, 2 }, "intNam", "classAtr");
		color = new Color(255, 255, 255);
		parentId = 0;
		rootId = 0;
		cluster = new Cluster(dataPoints, center, color, parentId, rootId);
		kmeans = new Kmeans();

		Parameters.setVerbose(false);
		Parameters.setClassAttribute(false);
		Parameters.setNumberOfClusterisationAlgIterations(10);

		HashMap<String, Integer> map = new HashMap<String, Integer>();
		map.put("classAtr", 0);

		stats = new DataStatistics(new double[] { 1, 1 }, new double[] { 2, 2 }, new double[] { 1, 1 }, 2, map,
				new int[] { 2 }, 0);
		data = new Data(dataPoints, 2, 2, stats, null);
	}

	@Test
	public void testRun() {
		Kmeans.setCenterMethod(new Centroid());
		Kmeans.setMeasure(new L2Norm());
		int liczbaBezZnaczenia = 0;
		ClustersAndTheirStatistics clustersAndTheirStatistics = kmeans.run(1, cluster, liczbaBezZnaczenia);
		assertEquals(2, clustersAndTheirStatistics.getClusters().length);
	}

	@Test
	public void testSetCenterMethod() {
		Kmeans.setCenterMethod(new Centroid());
	}

}
