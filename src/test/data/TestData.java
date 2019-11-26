package test.data;

import static org.junit.Assert.assertEquals;

import java.util.HashMap;

import org.junit.Test;

import data.Data;
import data.DataPoint;
import data.DataStatistics;
import data.Parameters;

public class TestData {

	DataPoint datapoint;
	DataPoint datapoint2;
	DataPoint[] datapoints;

	static DataStatistics stats;
	static Data data;

	public TestData() {
		datapoint = new DataPoint(new double[] { 0, 0 }, new double[] { -2, -2 }, "instnam", "classAt");
		datapoint2 = new DataPoint(new double[] { 1, 1 }, new double[] { 2, 2 }, "instnam", "classAt");
		datapoints = new DataPoint[] { datapoint, datapoint2 };
		Parameters.setVerbose(false);
		Parameters.setClassAttribute(false);

		HashMap<String, Integer> map = new HashMap<String, Integer>();
		map.put("classAtr", 0);

		stats = new DataStatistics(new double[] { 0, 1 }, new double[] { 0, 1 }, new double[] { 1, 1 }, 2, map,
				new int[] { 2 }, 0);
		data = new Data(datapoints, 2, 2, stats, null);
	}

	@Test
	public void testGetDimensionNumberAndItsName() {
		assertEquals(null, data.getDimensionNumberAndItsName());
	}

	@Test
	public void testGetNumberOfDimensions() {
		assertEquals(2, data.getNumberOfDimensions());
	}

	@Test
	public void testGetNumberOfPoints() {
		assertEquals(2, data.getNumberOfPoints());
	}

	@Test
	public void testGetPoints() {
		assertEquals(true, data.getPoints()[0].arePointsPointingTheSame(datapoint));
	}

	@Test
	public void testGetDataStats() {
		assertEquals(stats, data.getDataStats());
	}

}
