package org.iota.spamsim.simulation;

import org.iota.spamsim.Params;
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
            for (int j = 0; j < Params.honestNodes.getValue(); j++)
                honestNodes.add(new CfbNode(Params.honestRate.getValue()));

            Set<Node> maliciousNodes = new HashSet<>();
            for (int j = 0; j < Params.maliciousNodes.getValue(); j++)
                maliciousNodes.add(new MaliciousNode());

            Set<Node> allNodes = new HashSet<>();
            allNodes.addAll(honestNodes);
            allNodes.addAll(maliciousNodes);
            Network network = new RandomNetwork(allNodes);

            for (int it = 0; it < Params.iterations.getValue(); it++)
                network.iterate();

            double phs = 0, pms = 0;
            for(Node honest : honestNodes)
                phs += network.measureAverageAmountOfDeliveredTransactions(honest) / Params.iterations.getValue() / Params.honestRate.getValue() / honestNodes.size();
            for(Node malicious : maliciousNodes)
                pms += network.measureAverageAmountOfDeliveredTransactions(malicious) / Params.iterations.getValue() / Params.maliciousRate.getValue() / maliciousNodes.size();
            result.addRound(phs, pms);
        }

        simulation.addResult(result);
    }
}
