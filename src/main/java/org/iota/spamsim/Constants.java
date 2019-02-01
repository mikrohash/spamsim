package org.iota.spamsim;

public class Constants {

    // amount of times to run the entire simulation, to gather a weighted average
    public static final int SIMULATIONS = 500;

    // each simulation consists of that many iterations during which nodes exchange transactions
    public static final int ITERATIONS = 40;

    // the amount of neighbors each node should ideally have (not guaranteed, a single node might have less)
    public static final int NEIGHBORS_PER_NODE = 3;

    // total amount of honest participants in the network and their rate of issued transactions per iteration
    public static final int HONEST_NODES = 15;
    public static final int HONEST_RATE = 1;

    // total amount of malicious participants in the network and their rate of issued transactions per iteration
    public static final int MALICIOUS_NODES = 1;
    public static final int MALICIOUS_RATE = 10;
}
