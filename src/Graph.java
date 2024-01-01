/**
 * @file   Graph.java
 * @brief  This class represents a directed graph and provides methods for
 *         graph manipulation and Dijkstra's shortest path algorithm.
 *
 * @see    NodeTuple
 * @author Xin Cai
 */
import java.util.HashMap;
import java.util.ArrayList;
import java.util.PriorityQueue;


public class Graph {
    /* An adjacency list for representing the graph */
    private HashMap<Integer, HashMap<Integer, Integer>> nodeNeibsMap;
    
    /** default constructor */
    public Graph() {
        this.nodeNeibsMap = new HashMap<>();
    }

    /**
     * Construct the graph from given info.
     * 
     * @param graphData nodes and edges info of the graph.
     */
    public Graph(HashMap<Integer, HashMap<Integer, Integer>> graphData) {
        this.nodeNeibsMap = new HashMap<>(graphData);
    }

    /**
     * This function implements the Dijkstra's shortest path algorithm.
     * 
     * @param sid Id of the source node.
     * @param tid Id of the target node.
     * @return shortest path between these nodes.
     */
    public ArrayList<Integer> getShortestPath(int sid, int tid) {
        // data structures for Dijkstra's shortest path
        PriorityQueue<NodeTuple> costQueue = new PriorityQueue<>();
        HashMap<Integer, Integer> result = new HashMap<>();
        
        // init with target node
        costQueue.add(new NodeTuple(tid, -1, 0));

        // run Dijkstra's algorithm
        while (!costQueue.isEmpty()) {
            // get the node with minimum cost
            NodeTuple node = costQueue.poll();
            
            // duplication check
            if (result.containsKey(node.curr()))
                continue;

            result.put(node.curr(), node.prev());
            
            // termination check
            if (node.curr() == sid)
                break;
            
            // organize next level of nodes
            this.nodeNeibsMap.get(node.curr()).forEach((neib, cost) -> {
                if (!result.containsKey(neib)) {
                    costQueue.add(
                        new NodeTuple(neib, node.curr(), node.cost() + cost));
                }
            });
        }

        // rebuild shortest path
        ArrayList<Integer> path = new ArrayList<>();
        int nid = sid;
        
        while(nid != tid) {
            path.add(nid);
            nid = result.get(nid);
        }
        path.add(nid);

        return path;
    }

    /**
     * Check if a node id is in this graph.
     * 
     * @param nid Node id for checking.
     * @return True if this graph contains the given node id, otherwise, false.
     */
    public boolean hasNode(int nid) {
        return this.nodeNeibsMap.containsKey(nid);
    }

    /**
     * Check if a given edge (u,v) is in this graph.
     * 
     * @param u One node on an edge.
     * @param v The other node on the edge.
     * @return True if this graph contains that edge, otherwise, false.
     */
    public boolean hasEdge(int u, int v) {
        return this.nodeNeibsMap.containsKey(u) &&
               this.nodeNeibsMap.get(u).containsKey(v);
    }

    /**
     * Add a node in the graph.
     * 
     * @param nid
     * @return True if adding a new node, otherwise, false.
     */
    public boolean addNode(int nid) {
        if (this.nodeNeibsMap.containsKey(nid))
            return false;
        
        this.nodeNeibsMap.put(nid, new HashMap<>());
        return true;
    }

    /**
     * Remove a node from the graph.
     * 
     * @param nid node Id to be removed.
     * @return True if removed the node, otherwise, false.
     */
    public boolean removeNode(int nid) {
        if (!this.hasNode(nid)) {
            return false;
        }
        this.nodeNeibsMap.remove(nid);

        for (Integer key : this.nodeNeibsMap.keySet()) {
            this.nodeNeibsMap.get(key).remove(nid);
        }
        return true;
    }

    /**
     * Add an edge (u,v) to the graph.
     * 
     * @param u One node on an edge.
     * @param v Another node of the edge.
     * @param cost Cost of the edge.
     */
    public void addEdge(int u, int v, int cost) {
        this.nodeNeibsMap.computeIfAbsent(u, HashMap::new).put(v, cost);
    }

    /**
     * Add edge (u,v) and (v,u) to the graph with equal cost.
     * 
     * @param u One node on an edge.
     * @param v Another node of the edge.
     * @param cost Cost of the edge.
     */
    public void addBiEdge(int u, int v, int cost) {
        this.addEdge(u, v, cost);
        this.addEdge(v, u, cost);
    }

    /**
     * Remove an edge (u,v) from the graph.
     * 
     * @param u One node of the edge.
     * @param v Another node of the edge.
     */
    public void removeEdge(int u, int v) {
        if (this.nodeNeibsMap.containsKey(u))
            this.nodeNeibsMap.get(u).remove(v);
    }

    /**
     * REmove edges (u,v) and (v,u) from the graph.
     * 
     * @param u One node of the edge.
     * @param v Another node of the edge.
     */
    public void removeBiEdge(int u, int v) {
        this.removeEdge(u, v);
        this.removeEdge(v, u);
    }

    /**
     * Display the info of the graph to console.
     */
    @Override
    public String toString() {
        StringBuilder graphStruct = new StringBuilder();

        for (Integer node : this.nodeNeibsMap.keySet()) {
            HashMap<Integer, Integer> neibList = this.nodeNeibsMap.get(node);

            graphStruct.append(node).append(": ");

            neibList.forEach((neib, cost) -> {
                graphStruct.append("(")
                    .append(neib)
                    .append(",")
                    .append(cost)
                    .append(") ");
            });
            graphStruct.append("\n");
        }
        return graphStruct.toString();
    }
}
