package data;

public class MixtureNumberWithItsResponsibility {
private int mixtureNumber = Integer.MIN_VALUE;
private double responsibility = (-1)*Double.MAX_VALUE;

public MixtureNumberWithItsResponsibility(int mixtureNumber, double responsibility)
{
	this.mixtureNumber = mixtureNumber;
	this.responsibility = responsibility;
}

public int getMixtureNumber() {
	return mixtureNumber;
}
public double getResponsibility() {
	return responsibility;
}

}
