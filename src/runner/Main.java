package runner;

import data.Data;
import data.DataReader;
import data.Parameters;
import dendrogram.Dendrogram;
import utils.CmdLineParser;
import utils.Utils;

public class Main {

	public static void main(String[] args) {

		CmdLineParser parser = new CmdLineParser();
		parser.parse(args);

		Data inputData = DataReader.read(Parameters.getInputDataFilePath());
		Dendrogram dendrogram = new Dendrogram(inputData, Parameters.getMethod(), Parameters.getK(),
				Parameters.getDendrogramMaxHeight(), Parameters.getOutputFolder());
		dendrogram.run();

		// dendrogram.getHierarchyRepresentation().getRoot().printSubtree();

		System.out.println("\nDendrogram bottom statistic: " + dendrogram.getFinalStatistic() + "\n");
		Utils.stopTimer();
		System.out.println("Whole program run: " + Utils.getTimerReport());
	}

}
