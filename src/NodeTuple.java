/**
 * @file   NodeTuple.java
 * @brief  This is record class used in the Graph class for implementing the 
 *         Dijkstra's shortest path algorithm. It stores current node id, 
 *         some previous node id and the total cost on the path from the 
 *         source node to the current node.
 * 
 * @see    Graph
 * @author Xin Cai
 */
record NodeTuple(int curr, int prev, int cost) implements Comparable<NodeTuple> {
    
    /**
     * Construct a new NodeTuple.
     * 
     * @param curr Id of current node
     * @param prev Id of previous node
     * @param cost Cost on edge between these two nodes
     */
    public NodeTuple {
        if (cost < 0)
            throw new IllegalArgumentException("Cost cannot be negative.");
    }

    /**
     * Compares this NodeTuple with another based on the cost.
     *
     * @param other The other NodeTuple to compare with.
     * @return int value as result of comparison.
     */
    @Override
    public int compareTo(NodeTuple other) {
        return Integer.compare(this.cost, other.cost);
    }
}