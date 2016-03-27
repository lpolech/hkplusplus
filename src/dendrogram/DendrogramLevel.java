package dendrogram;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import utils.Constans;
import utils.Utils;
import center.method.CenterMethod;
import data.Cluster;
import data.ClustersAndTheirStatistics;
import data.ColorPalette;
import data.Data;
import data.DataPoint;
import data.DataStatistics;
import data.Parameters;
import distance.measures.Measure;

public class DendrogramLevel {
	private int numberOfNotExpandedNonStaticCenters;
	private static CenterMethod distanceMethod;
	private static Measure measure;
	private Cluster[] clusters;
	private BufferedImage imageRepresentation;//zakladam dane 2 wymiarowe!
	private double statistic = -1;
	private ClusterisationStatistics clusterisationStatistics;

	public DendrogramLevel(int numberOfNotExpandedNonStaticCenters, DataStatistics dataStatistics)
	{
		this.numberOfNotExpandedNonStaticCenters = numberOfNotExpandedNonStaticCenters;
		this.clusterisationStatistics = new ClusterisationStatistics(dataStatistics); 
	}
	
	public boolean allClustersDontHaveEnoughPoints(int k) {
		for(Cluster cls: clusters)
		{
			if(!cls.isStaticCenter() && cls.getNumberOfPoints() > k
					&& clusterContainsAtLeastKDifferentPoints(cls, k))
				return false;
		}
		return true;
	}

	public DendrogramLevel expandTree(int k, int currentLevelNumber, DataStatistics dataStatistics, int numberOfAlreadyCreatedNodes) {//FIXME strong refaktor rozwazyc: rezygnacja z tablic i przejscie na array list
		int numberOfClustersToExpand = getNumberOfNonStaticClusters();
		int numberOfClusters = getNumberOfClustersOnGivenLevel(k, currentLevelNumber, numberOfClustersToExpand);
		Cluster[] newLevelClusters = new Cluster[numberOfClusters];
		int elementCounter = 0;
		int clusterCounter = 1;
		int nonExpandedClustersCounter = 0;
		for(int i = 0; i < clusters.length; i++)
		{
			ClustersAndTheirStatistics newClusters = null;
			ClustersAndTheirStatistics bestFoundClusterisation = new ClustersAndTheirStatistics(null, measure.getTheWorstMeasureValue(), false);

			if(numberOfAlreadyCreatedNodes < Parameters.getMaxNumberOfNodes() && canSplitCluster(i, k))//pomijam stale centra oraz te, ktore juz nie mozna podzielic
			{
				if(Parameters.isVerbose())
				{
					System.out.print("\tExpanding cluster " + (clusterCounter++) + "/" + numberOfClustersToExpand + ". ");
				}
				for(int j = 0; j < Parameters.getNumberOfClusterisationAlgRepeats(); j++)
				{
					newClusters = clusters[i].performSplit(k, numberOfAlreadyCreatedNodes);
					if(measure.compareStatistics(bestFoundClusterisation.getClusterisationStatistic(), newClusters.getClusterisationStatistic()) < 0)
					{
						bestFoundClusterisation = newClusters;
					}
				}
				numberOfAlreadyCreatedNodes += bestFoundClusterisation.getClusters().length;
			}
			else//zostawiam koncowe centra w kazdyn z poziomow, aby kazde z centr bylo widoczne na kazdym poziomie
			{
				Cluster[] notSplittedCluster = new Cluster[]{clusters[i]};
				bestFoundClusterisation = new ClustersAndTheirStatistics(notSplittedCluster, measure.calculateClusterisationStatistic(notSplittedCluster), true);
				if(!notSplittedCluster[0].isStaticCenter())
				{
					nonExpandedClustersCounter++;
				}
				
				if(notSplittedCluster[0].isStaticCenter() && Parameters.isVerbose())
				{
					System.out.println("\tMoving static center (" + notSplittedCluster[0].getCenter() +") from upper level. " + measure.printClusterisationStatisticName()
							+ ": " + bestFoundClusterisation.getClusterisationStatistic());
				}
				else if(Parameters.isVerbose())
				{
					System.out.print("\tExpanding cluster " + (clusterCounter++) + "/" + numberOfClustersToExpand + ". Can't split, just copy. ");
				}
			}
			
			for(int j = 0; j < bestFoundClusterisation.getClusters().length; j++)
			{
				newLevelClusters[elementCounter++] = bestFoundClusterisation.getClusters()[j];
			}
			
			if(Parameters.isVerbose() && !clusters[i].isStaticCenter())
			{
				System.out.println("Done. " + measure.printClusterisationStatisticName()
							+ ": "  + bestFoundClusterisation.getClusterisationStatistic());
			}
		}
		
		DendrogramLevel newLevel = new DendrogramLevel(nonExpandedClustersCounter, dataStatistics);
		if(elementCounter != numberOfClusters)//powstalo mniej klastrow niz zakladalismy
		{
			newLevel.clusters = new Cluster[elementCounter];
			for(int i = 0; i < elementCounter; i++)// TODO pomijam niezainicjalizowane klastry, moze warto wykminic lepszys posob?
			{
				newLevel.clusters[i] = newLevelClusters[i];
			}
		}
		else
		{
			newLevel.clusters = newLevelClusters;
		}
		
		newLevel.statistic = measure.calculateClusterisationStatistic(newLevel.clusters);// TODO: zmieniæ nazwe na np. objectiveFunction
		newLevel.clusterisationStatistics.compute(newLevel);
		System.out.println("Level " + measure.printClusterisationStatisticName()
				+ ": " + newLevel.statistic);
		
		if(Parameters.isClassAttribute())
		{
			System.out.println("Level flat f-measure: " + newLevel.clusterisationStatistics.getFlatClusterisationFMeasure());
		}
		
		return newLevel;
	}

	private boolean canSplitCluster(int clusterNumber, int k) {
		return clusters[clusterNumber].getPoints().length >= k && !clusters[clusterNumber].isStaticCenter() 
				&& clusterContainsAtLeastKDifferentPoints(clusters[clusterNumber], k);
	}

	private boolean clusterContainsAtLeastKDifferentPoints(Cluster cluster,
			int k) {
		DataPoint[] kDifferentPoints = new DataPoint[k];
		int numberOfFoundDistinctPoints = 1;
		
		kDifferentPoints[0] = cluster.getPoints()[0];
		for(int i = 1; i < cluster.getPoints().length && numberOfFoundDistinctPoints < k; i++)
		{
			if(areDataPointsDifferentFromChoosenOne(kDifferentPoints,
					numberOfFoundDistinctPoints, cluster.getPoints(), i))
			{
				kDifferentPoints[numberOfFoundDistinctPoints++] = 
						cluster.getPoints()[i];
			}
		}
		return numberOfFoundDistinctPoints >= k;
	}

	private boolean areDataPointsDifferentFromChoosenOne(DataPoint[] dataPoints,
			int numberOfDataPoints, DataPoint[] clusterPoints, int dataPointToCompareIndex) {
		for(int j = 0; j < numberOfDataPoints; j++)
		{
			if(clusterPoints[dataPointToCompareIndex].arePointsPointingTheSame(dataPoints[j]))
			{
				return false;
			}
		}
		return true;
	}

	private int getNumberOfClustersOnGivenLevel(int k,
			int currentLevelNumber, int numberOfNonStaticClusters) {
		int numberOfStaticClustersOnThisLevel = clusters.length;
		int numberOfMaximumNewClusters = k*(numberOfNonStaticClusters-this.numberOfNotExpandedNonStaticCenters) + numberOfNotExpandedNonStaticCenters;//numberOfNonStaticClusters*k;
		return numberOfMaximumNewClusters + numberOfStaticClustersOnThisLevel;//uwzgledniam mozliwosc rozszerzania NIE-statycznych klkastrow
		//oraz obecnosc tych statycznych
	}

	private int getNumberOfNonStaticClusters() {
		int counter = 0;
		for(int i = 0; i < clusters.length; i++)
		{
			if(!clusters[i].isStaticCenter())
			{
				counter++;
			}
		}
		return counter;
	}

	public void makeRoot(Data points) {
		clusters = new Cluster[1];
		clusters[0] = distanceMethod.makeCluster(points, measure);
		this.statistic = measure.calculateClusterisationStatistic(this.clusters);
		this.clusterisationStatistics.compute(this);
	}

	public void write(Data points, File clusterisationFile) 
	{
		try (FileWriter result = new FileWriter(clusterisationFile, false)) 
		{
			result.write("Level summary statistics" + Constans.delimiter + statistic + "\n");//napisac statystyki poziomu

			for(int i = 0; i < clusters.length; i++)
			{
				result.write("Cluster" + Constans.delimiter + i + "\n");
				result.write("Id" + Constans.delimiter + clusters[i].getClusterId() + "\n");
				result.write("Parent Id" + Constans.delimiter + clusters[i].getParentId() + "\n");
				result.write("Static" + Constans.delimiter + (clusters[i].isStaticCenter()? "yes" : "no") + "\n");
				result.write("Center" + Constans.delimiter + clusters[i].getCenter() + "\n");
				result.write("Colour [r g b]" + Constans.delimiter + Utils.getStringOfColor(clusters[i].getColorOnImage()) + "\n");
				result.write("Points (" + clusters[i].getPoints().length + "):\n");
				for(int j = 0; j < clusters[i].getPoints().length; j++)
				{
					DataPoint pointToWrite = clusters[i].getPoints()[j];
					String dimensionsNames = "";
					
					if(points.getDimensionNumberAndItsName().size() > 0)
					{
						for(int k = 0; k < DataPoint.getNumberOfDimensions(); k++)
						{
							if(!Utils.isDoubleExtremelyNearZero(pointToWrite.getCoordinate(k)))
							{
								dimensionsNames += Constans.delimiter;
								dimensionsNames += points.getDimensionNumberAndItsName().get(k);
							}
						}
					}
					result.write(Constans.delimiter + pointToWrite + Constans.delimiter + dimensionsNames + "\n");
				}
				result.write("\n");
			}
		} 
		catch (IOException e) {
			System.out.println("DendrogramLevel.write" + e);
		}		
	}
	
	public BufferedImage getImage(Data data)
	{
		if(imageRepresentation == null)
		{
			createImageRepresentation(data.getDataStats());
		}
		return imageRepresentation;
	}

	private void createImageRepresentation(DataStatistics dataStats) {
		int imgWidth = Utils.roundToUpper(dataStats.getMaxValues()[0]) + 1;//TODO na razie zakladam 2 wymiarowe dane i to, ze dane sa dodatnie i zaczynaja sie od 0,0
		int imgHeight = Utils.roundToUpper(dataStats.getMaxValues()[1]) + 1;
		
		createEmptyImage(imgWidth, imgHeight);
		drawClusters();
		drawCenters();
		
	}

	private void createEmptyImage(int imgWidth, int imgHeight) {
		imageRepresentation = new BufferedImage(imgWidth, imgHeight, BufferedImage.TYPE_INT_RGB);
		Graphics2D graphics = imageRepresentation.createGraphics();
		graphics.setColor(Color.WHITE);
		graphics.fillRect(0, 0, imageRepresentation.getWidth(), imageRepresentation.getHeight());
		graphics.dispose();
	}

	private void drawClusters() 
	{
		Color clusterColor;
		for(Cluster clr: clusters)
		{
			if(clr.getColorOnImage() == null)
			{
				clr.setColorOnImage(ColorPalette.getNextColor());
			}
			clusterColor = clr.getColorOnImage();
			for(DataPoint p: clr.getPoints())
			{
				imageRepresentation.setRGB(Utils.roundToUpper(p.getCoordinate(0)), 
						Utils.roundToUpper(p.getCoordinate(1)), clusterColor.getRGB());				
			}
		}		
	}
	
	private void drawCenters() 
	{
		Color centerColor = new Color(0, 0, 0);
		for(Cluster clr: clusters)
		{
			imageRepresentation.setRGB(Utils.roundToUpper(clr.getCenter().getCoordinate(0)), 
					Utils.roundToUpper(clr.getCenter().getCoordinate(1)), centerColor.getRGB());
			if(Utils.roundToUpper(clr.getCenter().getCoordinate(0)) > 0 
					&& Utils.roundToUpper(clr.getCenter().getCoordinate(0)) < (imageRepresentation.getWidth() - 1)
					&& Utils.roundToUpper(clr.getCenter().getCoordinate(1)) > 0
					&& Utils.roundToUpper(clr.getCenter().getCoordinate(1)) < (imageRepresentation.getHeight() - 1))
			{
				//draw X sign
				imageRepresentation.setRGB(Utils.roundToUpper(clr.getCenter().getCoordinate(0) + 1), 
						Utils.roundToUpper(clr.getCenter().getCoordinate(1) + 1), centerColor.getRGB());
				imageRepresentation.setRGB(Utils.roundToUpper(clr.getCenter().getCoordinate(0) + 1), 
						Utils.roundToUpper(clr.getCenter().getCoordinate(1) - 1), centerColor.getRGB());
				imageRepresentation.setRGB(Utils.roundToUpper(clr.getCenter().getCoordinate(0) - 1), 
						Utils.roundToUpper(clr.getCenter().getCoordinate(1) - 1), centerColor.getRGB());
				imageRepresentation.setRGB(Utils.roundToUpper(clr.getCenter().getCoordinate(0) - 1), 
						Utils.roundToUpper(clr.getCenter().getCoordinate(1) + 1), centerColor.getRGB());				
			}
		}
	}

	public static void setDistanceMethod(CenterMethod distanceMethod) {
		DendrogramLevel.distanceMethod = distanceMethod;
	}

	public static void setMeasure(Measure measure) {
		DendrogramLevel.measure = measure;
	}

	public static Measure getMeasure() {
		return measure;
	}

	public double getStatistic() {
		return statistic;
	}

	public Cluster[] getClusters() {
		return clusters;
	}

	public ClusterisationStatistics getClusterisationStatistics() {
		return clusterisationStatistics;
	}


}
