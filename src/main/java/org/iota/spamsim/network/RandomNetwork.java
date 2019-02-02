package org.iota.spamsim.network;

import org.iota.spamsim.Params;
import org.iota.spamsim.nodes.Node;

import java.util.*;

public class RandomNetwork extends Network {

    public RandomNetwork(Collection<Node> nodes) {
        super(nodes);
        connectNodes();
    }

    private void connectNodes() {

        int iterations = 0;

        for (List<Node> poolOfNotNeighboredNodes = new LinkedList<>(nodes); poolOfNotNeighboredNodes.size() > 0; poolOfNotNeighboredNodes.removeIf((Node node) -> node.getAmountOfNeighbors() >= Params.neighborsPerNode.getValue())) {

            if(iterations++ > 50000)
                throw new RuntimeException("connecting nodes has been taking unexpectedly long");

            if(nodes.size()%2 == 1 && poolOfNotNeighboredNodes.size() == 1 && poolOfNotNeighboredNodes.get(0).getAmountOfNeighbors() == Params.neighborsPerNode.getValue()-1)
                break; // can't do anything about this unfortunately

            while (!areMoreConnectionsPossible(poolOfNotNeighboredNodes)) {
                if(nodes.size() <= Params.neighborsPerNode.getValue())
                    return; // can't do anything about this unfortunately
                breakUpExistingNeighborship(poolOfNotNeighboredNodes);
            }

            pairTwoRandomNodes(poolOfNotNeighboredNodes);
        }
    }

    private static void pairTwoRandomNodes(List<Node> nodes) {
        Node randomNodeA = pickRandomNode(nodes);
        Node randomNodeB = pickRandomNode(nodes);

        if(randomNodeA != randomNodeB && !randomNodeA.isNeighboredTo(randomNodeB)) {
            randomNodeA.neighbor(randomNodeB);
            randomNodeB.neighbor(randomNodeA);
        }
    }

    private static boolean areMoreConnectionsPossible(List<Node> poolOfNotNeighboredNodes) {
        if(poolOfNotNeighboredNodes.size() > Params.neighborsPerNode.getValue())
            return true;
        for(Node node : poolOfNotNeighboredNodes) {
            List<Node> nodeAndNeighbors = node.getNeighbors();
            nodeAndNeighbors.add(node);
            if(nodeAndNeighbors.containsAll(poolOfNotNeighboredNodes))
                return false;
        }
        return true;
    }

    private void breakUpExistingNeighborship(List<Node> poolOfNotNeighboredNodes) {
        List<Node> nodesAsList = new LinkedList<>(nodes);
        Node nodeWithNeighbor;
        do
            nodeWithNeighbor = pickRandomNode(nodesAsList);
        while (nodeWithNeighbor.getAmountOfNeighbors() == 0);
        Node randomNeighbor = pickRandomNode(nodeWithNeighbor.getNeighbors());

        nodeWithNeighbor.unneighbor(randomNeighbor);
        randomNeighbor.unneighbor(nodeWithNeighbor);

        if(!poolOfNotNeighboredNodes.contains(nodeWithNeighbor))
            poolOfNotNeighboredNodes.add(nodeWithNeighbor);
        if(!poolOfNotNeighboredNodes.contains(randomNeighbor))
            poolOfNotNeighboredNodes.add(randomNeighbor);
    }

    private static Node pickRandomNode(List<Node> nodes) {
        return nodes.get((int)(Math.random() * nodes.size()));
    }
}
