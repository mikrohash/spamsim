package org.iota.spamsim;

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
        assert incomingTransfers.size() < 1000;
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

    public static void neighbor(Node a, Node b) {
        if(a == b)
            throw new IllegalArgumentException("Can't neighbor a node to itself.");
        if(a == null || b == null)
            throw new NullPointerException("Nodes must not be null.");
        //if(a.neighbors.contains(b) || b.neighbors.contains(a))
        //    throw new IllegalArgumentException("Node's are already neighbored.");
        a.neighbor(b);
        b.neighbor(a);
    }

    public void neighbor(Node node) {
        neighbors.add(node);
    }

    public void log() {
        System.out.println(id + ": " + incomingTransfers.size() + "/" + outgoingTransfers.size() + "/" + tangle.size());
    }
}
