package test.dendrogram;

import static org.junit.Assert.*;

import java.util.ArrayList;

import org.junit.Test;

import data.Data;
import data.DataPoint;
import data.DataReader;
import data.Parameters;
import dendrogram.Dendrogram;
import dendrogram.DendrogramLevel;
import utils.CmdLineParser;
import utils.Utils;

public class TestDendrogram {
	String[] args = new String[]{
			"-i", "test.csv",
			"-lgmm",
			"-s", "2",
			"-v",
			"-o", "out",
			"-k", "2",
			"-n", "1",
			"-r", "1",
			"-e", "10",
			"-l", "0",
			"-c",
			"-dm",
			"-gi",
			"-w", "2147483600",
			"-in",
	};
	CmdLineParser parser;
	Data inputData;
	Dendrogram dendrogram;
	
	public TestDendrogram()
	{
		parser = new CmdLineParser();
		parser.parse(args);
		inputData = DataReader.read(Parameters.getInputDataFilePath());
		dendrogram = new Dendrogram(inputData, Parameters.getMethod(), Parameters.getK(),
				Parameters.getDendrogramMaxHeight(), Parameters.getOutputFolder());
	}
	
	@Test
	public void testDendrogram() {

		ArrayList<DendrogramLevel> list= dendrogram.run();
		assertEquals(2,list.size());
		assertEquals(1,list.get(0).getClusters().length);
		assertEquals(3,list.get(1).getClusters().length);
	}

	@Test
	public void testGetHierarchyRepresentation() {
		assertEquals(1, dendrogram.getHierarchyRepresentation().getNumberOfGroups());
	}

	@Test
	public void testGetFinalStatistic() {
		DataPoint.setNumberOfDimensions(2);
		assertEquals(-15, dendrogram.getFinalStatistic(), 1);
	}

	@Test
	public void testGetPoints() {
		assertEquals(10, dendrogram.getPoints().getNumberOfPoints());
	}

	@Test
	public void testGetDendrogram() {
		assertEquals(1, dendrogram.getDendrogram().size());
	}

}
