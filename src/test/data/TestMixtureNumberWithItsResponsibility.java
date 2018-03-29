package test.data;

import static org.junit.Assert.*;

import org.junit.Test;

import data.MixtureNumberWithItsResponsibility;

public class TestMixtureNumberWithItsResponsibility {

	@Test
	public void testMixtureNumberWithItsResponsibility() 
	{
		int mixtureNumber=10;
		double responsibility=10.0;
		MixtureNumberWithItsResponsibility test = new MixtureNumberWithItsResponsibility(mixtureNumber, responsibility);
		assertEquals(mixtureNumber, test.getMixtureNumber());
		assertEquals(responsibility, test.getResponsibility(), 0 );

	}

}
