package data;

import java.io.IOException;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Scanner;

import utils.Constans;

public class DataReader {
	
	public static Data read(Path inputFilePath, Path dimensionsNamesFilePath)
	{		
		NumberOfPointsAndDataDimension pointsAndDimension = getNumberOfPointsAndItsDimension(inputFilePath);
		DataPoint[] points = readAndParseDataPoints(inputFilePath, pointsAndDimension);
		HashMap<Integer, String> dimensionNumberAndItsName = readAndParseDimensionsNames(dimensionsNamesFilePath);
		
		HashMap<String, Integer> classNameAndItsId = getClassNameAndItsId(points);
		DataStatistics dataStats = calculateDataStatistics(points, pointsAndDimension.getDataDimension(), classNameAndItsId);
		
		if(Parameters.isVerbose())
		{
			System.out.print("Loaded data from: " + inputFilePath.toString()
					+ "\nPoints: " + pointsAndDimension.getNumberOfPoints() 
					+ "\nDim: " + pointsAndDimension.getDataDimension() 
					+ "\n");
		}
		
		return new Data(points, pointsAndDimension.getNumberOfPoints(), pointsAndDimension.getDataDimension(), dataStats,
				dimensionNumberAndItsName);
	}
	
	private static HashMap<String, Integer> getClassNameAndItsId(DataPoint[] points) {
		HashMap<String, Integer> returnMap = new HashMap<String, Integer>();
		
		if(Parameters.isClassAttribute())
		{
			int classNameId = 0;
			for(DataPoint p: points)
			{
				if(!p.getClassAttribute().contains("Noise") && !returnMap.containsKey(p.getClassAttribute()))
				{
					returnMap.put(p.getClassAttribute(), classNameId++);
				}
			}
		}
		
		return returnMap;
	}

	
	private static NumberOfPointsAndDataDimension getNumberOfPointsAndItsDimension(Path inputFilePath) {
		NumberOfPointsAndDataDimension pointsAndDimension = new NumberOfPointsAndDataDimension(-1, -1);
		try(Scanner scanner = new Scanner(inputFilePath))
		{
			if(scanner.hasNextLine())
			{
				pointsAndDimension = parseHeader(scanner.nextLine());
			}
			else
			{
				System.err.println("Input file do not have header with numberOfPoints"	+ Constans.delimiter 
						+ "numberOfDimensions!");
				System.exit(1);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return pointsAndDimension;
	}

	private static NumberOfPointsAndDataDimension parseHeader(String fileHeader) {
		String[] splittedFileHeader = fileHeader.split(Constans.delimiter);
		int numberOfPoints = 0;
		int numberOfDimensions = 0;
		if(splittedFileHeader.length == 2)
		{
			numberOfPoints = Integer.valueOf(splittedFileHeader[0]);
			numberOfDimensions = Integer.valueOf(splittedFileHeader[1]);
		}
		else
		{
			System.err.println("Input file header " + fileHeader + " don't fit pattern numberOfPoints" 
					+ Constans.delimiter + "numberOfDimensions!");
			System.exit(1);
		}
		return new NumberOfPointsAndDataDimension(numberOfPoints, numberOfDimensions);
	}
	
	private static DataPoint[] readAndParseDataPoints(Path inputFilePath,
			NumberOfPointsAndDataDimension pointsAndDimension) {
		int numberOfPoints = pointsAndDimension.getNumberOfPoints();
		int numberOfDimension = pointsAndDimension.getDataDimension();
		DataPoint[] data = new DataPoint[numberOfPoints];
		int readPointsCounter = 0;
		
		try(Scanner scanner = new Scanner(inputFilePath))
		{
			skipAlreadyParsedHeader(scanner);
			while(scanner.hasNextLine())
			{
				String rawDataPoint = scanner.nextLine();
				String[] dataPointValues = rawDataPoint.split(Constans.delimiter);
				if(dataPointValues.length != (numberOfDimension + (Parameters.isClassAttribute()? 1 : 0)))
				{
					System.err.println("Read line " + rawDataPoint + " from " + inputFilePath 
							+ " don't have proper points coordinates and/or class attribute!");
					System.exit(1);
				}
				else
				{
					parseCoordinates(numberOfDimension, data,
							readPointsCounter, dataPointValues);
					readPointsCounter++;
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return data;
	}
	
	private static void parseCoordinates(int numberOfDimension, DataPoint[] data,
			int readPointsCounter, String[] dataPointValues) {
		double[] coordinates = new double[numberOfDimension];
		
		String classAttribute = "void";
		if(Parameters.isClassAttribute())
		{
			classAttribute = dataPointValues[0];
		}
		
		for(int i = 0; i < numberOfDimension; i++)
		{
			try
			{
				coordinates[i] = Double.valueOf(dataPointValues[i + (Parameters.isClassAttribute()? 1 : 0)]);
			}
			catch(NumberFormatException e)
			{
				System.err.println("DataReader.parseCoordinates(...) Error while parsing read values from file. Cannot parse: " + dataPointValues[i]);
				System.exit(1);
			}
		}
		data[readPointsCounter] = new DataPoint(coordinates, coordinates, classAttribute);
	}

	private static HashMap<Integer, String> readAndParseDimensionsNames(
			Path dimensionsNamesFilePath) {
		HashMap<Integer, String> dimensionNumberAndItsName = new HashMap<Integer, String>();
		if(dimensionsNamesFilePath != null)
		{
			try(Scanner scanner = new Scanner(dimensionsNamesFilePath))
			{
				while(scanner.hasNextLine())
				{
					String rawLine = scanner.nextLine();
					String[] splittedLine = rawLine.split(Constans.delimiter);
					if(splittedLine.length != 2)
					{
						System.err.println("Read line " + rawLine + "from " + dimensionNumberAndItsName
								+ " don't have proper format! There should be <dimensionNumber>" 
								+ Constans.delimiter + "<name>" );
						System.exit(1);
					}
					else
					{
						parseDimensionAndItsName(dimensionNumberAndItsName, splittedLine);
					}
				}
			}
			catch (Exception e) {
				e.printStackTrace();
			}
		}
		return dimensionNumberAndItsName;
	}
	
	private static void parseDimensionAndItsName(
			HashMap<Integer, String> dimensionNumberAndItsName,
			String[] splittedLine) 
	{
		Integer dim = Integer.valueOf(splittedLine[0]);
		String name = splittedLine[1];
		dimensionNumberAndItsName.put(dim, name);
	}

	private static void skipAlreadyParsedHeader(Scanner scanner) {
		scanner.nextLine();
	}
	
	private static DataStatistics calculateDataStatistics(
			DataPoint[] points, int numberOfDimensions, HashMap<String, Integer> classNameAndItsId) {
		double[] minValues = new double[numberOfDimensions];
		double[] maxValues = new double[numberOfDimensions];
		double[] eachDimNormalisationInterval = new double[numberOfDimensions];
		
		for(int i = 0; i < numberOfDimensions; i++)
		{
			minValues[i] = Double.MAX_VALUE;
			maxValues[i] = Double.MIN_VALUE;
		}
		
		for(DataPoint p: points)
		{
			for(int i = 0; i < numberOfDimensions; i++)
			{
				if(p.getCoordinate(i) < minValues[i])
				{
					minValues[i] = p.getCoordinate(i);
				}
				if(p.getCoordinate(i) > maxValues[i])
				{
					maxValues[i] = p.getCoordinate(i);
				}
			}
		}
		
		int[] eachClassNumberOfInstanceWithInheritance = null;
		int numberOfNoisePoints = 0;
		if(Parameters.isClassAttribute())
		{
			eachClassNumberOfInstanceWithInheritance = new int[classNameAndItsId.size()];
			for(DataPoint p: points)
			{
				if(p.getClassAttribute().contains("Noise"))
				{
					numberOfNoisePoints++;
				}
				else
				{
					String classAttrib = p.getClassAttribute();
					eachClassNumberOfInstanceWithInheritance[classNameAndItsId.get(classAttrib)]++;
					for(String potentialParentClass: classNameAndItsId.keySet())
					{
						if(potentialParentClass.length() < classAttrib.length() 
							&& classAttrib.startsWith(potentialParentClass + basic_hierarchy.common.Constants.HIERARCHY_BRANCH_SEPARATOR))
						{
							eachClassNumberOfInstanceWithInheritance[classNameAndItsId.get(potentialParentClass)]++;
						}
					}
				}
			}
		}
		
		for(int i = 0; i < numberOfDimensions; i++)
		{
			eachDimNormalisationInterval[i] = maxValues[i] - minValues[i];
			if(minValues[i] == maxValues[i])
			{
				System.err.println("DataReader.calculateDataStatistics(..) Warning, found min and max values are equal!"
						+ " This means, that on dimension number " + i + " there is no diferent values.");
			}
		}
		
		return new DataStatistics(minValues, maxValues, eachDimNormalisationInterval, points.length, classNameAndItsId, 
				eachClassNumberOfInstanceWithInheritance, numberOfNoisePoints);
	}
}
