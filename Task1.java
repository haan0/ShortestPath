

import java.util.ArrayList;
import java.util.Random;



public class Task1 {


    public static void main(String[] args) {
        Random rand = new Random(); // for creating random numbers 
        Graph g = new Graph(); // the current graph

        int id = 0; // numbers of vertices created
        while(g.nodes().size() != 5) // must change this number of verteces every time as required in pdf instead of (5)
        {
            g.nodes().add(new Vertex(id));// add new vertex 
            id++; // increament the numbers of vertices created by one
        }
        for(int i = 0; i < g.nodes().size(); i++)
        {
            // function rand.nextInt (bound) get random number from 0 to bound - 1 
            int numOfEdges = rand.nextInt(g.nodes().size() - 1) + 1; // get a random number of edges for eavery vertex (from 1 to number of vertices - 1)
            while(g.nodes().get(i).adj().size() != numOfEdges) // loop until add all edges
            {
                int to = rand.nextInt(g.nodes().size());  // get a random number for the another vertex in this edge (from 0 to number of vertices - 1)
                if(to != i && !g.isEdgeExist(i, to)) // if the edge already exists before escape it and try again
                {
                    int w = rand.nextInt(20) + 1; // get a random weight from 1 to 20 for every edge
                     
                    g.nodes().get(i).adj().add(new Edge(g.nodes().get(i),g.nodes().get(to),w)); // add the new edge
                }
            }
        }
        
        // print every node and it's edges adj to it
        for(int i = 0; i < g.nodes().size(); i++)
        {
            System.out.println("Vertex : " + i);
            System.out.println("ADJ : ");
            for(int j = 0; j < g.nodes().get(i).adj().size();j++)
            {
                System.out.println("edge to node(" + g.nodes().get(i).adj().get(j).to().id() + ") with weight : " + g.nodes().get(i).adj().get(j).weight());
            }
        }
        System.out.println("******");
        int dist = rand.nextInt(g.nodes().size() - 1) + 1; // get a random distenation vertex
        Dijkstra.shortestPath(g, 0, dist);
        BellmanFord.shortestPath(g, 0, dist); //one algorithm at a time
        /*
        System.out.println("**** Dijkstra algorithm ****");
        long before = System.currentTimeMillis();   // get the time before 
        Dijkstra.shortestPath(g, 0, dist);  // apply Dijkstra algorithm
        long after = System.currentTimeMillis();  // get the time after
        System.out.println("it took time (" + (after - before) + ") milliseconds"); // calculate the time
        
        System.out.println("*******************************");
        // same but this time apply BellmanFord algorithm
        System.out.println("**** BellmanFord algorithm ****");
        before = System.currentTimeMillis();
        BellmanFord.shortestPath(g, 0, dist);
        after = System.currentTimeMillis();
        System.out.println("it took time (" + (after - before) + ") milliseconds"); */
    }
    
}

class Dijkstra {

    public static void shortestPath(Graph g , int source , int dist){
        
        ArrayList<Vertex> q = new ArrayList<>(); // for storing all vertices
        ArrayList<Vertex> path = new ArrayList<>(); // for the path
        
        //1- initialize every vertex  dist with max value except the source vertex make it 0 and make the previous vertex in the path for every vertex null
        for(int i = 0; i < g.nodes().size(); i++)
        {
            if(g.nodes().get(i).id() == source)
                g.nodes().get(i).setDist(0);  // source node (dist from source to source 0)
            q.add(g.nodes().get(i));
        }
        // loop until the q will be empty or finding the dist vertex
        while(!q.isEmpty())
        {
            boolean distFound = false;
            int min = Integer.MAX_VALUE;
            Vertex v = null;
            // get the vertex with minimum dist from source
            for(int i = 0; i < q.size(); i++)
            {
                if(q.get(i).getDist() < min)
                {
                    // reset the vertex with minimum dist from source
                   v = q.get(i);
                    min = q.get(i).getDist();
                }
            }
            if(v == null)
                break; // if there is no node found exit the loop
            q.remove(v); // 2- the vertex with minimum dist from source
            //3- for every adjecant vertex of this vertex reset it's dist to be the dist of this vertex + the weight of the edge bewteen it  
            for(int i = 0; i < v.adj().size(); i++)
            {
                int totalDist = v.getDist() + v.adj().get(i).weight(); // this vertex dist + the edge weight
                if(totalDist < v.adj().get(i).to().getDist())  // if the adj vertex dist greater than the new dist (this vertex dist + the edge weight)
                {
                    // reset the adj vertex dist value
                    v.adj().get(i).to().setDist(totalDist);
                    v.adj().get(i).to().setPrevVertex(v); // and put the previous vertex in the path from source to this vertex this vertex (the vertex with minimum dist from source we got)
                    // if the distenation vertex found break after add all vertex in the path
                    if(v.adj().get(i).to().id() == dist)
                    {
                        // add the path in reversed order (from dist to source)
                        Vertex currentVertex = v.adj().get(i).to();
                        path.add(currentVertex);
                        while(currentVertex.getPrevVertex() != null) // break if the source found (source has no previous vertex == null)
                        {
                            currentVertex = currentVertex.getPrevVertex();
                            path.add(currentVertex);
                        }
                        distFound = true;
                        break;
                    }
                }
            }
            if(distFound) // if the path found break; 
                break;
        }
        if(path.isEmpty()) // path not found
        {
            System.out.println("there is no path between source and dist");
            return;
        }
        // print the path with the minimum weight
        System.out.println("By using Dijkstra, the shortest path between " + source + " and " + dist + " is : ");
        System.out.print(path.get(path.size() - 1).id());
        for(int i = path.size() - 2; i >= 0; i--)
            System.out.print(" -> " + path.get(i).id());
        System.out.println();
        System.out.println("with minimum weight : " + path.get(0).getDist());
    }
}

class BellmanFord{
    
    public static void shortestPath(Graph g , int source , int dist){
        
        ArrayList<Edge> q = new ArrayList<>();// for storing all edges in the graph
        ArrayList<Vertex> path = new ArrayList<>();
        //1- initialize every vertex  dist with max value except the source vertex make it 0 and make the previous vertex in the path for every vertex null
        for(int i = 0; i < g.nodes().size(); i++)
        {
            if(g.nodes().get(i).id() == source)
                g.nodes().get(i).setDist(0);
            for(int j = 0; j < g.nodes().get(i).adj().size(); j++)
                q.add(g.nodes().get(i).adj().get(j));
        }
        //2- loop from 1 to number of nodes -1
        for(int i = 1; i < g.nodes().size(); i++)
        {
            for (int j = 0; j < q.size(); ++j) // for every edge  
            {
                Vertex from = q.get(i).from();  // get the vertex that this edge go from it to the another one
                Vertex to = q.get(i).to();  // get the vertex that this edge go to it from the another one
                // if "from" vertex dist value edited with new value and the total dist (the "from" vertex + the edge weight less than the "to" vertex)
                if(from.getDist() != Integer.MAX_VALUE && (from.getDist() + q.get(i).weight()) < to.getDist())
                {
                    // reset the dist value for "to" vertex and the previous vertex in the path
                    to.setDist(from.getDist() + q.get(i).weight());
                    to.setPrevVertex(from);
                }
            }
        }
        // loop until finding the distenation vertex
        for(int i = 0; i < g.nodes().size(); i++)
            if(g.nodes().get(i).id() == dist)
            {
                // if there is no path found from source to this node yet
                if(g.nodes().get(i).getDist() == Integer.MAX_VALUE)
                {
                    System.out.println("there is no path between source and dist");
                    return;
                }
                // add the path in reversed order (from dist to source)
                path.add(g.nodes().get(i));
                Vertex currentVertex = g.nodes().get(i);
                while(currentVertex.getPrevVertex() != null)
                {
                    currentVertex = currentVertex.getPrevVertex();
                    path.add(currentVertex);
                }
                if(currentVertex.id() != source)
                {
                    System.out.println("there is no path between source and dist");
                    return;
                } 
            }
        // print the path with the minimum weight
        System.out.println("By using Bellman-Ford, the shortest path between " + source + " and " + dist + " is : ");
        System.out.print(path.get(path.size() - 1).id());
        for(int i = path.size() - 2; i >= 0; i--)
            System.out.print(" -> " + path.get(i).id());
        System.out.println();
        System.out.println("with minimum weight : " + path.get(0).getDist());
    }
    
}

class Edge {

    private final Vertex from, to;
    private final int weight;

    public Edge(Vertex f, Vertex t, int weight) {
        this.from = f;
        this.to = t;
        this.weight = weight;
    }

    public Vertex from() {
        return from;
    }

    public Vertex to() {
        return to;
    }

    public int weight() {
        return weight;
    }
}

class Vertex{
    
    private final int id;
    private ArrayList<Edge> adj;
    private int dist;
    private Vertex PrevVertex;
    
    public Vertex(int id){
        this.id = id;
        adj = new ArrayList<>();
        dist = Integer.MAX_VALUE;
        PrevVertex = null;
    }
    
    public int id(){
        return id;
    }
    
    public ArrayList<Edge> adj(){
        return adj;
    }
    
    public int getDist() {
        return dist;
    }

    public void setDist(int dist) {
        this.dist = dist;
    }

    public Vertex getPrevVertex() {
        return PrevVertex;
    }

    public void setPrevVertex(Vertex PrevVertex) {
        this.PrevVertex = PrevVertex;
    }
}

class Graph {

    private ArrayList<Vertex> nodes;

    public Graph() {
        nodes = new ArrayList<>();
    }

    public void addEdge(Edge e) {
        Vertex v = e.from();
        for(int i = 0; i < nodes.size(); i++)
            if(nodes.get(i).id() == v.id()) // get the source and add new adj edj to it
            {
                nodes.get(i).adj().add(e);
                break;
            }
    }
    
    public ArrayList<Vertex> nodes(){
        return nodes;
    }
    
    public boolean isEdgeExist(int source , int dist)
    {
        // if there is edge exists between source and dist
        for(int i = 0; i < nodes.size(); i++)
            if(nodes.get(i).id() == source)
            {
                for(int j = 0; j < nodes.get(i).adj().size(); j++)
                    if(nodes.get(i).adj().get(j).to().id() == dist) // edge found
                        return true;
                break;
            }
        return false;
    }
}