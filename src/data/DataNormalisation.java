package data;

public class DataNormalisation {
	public static DataPoint[] normalise(DataPoint[] inputData)
	{
		examineData(inputData);
		
		DataPoint[] normalisedData = null;
		double[][] minAndMaxValuesOfEachDim = determineMinAndMaxValuesForEachDim(inputData);
		normalisedData = transformData(inputData, minAndMaxValuesOfEachDim);
		
		return normalisedData;
	}

	private static DataPoint[] transformData(DataPoint[] inputData,
			double[][] minAndMaxValuesOfEachDim) {
		DataPoint[] transformedData = new DataPoint[inputData.length];
		
		for(int i = 0; i < transformedData.length; i++)
		{
			double[] normalisedCoordinates = new double[DataPoint.getNumberOfDimensions()];
			for(int j = 0; j < DataPoint.getNumberOfDimensions(); j++)
			{
				if(minAndMaxValuesOfEachDim[1][j] != minAndMaxValuesOfEachDim[0][j])
				{
					normalisedCoordinates[j] = (inputData[i].getCoordinate(j) - minAndMaxValuesOfEachDim[0][j])
							/ (minAndMaxValuesOfEachDim[1][j] - minAndMaxValuesOfEachDim[0][j]);
				}
				else if(minAndMaxValuesOfEachDim[1][j] == minAndMaxValuesOfEachDim[0][j])
				{
					normalisedCoordinates[j] = 0;
				}
				else
				{
					System.err.println("Something went wrong with minAndMaxValuesOfEachDim[1][j] and/or minAndMaxValuesOfEachDim[0][j]"
							+ " Values are: " + minAndMaxValuesOfEachDim[1][j] + " and "+ minAndMaxValuesOfEachDim[0][j]);
					System.exit(1);
				}
			}
			transformedData[i] = new DataPoint(normalisedCoordinates, inputData[i].getClassAttribute());
		}
		return transformedData;
	}

	private static double[][] determineMinAndMaxValuesForEachDim(
			DataPoint[] inputData) {
		double[][] minAndMaxValues = new double[2][DataPoint.getNumberOfDimensions()];
		
		for(int i = 0; i < DataPoint.getNumberOfDimensions(); i++)
		{
			double minValue = Double.MAX_VALUE;
			double maxValue = (-1)*Double.MAX_VALUE;
			for(int j = 0; j < inputData.length; j++)
			{
				double value = inputData[j].getCoordinate(i);
				if(value < minValue)
				{
					minValue = value;
				}
				
				if(value > maxValue)
				{
					maxValue = value;
				}
			}
			minAndMaxValues[0][i] = minValue;
			minAndMaxValues[1][i] = maxValue;
			
			if(minValue == maxValue)
			{
				System.err.println("DataNormalisation.determineMinAndMaxValuesForEachDim(..) Warning, found min and max values are equal!"
						+ " This means, that on dimension number " + i + " there is no diferent values.");
			}
		}
		
		return minAndMaxValues;
	}

	private static void examineData(DataPoint[] inputData) {
		if(inputData == null)
		{
			System.err.println("DataNormalisation.normalise(..) Can't normalise data, inputData is null.");
			System.exit(1);
		}
		else if(inputData.length == 0)
		{
			System.err.println("DataNormalisation.normalise(..) Can't normalise data, inputData has 0 Data Points.");
			System.exit(1);
		}
		else if(inputData[0] == null)
		{
			for(int i = 0; i < inputData.length; i++)
			{
				if(inputData[i] == null)
				{
					System.err.println("DataNormalisation.normalise(..) Can't normalise data, inputData[" + i + "] is null.");
					System.exit(1);
				}
			}
		}
		else if(DataPoint.getNumberOfDimensions() <= 0)
		{
			System.err.println("DataNormalisation.normalise(..) Can't normalise data, DataPoint.getNumberOfDimensions() equals "
					+ DataPoint.getNumberOfDimensions() + ".");
			System.exit(1);
		}
	}
}
