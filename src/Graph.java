//Ken Phu
//Project 3

import java.util.*;
import java.io.*;

public class Graph {
    private ArrayList<EdgeNode> adjList[];
    private ArrayList<EdgeNode> adjListTranspose[];
    private int nVertices;
    private int nEdges;
    private String fileName;
    Scanner sc;

    /**
     * Constructor
     */
    public Graph(String inputFileName) {
        readFile(inputFileName);
        nVertices = sc.nextInt();
        adjList = new ArrayList[nVertices];
        nEdges = 0;

        for(int i = 0; i < adjList.length; i++) {
            adjList[i] = new ArrayList<EdgeNode>();
        }

        while(sc.hasNextInt()){
            int sourceVertex = sc.nextInt();
            int dest = sc.nextInt();
            int edgeWeight = sc.nextInt();
            adjList[sourceVertex].add(new EdgeNode(sourceVertex, dest, edgeWeight));
            nEdges += 1;
        }
    }

    private void readFile(String inputFileName){
        try{
            File file = new File(inputFileName);
            sc = new Scanner(file);
        }catch(FileNotFoundException e){ System.out.println("Error: File doesn't exist"); }
    }

    /**
     * Print Graph Method
     */
    public void printGraph() {
        System.out.print("\nnVertices = " + nVertices + "\tnEdges = " + nEdges);
        System.out.println("\nAdjacency Lists");
        
        for (int i = 0; i < adjList.length; i++) {
            System.out.print("v = " + i + "   [");
            for (int j = 0; j < adjList[i].size() && adjList[i].get(j) != null; j++) {
                EdgeNode edge = adjList[i].get(j);
                System.out.print(" (" + edge.vertex1 + ", " + edge.vertex2 + ", " + edge.weight + ")");
            }
            System.out.println(" ]");
        }
    }

    /******************* BFS Shortest paths  ******************/
    public SPPacket bfsShortestPaths(int start) {

        //Initialization
        boolean[] visitedNodes = new boolean[nVertices];
        int[] distance = new int[nVertices];
        int[] parent = new int[nVertices];

        //BFS Algorithm
        ArrayList<ArrayList<Integer>> list = new ArrayList<>();
        list.add(new ArrayList<Integer>());
        list.get(0).add(new Integer(start));
        int i = 0;
        while (!list.get(i).isEmpty()) {
            list.add(new ArrayList<Integer>());
            for (Integer x : list.get(i)) {
                int parentVertex = x.intValue();
                for (EdgeNode vertices : adjList[parentVertex]) {
                    int foundVertex = vertices.vertex2;
                    if (!visitedNodes[foundVertex]) {
                        visitedNodes[foundVertex] = true;
                        list.get(i + 1).add(new Integer(foundVertex));
                        parent[foundVertex] = parentVertex; //Found child
                        distance[foundVertex] = distance[parentVertex] + vertices.weight;
                    }
                }
            }
            i += 1;
        }
        //Placed at end since finding explored edge is cancerous
        distance[start] = 0;
        parent[start] = -1;
        SPPacket bfs = new SPPacket(start, distance, parent);
        return bfs;
    }

    /********************Dijkstra's Shortest Path Algorithm*** */
    public SPPacket dijkstraShortestPaths(int start) {
        int dist[] = new int[nVertices]; // The output array. dist[i] will hold
        // the shortest distance from src to i
        // path[i] will true if vertex i is included in shortest
        // path tree or shortest distance from src to i is finalized
        Boolean path[] = new Boolean[nVertices];
        int parent[] = new int[nVertices];

        // Initialize all distances as INFINITE and stpSet[] as false
        for (int i = 0; i < nVertices; i++)
        {
            dist[i] = Integer.MAX_VALUE;
            parent[i] = -1;
            path[i] = false;
        }
        dist[start] = 0;

        // Find shortest path for all vertices
        for (int i = 0; i < nVertices-1; i++) {
            int u = minDistance(dist, path);
            path[u] = true;

            // Update dist[v] only if is not in path,  weight of source + edge is smaller than current value of dist[v]
            for (int v = 0; v < adjList[u].size(); v++) {
                if (!path[adjList[u].get(v).vertex2] && dist[u] != Integer.MAX_VALUE &&
                        dist[u] + adjList[u].get(v).weight < dist[adjList[u].get(v).vertex2]) {
                    dist[adjList[u].get(v).vertex2] = dist[u] + adjList[u].get(v).weight;
                    parent[adjList[u].get(v).vertex2] = u;
                }
            }
        }
        SPPacket dijkstra = new SPPacket(start, dist, parent);
        return dijkstra;
    }

    private int minDistance(int dist[], Boolean path[]) {
        int min = Integer.MAX_VALUE, min_index=-1;
        for (int v = 0; v < nVertices; v++) {
            if (path[v] == false && dist[v] <= min) {
                min = dist[v];
                min_index = v;
            }
        }
        return min_index;
    }

    /********************Bellman Ford Shortest Paths ***************/
    public SPPacket bellmanFordShortestPaths(int start) {
        int V = nVertices, E = nEdges;
        int[] parent = new int[nVertices];
        int dist[] = new int[V];

        for (int i = 0; i < V; ++i) {
            dist[i] = Integer.MAX_VALUE;
            parent[i] = -1;
        }
        dist[start] = 0;

        // relax edges, updating dist[v] if source vertex + edge weight < dist[v]
        for (int i = 0; i < V; ++i) {
            for (int j = 0; j < adjList[i].size(); ++j) {
                int u = adjList[i].get(j).vertex1;
                int v = adjList[i].get(j).vertex2;
                int weight = adjList[i].get(j).weight;
                if ((dist[u] != Integer.MAX_VALUE) &&
                        (dist[u] + weight < dist[v])) {
                    dist[v] = dist[u] + weight;
                    parent[v] = u;
                }
            }
        }

        // if dist[v] gets updated after previous relaxation loops, negative cycle is detected
        for (int i = 0; i < V; ++i){
            for (int j = 0; j < adjList[i].size(); ++j) {
                int u = adjList[i].get(j).vertex1;
                int v = adjList[i].get(j).vertex2;
                int weight = adjList[i].get(j).weight;
                if (dist[u] != Integer.MAX_VALUE &&
                        dist[u] + weight < dist[v]) {
                    System.out.println("Graph has negative cycle");
                    return null;
                }
            }
        }

        SPPacket bellman = new SPPacket(start, dist, parent);
        return bellman;
    }


    /***********************Prints shortest paths*************************/
    public void printShortestPaths(SPPacket spp){
        System.out.println("Shortest Paths from vertex " + spp.source + " to vertex");
        for(int i = 0; i < nVertices; i++){
            System.out.print(i + ": " + "[ ");
            int[] traceStack = new int[nEdges];
            int finalVertex = i;
            int counter = 0;
            while(finalVertex != -1){
                traceStack[counter++] = finalVertex;
                finalVertex = spp.parent[finalVertex];
            }

            for(int j = counter - 1; j >= 0; j--)
                System.out.print(traceStack[j] + ", ");
            System.out.print(" ]");
            System.out.println(" Path weight = " + spp.d[i]);
        }
    }

    /*****************isStronglyConnected***************************/
    void DFSUtil(int start,Boolean visited[], ArrayList<EdgeNode> list[]) {
        // Mark the current node as visited and print it
        visited[start] = true;
        int n;

        // Recur for all the vertices adjacent to this vertex
        for (int i = 0; i < list[start].size(); i++) {
            n = list[i].get(i).vertex2;
            if (!visited[n])
                DFSUtil(n, visited, list);
        }
    }

    // Function that returns transpose of this graph
    public void getTranspose() {
        adjListTranspose = new ArrayList[nVertices];
        for (int i = 0; i < nVertices; i++){
            for (int j = 0; j < adjList[j].size(); j++){
                EdgeNode temp = adjList[j].get(j);
                temp.vertex1 = adjList[j].get(j).vertex2;
                temp.vertex2 = adjList[j].get(j).vertex1;
                adjListTranspose[temp.vertex2].add(temp);
            }
        }
    }
    public boolean isStronglyConnected(){
        //run dfs from starting vertex. if all nodes get visited, reverse the graph and run dfs again
        Boolean visited[] = new Boolean[nVertices];
        for (int i = 0; i < nVertices; i++)
            visited[i] = false;

        DFSUtil(0, visited, adjList);

        for (int i = 0; i < nVertices; i++)
            if (visited[i] == false)
                return false;

        getTranspose();

        for (int i = 0; i < nVertices; i++)
            visited[i] = false;

        DFSUtil(0, visited, adjListTranspose);

        for (int i = 0; i < nVertices; i++)
            if (visited[i] == false)
                return false;

        return true;
    }

    class EdgeNode{
        int vertex1;
        int vertex2;
        int weight;

        public EdgeNode(int v1, int v2, int w){
            vertex1 = v1;
            vertex2 = v2;
            weight = w;
        }

        @Override
        public String toString(){
            return "Vertex " + vertex1 + "\nVertex " + vertex2 + "\nWeight: " + weight + "\n";
        }
    }

    class SPPacket{
        int[] d;         //distance array
        int[] parent;    //parent path array
        int source;      //source vertex

        public SPPacket(int start, int[] distance, int[] pp){
            source = start;
            d = distance;
            parent = pp;
        }

        public int[] getDistance(){
            return d;
        }
        public int[] getParent(){
            return parent;
        }
        public int getSource(){
            return source;
        }
        public String toString(){
            String str = "Source vertex: " + source + "\nDistance Array = [ ";
            for(int i = 0; i < d.length; i++)
                str += d[i] + ", ";
            str += " ]\n";

            str += "Parent Array = [ ";
            for(int j = 0; j < parent.length; j++)
                str += parent[j] + ", ";
            str += " ]";
            return str;
        }
    }

    public static void main (String[] args)
    {

        System.out.println("Instructor Testcase D");
        System.out.println("\nBellman Ford Shortest Paths");
        Graph g1 = new Graph("C:\\Users\\Ken\\IdeaProjects\\Graph\\src\\inputSC.txt");
        g1.printGraph();
        int start = 0;

        SPPacket spp = g1.bellmanFordShortestPaths(start);
        if( spp != null)
        {
            System.out.println("\nPrint shortest paths from start vertex  = " + start);
            g1.printShortestPaths( spp );
        }
        else
            System.out.println("\nGraph has a negative cycle");

        if( g1.isStronglyConnected())
            System.out.println( "\nGraph is strongly connected");
        else
            System.out.println( "\nGraph is not strongly connected");

    }  //end main
}