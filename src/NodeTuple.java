/**
 * @file   NodeTuple.java
 * @brief  This is a lightweight helper class used in the Graph class for
 *         implementing the Dijkstra's shortest path algorithm.
 * @see    Graph
 * @author Xin Cai
 */
class NodeTuple implements Comparable<NodeTuple> {
    int curr;    // Id of current node
    int prev;    // Id of previous node
    int cost;    // Cost on edge between these two nodes

    /**
     * Construct a new NodeTuple.
     * 
     * @param curr Id of current node
     * @param prev Id of previous node
     * @param cost Cost on edge between these two nodes
     */
    public NodeTuple(int curr, int prev, int cost) {
        if (cost < 0) {
            throw new IllegalArgumentException("Cost cannot be negative.");
        }
        this.curr = curr;
        this.prev = prev;
        this.cost = cost;
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