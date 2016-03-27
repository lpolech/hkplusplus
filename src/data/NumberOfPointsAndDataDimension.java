package data;

public class NumberOfPointsAndDataDimension {
	private int numberOfPoints;
	private int dataDimension;
	
	public NumberOfPointsAndDataDimension(int numberOfPoints, int dataDimension)
	{
		setNumberOfPoints(numberOfPoints);
		setDataDimension(dataDimension);
	}
	
	public int getNumberOfPoints() {
		return numberOfPoints;
	}
	public void setNumberOfPoints(int numberOfPoints) {
		this.numberOfPoints = numberOfPoints;
	}
	public int getDataDimension() {
		return dataDimension;
	}
	public void setDataDimension(int dataDimension) {
		this.dataDimension = dataDimension;
	}
}
