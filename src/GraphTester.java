
public class GraphTester {
    public static void main(String...args){
        Graph graph = new Graph("C:\\Users\\Ken\\IdeaProjects\\Graph\\src\\sample.txt");
        graph.printGraph();
        Graph.SPPacket bfs = graph.bfsShortestPaths(1);
        System.out.println("BFS Shortest Path");
        System.out.println("Source: " + bfs.source);

        System.out.print("Parent Array: " + "[ ");
        for(int i = 0; i < bfs.parent.length; i++)
            System.out.print(bfs.parent[i] + ", ");
        System.out.println("\b\b ]");

        System.out.print("Distance Array: " + "[ ");
        for(int j = 0; j < bfs.d.length; j++)
            System.out.print(bfs.d[j] + ", ");
        System.out.println("\b\b ]\n");

        graph.printShortestPaths(bfs);
        System.out.println();


        Graph.SPPacket bellmanFord = graph.bellmanFordShortestPaths(1);
        System.out.println("Bellman Ford Shortest Path");
        System.out.println("Source: " + bellmanFord.source);

        System.out.print("Parent Array: " + "[ ");
        for(int i = 0; i < bellmanFord.parent.length; i++)
            System.out.print(bellmanFord.parent[i] + ", ");
        System.out.println("\b\b ]");

        System.out.print("Distance Array: " + "[ ");
        for(int j = 0; j < bellmanFord.d.length; j++)
            System.out.print(bellmanFord.d[j] + ", ");
        System.out.println("\b\b ]\n");

        graph.printShortestPaths(bellmanFord);
        System.out.println();


        Graph.SPPacket djikstra = graph.dijkstraShortestPaths(1);
        System.out.println("Djikstra's Shortest Path");
        System.out.println("Source: " + djikstra.source);

        System.out.print("Parent Array: " + "[ ");
        for(int i = 0; i < djikstra.parent.length; i++)
            System.out.print(djikstra.parent[i] + ", ");
        System.out.println("\b\b ]");

        System.out.print("Distance Array: " + "[ ");
        for(int j = 0; j < djikstra.d.length; j++)
            System.out.print(djikstra.d[j] + ", ");
        System.out.println("\b\b ]\n");
        graph.printShortestPaths(djikstra);

    }
}
