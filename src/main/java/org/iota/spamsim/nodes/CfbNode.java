package org.iota.spamsim.nodes;

import org.iota.spamsim.network.TransactionTransfer;

import java.util.*;

public class CfbNode extends SimpleNode {

    private Set<Node> ignoredNeighbors = new HashSet<>();

    public CfbNode(int emissionRate) {
        super(emissionRate);
    }

    @Override
    public void acceptTransactionsFromNeighbors() {
        int[] neighborsTransferCounts = new int[neighbors.size()];
        for(TransactionTransfer incoming : incomingTransfers)
            neighborsTransferCounts[neighbors.indexOf(incoming.sender)]++;

        LinkedList<Node> neighborsOrderedByTransferCount = new LinkedList<>(neighbors);
        Collections.shuffle(neighborsOrderedByTransferCount);
        neighborsOrderedByTransferCount.sort((a, b) -> Integer.compare(neighborsTransferCounts[neighbors.indexOf(b)], neighborsTransferCounts[neighbors.indexOf(a)]));
        assert neighborsOrderedByTransferCount.size() == 0 || neighborsTransferCounts[neighbors.indexOf(neighborsOrderedByTransferCount.getFirst())] >= neighborsTransferCounts[neighbors.indexOf((neighborsOrderedByTransferCount).getLast())];
        double median = neighborsTransferCounts[neighbors.indexOf(neighborsOrderedByTransferCount.get(neighborsOrderedByTransferCount.size()/2))];

        ignoredNeighbors = new HashSet<>();

        for(Node neighbor : neighbors)
            if(neighborsTransferCounts[neighbors.indexOf(neighbor)] > median * 1.33)
                ignoredNeighbors.add(neighbor);

        super.acceptTransactionsFromNeighbors();
    }

    @Override
    protected boolean acceptTransfer(TransactionTransfer transactionTransfer) {
        return !ignoredNeighbors.contains(transactionTransfer.sender);
    }
}
