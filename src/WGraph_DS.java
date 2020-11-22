package ex1.src;
import java.util.*;
import java.lang.Object;
import java.lang.StringBuffer;


public class WGraph_DS implements weighted_graph
{
    private class NodeDataW implements node_info,Comparable<node_info> {
        private double tag;
        private String info;
        private int key;

        private NodeDataW(int key) {
            this.key = key;
            this.tag = Integer.MAX_VALUE;
            info = "White";
        }

        NodeDataW(node_info node) {
            this.key = node.getKey();
            this.tag = node.getTag();
            this.info = node.getInfo();
        }

        @Override
        /**
         * Return the key (id) associated with this node.
         * Note: each node_data should have a unique key.
         * @return
         */
        public int getKey() {
            return this.key;
        }

        @Override
        /**
         * return the remark (meta data) associated with this node.
         * @return
         */
        public String getInfo() {
            return info;
        }

        @Override
        /**
         * Allows changing the remark (meta data) associated with this node.
         * @param s
         */
        public void setInfo(String s) {
            this.info = s;

        }

        @Override
        /**
         * Temporal data (aka distance, color, or state)
         * which can be used be algorithms
         * @return
         */
        public double getTag() {
            return this.tag;
        }

        @Override
        /**
         * Allow setting the "tag" value for temporal marking an node - common
         * practice for marking by algorithms.
         * @param t - the new value of the tag
         */
        public void setTag(double t) {
            tag = t;
        }

        @Override
        public String toString() {
            return "NodeDataW-{" +
                    "key=" + key +
                    ", info='" + info + '\'' +
                    ", tag=" + tag +
                    '}';
        }

        @Override
        public  int compareTo(node_info other) {
            if (this.getTag() > other.getTag())
                return 1;
            else if (this.getTag() == other.getTag())
                return 0;
            return -1;
        }
        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            return this.key==((NodeDataW)o).key;
        }
    }
    //class that represent node neighbors list
    private class Neighbors
    {
        private HashMap<Integer, Double> neighbors = new HashMap<>();//hold the keys of this node neighbors and the weight of the edge between them

        //simple constructor
        Neighbors() {
            HashMap<Integer, Double> neighbors = new HashMap<>();
        }

        //function that get node key and return true if he inside this neighbors list
        boolean hasNi(int node) {
            return this.neighbors.containsKey(node);
        }

        //function that get node key and return the weight of the edge between this node to the node that this neighbors list belong to
        double getWeight(int node) {

            return this.neighbors.get(node);
        }

        //add node key to this neighbor list and the weight of the edge between this node to the node that this neighbors list belong to
        void addNi(int node, double weight) {
            this.neighbors.put(node, weight);
        }

        //return list of Integer that represent all keys of the neighbors that belong to this key (that hold this object)
        Set getNi()
        {
            return neighbors.keySet();
        }
        int size()
        {
            return this.neighbors.size();
        }


        //remove key from this neighbors list
        void removeNi(int node) {
            neighbors.remove(node);
        }

        public String toString() {
            StringBuffer s=new StringBuffer();
            for (Integer index :neighbors.keySet())
                s.append(" "+ "["+index+","+neighbors.get(index)+"]"+",");
            return s.toString();
        }

    }

    private HashMap<Integer,node_info>nodesHash=new HashMap<>();//holds nodes in pair with their key
    private HashMap<Integer,Neighbors>edgesHash=new HashMap<>();//hold key of nodes in pair with his neighbor list
    private int modeCount;
    private  int edges;//counter for edges
    //simple constructor
    public WGraph_DS()
    {
        modeCount=0;
        edges=0;
        HashMap<Integer,node_info>nodesHash=new HashMap<>();
        HashMap<Integer,Neighbors>edgesHash=new HashMap<>();
    }
    //copy constructor
    public WGraph_DS(weighted_graph g)
    {
        if(g==null)
            return;
        for(node_info node : g.getV())//for loop that deep copy all the nodes from graph g and add them to this graph
        {
            this.nodesHash.put(node.getKey(), new NodeDataW(node.getKey()));
            this.edgesHash.put(node.getKey(),new Neighbors());
            //for each node from graph g adds all his neighbors too this graph
            for(node_info nodeIndex :g.getV(node.getKey()))
                this.edgesHash.get(node.getKey()).addNi(nodeIndex.getKey(),g.getEdge(nodeIndex.getKey(),node.getKey()));
        }
        this.modeCount=g.getMC();//copy the mode counter
        this.edges=g.edgeSize();//copy the edge size

    }
    @Override
    /**
     * return the node_data by the node_id,
     * @param key - the node_id
     * @return the node_data by the node_id, null if none.
     */
    public node_info getNode(int key)
    {
        return nodesHash.get(key);//hash map that hold all the node with their key
    }

    @Override
    /**
     * return true iff (if and only if) there is an edge between node1 and node2
     * Note: this method should run in O(1) time.
     * @param node1
     * @param node2
     * @return
     */
    public boolean hasEdge(int node1, int node2)
    {
        if(!nodesHash.containsKey(node1))//its enough to check only for one of the because of the other is not in the graph this edge cant exist
            return false;
        return edgesHash.get(node1).hasNi(node2);//go the node1 neighbors list and check if it contain node 2
    }

    @Override
    /**
     * return the weight if the edge (node1, node1). In case
     * there is no such edge - should return -1
     * Note: this method should run in O(1) time.
     * @param node1
     * @param node2
     * @return
     */
    public double getEdge(int node1, int node2)
    {
        if (!hasEdge(node1,node2))//no edge between them
           return -1;
        return edgesHash.get(node1).getWeight(node2);//get the information from object neighbors that belong to node 1
    }

    @Override
    /**
     * add a new node to the graph with the given key.
     * Note: this method should run in O(1) time.
     * Note2: if there is already a node with such a key -> no action should be performed.
     * @param key
     */
    public void addNode(int key)
    {

        if(this.getNode(key)!=null)//already have this key
            return;
        this.nodesHash.put(key,new NodeDataW(key));//add it to the hashMap that hold nodes
        this.edgesHash.put(key,new Neighbors());//add it to the hashMap that holed the neighbors list mapped by the nodes keys
        modeCount++;

    }

    @Override
    /**
     * Connect an edge between node1 and node2, with an edge with weight >=0.
     * Note: this method should run in O(1) time.
     * Note2: if the edge node1-node2 already exists - the method simply updates the weight of the edge.
     */
    public void connect(int node1, int node2, double w)
    {
        if (w<0)
            return;
        if(!nodesHash.containsKey(node1) || !nodesHash.containsKey(node2))//one of the is not in the graph
            return;
        if(node1 == node2)//no node form node to itself
            return;
        double edge=getEdge(node1,node2);
        if(edge==-1)//they didnt have edge before so add to the edges counter
            edges++;
        if(edge==w)
            return;
        edgesHash.get(node1).addNi(node2,w);//add node2 to node 1 neighbors list
        edgesHash.get(node2).addNi(node1,w);//""  node1 ""  node2 ""  ""
        modeCount++;

    }

    @Override
    /**
     * This method return a pointer (shallow copy) for a
     * Collection representing all the nodes in the graph.
     * Note: this method should run in O(1) tim
     * @return Collection<node_data>
     */
    public Collection<node_info> getV()
    {
        return nodesHash.values();
    }

    @Override
    /**
     *
     * This method returns a Collection containing all the
     * nodes connected to node_id
     * Note: this method can run in O(k) time, k - being the degree of node_id.
     * @return Collection<node_data>
     */
    public Collection<node_info> getV(int node_id)
    {
        LinkedList<node_info>ans=new LinkedList<>();
        for(Object inte : edgesHash.get(node_id).getNi())
            ans.add(this.getNode((Integer)inte));
        return ans;
    }

    @Override
    /**
     * Delete the node (with the given ID) from the graph -
     * and removes all edges which starts or ends at this node.
     * This method should run in O(n), |V|=n, as all the edges should be removed.
     * @return the data of the removed node (null if none).
     * @param key
     */
    public node_info removeNode(int key) {
        if (!nodesHash.containsKey(key))//if its not in the graph
            return null;

        for (node_info node : this.getV(key))
        {
            this.removeEdge(key,node.getKey());//removes all the edges that connected to this node(key)
        }
        modeCount++;
        edgesHash.remove(key);
        return nodesHash.remove(key);//remove the node
    }



    @Override
    /**
     * Delete the edge from the graph,
     * Note: this method should run in O(1) time.
     * @param node1
     * @param node2
     */
    public void removeEdge(int node1, int node2)
    {
        if(!hasEdge(node1,node2))//no edge from the beginning
            return;
        edgesHash.get(node1).removeNi(node2);//remove node2 form node 1 neighbors object
        edgesHash.get(node2).removeNi(node1);/// ""    node1 ""  ""  node 2 ""  ""
        edges--;
        modeCount++;
    }

    @Override
    /** return the number of vertices (nodes) in the graph.
     * Note: this method should run in O(1) time.
     * @return
     */
    public int nodeSize()
    {
        return this.nodesHash.size();
    }

    @Override
    /**
     * return the number of edges (undirectional graph).
     * Note: this method should run in O(1) time.
     * @return
     */
    public int edgeSize() {
        return edges;
    }

    @Override
    /**
     * return the Mode Count - for testing changes in the graph.
     * Any change in the inner state of the graph should cause an increment in the ModeCount
     * @return
     */
    public int getMC() {
        return modeCount;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        WGraph_DS o1 = (WGraph_DS) o;
        if(this.nodeSize() != o1.nodeSize())
            return false;
        if (this.edgeSize() != o1.edgeSize())
            return false;
        for (node_info node : this.nodesHash.values())
        {
            if (o1.getNode(node.getKey()) == null)
                return false;
            for(node_info nodeIndex :this.getV(node.getKey()))
                if(this.getEdge(node.getKey(),nodeIndex.getKey())!=o1.getEdge(node.getKey(),nodeIndex.getKey()))
                    return false;
        }
        return true;
    }


    @Override
    public int hashCode() {
        return Objects.hash(nodesHash, edgesHash, modeCount, edges);
    }

    @Override
    public String toString() {
        StringBuffer s=new StringBuffer("");
        for(node_info node : this.getV())
            s.append(" "+node+"<"+edgesHash.get(node.getKey())+">"+"\n");
        return s.toString();
    }
}