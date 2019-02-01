package org.iota.spamsim;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

public abstract class Network {

    private int iteration = 0;
    protected final Set<Node> nodes;

    public Network(Collection<Node> nodes) {
        this.nodes = new HashSet<>(nodes);
    }

    public void iterate() {
        iteration++;
        for(Node node : nodes)
            node.acceptTransactionsFromNeighbors();
        for(Node node : nodes)
            node.submitTransactionsToNeighbors();
    }

    public void log() {
        System.out.println("=== ITERATION " + iteration + " ===");
        for(Node node : nodes)
            node.log();
    }

    public double measureAverageAmountOfDeliveredTransactions(Node node) {
        if(!nodes.contains(node))
            throw new IllegalArgumentException("Node not part of network.");

        int transactionsDelivered = 0;

        for(Node otherNode : nodes)
            if(otherNode != node)
                transactionsDelivered += otherNode.transactionsInTangleIssuedBy(node);

        return transactionsDelivered * 1.0 / (nodes.size()-1);
    }
}
