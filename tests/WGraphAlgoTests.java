package ex1.tests;

import ex1.src.*;
import org.junit.jupiter.api.Test;

import java.time.Duration;
import java.util.LinkedList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class WGraphAlgoTests {
    public weighted_graph graphBuilder(int size) {
        weighted_graph g = new WGraph_DS();
        for (int i = 0; i < size; i++) {
            g.addNode(i);
        }
        return g;
    }

    @Test
    void copy()//build graph of 10^6 nodes and check if its able to copy it under 10 seconds
    {
        weighted_graph g = graphBuilder(1000000);
        for (int i = 0; i < g.nodeSize(); i++)
            for (int j = 0; j < 10; j++)
                g.connect(i, j, 1);
        assertTimeoutPreemptively(Duration.ofMillis(10000), () -> {

            weighted_graph gc = new WGraph_DS(g);
        });

    }




    @Test
    public void isConnected()
    {
        weighted_graph g = new WGraph_DS();
        weighted_graph_algorithms algo=new WGraph_Algo();
        assertTrue(algo.isConnected());//null graph
        algo.init(g);
        assertTrue(algo.isConnected());//empty graph
        g.addNode(0);
        assertTrue(algo.isConnected());//one node graph
        weighted_graph g2 = graphBuilder(50);
        algo.init(g2);
        g.connect(0, 1, 1);
        g.connect(0, 2, 2);
        assertFalse(algo.isConnected());
        for (int i = 0; i < g2.nodeSize(); i++)
            g2.connect(0, i, 2);//connect all the graph
        assertTrue(algo.isConnected());
    }

    @Test
    public void shortestPathDist()
    {
        weighted_graph g = new WGraph_DS();
        WGraph_Algo algo1 = new WGraph_Algo(g);
        assertEquals(-1,algo1.shortestPathDist(5,9));//not in the graph
        g.addNode(0);
        assertEquals(0, algo1.shortestPathDist(0, 0));//node to itself should be -1
        for (int i = 1; i < 10; i++)
            g.addNode(i);
        g.connect(0, 1, 1);
        g.connect(0, 5, 20);
        g.connect(1, 2, 1);
        g.connect(2, 8, 1);
        g.connect(5, 4, 20);
        g.connect(5, 3, 20);
        g.connect(8, 3, 1);
        assertEquals(4, algo1.shortestPathDist(0, 3));
        assertEquals(20,algo1.shortestPathDist(0,5));
        assertEquals(0,algo1.shortestPathDist(5,5));
        assertEquals(1,algo1.shortestPathDist(8,3));

    }

    @Test
    public void shortestPathDist2() {
        weighted_graph g = graphBuilder(100);
        weighted_graph_algorithms algo1 = new WGraph_Algo(g);
        g.connect(0, 1, 0);
        g.connect(1, 2, 0);
        g.connect(2, 3, 0);
        g.connect(3, 4, 0);
        g.connect(0, 5, 1);
        g.connect(5, 6, 1);
        g.connect(6, 7, 1);
        g.connect(7, 4, 1);
        assertEquals(0, algo1.shortestPathDist(0, 4));
        g=graphBuilder(10);
        algo1.init(g);
        g.connect(0,1,1);
        g.connect(1,2,1);
        g.connect(2,3,0);
        assertEquals(2,algo1.shortestPathDist(0,3));
    }

    @Test
    public void copy2() {
        weighted_graph g = graphBuilder(1000);
        weighted_graph_algorithms algo1 = new WGraph_Algo(g);
        weighted_graph gCopy = algo1.copy();
       assertEquals(g,gCopy);
        g.connect(0, 2, 2);
        assertFalse(gCopy.hasEdge(0, 2));//changes in g does not affect g copy
        g.removeNode(50);
        assertNotNull(gCopy.getNode(50));//changes in g does not affect g copy
        g.connect(0, 5, 20);
        assertNotEquals(gCopy.getEdge(0, 5), g.getEdge(0, 5), 0.0);
        g.connect(0, 10, 5);
        g.connect(0, 20, 30);
        assertFalse(gCopy.hasEdge(0, 20));
        assertFalse(gCopy.hasEdge(0, 10));
        assertFalse(gCopy.hasEdge(0, 5));
        assertNotEquals(gCopy.getMC(), g.getMC());
        assertNotSame(g, gCopy);//on memory


    }

    @Test
    void save() {
        weighted_graph g = graphBuilder(22);
        weighted_graph_algorithms algo1 = new WGraph_Algo(g);
        g.connect(0, 1, 1);
        g.connect(0, 2, 2);
        g.connect(2, 1, 1);
        assertTrue(algo1.save("test20"));
        assertTrue(algo1.load("test20"));
        assertEquals(algo1.getGraph(),g);
        algo1.getGraph().connect(0,20,0);
        assertNotEquals(algo1.getGraph(),g);
    }
        @Test
        void saveNullGraph()
        {
            weighted_graph_algorithms algo=new WGraph_Algo();
            algo.init(null);
            assertFalse(algo.save("nullGraph"));//should not save null graph
        }
    @Test
     void saveEmptyGraph()
    {
        weighted_graph g=new WGraph_DS();
        weighted_graph_algorithms algo= new WGraph_Algo();
        algo.init(g);
        assertTrue(algo.save("emptyGraphTest"));
        assertTrue(algo.load("emptyGraphTest"));
        assertEquals(algo.getGraph(),g);

     }
     @Test
     public void shortestPath()
     {
         weighted_graph g=graphBuilder(10);
         weighted_graph_algorithms algo=new WGraph_Algo();
         assertNull(algo.shortestPath(10,100));
         algo.init(g);
         List testList=new LinkedList();
         testList.add(g.getNode(0));
         assertTrue(compareListOfNodes(testList,algo.shortestPath(0,0)));//node to itself only he is in the list
         g.connect(0,1,1);
         testList.add(g.getNode(1));
         assertTrue(compareListOfNodes(testList,algo.shortestPath(0,1)));
         assertFalse(compareListOfNodes(testList,algo.shortestPath(1,0)));
         g.connect( 0,3,0);
         g.connect(3,1,0);
         assertFalse(compareListOfNodes(testList,algo.shortestPath(0,1)));
         g.connect(1,3,1000);
         assertTrue(compareListOfNodes(testList,algo.shortestPath(0,1)));
     }

    public boolean compareListOfNodes(List<node_info> a, List<node_info> b) {
        if (a.size() != b.size())
            return false;
        for (int i = 0; i < a.size(); i++)
            if(!a.get(i).equals(b.get(i)))
                return false;
        return true;
    }

    }





