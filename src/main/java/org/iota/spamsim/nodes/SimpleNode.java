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

        List<Transaction> tipPool = new LinkedList<>();
        for(TransactionTransfer transactionTransfer : incomingTransfers)
            tipPool.add(transactionTransfer.transaction);
        emitNewTransactions(tipPool);

        for(Node nb : neighbors)
            for(TransactionTransfer outgoing : outgoingTransfers)
                if(outgoing.sender != nb) {
                    nb.receiveTransactionTransfer(new TransactionTransfer(this, outgoing.transaction));
                }

        outgoingTransfers = new HashSet<>();
    }

    public Set<Transaction> emitNewTransactions(List<Transaction>  tipPool) {
        Set<Transaction> emitted = new HashSet<>();
        for(int i = 0; i < emissionRate; i++) {
            Transaction randomBranch = pickRandomTransaction(tipPool);
            Transaction randomTrunk = pickRandomTransaction(tipPool);
            Transaction transaction = new Transaction(this, randomBranch, randomTrunk);
            emitted.add(transaction);
            outgoingTransfers.add(new TransactionTransfer(this, transaction));
        }
        return emitted;
    }

    private static Transaction pickRandomTransaction(List<Transaction> pool) {
        return pool.size() == 0 ? null : pool.get((int)(Math.random()*pool.size()));
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
                requestTransactionIfNotInTangle(incoming.transaction.branch);
                requestTransactionIfNotInTangle(incoming.transaction.trunk);
            }
        }
        incomingTransfers = new HashSet<>();
    }

    private void requestTransactionIfNotInTangle(Transaction transaction) {
        if(!tangle.contains(transaction) && transaction != null && anyNeighborKnowsTransaction(transaction))
            tangle.add(transaction);
    }

    private boolean anyNeighborKnowsTransaction(Transaction transaction) {
        for(Node neighbor : neighbors)
            if(neighbor.knowsTransact(transaction))
                return true;
        return false;
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

    @Override
    public boolean knowsTransact(Transaction transaction) {
        return tangle.contains(transaction);
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
