package data;

import java.nio.file.Path;

import algorithms.AlgEnum;

public class Parameters {
	private static Path inputDataFilePath;
	private static Path outputFolder;
	private static Path dimenionsNamesFilePath;
	private static int numberOfClusterisationAlgIterations;
	private static int numberOfClusterisationAlgRepeats;
	private static int dendrogramMaxHeight;
	private static boolean verbose;
	private static int k;
	private static AlgEnum method;
	private static double epsilon;
	private static double littleValue;
	private static boolean classAttribute;
	private static int generatedImgsSize;
	private static boolean disableStaticCenter;
	private static int maxNumberOfNodes;
	private static double responsibilityScallingFactor;
	private static boolean staticCenterResponsibilityScalling = false;
	private static double covarianceScallingFactor;
	private static boolean staticCenterAdaptiveCovarianve = false;
	private static boolean staticCenterCovarianceScalling = false;
	private static boolean diagonalCovarianceMatrix = false;
	private static boolean clusterReestimationBasedOnItsData = false;
	
	private Parameters()
	{
	}

	public static Path getInputDataFilePath() {
		return inputDataFilePath;
	}

	public static void setInputDataFilePath(Path path) {
		Parameters.inputDataFilePath = path;
	}

	public static Path getOutputFolder() {
		return outputFolder;
	}

	public static void setOutputFolder(Path path) {
		Parameters.outputFolder = path;
	}

	public static Path getDimensionsNamesFilePath() {
		return dimenionsNamesFilePath;
	}

	public static void setDimensionsNamesFilePath(Path dimenionsNamesFilePath) {
		Parameters.dimenionsNamesFilePath = dimenionsNamesFilePath;
	}

	public static int getNumberOfClusterisationAlgIterations() {
		return numberOfClusterisationAlgIterations;
	}

	public static void setNumberOfClusterisationAlgIterations(
			int numberOfClusterisationAlgIterations) {
		Parameters.numberOfClusterisationAlgIterations = numberOfClusterisationAlgIterations;
	}

	public static int getDendrogramMaxHeight() {
		return dendrogramMaxHeight;
	}

	public static void setDendrogramMaxHeight(int dendrogramMaxHeight) {
		Parameters.dendrogramMaxHeight = dendrogramMaxHeight;
	}

	public static boolean isVerbose() {
		return verbose;
	}

	public static void setVerbose(boolean verbose) {
		Parameters.verbose = verbose;
	}

	public static int getK() {
		return k;
	}

	public static void setK(int k) {
		Parameters.k = k;
	}

	public static int getNumberOfClusterisationAlgRepeats() {
		return numberOfClusterisationAlgRepeats;
	}

	public static void setNumberOfClusterisationAlgRepeats(
			int numberOfClusterisationAlgRepeats) {
		Parameters.numberOfClusterisationAlgRepeats = numberOfClusterisationAlgRepeats;
	}

	public static void setMethod(AlgEnum method) {
		Parameters.method = method;		
	}

	public static AlgEnum getMethod() {
		return method;
	}

	public static double getEpsilon() {
		return epsilon;
	}

	public static void setEpsilon(double epsilon) {
		Parameters.epsilon = epsilon;
	}

	public static double getLittleValue() {
		return littleValue;
	}

	public static void setLittleValue(double littleValue) {
		Parameters.littleValue = littleValue;
	}

	public static boolean isClassAttribute() {
		return classAttribute;
	}

	public static void setClassAttribute(boolean isClassAttribute) {
		Parameters.classAttribute = isClassAttribute;
	}

	public static int getGenerateImagesSize() {
		return Parameters.generatedImgsSize;
	}

	public static void setGenerateImagesSize(int generatedImgsSize) {
		Parameters.generatedImgsSize = generatedImgsSize;
	}

	public static boolean isDisableStaticCenter() {
		return disableStaticCenter;
	}

	public static void setDisableStaticCenter(boolean disableStaticCenter) {
		Parameters.disableStaticCenter = disableStaticCenter;
	}

	public static int getMaxNumberOfNodes() {
		return maxNumberOfNodes;
	}

	public static void setMaxNumberOfNodes(int maxNumberOfNodes) {
		Parameters.maxNumberOfNodes = maxNumberOfNodes;
	}

	public static double getResponsibilityScallingFactor() {
		return responsibilityScallingFactor;
	}
	
	public static void setResponsibilityScallingFactor(double responsibilityScallingFactor) {
		Parameters.responsibilityScallingFactor = responsibilityScallingFactor;
	}

	public static boolean isStaticCenterResponsibilityScalling() {
		return staticCenterResponsibilityScalling;
	}

	public static void setStaticCenterResponsibilityScalling(boolean staticCenterResponsibilityScalling) {
		Parameters.staticCenterResponsibilityScalling = staticCenterResponsibilityScalling;
	}

	public static boolean isStaticCenterCovarianceScalling() {
		return staticCenterCovarianceScalling;
	}

	public static void setStaticCenterCovarianceScalling(boolean staticCenterScalledCovariance) {
		Parameters.staticCenterCovarianceScalling = staticCenterScalledCovariance;
	}

	public static boolean isStaticCenterAdaptiveCovarianve() {
		return staticCenterAdaptiveCovarianve;
	}

	public static void setStaticCenterAdaptiveCovarianve(boolean staticCenterAdaptiveCovarianve) {
		Parameters.staticCenterAdaptiveCovarianve = staticCenterAdaptiveCovarianve;
	}

	public static double getCovarianceScallingFactor() {
		return covarianceScallingFactor;
	}

	public static void setCovarianceScallingFactor(double covarianceScallingFactor) {
		Parameters.covarianceScallingFactor = covarianceScallingFactor;
	}

	public static boolean isDiagonalCovarianceMatrix() {
		return diagonalCovarianceMatrix;
	}

	public static void setDiagonalCovarianceMatrix(boolean diagonalCovarianceMatrix) {
		Parameters.diagonalCovarianceMatrix = diagonalCovarianceMatrix;
	}

	public static boolean isClusterReestimationBasedOnItsData() {
		return Parameters.clusterReestimationBasedOnItsData;
	}
	
	public static void setClusterReestimationBasedOnItsData(boolean clusterReestimationBasedOnItsData) {
		Parameters.clusterReestimationBasedOnItsData = clusterReestimationBasedOnItsData;
	}

}
