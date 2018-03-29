package data;

import utils.Constans;
import Jama.Matrix;

public class DataPoint {
	private static int numberOfDimensions;
	private double[] coordinates;
	private double[] sourceCoordinates;
	private String classAttribute;
	private String instanceName;
	
	
	public DataPoint(double[] coordinates, double[] sourceCoordinates, String instanceName, String classAttribute)
	{
		this.setCoordinates(coordinates);
		this.setClassAttribute(classAttribute);
		this.setInstanceName(instanceName);
		this.setSourceCoordinates(sourceCoordinates);
	}
	
	public double getCoordinate(int number)
	{
		return coordinates[number];
	}
	
	public void setCoordinates(double[] coordinates)
	{
		
		this.coordinates = coordinates.clone();
	}
	
	public double[] getSourceCoordinates()
	{
		return sourceCoordinates;
	}
	
	public double getSourceCoordinate(int number)
	{
		return sourceCoordinates[number];
	}
	
	//przesuniecie warunku z konstruktora do setera
	public void setSourceCoordinates(double[] sourceCoordinates)
	{
		if(sourceCoordinates != null)
		{
			this.sourceCoordinates = sourceCoordinates.clone();
		}
		else
		{
			this.sourceCoordinates = null;
		}
	}
	
	public void setCoordinate(int number, double value)
	{
		coordinates[number] = value;		
	}
	
	public double[] getCoordinates()
	{
		return coordinates;
	}
	
	public static int getNumberOfDimensions()
	{
		return numberOfDimensions;
	}
	
	public static void setNumberOfDimensions(int num)
	{
		DataPoint.numberOfDimensions = num;
	}
	
	public Matrix getMatrix()
	{
		Matrix convertedValue = new Matrix(numberOfDimensions, 1);
		for(int i = 0; i < this.coordinates.length; i++)
		{
			convertedValue.set(i, 0, coordinates[i]);
		}
		return convertedValue;
	}
	
	@Override
	public String toString()
	{
		String returnValue = "";
		
		if(Parameters.isClassAttribute())
		{
			returnValue += "Atr: " + Constans.delimiter + classAttribute 
					+ Constans.delimiter + Constans.delimiter;
		}
		
		if(Parameters.isInstanceName())
		{
			returnValue += "InstName: " + Constans.delimiter + instanceName 
					+ Constans.delimiter + Constans.delimiter;
		}
		
		for(int i = 0; i < numberOfDimensions; i++)
		{
			returnValue += coordinates[i];
			returnValue += Constans.delimiter;
		}
		
		if(sourceCoordinates != null)
		{
			returnValue += Constans.delimiter + "Source:" + Constans.delimiter;
			for(int i = 0; i < numberOfDimensions; i++)
			{
				returnValue += sourceCoordinates[i];
				returnValue += Constans.delimiter;
			}
		}
		return returnValue;
	}
	
	public boolean arePointsPointingTheSame(DataPoint other)
	{
		for(int i = 0; i < this.coordinates.length; i++)
		{
			if(this.coordinates[i] != other.getCoordinate(i))
			{
				return false;
			}
		}
		return true;
	}

	public String getClassAttribute() {
		return classAttribute;
	}

	public void setClassAttribute(String classAttribute) {
		this.classAttribute = classAttribute;
	}

	public String getInstanceName() {
		return instanceName;
	}

	public void setInstanceName(String instanceName) {
		this.instanceName = instanceName;
	}
}
