package data;

import static org.junit.Assert.*;
import java.util.HashMap;

public class DataTest {
    private DataPoint[] preparePoints() {
        return new DataPoint[]{
                new DataPoint(
                        new double[]{ 1.0, 2.0, 3.0 },
                        new double[]{ 4.0, 5.0, 6.0 },
                        "instance_1",
                        "classAttr_1"
                ),
                new DataPoint(
                        new double[]{ 2.1, 3.4, 2.3 },
                        new double[]{ 4.3, 2.3, 4.5 },
                        "instance_2",
                        "classAttr_2"
                ),
                new DataPoint(
                        new double[]{ 1.1, 2.2, 3.3 },
                        new double[]{ 4.4, 5.4, 6.5 },
                        "instance_3",
                        "classAttr_3"
                )
        };
    }

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
        DataPoint[] points   = this.preparePoints();
        DataStatistics stats = this.prepareDataStats();

        Data data = new Data(
                points,
                3,
                2,
                stats,
                new HashMap<Integer, String>()
                );

        assertEquals(2, data.getNumberOfDimensions());
    }
}
