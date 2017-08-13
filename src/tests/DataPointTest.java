package data;

import org.junit.Before;
import static org.junit.Assert.*;

import data.DataPoint;

public class DataPointTest {

    @org.junit.Test
    public void testCreate() {
        DataPoint dataPoint = new DataPoint(
                new double[]{ 1.0, 2.0, 3.0 },
                new double[]{ 4.0, 5.0, 6.0 },
                "instance_1",
                "classAttr_1"
        );

        assertArrayEquals(new double[]{ 1.0, 2.0, 3.0 }, dataPoint.getCoordinates(), 1e-5);
        assertArrayEquals(new double[]{ 4.0, 5.0, 6.0 }, dataPoint.getSourceCoordinates(), 1e-5);
    }
}
