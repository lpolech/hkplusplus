package algorithms;

import data.Cluster;
import data.ClustersAndTheirStatistics;

public interface Algorithm {
	public ClustersAndTheirStatistics run(int k, Cluster parent, int numberOfAlreadyCreatedNodes);
}
