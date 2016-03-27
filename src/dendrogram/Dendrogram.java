package dendrogram;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;

import javax.imageio.ImageIO;

import utils.Constans;
import utils.MoorePenrosePseudoinverse;
import utils.Utils;
import algorithms.AlgEnum;
import algorithms.Algorithm;
import algorithms.EM;
import algorithms.Kmeans;
import center.method.Centroid;
import data.Cluster;
import data.Data;
import data.Parameters;
import distance.measures.GMMBayesMLE;
import distance.measures.L2Norm;

public class Dendrogram {
	private Data points;
	private ArrayList<DendrogramLevel> dendrogram;
	private int k;
	private int levelsLimit;
	private Path levelsResultsStorage;
	private Path summaryStatisticsFile;
	private Path compactSummaryStaticticsFile;
	private int currentNumberOfNodes;
	
	public Dendrogram(Data points, AlgEnum clusterisationMethod, int k, int levelsLimit, 
			Path levelsImgsStorage)
	{
		this.points = points;
		this.k = k;
		this.levelsLimit = levelsLimit;
		this.levelsResultsStorage = levelsImgsStorage;
		dendrogram = new ArrayList<DendrogramLevel>();
		setClusterisationAlgorithm(clusterisationMethod);
		createSummaryStatisticsFile();
		createCompactSummaryStatisticsFile();
		insertRoot();
	}

	private void createSummaryStatisticsFile() 
	{
		summaryStatisticsFile = Utils.createCsvFileIfNotExists(Constans.summaryStatisticsFileName);
		writeHeaderInSummaryFile();
	}	
	
	private void createCompactSummaryStatisticsFile() {

		boolean insertHeader = !Files.exists(Paths.get(Constans.summaryCompactStatisticsFileName), LinkOption.NOFOLLOW_LINKS);
		compactSummaryStaticticsFile = Utils.createCsvFileIfNotExists(Constans.summaryCompactStatisticsFileName);
		if(insertHeader)
		{
			writeHeaderInCompactSummaryFile();
		}
	}
	
	private void writeHeaderInSummaryFile() 
	{
		try (FileWriter result = new FileWriter(summaryStatisticsFile.toString(), true)) 
		{
			result.write("Dataset" + Constans.delimiter + "Num. of Clusters" + Constans.delimiter + "Num. of Alg. Iterations (n)" + Constans.delimiter + "Num. of Alg. Repeats (r)" 
					+ Constans.delimiter + "Max Dendrogram Size (s)" + Constans.delimiter + "Epsilon" + Constans.delimiter + "Little Value" + Constans.delimiter 
					+ "Resp. Scal. Fact." + Constans.delimiter 
					+ "Clusterisation Method" + Constans.delimiter + "Output" + Constans.delimiter + "Dimension File" + Constans.delimiter + "Verbose" + Constans.delimiter
					+ "Use static center\n");
			
			result.write(Parameters.getInputDataFilePath() + Constans.delimiter + Parameters.getK() + Constans.delimiter + Parameters.getNumberOfClusterisationAlgIterations()
					+ Constans.delimiter + Parameters.getNumberOfClusterisationAlgRepeats()	+ Constans.delimiter + Parameters.getDendrogramMaxHeight() 
					+ Constans.delimiter + Parameters.getEpsilon() + Constans.delimiter + Parameters.getLittleValue() + Constans.delimiter + Parameters.getResponsibilityScallingFactor()
					+ Constans.delimiter + Parameters.getMethod() 
					+ Constans.delimiter + Parameters.getOutputFolder() + Constans.delimiter + Parameters.getDimensionsNamesFilePath() + Constans.delimiter
					+ (Parameters.isVerbose()? "true" : "false") + Constans.delimiter + (Parameters.isDisableStaticCenter()? "false" : "true") + "\n");
			result.write("Clust. Level" + Constans.delimiter + DendrogramLevel.getMeasure().printClusterisationStatisticName() 
					+ Constans.delimiter + "Flat FMeasure" + Constans.delimiter + "Incremental hierarch. FMeasure" + "\n");
		} 
		catch (IOException e) {
			System.out.println("Dendrogram.writeHeaderInSummaryFile" + e);
		}
	}
	
	private void writeHeaderInCompactSummaryFile() {
		try (FileWriter result = new FileWriter(compactSummaryStaticticsFile.toString(), true)) 
		{
			result.write("Dataset" + Constans.delimiter + "Number of Levels" + Constans.delimiter + "Statistic Value" + Constans.delimiter + "FMeasure"
					+ Constans.delimiter + "Num. of Max. Clusters" + Constans.delimiter + "Num. of Max. Alg. Iterations (n)" + Constans.delimiter 
					+ "Num. of Max. Alg. Repeats (r)" + Constans.delimiter + "Max Dendrogram Size (s)" + Constans.delimiter + "Max. number of nodes (w)" + Constans.delimiter
					+ "Epsilon" + Constans.delimiter + "Little Value" + Constans.delimiter + "Resp. Scal. Fact." + Constans.delimiter + "Clusterisation Method" + Constans.delimiter + "Output" + Constans.delimiter + "Dimension File" 
					+ Constans.delimiter + "Verbose" + Constans.delimiter + "Use static center" + Constans.delimiter +  "Eclapsed Time [min]\n");
		} 
		catch (IOException e) {
			System.out.println("Dendrogram.FileWriter" + e);
		}
	}

	private void setClusterisationAlgorithm(AlgEnum clusterisationMethod) 
	{
		if(Parameters.isVerbose())
		{
			System.out.println("Clusterisation algorithm: " + clusterisationMethod);
		}
		Algorithm clusterisationAlgorithm;
		switch(clusterisationMethod)
		{
		case KMEANS:
			Kmeans.setMeasure(new L2Norm());
			Kmeans.setCenterMethod(new Centroid());
			clusterisationAlgorithm = new Kmeans();
			Cluster.setAlgorithm(clusterisationAlgorithm);
			DendrogramLevel.setDistanceMethod(new Centroid());
			DendrogramLevel.setMeasure(new L2Norm());
			break;
			
		case GMM:
			EM.setMeasure(new GMMBayesMLE());
			clusterisationAlgorithm = new EM();
			Cluster.setAlgorithm(clusterisationAlgorithm);
			DendrogramLevel.setDistanceMethod(new EM());
			DendrogramLevel.setMeasure(new GMMBayesMLE());
			MoorePenrosePseudoinverse.updateMacheps();
			break;
		}
	}

	private void insertRoot() {
		DendrogramLevel root = new DendrogramLevel(0, points.getDataStats());
		root.makeRoot(points);
		dendrogram.add(root);
		computeDendrogramStatistics();
		saveClusterisationResults(root, 0);
	}

	public ArrayList<DendrogramLevel> run()
	{
		Utils.startTimer();
		
		int levelNumber = 1;//0 - korzen tworzony w konstruktorze
		while(!shouldTerminate(levelNumber))
		{
			System.out.println("Working on level " + levelNumber + "... ");
			DendrogramLevel newLevel = getDendrogramBottom().expandTree(k, levelNumber, points.getDataStats(), currentNumberOfNodes);
			dendrogram.add(newLevel);
			computeDendrogramStatistics();
			saveClusterisationResults(newLevel, levelNumber);
			levelNumber++;
			System.out.println("Done.");
		}
		
		Utils.stopTimer();
		
		saveCompactSummaryStatistics(dendrogram.get(dendrogram.size()-1), levelNumber, Utils.getTimerInMinutes());
		return dendrogram;
	}
	
	private void computeDendrogramStatistics() {
		getDendrogramBottom().getClusterisationStatistics().setIncrementalHierarchicalFMeasure(calculateFMeasure());
		
	}

	private void saveClusterisationResults(DendrogramLevel newLevel, int levelNumber) {
		saveClusterisation(newLevel, levelNumber);
		saveSummaryStatistics(newLevel, levelNumber);
		if(!Parameters.isNormaliseData())
		{
			saveImage(newLevel, levelNumber);
		}
	}

	private void saveClusterisation(DendrogramLevel newLevel, int levelNumber) {
		if(levelsResultsStorage != null)
		{
			File clusterisationFile = new File(levelsResultsStorage.toString() + File.separator + levelNumber + "_clusterisation" + ".csv");
			newLevel.write(points, clusterisationFile);
		}		
	}

	public void saveSummaryStatistics(DendrogramLevel newLevel, int levelNumber) {
		try (FileWriter result = new FileWriter(summaryStatisticsFile.toString(), true)) 
		{
			result.write(levelNumber + Constans.delimiter + newLevel.getStatistic() + Constans.delimiter 
					+ newLevel.getClusterisationStatistics().getFlatClusterisationFMeasure() 
					+ Constans.delimiter 
					+ getDendrogramBottom().getClusterisationStatistics().getIncrementalHierarchicalFMeasure() + "\n");
		} 
		catch (IOException e) {
			System.out.println("Dendrogram.saveSummaryStatistics" + e);
		}
	}
	
	private void saveCompactSummaryStatistics(DendrogramLevel newLevel, int levelNumber, double eclapsedTimeInMin) {
		try (FileWriter result = new FileWriter(compactSummaryStaticticsFile.toString(), true)) 
		{
			result.write(Parameters.getInputDataFilePath() + Constans.delimiter + levelNumber + Constans.delimiter + newLevel.getStatistic()
					+ Constans.delimiter + getDendrogramBottom().getClusterisationStatistics().getIncrementalHierarchicalFMeasure() + Constans.delimiter 
					+ Parameters.getK() + Constans.delimiter + Parameters.getNumberOfClusterisationAlgIterations()
					+ Constans.delimiter + Parameters.getNumberOfClusterisationAlgRepeats()	+ Constans.delimiter + Parameters.getDendrogramMaxHeight() 
					+ Constans.delimiter + Parameters.getMaxNumberOfNodes()
					+ Constans.delimiter + Parameters.getEpsilon() + Constans.delimiter + Parameters.getLittleValue() + Constans.delimiter 
					+ Parameters.getResponsibilityScallingFactor() + Constans.delimiter + Parameters.getMethod() 
					+ Constans.delimiter + Parameters.getOutputFolder() + Constans.delimiter + Parameters.getDimensionsNamesFilePath() + Constans.delimiter
					+ (Parameters.isVerbose()? "true" : "false") + Constans.delimiter + (Parameters.isDisableStaticCenter()? "false" : "true") + Constans.delimiter 
					+ Double.toString(eclapsedTimeInMin) + "\n");
			
		} 
		catch (IOException e) {
			System.out.println("Dendrogram.saveCompactSummaryStatistics" + e);
		}		
	}

	private Double calculateFMeasure() {
		double fMeasure = Double.NaN;
		if(Parameters.isClassAttribute())
		{
			fMeasure = 0.0;
			int numberOfClasses = points.getDataStats().getClassNameAndItsId().size();
			int[] eachClassNumOfInstances = points.getDataStats().getEachClassNumberOfInstance();
			double[] eachClassMaxFMeasure = new double[numberOfClasses]; //TODO: czy tam sa 0?
			for(int classNum = 0; classNum < numberOfClasses; classNum++)
			{
				for(int dendLevel = 0; dendLevel < dendrogram.size(); dendLevel++)
				{
					double levelMaxClassFMeasure = dendrogram.get(dendLevel).getClusterisationStatistics().getEachClassMaxFMeasure()[classNum];
					if(levelMaxClassFMeasure > eachClassMaxFMeasure[classNum])
					{
						eachClassMaxFMeasure[classNum] = levelMaxClassFMeasure;
					}
				}
				fMeasure += (eachClassNumOfInstances[classNum]/(double)(points.getDataStats().getDatasetLength()-points.getDataStats().getNumberOfNoisePoints())) 
						* eachClassMaxFMeasure[classNum];
			}
		}
		return fMeasure;
	}

	private void saveImage(DendrogramLevel newLevel, int levelNumber) 
	{
		if(levelsResultsStorage != null)
		{
			try {
				File imgFile = new File(levelsResultsStorage.toString() + File.separator + levelNumber + ".png");
				ImageIO.write(newLevel.getImage(points), "png", imgFile);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	private boolean shouldTerminate(int numberOfLevels) {
		if(leafClustersDontHaveEnoughPoints())
		{
			return true;
		}
		else if(numberOfLevels >= levelsLimit)
		{
			return true;
		}
		else if(numberOfNodesExceedsLimit())
		{
			return true;
		}
		
		return false;
	}

	private boolean leafClustersDontHaveEnoughPoints() {
		return getDendrogramBottom().allClustersDontHaveEnoughPoints(k);
	}
	
	private boolean numberOfNodesExceedsLimit() {
		currentNumberOfNodes = 0;
		for(DendrogramLevel level: dendrogram)
		{
			for(Cluster c: level.getClusters())
			{
				if(c.getNumberOfPoints() > 0)
				{
					currentNumberOfNodes++;
				}
			}
		}
		if(currentNumberOfNodes >= Parameters.getMaxNumberOfNodes())
		{
			return true;
		}
		return false;
	}

	private DendrogramLevel getDendrogramBottom()
	{
		if(dendrogram.size() > 0)
			return dendrogram.get(dendrogram.size()-1);
		else
			return null;
	}
	
	public double getFinalStatistic()
	{
		return getDendrogramBottom().getStatistic();
	}
}
