package org.iota.spamsim.nodes;

import org.iota.spamsim.network.Transaction;
import org.iota.spamsim.network.TransactionTransfer;

import java.util.List;

public interface Node {

    void receiveTransactionTransfer(TransactionTransfer transfer);

    void submitTransactionsToNeighbors();

    void acceptTransactionsFromNeighbors();

    boolean knowsTransact(Transaction transaction);

    int transactionsInTangleIssuedBy(Node issuer);

    int getAmountOfNeighbors();

    void neighbor(Node node);

    void unneighbor(Node node);

    List<Node> getNeighbors();

    int getID();

    boolean isNeighboredTo(Node node);
}
