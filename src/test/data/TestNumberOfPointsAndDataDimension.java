package test.data;

import static org.junit.Assert.*;

import org.junit.Test;

import data.NumberOfPointsAndDataDimension;

public class TestNumberOfPointsAndDataDimension {

	@Test
	public void testNumberOfPointsAndDataDimension()
	{
		int nr_of_ponts=1000;
		int dim =100;
		NumberOfPointsAndDataDimension test= new NumberOfPointsAndDataDimension(nr_of_ponts, dim);
		assertEquals(nr_of_ponts, test.getNumberOfPoints());
		assertEquals(dim,test.getDataDimension());

	}
}
