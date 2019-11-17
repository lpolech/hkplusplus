package dendrogram;

import data.DataStatistics;
import org.junit.Before;

import java.util.HashMap;

import static org.junit.Assert.*;

public class DendrogramLevelTest {
    private DataStatistics prepareDataStats() {
        return new DataStatistics(
                new double[]{ 1.0, 2.0 },
                new double[]{ 2.0, 3.0 },
                new double[]{ 0.0, 1.0 },
                3,
                new HashMap<String, Integer>(),
                new int[]{ 1, 1, 1 },
                0
        );
    }

    @org.junit.Test
    public void testCreate() {
        DataStatistics dataStats = this.prepareDataStats();

        DendrogramLevel dl = new DendrogramLevel(1, dataStats);
        assertNotNull(dl);
    }
}
