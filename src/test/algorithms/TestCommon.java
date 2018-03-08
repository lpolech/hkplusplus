package test.algorithms;

import static org.junit.Assert.*;

import java.awt.Color;

import org.junit.Test;

import data.Cluster;
import data.DataPoint;
import distance.measures.L2Norm;
import algorithms.Common;

public class TestCommon {

	Cluster cluster;
	DataPoint [] dataPoints;
	DataPoint center;
	Color color;
	int parentId;
	int rootId;
	Common common;
	
	//Jak dostac sie do metod protected z obiektu? i jak je testowac?
	public TestCommon()
	{
		dataPoints = new DataPoint[] {new DataPoint(new double []{2,1},new double []{1,2},"intNam","classAtr"),
				new DataPoint(new double []{1,2},new double []{1,2},"intNam","classAtr")};
		center = new DataPoint(new double []{1,2},new double []{1,2},"intNam","classAtr") ;
		color= new Color(255,255,255);
		parentId=0;
		rootId=0;
		cluster = new Cluster(dataPoints,center,color,parentId,rootId);
		common = new Common ();
	}
	
	@Test
	public void testRun() {
		assertEquals(null, common.run(1, cluster, 0));
	}


	@Test
	public void testSetMeasure() {
		Common.setMeasure(new L2Norm());
	}

}
