package test.utils;

import static org.junit.Assert.*;


import java.nio.file.Paths;

import org.junit.Test;

import data.Parameters;
import utils.CmdLineParser;

public class TestCmdLineParser {

	@Test
	public void testCmdLineParser() {
		String[] args = new String[]{
				"-i", "test.csv",
//				"-h",
//				"-gmm",
				"-lgmm",
				"-s", "2",
				"-v",
				"-o", "out",
				"-k", "4",
				"-n", "1",
				"-r", "1",
				"-e", "10",
				"-l", "0",
				"-c",
				"-dm",
				"-gi", "900",
				"-w", "2147483600",
				"-in",
//				"-ds",
//				"-scac",
//				"-sccs",
//				"-scrs",
//				"-rf", "0.5",
//				"-cf", "0.88",
//				"-re"
		};
		CmdLineParser parser = new CmdLineParser();
		parser.parse(args);
		
		assertEquals( 0 , Parameters.getCovarianceScallingFactor(), 1.0E-10 );
		assertEquals( 2 , Parameters.getDendrogramMaxHeight() );
		assertEquals( null , Parameters.getDimensionsNamesFilePath() );
		assertEquals( 1.0E-10 , Parameters.getEpsilon(), 1.0E-10 );
		assertEquals( 900 , Parameters.getGenerateImagesSize() );
		assertEquals( "test.csv" , Parameters.getInputDataFilePath().toString() );
		assertEquals( 4 , Parameters.getK() );
		assertEquals( 1 , Parameters.getLittleValue(), 0 );
		assertEquals( 2147483600 , Parameters.getMaxNumberOfNodes() );
		assertEquals( "LOG_GMM" , Parameters.getMethod().toString() );
		assertEquals( 1 , Parameters.getNumberOfClusterisationAlgIterations() );
		assertEquals( 1 , Parameters.getNumberOfClusterisationAlgRepeats() );
		assertEquals( "out" , Parameters.getOutputFolder().toString() );
		assertEquals( 0 , Parameters.getResponsibilityScallingFactor(), 1.0E-10 );
		assertEquals(true, Parameters.isClassAttribute());
		assertEquals(false, Parameters.isClusterReestimationBasedOnItsData());
		assertEquals(true, Parameters.isDiagonalCovarianceMatrix());
		assertEquals(false, Parameters.isDisableStaticCenter());
		assertEquals(true, Parameters.isInstanceName());
		assertEquals(false, Parameters.isStaticCenterAdaptiveCovarianve());
		assertEquals(false, Parameters.isStaticCenterCovarianceScalling());
		assertEquals(false, Parameters.isStaticCenterResponsibilityScalling());
		assertEquals(true, Parameters.isVerbose());
		
		args = new String[]{
				"-i", "test.csv",
//				"-h",
				"-gmm",
//				"-lgmm",
				"-s", "2",
				"-v",
				"-o", "out",
				"-k", "4",
				"-n", "2",
				"-r", "3",
				"-e", "10",
				"-l", "0",
				"-c",
				"-dm",
				"-gi", "900",
				"-w", "2147483600",
				"-in",
				"-ds",
				"-scac",
//				"-sccs",
//				"-scrs",
				"-rf", "0.5",
				"-cf", "0.88",
				"-re"
		};
		parser.parse(args);
		
		Parameters.setDimensionsNamesFilePath( Paths.get("test") );
		
		assertEquals( 0.88 , Parameters.getCovarianceScallingFactor(), 1.0E-10 );
		assertEquals( 2 , Parameters.getDendrogramMaxHeight() );
		assertEquals( "test" , Parameters.getDimensionsNamesFilePath().toString() );
		assertEquals( 1.0E-10 , Parameters.getEpsilon(), 1.0E-10 );
		assertEquals( 900 , Parameters.getGenerateImagesSize() );
		assertEquals( "test.csv" , Parameters.getInputDataFilePath().toString() );
		assertEquals( 4 , Parameters.getK() );
		assertEquals( 1 , Parameters.getLittleValue(), 0 );
		assertEquals( 2147483600 , Parameters.getMaxNumberOfNodes() );
		assertEquals( "GMM" , Parameters.getMethod().toString() );
		assertEquals( 2 , Parameters.getNumberOfClusterisationAlgIterations() );
		assertEquals( 3 , Parameters.getNumberOfClusterisationAlgRepeats() );
		assertEquals( "out" , Parameters.getOutputFolder().toString() );
		assertEquals( 0.5 , Parameters.getResponsibilityScallingFactor(), 1.0E-10 );
		assertEquals(true, Parameters.isClassAttribute());
		assertEquals(true, Parameters.isClusterReestimationBasedOnItsData());
		assertEquals(true, Parameters.isDiagonalCovarianceMatrix());
		assertEquals(true, Parameters.isDisableStaticCenter());
		assertEquals(true, Parameters.isInstanceName());
		assertEquals(true, Parameters.isStaticCenterAdaptiveCovarianve());
		assertEquals(false, Parameters.isStaticCenterCovarianceScalling());
		assertEquals(false, Parameters.isStaticCenterResponsibilityScalling());
		assertEquals(true, Parameters.isVerbose());
	}

	

}
