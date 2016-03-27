package dendrogram;

import java.util.HashMap;

import data.DataPoint;
import data.DataStatistics;
import data.Parameters;

public class ClusterisationStatistics {
	private int numberOfClasses;
	private int[] eachClassNumberOfInstancesWithInheritance;
	private double[] eachClassMaxFMeasure;
	private double flatClusterisationFMeasure;
	private HashMap<String, Integer> classNameAndItsId;
	
	//miary dla dendrogramu DO TEGO POZIOMU WLACZNIE (do poziomu ktory posiada TEN obiekt clusterisation statistics)
	private double incrementalHierarchicalFMeasure;
	private double adaptedFmeasureWithInheritance;
	private double adaptedFmeasureWithOUTInheritance;
	private double standardFmeasure;
	private double partialOrderFscore;
	
	public ClusterisationStatistics(DataStatistics dataStatistics)//TODO: prawdopodobnie  to ze obracamy sobie tym obiektem w programie, powoduje zjadanie pamieci! 
	{
		this.flatClusterisationFMeasure = Double.NaN;
		this.numberOfClasses = dataStatistics.getClassNameAndItsId().size();
		this.eachClassNumberOfInstancesWithInheritance = dataStatistics.getEachClassNumberOfInstanceWithInheritance();
		this.classNameAndItsId = dataStatistics.getClassNameAndItsId();
	}
	
	//this F-measure assume inheritance property (children belongs to its father)
	public void computeFlatClusterisationFMeasure(DendrogramLevel clusterisation) {
		if(Parameters.isClassAttribute())
		{
			flatClusterisationFMeasure = 0.0;
			int numberOfClusters = clusterisation.getClusters().length;
			int[] eachClusterNumberOfInstances = new int[numberOfClusters];
			int[][] eachClassToClusterNumberOfInstances = new int[numberOfClasses][numberOfClusters];//liczba zgodnych obiektow w kazdym klastrze dla kastej klasy
			
			
			for(int cluster = 0; cluster < numberOfClusters; cluster++)
			{
				eachClusterNumberOfInstances[cluster] = clusterisation.getClusters()[cluster].getNumberOfPoints();
				for(DataPoint p: clusterisation.getClusters()[cluster].getPoints())
				{
					if(!p.getClassAttribute().contains("Noise"))
					{
						String classAttrib = p.getClassAttribute();
						eachClassToClusterNumberOfInstances[classNameAndItsId.get(p.getClassAttribute())][cluster]++;
						for(String potentialParentClass: classNameAndItsId.keySet())
						{
							if(potentialParentClass.length() < classAttrib.length() 
									&& classAttrib.startsWith(potentialParentClass + basic_hierarchy.common.Constants.HIERARCHY_BRANCH_SEPARATOR))
							{
								eachClassToClusterNumberOfInstances[classNameAndItsId.get(potentialParentClass)][cluster]++;
							}
						}
					}
				}
			}
			
			//precision and recall nad F-measure
			double[][] classToClusterPrecision = new double[numberOfClasses][numberOfClusters];//[class][cluster]
			double[][] classToClusterRecall = new double[numberOfClasses][numberOfClusters];
			double[][] classToClusterFMeasure = new double[numberOfClasses][numberOfClusters];
			eachClassMaxFMeasure = new double[numberOfClasses];
			
			int normalizingFactor = 0;
			for(int num: eachClassNumberOfInstancesWithInheritance)
			{
				normalizingFactor += num;
			}
			
			for(int classNum = 0; classNum < numberOfClasses; classNum++)
			{
				for(int clusterNum = 0; clusterNum < numberOfClusters; clusterNum++)
				{
					if(!clusterisation.getClusters()[clusterNum].isStaticCenter()) //If true that means that this cluster was already computed in
					{	//the upper level with proper inheritance
						classToClusterPrecision[classNum][clusterNum] = eachClassToClusterNumberOfInstances[classNum][clusterNum]/(double)eachClusterNumberOfInstances[clusterNum];
						classToClusterRecall[classNum][clusterNum] = eachClassToClusterNumberOfInstances[classNum][clusterNum]/(double)eachClassNumberOfInstancesWithInheritance[classNum];
						
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
				}
				//flat f-measure for this level, wartosc ta jest w pewien sposob przeklamana, bo bierze pod uwage liczbe wszystkich instancji a w danej klasteryzajci mozeby byc obecna tylko czesc
				flatClusterisationFMeasure += (eachClassNumberOfInstancesWithInheritance[classNum]/(double)normalizingFactor)*eachClassMaxFMeasure[classNum];
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

	public double getAdaptedFmeasureWithInheritance() {
		return adaptedFmeasureWithInheritance;
	}

	public void setAdaptedFmeasureWithInheritance(double adaptedFmeasureWithInheritance) {
		this.adaptedFmeasureWithInheritance = adaptedFmeasureWithInheritance;
	}

	public double getAdaptedFmeasureWithOUTInheritance() {
		return adaptedFmeasureWithOUTInheritance;
	}

	public void setAdaptedFmeasureWithOUTInheritance(double adaptedFmeasureWithOUTInheritance) {
		this.adaptedFmeasureWithOUTInheritance = adaptedFmeasureWithOUTInheritance;
	}

	public double getStandardFmeasure() {
		return standardFmeasure;
	}

	public void setStandardFmeasure(double standardFmeasure) {
		this.standardFmeasure = standardFmeasure;
	}

	public double getPartialOrderFscore() {
		return partialOrderFscore;
	}

	public void setPartialOrderFscore(double partialOrderFscore) {
		this.partialOrderFscore = partialOrderFscore;
	}
}
