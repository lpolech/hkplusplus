package test.dendrogram;

import static org.junit.Assert.*;

import org.junit.Test;

import data.Data;
import data.DataReader;
import data.Parameters;
import dendrogram.Dendrogram;
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
			"-k", "5",
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
	CmdLineParser parser = new CmdLineParser();
	
	
	@Test
	public void testDendrogram() {
		parser.parse(args);
		Data inputData = DataReader.read(Parameters.getInputDataFilePath());
		Dendrogram dendrogram = new Dendrogram(inputData, Parameters.getMethod(), Parameters.getK(),
				Parameters.getDendrogramMaxHeight(), Parameters.getOutputFolder());
		
		dendrogram.run();
		
		System.out.println("\nDendrogram bottom statistic: " + dendrogram.getFinalStatistic() + "\n");
		Utils.stopTimer();
		System.out.println("Whole program run: " + Utils.getTimerReport());
		
		System.out.println("\nDendrogram hierarchy rep: " + dendrogram.getHierarchyRepresentation() + "\n");

		System.out.println("\nDendrogram get den: " + dendrogram.getDendrogram().get(0) + "\n");
		//fail("Not yet implemented");
	}


	@Test
	public void testSaveSummaryStatistics() {
		fail("Not yet implemented");
	}

	@Test
	public void testGetHierarchyRepresentation() {
		fail("Not yet implemented");
	}

	@Test
	public void testGetFinalStatistic() {
		fail("Not yet implemented");
	}

	@Test
	public void testGetPoints() {
		fail("Not yet implemented");
	}

	@Test
	public void testGetDendrogram() {
		fail("Not yet implemented");
	}

}
