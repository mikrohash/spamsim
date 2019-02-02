package org.iota.spamsim.nodes;

import org.iota.spamsim.network.Transaction;
import org.iota.spamsim.network.TransactionTransfer;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

public class SimpleNode implements Node {

    public static int idCounter = 0;

    protected final int id = idCounter++;
    protected final int emissionRate;
    protected Set<TransactionTransfer> incomingTransfers = new HashSet<>();
    protected Set<TransactionTransfer> outgoingTransfers = new HashSet<>();
    protected Set<Transaction> tangle = new HashSet<>();
    protected List<Node> neighbors = new LinkedList<>();

    public SimpleNode(int emissionRate) {
        this.emissionRate = emissionRate;
    }

    public void submitTransactionsToNeighbors() {

        emitNewTransactions();

        for(Node nb : neighbors)
            for(TransactionTransfer outgoing : outgoingTransfers)
                if(outgoing.sender != nb)
                    nb.receiveTransactionTransfer(new TransactionTransfer(this, outgoing.transaction));

        outgoingTransfers = new HashSet<>();
    }

    public void emitNewTransactions() {
        for(int i = 0; i < emissionRate; i++)
            outgoingTransfers.add(new TransactionTransfer(this, new Transaction(this)));
    }

    @Override
    public void receiveTransactionTransfer(TransactionTransfer transfer) {
        if(!tangle.contains(transfer.transaction))
            incomingTransfers.add(transfer);
    }

    public void acceptTransactionsFromNeighbors() {
        for(TransactionTransfer incoming : incomingTransfers) {
            if(acceptTransfer(incoming)) {
                outgoingTransfers.add(incoming);
                tangle.add(incoming.transaction);
            }
        }
        incomingTransfers = new HashSet<>();
    }

    protected boolean acceptTransfer(TransactionTransfer transactionTransfer) {
        return true;
    }

    public int transactionsInTangleIssuedBy(Node issuer) {
        int amount = 0;
        for(Transaction transaction : tangle)
            if(transaction.issuer == issuer)
                amount++;
        return amount;
    }

    public int getAmountOfNeighbors() {
        return neighbors.size();
    }

    @Override
    public boolean isNeighboredTo(Node node) {
        return neighbors.contains(node);
    }

    public void neighbor(Node node) {
        if(node == this)
            throw new IllegalArgumentException("Node cannot be neighbored with itself.");
        if(isNeighboredTo(node))
            throw new IllegalArgumentException("Node is already neighbored to this node.");
        neighbors.add(node);
    }

    @Override
    public void unneighbor(Node node) {
        if(!neighbors.remove(node))
            throw new IllegalArgumentException("Node is not neighbored to this node.");
    }

    @Override
    public List<Node> getNeighbors() {
        return new LinkedList<>(neighbors);
    }

    @Override
    public int getID() {
        return id;
    }
}
