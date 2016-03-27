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
//		args = new String[]{"-i",
//				"iris.csv",
//				//"-h",
//				"-gmm",
//				"-n",
//				"10",//40//60 back
//				"-s",
//				"10",
////				"-v",
//				"-o",
//				"ODBYTY",//.concat(String.valueOf(L)).concat("_E").concat(String.valueOf(E)),
//				"-k",
//				"2",
//				"-r",
//				"10",//10 //60 back
//				"-w",
//				"2",
////				"-d",
////				"testowe.list",
//				"-e",
//				"15",
//				"-l",
//				"4",
//				"-c",
////				"-nn",
////				"-ds",
//				"-f",
//				"0.5",				
//		};
		CmdLineParser parser = new CmdLineParser();
		parser.parse(args);
		
		Data inputData = DataReader.read(Parameters.getInputDataFilePath(), Parameters.getDimensionsNamesFilePath());
		Dendrogram dendrogram = new Dendrogram(inputData, Parameters.getMethod(), Parameters.getK(), 
				Parameters.getDendrogramMaxHeight(), Parameters.getOutputFolder());
		dendrogram.run();
		
		System.out.println("\nDendrogram bottom statistic: " + dendrogram.getFinalStatistic() + "\n");
		System.out.println(Utils.getTimerReport());
	}

}
