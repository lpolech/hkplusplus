package data;

import java.util.HashMap;

public class DataStatistics {
	private double[] minValues;
	private double[] maxValues;
	private double[] eachDimNormalisationInterval;
	private int[] eachClassNumberOfInstanceWithInheritance;
	private HashMap<String,Integer> classNameAndItsId;
	private int datasetLength;
	private int numberOfNoisePoints;
	
	public DataStatistics(double[] minValues, double[] maxValues, double[] eachDimNormalisationInterval, int datasetLength, 
			HashMap<String,Integer> classNameAndItsId, int[] eachClassNumberOfInstanceWithInheritance, int numberOfNoisePoints)
	{
		this.minValues = minValues;
		this.maxValues = maxValues;
		this.eachDimNormalisationInterval = eachDimNormalisationInterval;
		this.eachClassNumberOfInstanceWithInheritance = eachClassNumberOfInstanceWithInheritance;
		this.classNameAndItsId = classNameAndItsId;
		this.datasetLength = datasetLength;
		this.numberOfNoisePoints = numberOfNoisePoints;
	}
	
	public double[] getMinValues() {
		return minValues;
	}
	public void setMinValues(double[] minValues) {
		this.minValues = minValues;
	}
	public double[] getMaxValues() {
		return maxValues;
	}
	public void setMaxValues(double[] maxValues) {
		this.maxValues = maxValues;
	}

	public int[] getEachClassNumberOfInstanceWithInheritance() {
		return eachClassNumberOfInstanceWithInheritance;
	}

	public void setEachClassNumberOfInstanceWithInheritance(int[] eachClassNumberOfInstance) {
		this.eachClassNumberOfInstanceWithInheritance = eachClassNumberOfInstance;
	}

	public HashMap<String, Integer> getClassNameAndItsId() {
		return classNameAndItsId;
	}

	public void setClassNameAndItsId(HashMap<String, Integer> classNameAndItsId) {
		this.classNameAndItsId = classNameAndItsId;
	}

	public int getDatasetLength() {
		return datasetLength;
	}

	public void setDatasetLength(int datasetLength) {
		this.datasetLength = datasetLength;
	}

	public int getNumberOfNoisePoints() {
		return numberOfNoisePoints;
	}

	public void setNumberOfNoisePoints(int numberOfNoisePoints) {
		this.numberOfNoisePoints = numberOfNoisePoints;
	}

	public double[] getEachDimNormalisationInterval() {
		return eachDimNormalisationInterval;
	}

	public void setEachDimNormalisationInterval(double[] eachDimNormalisationInterval) {
		this.eachDimNormalisationInterval = eachDimNormalisationInterval;
	}
}
