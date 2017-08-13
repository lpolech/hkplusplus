package dendrogram;

import algorithms.AlgEnum;
import basic_hierarchy.implementation.BasicHierarchy;
import data.Data;
import data.DataPoint;

import java.nio.file.FileSystems;
import java.util.ArrayList;
import java.util.HashMap;

import data.DataStatistics;

import static org.junit.Assert.*;

import data.Parameters;
import org.junit.Before;
import runner.Main;

public class DendrogramTest {
    private Dendrogram dendrogramObj = null;

    private DataPoint[] preparePoints() {
        return new DataPoint[] {
                new DataPoint(
                        new double[]{ 1.0, 2.0, 3.0 },
                        new double[]{ 1.0, 2.0, 3.0 },
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

    private Data prepareData(DataPoint[] points, DataStatistics stats) {
        return new Data(
                points,
                3,
                2,
                stats,
                new HashMap<Integer, String>()
        );
    }

    // initialization
    @Before
    public void initDendrogram() {
        Data data = this.prepareData(
            this.preparePoints(),
            this.prepareDataStats()
        );

        this.dendrogramObj = new Dendrogram(
            data,
            AlgEnum.GMM,
            3,
            5,
            FileSystems.getDefault().getPath("logs", "access.log")
        );
    }

    @org.junit.Test
    public void testCreation(){
        assertNotNull(this.dendrogramObj);
    }

    @org.junit.Test
    public void testGetHierarchyRepresentation() {
        BasicHierarchy hierarchy = ((BasicHierarchy)this.dendrogramObj.getHierarchyRepresentation());

        assertNotNull(hierarchy);
        assertEquals(3, hierarchy.getRoot().getNodeInstances().size());
        assertEquals(1, hierarchy.getNumberOfGroups());
        assertEquals(0, hierarchy.getNumberOfClasses());
        assertEquals(3, hierarchy.getOverallNumberOfInstances());
    }

    @org.junit.Test
    public void testGetDendrogram() {
        ArrayList<DendrogramLevel> dendrogram = this.dendrogramObj.getDendrogram();

        assertNotNull(dendrogram);
        assertEquals(1, dendrogram.size());
        assertEquals(1, dendrogram.get(0).getClusters().length);
    }

    @org.junit.Test
    public void testGetFinalStatistics() {
        double s = this.dendrogramObj.getFinalStatistic();
        double eps = 1e-3;
        double expectedValue = 3.79;

        assertTrue(Math.abs(expectedValue - s) < eps);
    }

    @org.junit.Test
    public void testRun() {
        // ARRANGE
        Data data = this.prepareData(
                this.preparePoints(),
                this.prepareDataStats()
        );

        Dendrogram d = new Dendrogram(
            data,
            AlgEnum.GMM,
            3,
            5,
            FileSystems.getDefault().getPath("logs", "access.log")
        );

        Parameters.setMaxNumberOfNodes(100);

        // ACT
        ArrayList<DendrogramLevel> dendrogram = d.run();

        // ASSERT
        assertEquals(1, dendrogram.size());
    }
}