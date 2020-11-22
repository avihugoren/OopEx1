package ex1.tests;

import ex1.src.WGraph_DS;
import ex1.src.node_info;
import ex1.src.weighted_graph;
import org.junit.jupiter.api.Test;

import java.time.Duration;
import java.util.Arrays;
import java.util.Collection;
import java.util.LinkedList;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.*;


public class WGraph_DsTests {
    private static Random _rnd = null;
    public weighted_graph graphBuilder(int size) //function that build graph
    {
        weighted_graph g=new WGraph_DS();
        for (int i = 0; i <size ; i++) {
            g.addNode(i);
        }
        return g;
    }
    @Test
    public void equalTest()
    {
        weighted_graph g= graphBuilder(100);
        assertEquals(g,g);
        weighted_graph graph2=graphBuilder(100);
        assertEquals(g,graph2);
        g.connect(0,2,2);
        assertNotEquals(g,graph2);
        graph2.connect(0,2,1);
        assertNotEquals(g,graph2);
        graph2.connect(0,2,2);
        assertEquals(g,graph2);
    }


    @Test
    public void graphTest_addNode()//check add function
    {
        weighted_graph g=graphBuilder(7);
        assertEquals(7,g.nodeSize());
        for (int i = 0; i <g.nodeSize() ; i++)
            assertNotNull(g.getNode(i));
    }

    @Test
    public void graphTest_removeNode()//check remove node function
    {
        weighted_graph g=graphBuilder(50);
        g.removeNode(0);
        assertEquals(49,g.nodeSize());
        assertNull(g.getNode(0));
    }
    @Test
    public void graphTest_ConnectAndHasEdge()//check connect function
    {
        weighted_graph g =graphBuilder(80);
        for (int i = 0; i <g.nodeSize(); i++)
            g.connect(0,i,1);//connect node(0) to each node in the graph
        assertTrue(g.hasEdge(0, 1));
        assertTrue(g.hasEdge(0, 2));
        assertTrue(g.hasEdge(0, 3));
        assertFalse(g.hasEdge(1, 2));
        assertFalse(g.hasEdge(5, 6));
        assertFalse(g.hasEdge(4, 9));
    }
    @Test
    public void graphTest_removeEdge()//check the function remove edge
    {
        weighted_graph g=graphBuilder(50);
        g.connect(0,1,1);
        g.connect(0,2,1);
        assertEquals(true,g.hasEdge(0,1));
        assertEquals(2,g.edgeSize());
        g.removeEdge(0,1);
        assertEquals(1,g.edgeSize());
        g.removeEdge(0,1);
        assertEquals(1,g.edgeSize());//edge size check
        assertEquals(false,g.hasEdge(0,1));
        g.removeNode(0);
        assertEquals(false,g.hasEdge(0,2));//remove +remove edge

    }
    @Test
    public void graphTest_removeNodePlusEdge()
    {
        weighted_graph g=graphBuilder(60);
        g.connect(0,1,1);//connect 0 to nodes [1-6]
        g.connect(0,2,1);
        g.connect(0,3,1);
        g.connect(0,4,1);
        g.connect(0,5,1);
        g.connect(0,6,1);
        assertEquals(60,g.nodeSize());
        assertTrue(g.hasEdge(0,1));
        g.removeNode(0);
        assertFalse(g.hasEdge(0,1));
        assertEquals(59,g.nodeSize());
        assertEquals(0,g.edgeSize());

    }
    @Test
    public void hasEdge()
    {
        weighted_graph g=graphBuilder(20);
        for (int i=0;i<g.nodeSize();i++)
            g.connect(0, i, 1);
        for (int j = 1; j <g.nodeSize() ; j++)
            assertTrue(g.hasEdge(0,j));
        assertFalse(g.hasEdge(0,0));

    }
    @Test
    public void graphTest_Mc ()
    {
        weighted_graph g= graphBuilder(50);
        assertEquals(50,g.getMC());
        g.connect(0,1,1);
        g.connect(0,2,1);
        g.connect(0,3,1);
        g.connect(0,4,1);
        assertEquals(54,g.getMC());
        g.connect(0,1,1);//already have this node with same weight
        assertEquals(54,g.getMC());
        g.removeNode(0);
        assertEquals(59,g.getMC());


    }
    @Test
    public void graphTest_edgeCounter(){
        weighted_graph g= graphBuilder(50);
        assertEquals(0,g.edgeSize());
        g.connect(0,1,1);
        g.connect(0,1,1);
        g.connect(0,2,1);
        g.connect(0,3,1);
        g.connect(0,4,1);
        g.connect(0,5,1);
        g.connect(0,6,1);
        assertEquals(6,g.edgeSize());
        g.removeEdge(0,5);
        g.removeEdge(0,4);
        assertEquals(4,g.edgeSize());
    }


    @Test
    public  void graphTest_BuildRunTime()
    {
        assertTimeoutPreemptively(Duration.ofMillis(10000), () -> {

            weighted_graph g=graphBuilder(1000000);
            for (int i = 0; i <g.nodeSize() ; i++)
                for (int j = 0; j <10 ; j++)
                    g.connect(i,j,1);
        });
    }
    @Test
    public void getV()
    {
        weighted_graph g= graphBuilder(20);
        Collection collec=new LinkedList();
        assertTrue(compareCollectionOfNodes(g.getV(0),collec));
        g.connect(0,2,2);
        collec.add(g.getNode(2));
        assertTrue(compareCollectionOfNodes(g.getV(0),collec));
        g.connect(0,1,2);
        assertFalse(compareCollectionOfNodes(g.getV(0),collec));
        collec.add(g.getNode(1));
        assertTrue(compareCollectionOfNodes(g.getV(0),collec));
        g.connect(0,3,2);
        assertFalse(compareCollectionOfNodes(g.getV(0),collec));
        collec.add(g.getNode(3));
        assertTrue(compareCollectionOfNodes(g.getV(0),collec));
        g.removeEdge(0,3);
        assertFalse(compareCollectionOfNodes(g.getV(0),collec));
        g.connect(0,4,2);
        g.connect(0,5,2);
        g.connect(0,6,2);
    }
    @Test
    public void graphGetV()
    {
        weighted_graph g=new WGraph_DS();
        Collection<node_info>collect=new LinkedList<>();
        assertTrue(compareCollectionOfNodes(g.getV(),collect));
        g=graphBuilder(5);
        for (int i = 0; i <g.nodeSize() ; i++)
        {
            collect.add(g.getNode(i));
        }
        assertTrue(compareCollectionOfNodes(g.getV(),collect));
        node_info temp =g.removeNode(4);
        assertFalse(compareCollectionOfNodes(g.getV(),collect));
        collect.remove(temp);
        assertTrue(compareCollectionOfNodes(g.getV(),collect));
    }
    @Test
    public void RandomBuild()
    {
        weighted_graph g=graph_creator(1000000,10000000,6);
    }
    /**
     * Generate a random graph with v_size nodes and e_size edges
     * @param v_size
     * @param e_size
     * @param seed
     * @return
     */
    public static weighted_graph graph_creator(int v_size, int e_size, int seed) //taken from Ex1 Test
    {
        weighted_graph g = new WGraph_DS();
        _rnd = new Random(seed);
        for(int i=0;i<v_size;i++) {
            g.addNode(i);
        }
        // Iterator<node_data> itr = V.iterator(); // Iterator is a more elegant and generic way, but KIS is more important
        int[] nodes = nodes(g);
        while(g.edgeSize() < e_size) {
            int a = nextRnd(0,v_size);
            int b = nextRnd(0,v_size);
            int i = nodes[a];
            int j = nodes[b];
            double w = _rnd.nextDouble();
            g.connect(i,j, w);
        }
        return g;
    }
    private static int nextRnd(int min, int max) {
        double v = nextRnd(0.0+min, (double)max);
        int ans = (int)v;
        return ans;
    }
    private static double nextRnd(double min, double max) {
        double d = _rnd.nextDouble();
        double dx = max-min;
        double ans = d*dx+min;
        return ans;
    }
    /**
     * Simple method for returning an array with all the node_data of the graph,
     * Note: this should be using an Iterator<node_edge> to be fixed in Ex1
     * @param g
     * @return
     */
    private static int[] nodes(weighted_graph g) {
        int size = g.nodeSize();
        Collection<node_info> V = g.getV();
        node_info[] nodes = new node_info[size];
        V.toArray(nodes); // O(n) operation
        int[] ans = new int[size];
        for(int i=0;i<size;i++) {ans[i] = nodes[i].getKey();}
        Arrays.sort(ans);
        return ans;
    }
    public boolean compareCollectionOfNodes(Collection<node_info> a, Collection<node_info> b) {
        if (a.size() != b.size())
            return false;
        boolean answer=true;
        for (node_info nodea : a) {
            for (node_info nodeb : b) {
                answer=nodea.equals(nodeb);
                if (answer)
                    break;
            }
            if (!answer)
                return false;
        }
        return true;
    }




}
