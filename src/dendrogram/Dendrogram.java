package dendrogram;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Stack;

import javax.imageio.ImageIO;

import algorithms.AlgEnum;
import algorithms.Algorithm;
import algorithms.EM;
import algorithms.Kmeans;
import basic_hierarchy.implementation.BasicHierarchy;
import basic_hierarchy.implementation.BasicInstance;
import basic_hierarchy.implementation.BasicNode;
import basic_hierarchy.interfaces.Hierarchy;
import basic_hierarchy.interfaces.Instance;
import basic_hierarchy.interfaces.Node;
import center.method.Centroid;
import data.Cluster;
import data.Data;
import data.DataPoint;
import data.DataStatistics;
import data.Parameters;
import distance.measures.GMMBayesMLE;
import distance.measures.L2Norm;
import distance.measures.LOG_GMMBayesMLE;
import external_measures.AdaptedFmeasure;
import external_measures.statistical_hypothesis.FlatHypotheses;
import external_measures.statistical_hypothesis.PartialOrderHypotheses;
import external_measures.statistical_hypothesis.Fmeasure;
import utils.Constans;
import utils.Utils;

public class Dendrogram {
	private Data points;
	private ArrayList<DendrogramLevel> dendrogram;
	private int k;
	private int levelsLimit;
	private Path levelsResultsStorage;
	private Path summaryStatisticsFile;
	private Path compactSummaryStaticticsFile;
	private int currentNumberOfNodes;
	private AdaptedFmeasure adaptedFmeasureWithInheritance;
	private AdaptedFmeasure adaptedFmeasureWithOUTInheritance;
	private Fmeasure standardFmeasure;
	private Fmeasure partialOrderFscore;
	
	public Dendrogram(Data points, AlgEnum clusterisationMethod, int k, int levelsLimit, 
			Path levelsImgsStorage)
	{
		this.points = points;
		this.k = k;
		this.levelsLimit = levelsLimit;
		this.levelsResultsStorage = levelsImgsStorage;
		this.adaptedFmeasureWithInheritance = new AdaptedFmeasure(true);//boolean instancesInheritance, boolean imitateFlatClustering
		this.adaptedFmeasureWithOUTInheritance = new AdaptedFmeasure(false);//boolean instancesInheritance, boolean imitateFlatClustering
		this.standardFmeasure = new Fmeasure(1.0f, new FlatHypotheses());
		this.partialOrderFscore = new Fmeasure(1.0f, new PartialOrderHypotheses());
		
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
			result.write("Dataset" + Constans.delimiter + "Num. of Clusters" + Constans.delimiter + "Num. of Alg. Iterations (n)" 
					+ Constans.delimiter + "Num. of Alg. Repeats (r)" + Constans.delimiter + "Max Dendrogram Size (s)" + Constans.delimiter 
					+ "Epsilon" + Constans.delimiter + "Little Value" + Constans.delimiter + "Resp. Scal. Fact. (rf)" + Constans.delimiter 
					+ "Covar. Scal. Fact. (cf)" + Constans.delimiter + "Clusterisation Method" + Constans.delimiter + "Output" 
					+ Constans.delimiter + "Dimension File" + Constans.delimiter + "Verbose" + Constans.delimiter + "Use static center" 
					+ Constans.delimiter + "Adapt. Static Cen. Cov. Matrix" + Constans.delimiter + "Scalling Static Cen. Cov. Matrix"
					+ Constans.delimiter + "Scalling Static Cen. Resp. Value" + Constans.delimiter + "Diagonal matrix" + Constans.delimiter
					+ "Reestimate clusters\n");
			
			result.write(Parameters.getInputDataFilePath() + Constans.delimiter + Parameters.getK() + Constans.delimiter + Parameters.getNumberOfClusterisationAlgIterations()
					+ Constans.delimiter + Parameters.getNumberOfClusterisationAlgRepeats()	+ Constans.delimiter + Parameters.getDendrogramMaxHeight() 
					+ Constans.delimiter + Parameters.getEpsilon() + Constans.delimiter + Parameters.getLittleValue() + Constans.delimiter 
					+ Parameters.getResponsibilityScallingFactor() + Constans.delimiter + Parameters.getCovarianceScallingFactor()
					+ Constans.delimiter + Parameters.getMethod() + Constans.delimiter + Parameters.getOutputFolder() + Constans.delimiter 
					+ Parameters.getDimensionsNamesFilePath() + Constans.delimiter + (Parameters.isVerbose()? "true" : "false") 
					+ Constans.delimiter + (Parameters.isDisableStaticCenter()? "false" : "true") + Constans.delimiter 
					+ Parameters.isStaticCenterAdaptiveCovarianve() + Constans.delimiter + Parameters.isStaticCenterCovarianceScalling()
					+ Constans.delimiter + Parameters.isStaticCenterResponsibilityScalling() + Constans.delimiter 
					+ Parameters.isDiagonalCovarianceMatrix() + Constans.delimiter + Parameters.isClusterReestimationBasedOnItsData() + "\n");
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
			result.write("Dataset" + Constans.delimiter + "Number of Levels" + Constans.delimiter + "Statistic Value" + Constans.delimiter + "FMeasure (first attempt)"
					+ Constans.delimiter + "PO Fmeasure" + Constans.delimiter + "Standard FMeasure" + Constans.delimiter + "Adapted WITH inheritance FMeasure"
					+ Constans.delimiter + "Adapted WITHOUT inheritance FMeasure"
					+ Constans.delimiter + "Num. of Max. Clusters" + Constans.delimiter + "Num. of Max. Alg. Iterations (n)" + Constans.delimiter 
					+ "Num. of Max. Alg. Repeats (r)" + Constans.delimiter + "Max Dendrogram Size (s)" + Constans.delimiter + "Max. number of nodes (w)" 
					+ Constans.delimiter + "Epsilon" + Constans.delimiter + "Little Value" + Constans.delimiter + "Resp. Scal. Fact. (rf)"
					+ Constans.delimiter + "Covar. Scal. Fact. (cf)" + Constans.delimiter + "Clusterisation Method" + Constans.delimiter 
					+ "Output" + Constans.delimiter + "Dimension File" + Constans.delimiter + "Verbose" + Constans.delimiter + "Use static center" 
					+ Constans.delimiter + "Adapt. Static Cen. Cov. Matrix" + Constans.delimiter + "Scalling Static Cen. Cov. Matrix"
					+ Constans.delimiter + "Scalling Static Cen. Resp. Value" + Constans.delimiter + "Diagonal matrix" + Constans.delimiter 
					+ "Reestimate clusters" + Constans.delimiter + "Eclapsed Time [min]\n");
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
			break;
		case LOG_GMM:
			EM.setMeasure(new LOG_GMMBayesMLE());
			clusterisationAlgorithm = new EM();
			Cluster.setAlgorithm(clusterisationAlgorithm);
			DendrogramLevel.setDistanceMethod(new EM());
			DendrogramLevel.setMeasure(new LOG_GMMBayesMLE());
			break;
		}
	}

	private void insertRoot() {
        System.out.println("Inserting root...");
        DendrogramLevel root = new DendrogramLevel(0, points.getDataStats());
		root.makeRoot(points);
		dendrogram.add(root);
		computeDendrogramStatistics(root);
		saveClusterisationResults(root, 0, points.getDataStats());
        System.out.println("Root inserted");
    }

	public ArrayList<DendrogramLevel> run()
	{
		Utils.startTimer();
		
		int levelNumber = 1;//0 - root is created in the constructor
		while(!shouldTerminate(levelNumber))
		{
			System.out.println("Working on level " + levelNumber + "... ");
			DendrogramLevel newLevel = getDendrogramBottom().expandTree(k, levelNumber, points.getDataStats(), currentNumberOfNodes);
			dendrogram.add(newLevel);
			computeDendrogramStatistics(newLevel);
			saveClusterisationResults(newLevel, levelNumber, points.getDataStats());
			levelNumber++;
			System.out.println("Done.");
		}
		
		Utils.stopTimer();
		System.out.println("Method time: " + Utils.getTimerReport());
		
		System.out.print("Computing final statistics... ");
		Hierarchy h = computeFinalResultStatistics(getDendrogramBottom());
		saveCompactSummaryStatistics(getDendrogramBottom(), levelNumber, Utils.getTimerInMinutes());
		saveFinalHierarchyOfGroups(Constans.finalHierarchyOfGroupsFileName, this.getDendrogramBottom(), h);
		System.out.println("Done.");
		return dendrogram;
	}
	
	private void saveFinalHierarchyOfGroups(String finalHierarchyOfGroupsFilename,
			DendrogramLevel dendrogramBottom, Hierarchy h) {
		//new File(levelsResultsStorage.toString() + File.separator + levelNumber + "_clusterisation" + ".csv");
		//Path finalHierarchyOfGroupsFile = Utils.createCsvFileIfNotExists(finalHierarchyOfGroupsFilename);
		File finalHierarchyOfGroupsFile = new File(levelsResultsStorage.toString() + File.separator + finalHierarchyOfGroupsFilename + ".csv");
		
		try (FileWriter result = new FileWriter(finalHierarchyOfGroupsFile.toString(), false)) 
		{
			if(h == null)
			{
				h = this.getHierarchyRepresentation();
			}
			
			LinkedList<Instance> instances = (LinkedList<Instance>) h.getRoot().getSubtreeInstances().clone();
			for(DataPoint dp: this.points.getPoints())
			{
				boolean found = false;
				for(int i = 0; i < instances.size() && !found; i++)
				{
					Instance inst = instances.get(i);
					if(Arrays.equals(inst.getData(), dp.getSourceCoordinates()))
					{
						found = true;
						String trueClass = (inst.getTrueClass().equals("void")? "" : Constans.delimiter + inst.getTrueClass());
						String instanceName = (inst.getInstanceName().equals("void")? "" : Constans.delimiter + inst.getInstanceName());
						String resultFileLine = inst.getNodeId() + trueClass + instanceName;
						
						for(double coordinate: inst.getData())
						{
							resultFileLine += Constans.delimiter + coordinate;
						}
						
						resultFileLine += "\n";
						
						result.write(resultFileLine);
						instances.remove(i);
					}
				}
			}
		} 
		catch (IOException e) {
			System.out.println("Dendrogram.saveFinalHierarchyOfGroups" + e);
		}
	}

	private Hierarchy computeFinalResultStatistics(DendrogramLevel dendrogramBottom) {
		double adaptedFmeasureWithInheritanceValue = Double.NaN;
		double adaptedFmeasureWithOUTInheritanceValue = Double.NaN;
		double standardFmeasureValue = Double.NaN;
		double partialOrderFscoreValue = Double.NaN;
		
		Hierarchy h = null;
		
		if(Parameters.isClassAttribute())
		{
			h = getHierarchyRepresentation();
			adaptedFmeasureWithInheritanceValue = adaptedFmeasureWithInheritance.getMeasure(h);
			adaptedFmeasureWithOUTInheritanceValue = adaptedFmeasureWithOUTInheritance.getMeasure(h);
			standardFmeasureValue = standardFmeasure.getMeasure(h);
			partialOrderFscoreValue = partialOrderFscore.getMeasure(h);	
		}
		
		dendrogramBottom.getClusterisationStatistics().setAdaptedFmeasureWithInheritance(adaptedFmeasureWithInheritanceValue);
		dendrogramBottom.getClusterisationStatistics().setAdaptedFmeasureWithOUTInheritance(adaptedFmeasureWithOUTInheritanceValue);
		dendrogramBottom.getClusterisationStatistics().setStandardFmeasure(standardFmeasureValue);
		dendrogramBottom.getClusterisationStatistics().setPartialOrderFscore(partialOrderFscoreValue);
		
		return h;
	}

	private void computeDendrogramStatistics(DendrogramLevel newLevel) {
		double hierarchicalFMeasure = Double.NaN;
		
		if(Parameters.isClassAttribute())//Exteran measures
		{
			hierarchicalFMeasure = calculateHierarchicalFMeasure();
		}
		newLevel.getClusterisationStatistics().setIncrementalHierarchicalFMeasure(hierarchicalFMeasure);
	}

	private void saveClusterisationResults(DendrogramLevel newLevel, int levelNumber, DataStatistics dataStats) {
		saveClusterisation(newLevel, levelNumber, dataStats);
		saveSummaryStatistics(newLevel, levelNumber);
		if(Parameters.getGenerateImagesSize() > 0)
		{
			saveImage(newLevel, levelNumber);
		}
	}

	private void saveClusterisation(DendrogramLevel newLevel, int levelNumber, DataStatistics dataStats) {
		if(levelsResultsStorage != null)
		{
			File clusterisationFile = new File(levelsResultsStorage.toString() + File.separator + levelNumber + "_clusterisation" + ".csv");
			newLevel.write(points, clusterisationFile, dataStats);
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
			ClusterisationStatistics finalStats = getDendrogramBottom().getClusterisationStatistics();
			result.write(Parameters.getInputDataFilePath() + Constans.delimiter + levelNumber + Constans.delimiter + newLevel.getStatistic()
					+ Constans.delimiter + finalStats.getIncrementalHierarchicalFMeasure() 
					+ Constans.delimiter + finalStats.getPartialOrderFscore()
					+ Constans.delimiter + finalStats.getStandardFmeasure()
					+ Constans.delimiter + finalStats.getAdaptedFmeasureWithInheritance()
					+ Constans.delimiter + finalStats.getAdaptedFmeasureWithOUTInheritance()
					+ Constans.delimiter + Parameters.getK() + Constans.delimiter + Parameters.getNumberOfClusterisationAlgIterations()
					+ Constans.delimiter + Parameters.getNumberOfClusterisationAlgRepeats()	+ Constans.delimiter + Parameters.getDendrogramMaxHeight() 
					+ Constans.delimiter + Parameters.getMaxNumberOfNodes()
					+ Constans.delimiter + Parameters.getEpsilon() + Constans.delimiter + Parameters.getLittleValue() + Constans.delimiter 
					+ Parameters.getResponsibilityScallingFactor() + Constans.delimiter + Parameters.getCovarianceScallingFactor()
					+ Constans.delimiter + Parameters.getMethod() + Constans.delimiter + Parameters.getOutputFolder() + Constans.delimiter 
					+ Parameters.getDimensionsNamesFilePath() + Constans.delimiter + (Parameters.isVerbose()? "true" : "false") 
					+ Constans.delimiter + (Parameters.isDisableStaticCenter()? "false" : "true") + Constans.delimiter 
					+ Parameters.isStaticCenterAdaptiveCovarianve() + Constans.delimiter + Parameters.isStaticCenterCovarianceScalling()
					+ Constans.delimiter + Parameters.isStaticCenterResponsibilityScalling() + Constans.delimiter 
					+ Parameters.isDiagonalCovarianceMatrix() + Constans.delimiter + Parameters.isClusterReestimationBasedOnItsData() 
					+ Constans.delimiter + Double.toString(eclapsedTimeInMin) + "\n");
			
		} 
		catch (IOException e) {
			System.out.println("Dendrogram.saveCompactSummaryStatistics" + e);
		}		
	}

	private Double calculateHierarchicalFMeasure() {
		double fMeasure = 0.0;
		int numberOfClasses = points.getDataStats().getClassNameAndItsId().size();
		int[] eachClassNumOfInstancesWithInheritance = points.getDataStats().getEachClassNumberOfInstanceWithInheritance();
		int totalNumberOfPointsWithInheritance = 0;
		double[] eachClassMaxFMeasure = new double[numberOfClasses];

		for(int num: eachClassNumOfInstancesWithInheritance)
		{
			totalNumberOfPointsWithInheritance += num;
		}
		
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
			
			fMeasure += eachClassNumOfInstancesWithInheritance[classNum]*eachClassMaxFMeasure[classNum];
		}
		fMeasure /= totalNumberOfPointsWithInheritance;
		return fMeasure;
	}

	private void saveImage(DendrogramLevel newLevel, int levelNumber) 
	{
		if(levelsResultsStorage != null)
		{
			try {
				File imgFile = new File(levelsResultsStorage.toString() + File.separator + levelNumber + ".png");
				ImageIO.write(newLevel.getImage(), "png", imgFile);
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
	
	public Hierarchy getHierarchyRepresentation()
	{
		BasicNode root = null;//will be created as last node
		ArrayList<BasicNode> tmpGroups = new ArrayList<BasicNode>();
		HashMap<String, Integer> eachClassWithCount = new HashMap<String, Integer>();
		int numberOfInstances = 0;
		LinkedList<DataPoint> alreadyAssignedPoints = new LinkedList<DataPoint>();//points that have been already used on lower level
		//and need to be excluded from the upper levels
		
		HashMap<BasicNode, Integer> nodeWhichIsLookingForParentId = new HashMap<BasicNode, Integer>();
		
		for(int i = dendrogram.size()-1; i >= 0; i--)//from bottom to the top in order to EXCLUDE points from upper levels
		{
			DendrogramLevel currLevel = dendrogram.get(i);
			
			for(Cluster c: currLevel.getClusters())
			{
				if(!c.isStaticCenter() && !clusterAlreadyTransformed(tmpGroups, c))//static centers will be modeled on upper levels
				{//but there is an option that on several dendeogram levels an cluster WONT be static, so we need to check manually if this cluster wasn't considered already
					String nodeId = String.valueOf(c.getClusterId());
										
					LinkedList<Instance> instances = new LinkedList<Instance>();
					for(DataPoint p: c.getPoints())
					{
						if(!alreadyAssignedPoints.contains(p))
						{
							alreadyAssignedPoints.add(p);
							instances.add(new BasicInstance(p.getInstanceName(), nodeId, p.getSourceCoordinates(), p.getClassAttribute()));
							if(Parameters.isClassAttribute())
							{
								String classAttrib = p.getClassAttribute();
								if(eachClassWithCount.containsKey(classAttrib))
								{
									eachClassWithCount.put(classAttrib, eachClassWithCount.get(classAttrib) + 1);
								}
								else
								{
									eachClassWithCount.put(classAttrib, 1);
								}
							}
							numberOfInstances++;
						}
					}
					BasicNode newNode = new BasicNode(nodeId, null, new LinkedList<Node>(), instances, false);
					
					LinkedList<BasicNode> keyesToRemoveFromMap = new LinkedList<BasicNode>();
					for(Entry<BasicNode, Integer> potentialChild: nodeWhichIsLookingForParentId.entrySet())
					{
						if(potentialChild.getValue().equals(c.getClusterId()))
						{
							BasicNode child = potentialChild.getKey();
							newNode.addChild(child);
							child.setParent(newNode);
							keyesToRemoveFromMap.add(child);
						}
					}
					
					for(BasicNode key: keyesToRemoveFromMap)
					{
						nodeWhichIsLookingForParentId.remove(key);
					}
					
					tmpGroups.add(newNode);
					nodeWhichIsLookingForParentId.put(newNode, c.getParentId());
				}
			}
			
			if(i == 0)
			{
				root = tmpGroups.get(tmpGroups.size()-1);
			}
		}
		
		standariseInstancesAndGroupNames(root, eachClassWithCount);
		
		LinkedList<Node> groups = new LinkedList<Node>();
		groups.addAll(tmpGroups);
		return new BasicHierarchy(root, groups, eachClassWithCount, numberOfInstances);
	}

	private boolean clusterAlreadyTransformed(ArrayList<BasicNode> transformedGroups, Cluster c) {
		String clusterId = String.valueOf(c.getClusterId());
		for(BasicNode n: transformedGroups)
		{
			if(n.getId().equals(clusterId))
			{
				return true;
			}
		}
		return false;
	}

	private void standariseInstancesAndGroupNames(BasicNode root, HashMap<String, Integer> eachClassWithCount) {
		Stack<Map.Entry<BasicNode, LinkedList<Integer>>> s = new Stack<>();//basic node and its new name coordinates
		LinkedList<Integer> nameSkeletone = new LinkedList<>();
		nameSkeletone.add(0);
		s.push(new AbstractMap.SimpleEntry<>(root, nameSkeletone));
		while(!s.isEmpty())
		{
			Map.Entry<BasicNode, LinkedList<Integer>> elem = s.pop();
			changeName(elem, eachClassWithCount);
			
			LinkedList<Node> children = elem.getKey().getChildren(); 
			for(int i = 0; i < children.size(); i++)
			{
				@SuppressWarnings("unchecked")
				LinkedList<Integer> childNameSkeletone = (LinkedList<Integer>) elem.getValue().clone();
				childNameSkeletone.add(i);
				s.push(new AbstractMap.SimpleEntry<BasicNode, LinkedList<Integer>>((BasicNode)children.get(i), childNameSkeletone));
			}
		}
		
	}
	
	private void changeName(Entry<BasicNode, LinkedList<Integer>> element, HashMap<String,Integer> eachClassWithCount) {
		LinkedList<Integer> nameCoordinates = element.getValue();
		String newName = Constans.groupHierarchyPrefix;
		for(int i = 0; i < nameCoordinates.size(); i++)
		{
			newName += Constans.groupsHierarchySeparator + nameCoordinates.get(i);
		}
		
		BasicNode node = element.getKey();
		if(eachClassWithCount.containsKey(node.getId()))
		{
			int count = eachClassWithCount.remove(node.getId());
			eachClassWithCount.put(newName, count);
		}
		node.setId(newName);
		for(Instance i: node.getNodeInstances())
		{
			i.setNodeId(newName);
		}
		
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

	public Data getPoints() {
		return points;
	}

	public ArrayList<DendrogramLevel> getDendrogram() {
		return dendrogram;
	}
}
