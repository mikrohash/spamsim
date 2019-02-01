package org.iota.spamsim;

public class TransactionTransfer {
    final Node sender;
    final Transaction transaction;

    TransactionTransfer(Node sender, Transaction transaction) {
        this.sender = sender;
        this.transaction = transaction;
    }
}