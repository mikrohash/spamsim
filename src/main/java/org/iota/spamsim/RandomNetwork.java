package org.iota.spamsim;

import java.util.Collection;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

public class RandomNetwork extends Network {

    public RandomNetwork(Collection<Node> nodes) {
        super(nodes);
        connectNodes();
    }

    private void connectNodes() {

        int iterations = 0;

        for (List<Node> poolOfNotNeighboredNodes = new LinkedList<>(nodes); poolOfNotNeighboredNodes.size() > 1; poolOfNotNeighboredNodes.removeIf((Node node) -> node.getAmountOfNeighbors() >= Constants.NEIGHBORS_PER_NODE)) {
            Collections.shuffle(poolOfNotNeighboredNodes);

            Node randomNodeA = pickRandomNode(poolOfNotNeighboredNodes);
            Node randomNodeB = pickRandomNode(poolOfNotNeighboredNodes);

            if(randomNodeA != randomNodeB) {
                randomNodeA.neighbor(randomNodeB);
                randomNodeB.neighbor(randomNodeA);
            }

            if(iterations++ > 1000)
                System.err.println(poolOfNotNeighboredNodes.size());

            for(Node node : nodes)
                assert node.getAmountOfNeighbors() >= Constants.NEIGHBORS_PER_NODE || poolOfNotNeighboredNodes.contains(node);
        }
    }

    private static Node pickRandomNode(List<Node> nodes) {
        return nodes.get((int)(Math.random() * nodes.size()));
    }
}
