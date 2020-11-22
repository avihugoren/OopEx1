package ex1.src;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import java.util.*;

public class WGraph_Algo implements weighted_graph_algorithms {
    weighted_graph myGraph;
    //constructor that gets graph
    public WGraph_Algo(weighted_graph g)
    {
        myGraph = g;
    }
    //default constructor
    public WGraph_Algo()
    {
        myGraph = new WGraph_DS();
    }

    @Override

    /**
     * Init the graph on which this set of algorithms operates on.
     * @param g
     */
    public void init(weighted_graph g) {
        myGraph = g;

    }

    @Override
    /**
     * Return the underlying graph of which this class works.
     * @return
     */
    public weighted_graph getGraph()
    {
        return myGraph;
    }

    @Override
    /**
     * Compute a deep copy of this weighted graph.
     * @return
     */
    public weighted_graph copy()
    {
        if (myGraph == null)
            return null;
        return new WGraph_DS(myGraph);//calling WGraph_DS copy constructor

    }

    @Override
    /**
     * Returns true if and only if (iff) there is a valid path from EVREY node to each
     * other node. NOTE: assume ubdirectional graph.
     * @return
     */
    //time complexity O(|E|log|V|+|V|log|V|)
    public boolean isConnected()
    {
        if(myGraph == null)//empty graph isConnected
            return true;
        if(myGraph.nodeSize() == 0)//emprt graph isConnected
            return true;
        node_info temp = null;
        for (node_info node : myGraph.getV()) //need one node so i can send it to Dijkstras_Algorithm
        {
            temp = node;
            break;
        }
        Dijkstras_Algorithm(temp);//updating each node tag to  be the distance of weights from node temp
        for(node_info nodeIndex : myGraph.getV())
            if(nodeIndex.getInfo() == "White")//only a node that is unreachable stay white
                return false;
        return true;//no white nodes

    }

    @Override
    /**
     * returns the length of the shortest path between src to dest
     * Note: if no such path --> returns -1
     * @param src - start node
     * @param dest - end (target) node
     * @return
     */
    //time complexity O(|E|log|V|+|V|log|V|)
    public double shortestPathDist(int src, int dest)
    {
        if(myGraph == null)//empty graph
            return -1;
        if(myGraph.getNode(src) == null || myGraph.getNode(dest) == null )//one of the nodes in not in the graph
            return -1;
        if(src == dest)//node to itself
            return 0;

        Dijkstras_Algorithm(myGraph.getNode(src));//updating each node tag to  be the distance of weights from node temp
        node_info temp=myGraph.getNode(dest);
        if(temp.getInfo().equals("White"))
            return -1;
        return temp.getTag();//return the distance of node dest from node temp
    }

    @Override
    /**
     * returns the the shortest path between src to dest - as an ordered List of nodes:
     * src--> n1-->n2-->...dest
     * see: https://en.wikipedia.org/wiki/Shortest_path_problem
     * Note if no such path --> returns null;
     * @param src - start node
     * @param dest - end (target) node
     * @return
     */
    //time complexity O(|E|log|V|+|V|log|V|)
    public List<node_info> shortestPath(int src, int dest)
    {
        List ans=new LinkedList();
        if(myGraph == null)//graph is empty
            return null;
        if(myGraph.getNode(src)==null||myGraph.getNode(dest) == null)//one of the nodes in not in the graph
            return null;
        if(src == dest)//from node to itself
        {
            ans.add(myGraph.getNode(src));
            return ans;
        }
        HashMap<Integer,node_info>helpHash=Dijkstras_Algorithm(myGraph.getNode(dest));//return hashmap that contain each node key with his father(the node that called him in the Dijkstras_Algorithm)
        if(myGraph.getNode(src).getInfo().equals("White"))//no route from dest to src
            return null;
        node_info temp=null;
        node_info nodeHelp=myGraph.getNode(src);
        while (nodeHelp.getKey()!=myGraph.getNode(dest).getKey())//because there is route for sure(src is not white)going back from src to his father and so on until i reach dest
        {
            ans.add(nodeHelp);
            nodeHelp=helpHash.get(nodeHelp.getKey());
        }
        ans.add(myGraph.getNode(dest));//return all the nodes that in the route
        return ans;

    }

    @Override
    /**
     * Saves this weighted (undirected) graph to the given
     * file name
     * @param file - the file name (may include a relative path).
     * @return true - iff the file was successfully saved
     */
    public boolean save(String file)
    {
        //if the graph is null no need to save it
        if(this.myGraph == null)
            return false;
        try {
            File myObj = new File(file);
            //if the file successfully created
            if (myObj.createNewFile())
            {
                System.out.println("File created: " + myObj.getName());
                try {
                    FileWriter myWriter = new FileWriter(file);
                    myWriter.write(String.valueOf(myGraph));//writing the graph to the  file
                    myWriter.close();

                    return true;
                }
                catch (IOException e)
                {
                    return false;
                }
            }
            else
                System.out.println("File already exists so delete it and create new one.");
            myObj.delete();//delete the old one
            this.save(file);
            return true;
        }
        catch (IOException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
            return false;
        }
    }

    @Override
    /**
     * This method load a graph to this graph algorithm.
     * if the file was successfully loaded - the underlying graph
     * of this class will be changed (to the loaded one), in case the
     * graph was not loaded the original graph should remain "as is".
     * @param file - file name
     * @return true - iff the graph was successfully loaded.
     */
    public boolean load(String file)
    {
        int key;
        weighted_graph answer=new WGraph_DS();
        try {
            Scanner scanner = new Scanner(new  File(file));
            //empty graph
            if(!scanner.hasNext())
            {
                this.init(answer);
                return true;
            }
            scanner.useDelimiter("-");
            scanner.next();
            Scanner scanner2= new Scanner(new  File(file));
            scanner2.useDelimiter("<");
            scanner2.next();
            while (scanner.hasNext())
            {

                //function that found the key in the string and add it to the graph with its value(info,tag)
                key=StringToNode(answer,scanner.next());
                //return array of all the neighbors of this node and there value of the edge between them
                double[][] help=StringToNeighbors(answer,scanner2.next());
                //for loop that connect the node with its neighbors
                for (int i = 0; i <help.length ; i++)
                {
                    answer.connect(key,(int)help[i][0],help[i][1]);
                }
            }
            this.init(answer);

        }
        catch (IOException e)
        {
            System.out.println("An error occurred. file not exists");
            return false;
        }
        this.init(answer);
        return true;
    }



    //time complexity O(|E|log|V|+|V|log|V|)
    private HashMap Dijkstras_Algorithm(node_info node)
    {
        if (myGraph == null)//empty graph
            return null;
        //set all not tag too infinity and there info to white(un visited)
        for(node_info nodeZero :myGraph.getV())
        {
            nodeZero.setTag(Integer.MAX_VALUE);
            nodeZero.setInfo("White");
        }
        node.setTag(0);//distance from node to himself is zero
        double temp;
        PriorityQueue<node_info> helpQueue = new PriorityQueue<>();
        helpQueue.add(node);//add the first node to the priorityQueue
        HashMap<Integer,node_info>ans=new HashMap<>();
        while (!helpQueue.isEmpty())
        {
            if(myGraph.getNode(helpQueue.peek().getKey()).getInfo() != "Black")//this node is already visited and so all his neighbors
                for (node_info nodeIndex : myGraph.getV(helpQueue.peek().getKey()) )//running on all his neighbors
                {
                    temp = helpQueue.peek().getTag() + myGraph.getEdge(helpQueue.peek().getKey(), nodeIndex.getKey());//the distance of node index from src
                    //not visited and is current distance from src is bigger
                    if (!nodeIndex.getInfo().equals("Black")  &&nodeIndex.getTag() > temp)
                    {
                        nodeIndex.setTag(temp);//update its distance from src
                        helpQueue.add(nodeIndex);//add it to the priorityQueue so i can reach its neighbors
                        ans.put(nodeIndex.getKey(),helpQueue.peek());//also add it to the fathers hashMap
                    }
                }
            helpQueue.poll().setInfo("Black");//no need form it any more and all this node neighbors are visited so his black
        }
        return ans;//return the hashMap of each node with his father <Integer,node_info>(father = the previous node on the route)
    }
    //function that get graph and String(this string hold one node info) from a txt file that represent a graph
    //the function adds the node in this line to the graph and return its key
    private int StringToNode(weighted_graph g,String s)
    {
        int key;
        String info;
        Double tag;
        Scanner scanner=new Scanner(s);
        scanner.useDelimiter("=");
        scanner.next();
        Scanner scannerHelp=new Scanner(scanner.next());
        scannerHelp.useDelimiter(",");
        //extracting the key
        key=Integer.parseInt(scannerHelp.next());
        //extracting the info
        scannerHelp=new Scanner(scanner.next());
        scannerHelp.useDelimiter("'");
        info=scannerHelp.next();
        //extracting the tag
        scannerHelp=new Scanner(scanner.next());
        scannerHelp.useDelimiter("}");
        tag=Double.parseDouble(scannerHelp.next());
        //add the node to the graph and set his info and tag
        g.addNode(key);
        node_info temp =g.getNode(key);
        temp.setInfo(info);
        temp.setTag(tag);
        return key;
    }
    //function that gets string(that represent one node and its neighbors) from a txt file that represent graph
    //and return array of the neighbors with the weight of the edge between them
    private double[][] StringToNeighbors(weighted_graph g,String s)
    {
        Scanner scanner=new Scanner(s);
        scanner.useDelimiter(">");
        s=scanner.next();
        int counter =0;
        //for that count how many neighbors this node have
        for (int i = 0; i <s.length() ; i++)
            if(s.charAt(i)== ']')
                counter++;
        //create array to the neighbors
        double[][]ans=new double[counter][2];
        StringBuffer help=new StringBuffer();
        //for loop that delete all the unnecessary information from the string
        for (int i = 0; i <s.length() ; i++)
        {
            if(s.charAt(i) != '['&&s.charAt(i)!= '<'&&s.charAt(i)!=':'&&s.charAt(i)!='>'&&s.charAt(i) !=']')
                help.append(s.charAt(i));
        }
        String help2=help.toString();
        //split the string to numbers even index will have key values and odd index will have the value of the edge
        String[] helpArray=help2.split(",");
        int j=0;
        //put the information on the array that this function return
        for (int i=0;i<ans.length*2;i++)
        {
            if(i%2 == 0)
                ans[j][0]=Double.parseDouble(helpArray[i]);
            else
            {
                ans[j][1] = Double.parseDouble(helpArray[i]);
                j++;
            }
        }

        return ans;
    }
}
