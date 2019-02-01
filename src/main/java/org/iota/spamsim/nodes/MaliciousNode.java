package org.iota.spamsim.nodes;

import org.iota.spamsim.Constants;
import org.iota.spamsim.network.Transaction;
import org.iota.spamsim.network.TransactionTransfer;

public class MaliciousNode extends CfbNode {

    public MaliciousNode() {
        super(Constants.MALICIOUS_RATE);
    }

    @Override
    protected boolean acceptTransfer(TransactionTransfer transactionTransfer) {
        return false; // not broadcasting anyone else's transactions so I can broadcast as many of my own as possible
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