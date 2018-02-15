package test.data;

import static org.junit.Assert.*;

import java.nio.file.Paths;

import org.junit.Test;

import data.Data;
import data.DataReader;
import data.Parameters;

public class TestDataReader {

	@Test
	public void testRead() {
		Parameters.setVerbose(true);
		Parameters.setClassAttribute(true);
		//Parameters.setInstanceName(true);
		Data inputData = DataReader.read(Paths.get("test3.csv"));
		
		System.out.println( inputData.getPoints()[0]);
		System.out.println( inputData.getPoints()[1]);
		System.out.println( inputData.getPoints()[2]);
		System.out.println( inputData.getPoints()[9]);
		
		assertEquals(10,  inputData.getPoints().length);
		assertEquals(0,  inputData.getPoints()[0].getSourceCoordinate(0),Parameters.getEpsilon());
		assertEquals(0,  inputData.getPoints()[0].getSourceCoordinate(1),Parameters.getEpsilon());
		assertEquals(1,  inputData.getPoints()[9].getSourceCoordinate(1),Parameters.getEpsilon());
		assertEquals(1,  inputData.getPoints()[9].getSourceCoordinate(0),Parameters.getEpsilon());
		
		assertEquals(0,  inputData.getPoints()[1].getCoordinate(0),Parameters.getEpsilon());
		assertEquals(0,  inputData.getPoints()[1].getCoordinate(1),Parameters.getEpsilon());
		assertEquals(1,  inputData.getPoints()[2].getCoordinate(0),Parameters.getEpsilon());
		assertEquals(1,  inputData.getPoints()[2].getCoordinate(1),Parameters.getEpsilon());
	
	}

}
