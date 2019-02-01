package org.iota.spamsim.network;

import org.iota.spamsim.nodes.Node;

public class Transaction {

    public final Node issuer;

    public Transaction(Node issuer) {
        this.issuer = issuer;
    }
}
