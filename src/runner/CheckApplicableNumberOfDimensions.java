package runner;

public class CheckApplicableNumberOfDimensions {

	public static void main(String[] args) {
		double revOfSqrtOfPoweredDoublePi;
		int numberOfDimensions = 1;
		do
		{
			revOfSqrtOfPoweredDoublePi = (double)(1.0d/Math.sqrt(Math.pow(Math.PI*2, (double)numberOfDimensions)));
			System.out.println("Number of dim: " + numberOfDimensions + " value: " + revOfSqrtOfPoweredDoublePi);
			numberOfDimensions++;
		}
		while(revOfSqrtOfPoweredDoublePi != 0.0);

	}

}
