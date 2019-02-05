package org.iota.spamsim.network;

import org.iota.spamsim.nodes.Node;

public class Transaction {

    public final Transaction branch, trunk;
    public final Node issuer;

    public Transaction(Node issuer, Transaction branch, Transaction trunk) {
        this.issuer = issuer;
        this.branch = branch;
        this.trunk = trunk;
    }
}
