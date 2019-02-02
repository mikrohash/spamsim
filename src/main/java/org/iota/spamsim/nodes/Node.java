package org.iota.spamsim.nodes;

import org.iota.spamsim.network.TransactionTransfer;

import java.util.List;

public interface Node {

    void receiveTransactionTransfer(TransactionTransfer transfer);

    void submitTransactionsToNeighbors();

    void acceptTransactionsFromNeighbors();

    int transactionsInTangleIssuedBy(Node issuer);

    int getAmountOfNeighbors();

    void neighbor(Node node);

    void unneighbor(Node node);

    List<Node> getNeighbors();

    int getID();

    boolean isNeighboredTo(Node node);
}
