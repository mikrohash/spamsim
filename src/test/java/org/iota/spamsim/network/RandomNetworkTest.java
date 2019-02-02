package org.iota.spamsim.network;

import org.iota.spamsim.Params;
import org.iota.spamsim.nodes.Node;
import org.iota.spamsim.nodes.SimpleNode;
import org.junit.Assert;
import org.junit.Test;

import java.util.HashSet;
import java.util.Set;

public class RandomNetworkTest {

    @Test
    public void testTopology() {
        for(int i = 0; i < 100; i++) {
            int amountOfNodes = (int)(Math.random() * 20) + Params.neighborsPerNode.getValue() + 1;
            System.out.print("testing random network topology for " + amountOfNodes + " nodes ... ");
            Set<Node> nodes = createNodeSetOfSize(amountOfNodes);
            new RandomNetwork(nodes);

            boolean oneNodeMightHaveLessNeighbors = amountOfNodes%2 == 1;

            for(Node node : nodes) {
                if(oneNodeMightHaveLessNeighbors && node.getAmountOfNeighbors() == Params.neighborsPerNode.getValue()-1) {
                    oneNodeMightHaveLessNeighbors = false;
                    continue;
                }
                Assert.assertEquals("Node does not have required amount of neighbors.", Params.neighborsPerNode.getValue(), node.getAmountOfNeighbors());
            }
             System.out.print("success\n");
        }
    }

    private static Set<Node> createNodeSetOfSize(int size) {
        Set<Node> nodes = new HashSet<>();
        for(int i = 0; i < size; i++)
            nodes.add(new SimpleNode(0));
        return nodes;
    }
}