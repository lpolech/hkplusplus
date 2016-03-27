package data;

public class ClustPtsPostAndSumOfPost 
{
	private Cluster[] clusterisation;
	private double[][] pointsToMixturePosteriories;
	private double[] clustersSumOfPosteriories;
	
	public ClustPtsPostAndSumOfPost(Cluster[] clusterisation,
			double[][] pointsToMixturePosteriories,
			double[] clustersSumOfPosteriories)
	{
		this.clusterisation = clusterisation;
		this.pointsToMixturePosteriories = pointsToMixturePosteriories;
		this.clustersSumOfPosteriories = clustersSumOfPosteriories;
	}

	public Cluster[] getClusterisation() {
		return clusterisation;
	}

	public double[][] getPointsToMixturePosteriories() {
		return pointsToMixturePosteriories;
	}

	public double[] getClustersSumOfPosteriories() {
		return clustersSumOfPosteriories;
	}

}
