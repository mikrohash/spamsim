package org.iota.spamsim.nodes;

import org.iota.spamsim.Params;
import org.iota.spamsim.network.Transaction;
import org.iota.spamsim.network.TransactionTransfer;

import java.util.LinkedList;
import java.util.List;
import java.util.Set;

public class MaliciousNode extends CfbNode {

    private List<Transaction> tipPool = new LinkedList<>();

    public MaliciousNode() {
        super(Params.maliciousRate.getValue());
    }

    @Override
    public void receiveTransactionTransfer(TransactionTransfer transfer) {
        if(transfer.transaction.issuer == this)
            tipPool.remove(transfer.transaction); // looks like it's already in the network
        super.receiveTransactionTransfer(transfer);
    }

    @Override
    protected boolean acceptTransfer(TransactionTransfer transactionTransfer) {
        return false; // not broadcasting anyone else's transactions so I can broadcast as many of my own as possible
    }

    @Override
    public Set<Transaction> emitNewTransactions(List<Transaction> tipPool) {
        Set<Transaction> emitted = super.emitNewTransactions(tipPool);
        tipPool.addAll(emitted); // only reference own transactions
        return emitted;
    }

    /*
    private boolean oddIteration = false;

    @Override
    public void emitNewTransactions() {
        if(oddIteration) {
            super.emitNewTransactions();
            super.emitNewTransactions();
        }
        oddIteration = !oddIteration;
    }*/
}