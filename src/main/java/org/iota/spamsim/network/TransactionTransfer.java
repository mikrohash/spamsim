package org.iota.spamsim.network;

import org.iota.spamsim.nodes.Node;

public class TransactionTransfer {
    public final Node sender;
    public final Transaction transaction;

    public TransactionTransfer(Node sender, Transaction transaction) {
        this.sender = sender;
        this.transaction = transaction;
    }
}