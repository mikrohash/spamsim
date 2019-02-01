package org.iota.spamsim.simulation;

import org.iota.spamsim.Constants;
import org.iota.spamsim.network.Network;
import org.iota.spamsim.network.RandomNetwork;
import org.iota.spamsim.nodes.CfbNode;
import org.iota.spamsim.nodes.MaliciousNode;
import org.iota.spamsim.nodes.Node;

import java.util.HashSet;
import java.util.Set;

public class SimWorker extends Thread {

    private final Simulation simulation;

    public SimWorker(Simulation simulation) {
        this.simulation = simulation;
        setPriority(MAX_PRIORITY);
    }

    @Override
    public void run() {

        SimResult result = new SimResult();

        while (simulation.keepSimulating()) {

            Set<Node> honestNodes = new HashSet<>();
            for (int j = 0; j < Constants.HONEST_NODES; j++)
                honestNodes.add(new CfbNode(Constants.HONEST_RATE));

            Set<Node> maliciousNodes = new HashSet<>();
            for (int j = 0; j < Constants.MALICIOUS_NODES; j++)
                maliciousNodes.add(new MaliciousNode());

            Set<Node> allNodes = new HashSet<>();
            allNodes.addAll(honestNodes);
            allNodes.addAll(maliciousNodes);
            Network network = new RandomNetwork(allNodes);

            for (int it = 0; it < Constants.ITERATIONS; it++)
                network.iterate();

            double phs = 0, pms = 0;
            for(Node honest : honestNodes)
                phs += network.measureAverageAmountOfDeliveredTransactions(honest) / Constants.ITERATIONS / Constants.HONEST_RATE / honestNodes.size();
            for(Node malicious : maliciousNodes)
                pms += network.measureAverageAmountOfDeliveredTransactions(malicious) / Constants.ITERATIONS / Constants.MALICIOUS_RATE / maliciousNodes.size();
            result.addRound(phs, pms);
        }

        simulation.addResult(result);
    }
}
