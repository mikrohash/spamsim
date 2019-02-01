package org.iota.spamsim;

import java.util.HashSet;
import java.util.Set;

public class Main {

    public static void main(String[] args) {

        double honestScore = 0, maliciousSocre = 0;

        for(int sim = 0; sim < Constants.SIMULATIONS; sim++) {

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

            for(Node honest : honestNodes)
                honestScore += network.measureAverageAmountOfDeliveredTransactions(honest) / Constants.ITERATIONS / Constants.HONEST_RATE / honestNodes.size();
            for(Node malicious : maliciousNodes)
                maliciousSocre += network.measureAverageAmountOfDeliveredTransactions(malicious) / Constants.ITERATIONS / Constants.MALICIOUS_RATE / maliciousNodes.size();
        }

        honestScore /= Constants.SIMULATIONS;
        maliciousSocre /= Constants.SIMULATIONS;

        System.out.println("HONEST:    " + honestScore);
        System.out.println("MALICIOUS: " + maliciousSocre);
        System.out.println("SCORE:     " + (honestScore / maliciousSocre));
    }
}
