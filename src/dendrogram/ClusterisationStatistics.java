package dendrogram;

import java.util.HashMap;

import data.DataPoint;
import data.DataStatistics;
import data.Parameters;

public class ClusterisationStatistics {
	private int numberOfClasses;
	private int numberOfClusters;
	private int[] eachClassNumberOfInstances;
	private int[] eachClusterNumberOfInstances;
	private int[][] eachClassToClusterNumberOfInstances; //liczba zgodnych obiektow w kazdym klastrze dla kastej klasy
	private double[][] classToClusterPrecision;//[class][cluster]
	private double[][] classToClusterRecall;
	private double[][] classToClusterFMeasure;
	private double[] eachClassMaxFMeasure;
	private double flatClusterisationFMeasure;
	private double incrementalHierarchicalFMeasure;//f measure dla dendrogramu DO TEGO POZIOMU WLACZNIE (do poziomu ktory posiada TEN obiekt clusterisation statistics)
	private HashMap<String, Integer> classNameAndItsId;
	private int datasetLength;
	private int numberOfNoisePoints;
	
	public ClusterisationStatistics(DataStatistics dataStatistics)
	{
		this.flatClusterisationFMeasure = Double.NaN;
		this.numberOfClasses = dataStatistics.getClassNameAndItsId().size();
		this.eachClassNumberOfInstances = dataStatistics.getEachClassNumberOfInstance();
		this.classNameAndItsId = dataStatistics.getClassNameAndItsId();
		this.datasetLength = dataStatistics.getDatasetLength();
		this.numberOfNoisePoints = dataStatistics.getNumberOfNoisePoints();
	}
	
	public void compute(DendrogramLevel clusterisation) {
		if(Parameters.isClassAttribute())
		{
			flatClusterisationFMeasure = 0.0;
			numberOfClusters = clusterisation.getClusters().length;
			eachClusterNumberOfInstances = new int[numberOfClusters];
			eachClassToClusterNumberOfInstances = new int[numberOfClasses][numberOfClusters];
			
			
			for(int cluster = 0; cluster < numberOfClusters; cluster++)
			{
				eachClusterNumberOfInstances[cluster] = clusterisation.getClusters()[cluster].getNumberOfPoints();
				for(DataPoint p: clusterisation.getClusters()[cluster].getPoints())
				{
					if(!p.getClassAttribute().contains("Noise"))
					{
						eachClassToClusterNumberOfInstances[classNameAndItsId.get(p.getClassAttribute())][cluster]++;
					}
				}
			}
			
			//precision and recall nad F-measure
			classToClusterPrecision = new double[numberOfClasses][numberOfClusters];
			classToClusterRecall = new double[numberOfClasses][numberOfClusters];
			classToClusterFMeasure = new double[numberOfClasses][numberOfClusters];
			eachClassMaxFMeasure = new double[numberOfClasses];
			
			for(int classNum = 0; classNum < numberOfClasses; classNum++)
			{
				for(int clusterNum = 0; clusterNum < numberOfClusters; clusterNum++)
				{
					classToClusterPrecision[classNum][clusterNum] = eachClassToClusterNumberOfInstances[classNum][clusterNum]/(double)eachClusterNumberOfInstances[clusterNum];
					classToClusterRecall[classNum][clusterNum] = eachClassToClusterNumberOfInstances[classNum][clusterNum]/(double)eachClassNumberOfInstances[classNum];
					
					classToClusterFMeasure[classNum][clusterNum] = (2.0*classToClusterRecall[classNum][clusterNum]*classToClusterPrecision[classNum][clusterNum])
							/(classToClusterRecall[classNum][clusterNum] + classToClusterPrecision[classNum][clusterNum]);
					if(Double.isNaN(classToClusterFMeasure[classNum][clusterNum]))
					{
						classToClusterFMeasure[classNum][clusterNum] = 0.0;
					}
					if(classToClusterFMeasure[classNum][clusterNum] > eachClassMaxFMeasure[classNum])
					{
						eachClassMaxFMeasure[classNum] = classToClusterFMeasure[classNum][clusterNum];
						// TODO: jezeli chcemy zachowac informacje, ktory klaster maxymalizuje FMeasure dla klasy, to trze TO ZROBIC TUTAJ!
					}
				}
				//flat f-measure for this level, wartosc ta jest w pewien sposob przeklamana, bo bierze pod uwage liczbe wszystkich instancji a w danej klasteryzajci mozeby byc obecna tylko czesc
				flatClusterisationFMeasure += (eachClassNumberOfInstances[classNum]/(double)(datasetLength-numberOfNoisePoints))*eachClassMaxFMeasure[classNum];
			}
		}
		
	}

	public double[] getEachClassMaxFMeasure() {
		return eachClassMaxFMeasure;
	}

	public double getFlatClusterisationFMeasure() {
		return flatClusterisationFMeasure;
	}

	public double getIncrementalHierarchicalFMeasure() {
		return incrementalHierarchicalFMeasure;
	}

	public void setIncrementalHierarchicalFMeasure(
			double incrementalHierarchicalFMeasure) {
		this.incrementalHierarchicalFMeasure = incrementalHierarchicalFMeasure;
	}
}
