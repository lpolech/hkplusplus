package runner;

import data.Data;
import data.DataReader;
import data.Parameters;
import dendrogram.Dendrogram;
import utils.CmdLineParser;
import utils.Utils;

public class Main {

	public static void main(String[] args) {
		//java -jar "Hk++Clust.jar" -d testowe5_5000.list -gmm -i testowe5_5000.csv -k 2 -n 25 -r 30 -s 10 -v -o testowe5_5000
		//L = {5, 4, 3, 2, 1, 0, -1, -2, -3, -4, -5}
		//E = {15, 14, 13, 12, 11, 10, 9, 8, 7, 6, 5}
//		args = new String[]{
////				"-i", "E:/VirtualBox_Shared/mat_ORG_rozmiary_Edge_tool_n03265032/Edge_tool.sbow.csv",//"Edge_tool.sbowSIMPLE_BINARY_SEARCH/375.csv",//"easy3Simply.txt",
////				"-i", "data_emoji_Polarity1_HK_postprocess_medoids_clus_20030_b_size_400612_iter_1000_init_10.csv",
//				"-i", "/Users/lo/Programming/hkplusplus/out/artifacts/hkplusplus_jar/benchmark-datasets 2/set00/GENERATOR_set00_a-1,0_l-0,5_g-0,2_N-10000_d-2_P-1,0_Q-5,0_minSD-0,05_maxSd-10,0_11.gt.csv",
////				"-h",
////				"-gmm",
//				"-lgmm",
//				"-s", "2147483600",
//				"-v",
//				"-o", "proba_se",//.concat(String.valueOf(L)).concat("_E").concat(String.valueOf(E)),
//				"-k", "2",
//				"-n", "50",//40//60 back
//				"-r", "50",//10 //60 back
////				"-d", "testowe.list",
//				"-e", "3",
//				"-l", "3",
//				"-c",
//				"-dm",
////				"-gi", //"666",
//				"-w", "2147483600",
//				"-in",
////				"-ds",
////				"-scac",
////				"-sccs",
////				"-scrs",
////				"-rf", "0.5",
////				"-cf", "0.88",
////				"-re"
//		};
		CmdLineParser parser = new CmdLineParser();
		parser.parse(args);
		
		Data inputData = DataReader.read(Parameters.getInputDataFilePath());
		Dendrogram dendrogram = new Dendrogram(inputData, Parameters.getMethod(), Parameters.getK(),
				Parameters.getDendrogramMaxHeight(), Parameters.getOutputFolder());
		dendrogram.run();
		
		//dendrogram.getHierarchyRepresentation().getRoot().printSubtree();
		
		
		System.out.println("\nDendrogram bottom statistic: " + dendrogram.getFinalStatistic() + "\n");
		Utils.stopTimer();
		System.out.println("Whole program run: " + Utils.getTimerReport());
	}

}
