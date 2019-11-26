package test.dendrogram;

import static org.junit.Assert.*;

import java.awt.Color;
import java.util.HashMap;

import org.junit.Test;

import center.method.Centroid;
import data.Cluster;
import data.Data;
import data.DataPoint;
import data.DataStatistics;
import data.Parameters;
import dendrogram.ClusterisationStatistics;
import dendrogram.DendrogramLevel;
import distance.measures.L2Norm;

public class TestClusterisationStatistics {
	
	Cluster cluster;
	DataPoint [] dataPoints;
	DataPoint center;
	Color color;
	int parentId;
	int rootId;
	DendrogramLevel dendrogramLevel;
	DataStatistics dataStatistics; 
	DataStatistics dataStatistics2; 
	
	Data data;
	ClusterisationStatistics clusterisationStatistics;
	public TestClusterisationStatistics() {
		
		dataPoints = new DataPoint[] {new DataPoint(new double []{2,1},new double []{1,2},"intNam","classAtr"),
				new DataPoint(new double []{1,2},new double []{1,2},"intNam","classAtr")};
		center = new DataPoint(new double []{1,2},new double []{1,2},"intNam","classAtr") ;
		color= new Color(255,255,255);
		parentId=0;
		rootId=0;
		cluster = new Cluster(dataPoints,center,color,parentId,rootId);
		
		Parameters.setVerbose(true);
		Parameters.setClassAttribute(false);
		Parameters.setMaxNumberOfNodes(100);
		Parameters.setNumberOfClusterisationAlgIterations(10);
		Parameters.setNumberOfClusterisationAlgRepeats(5);
		
		Parameters.setClassAttribute(true);
		
		HashMap<String, Integer> map= new HashMap<String, Integer>();
		map.put("classAtr", 0);
		
		dataStatistics=new DataStatistics(new double [] {1,1}, new double [] {2,2}, new double[] {1,1},
				2, map, new int [] {2}, 0);
		
		data = new Data(dataPoints,2,2, dataStatistics, (new HashMap<Integer, String>()) );
		
		dendrogramLevel= new DendrogramLevel(0, dataStatistics);
		
		DendrogramLevel.setDistanceMethod(new Centroid());
		DendrogramLevel.setMeasure(new L2Norm());
		dendrogramLevel.makeRoot(data);
		Parameters.setVerbose(false);
		
		clusterisationStatistics= new ClusterisationStatistics(dataStatistics);
	}

	@Test
	public void testComputeFlatClusterisationFMeasure() {
		clusterisationStatistics.computeFlatClusterisationFMeasure(dendrogramLevel);
	}

	@Test
	public void testGetEachClassMaxFMeasure() {
		assertArrayEquals(null, clusterisationStatistics.getEachClassMaxFMeasure(), 0.1);
		clusterisationStatistics.computeFlatClusterisationFMeasure(dendrogramLevel);
		assertArrayEquals(new double[] {1.0}, clusterisationStatistics.getEachClassMaxFMeasure(), 0.1);
	}

	@Test
	public void testGetFlatClusterisationFMeasure() {
		assertEquals(Double.NaN, clusterisationStatistics.getFlatClusterisationFMeasure(), 0.1);
		clusterisationStatistics.computeFlatClusterisationFMeasure(dendrogramLevel);
		assertEquals(1.0, clusterisationStatistics.getFlatClusterisationFMeasure(), 0.1);
	}

	@Test
	public void testGetIncrementalHierarchicalFMeasure() {
		assertEquals(0,clusterisationStatistics.getIncrementalHierarchicalFMeasure(), 0.1);
	}

	@Test
	public void testSetIncrementalHierarchicalFMeasure() {
		assertEquals(0,clusterisationStatistics.getIncrementalHierarchicalFMeasure(), 0.1);
		clusterisationStatistics.setIncrementalHierarchicalFMeasure(1.0);
		assertEquals(1.0,clusterisationStatistics.getIncrementalHierarchicalFMeasure(), 0.1);
	}

	@Test
	public void testGetAdaptedFmeasureWithInheritance() {
		assertEquals(0,clusterisationStatistics.getAdaptedFmeasureWithInheritance(), 0.1);
	}

	@Test
	public void testSetAdaptedFmeasureWithInheritance() {
		assertEquals(0,clusterisationStatistics.getAdaptedFmeasureWithInheritance(), 0.1);
		clusterisationStatistics.setAdaptedFmeasureWithInheritance(1.0);
		assertEquals(1.0,clusterisationStatistics.getAdaptedFmeasureWithInheritance(), 0.1);
	}

	@Test
	public void testGetAdaptedFmeasureWithOUTInheritance() {
		assertEquals(0,clusterisationStatistics.getAdaptedFmeasureWithOUTInheritance(), 0.1);
	}

	@Test
	public void testSetAdaptedFmeasureWithOUTInheritance() {
		assertEquals(0,clusterisationStatistics.getAdaptedFmeasureWithOUTInheritance(), 0.1);
		clusterisationStatistics.setAdaptedFmeasureWithOUTInheritance(1.0);
		assertEquals(1.0,clusterisationStatistics.getAdaptedFmeasureWithOUTInheritance(), 0.1);
	}

	@Test
	public void testGetStandardFmeasure() {
		assertEquals(0,clusterisationStatistics.getStandardFmeasure(), 0.1);
	}

	@Test
	public void testSetStandardFmeasure() {
		assertEquals(0,clusterisationStatistics.getStandardFmeasure(), 0.1);
		clusterisationStatistics.setStandardFmeasure(1.0);
		assertEquals(1.0,clusterisationStatistics.getStandardFmeasure(), 0.1);
	}

	@Test
	public void testGetPartialOrderFscore() {
		assertEquals(0,clusterisationStatistics.getPartialOrderFscore(), 0.1);
	}

	@Test
	public void testSetPartialOrderFscore() {
		assertEquals(0,clusterisationStatistics.getPartialOrderFscore(), 0.1);
		clusterisationStatistics.setPartialOrderFscore(1.0);
		assertEquals(1.0,clusterisationStatistics.getPartialOrderFscore(), 0.1);
	}
}
