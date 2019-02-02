package org.iota.spamsim.nodes;

import org.iota.spamsim.network.TransactionTransfer;

import java.util.*;

public class CfbNode extends SimpleNode {

    private Set<Node> ignoredNeighbors = new HashSet<>();

    public CfbNode(int emissionRate) {
        super(emissionRate);
    }

    @Override
    public void submitTransactionsToNeighbors() {
        super.submitTransactionsToNeighbors();

        int[] neighborsTransferCounts = new int[neighbors.size()];
        for(TransactionTransfer incoming : incomingTransfers)
            neighborsTransferCounts[neighbors.indexOf(incoming.sender)]++;

        LinkedList<Node> neighborsOrderedByTransferCount = new LinkedList<>(neighbors);
        Collections.shuffle(neighborsOrderedByTransferCount);
        neighborsOrderedByTransferCount.sort((a, b) -> Integer.compare(neighborsTransferCounts[neighbors.indexOf(b)], neighborsTransferCounts[neighbors.indexOf(a)]));
        assert neighborsOrderedByTransferCount.size() == 0 || neighborsTransferCounts[neighbors.indexOf(neighborsOrderedByTransferCount.getFirst())] >= neighborsTransferCounts[neighbors.indexOf((neighborsOrderedByTransferCount).getLast())];
        ignoredNeighbors = new HashSet<>(neighborsOrderedByTransferCount.subList(0, (int)Math.ceil(neighborsOrderedByTransferCount.size() / 3.0)));
    }

    @Override
    protected boolean acceptTransfer(TransactionTransfer transactionTransfer) {
        return !ignoredNeighbors.contains(transactionTransfer.sender);
    }
}
