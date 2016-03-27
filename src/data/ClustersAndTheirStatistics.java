package data;

public class ClustersAndTheirStatistics {
	private Cluster[] clusters;
	private double[][] clustersStDev;
	private double statistic;
	
	public ClustersAndTheirStatistics(Cluster[] clusters, double statistic, boolean calculateOtherStatistics)
	{
		this.clusters = clusters;
		this.statistic = statistic;
		
		if(calculateOtherStatistics)
		{
			calculateStandardStatistics();
		}
	}
	
	private void calculateStandardStatistics() {
		calculateStDevForEachCluster();
		
	}

	private void calculateStDevForEachCluster() { // TODO TRZEBA ZROBIC PRAWDZIWE ODCHYLENIE STANDARDOWE
		clustersStDev = new double[clusters.length][DataPoint.getNumberOfDimensions()];
		
		for(int i = 0; i < clusters.length; i++)
		{
			clustersStDev[i] = sumClusterPoints(i);
			clustersStDev[i] = averageTableValues(clustersStDev[i], clusters[i].getPoints().length);
		}
		
	}

	private double[] sumClusterPoints(int clusterIndex) {
		double[] sum = new double[DataPoint.getNumberOfDimensions()];
		for(int j = 0; j < clusters[clusterIndex].getPoints().length; j++)
		{
			for(int k = 0; k < DataPoint.getNumberOfDimensions(); k++)
			{
				sum[k] += clusters[clusterIndex].getPoints()[j].getCoordinate(k);
			}
		}
		return sum;
	}
	
	private double[] averageTableValues(double[] table, int denominator) {
		for(int i = 0; i < table.length; i++)
		{
			table[i] /= (double)denominator;
		}
		return table;
	}

	public Cluster[] getClusters() {
		return clusters;
	}

	public double getClusterisationStatistic() {
		return statistic;
	}
}
