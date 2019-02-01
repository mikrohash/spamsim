package org.iota.spamsim.simulation;

import org.iota.spamsim.Params;

import java.util.HashSet;
import java.util.Set;

public class Simulation {

    private int simulationsLeft = Params.simulations.getValue();
    private Set<SimResult> results = new HashSet<>();

    private Simulation() {

    }

    synchronized boolean keepSimulating() {
        return simulationsLeft-- > 0;
    }

    synchronized void addResult(SimResult result) {
        results.add(result);
        if(results.size() == Params.threads.getValue())
            synchronized (results) { results.notify(); }
    }

    public static SimResult run() {
        Simulation simulation = new Simulation();
        for(int i = 0; i < Params.threads.getValue(); i++)
            new SimWorker(simulation).start();
        simulation.waitForSubworkers();
        return new SimResult(simulation.results);
    }

    private void waitForSubworkers() {
        try {
            synchronized (results) { results.wait(); }
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}
