package org.iota.spamsim;

public interface Node {

    void receiveTransactionTransfer(TransactionTransfer transfer);

    void submitTransactionsToNeighbors();

    void acceptTransactionsFromNeighbors();

    int transactionsInTangleIssuedBy(Node issuer);

    int getAmountOfNeighbors();

    void log();

    void neighbor(Node node);
}
