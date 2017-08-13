package data;

import data.DataStatistics;

import static org.junit.Assert.*;
import java.util.HashMap;

public class DataStatisticsTest {
    @org.junit.Test
    public void testCreate() {
        DataStatistics dataStats = new DataStatistics(
                new double[]{ 1.0, 2.0 },
                new double[]{ 2.0, 3.0 },
                new double[]{ 0.0, 1.0 },
                99,
                new HashMap<String, Integer>(),
                new int[]{ 30, 30, 30 },
                9
        );

        assertArrayEquals(new double[]{ 1.0, 2.0 }, dataStats.getMinValues(), 1e-5);
        assertArrayEquals(new double[]{ 2.0, 3.0 }, dataStats.getMaxValues(), 1e-5);
        assertArrayEquals(new double[]{ 0.0, 1.0 }, dataStats.getEachDimNormalisationInterval(), 1e-5);
        assertEquals(99, dataStats.getDatasetLength());
        assertEquals(9, dataStats.getNumberOfNoisePoints());
    }
}
