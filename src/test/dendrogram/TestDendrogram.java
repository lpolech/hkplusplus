package test.dendrogram;

import static org.junit.Assert.*;

import java.util.ArrayList;

import org.junit.Test;

import data.Data;
import data.DataReader;
import data.Parameters;
import dendrogram.Dendrogram;
import dendrogram.DendrogramLevel;
import utils.CmdLineParser;
import utils.Utils;

public class TestDendrogram {
	String[] args = new String[]{
//			"-i", "E:/VirtualBox_Shared/mat_ORG_rozmiary_Edge_tool_n03265032/Edge_tool.sbow.csv",//"Edge_tool.sbowSIMPLE_BINARY_SEARCH/375.csv",//"easy3Simply.txt",
			"-i", "test.csv",
//			"-h",
//			"-gmm",
			"-lgmm",
			"-s", "2",
			"-v",
			"-o", "out",//.concat(String.valueOf(L)).concat("_E").concat(String.valueOf(E)),
			"-k", "1",
			"-n", "1",//40//60 back
			"-r", "1",//10 //60 back
//			"-d", "testowe.list",
			"-e", "10",
			"-l", "0",
			"-c",
			"-dm",
			"-gi", //"666",
			"-w", "2147483600",
			"-in",
//			"-ds",
//			"-scac",
//			"-sccs",
//			"-scrs",
//			"-rf", "0.5",
//			"-cf", "0.88",
//			"-re"
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
		
	}


	@Test
	public void testSaveSummaryStatistics() {
		//funkcja wykorzystywana przy dendrogram.run();
	}

	@Test
	public void testGetHierarchyRepresentation() {
		assertEquals(1, dendrogram.getHierarchyRepresentation().getNumberOfGroups());
	}

	//tez jakis parametr zmienia wartosc wyjsciowa, 
	@Test
	public void testGetFinalStatistic() {
		assertEquals(-46427, dendrogram.getFinalStatistic(), 1);
	}

	@Test
	public void testGetPoints() {
		assertEquals(9999, dendrogram.getPoints().getNumberOfPoints());
	}

	@Test
	public void testGetDendrogram() {
		assertEquals(1, dendrogram.getDendrogram().size());
	}

}
