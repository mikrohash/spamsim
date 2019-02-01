package org.iota.spamsim.simulation;

import org.iota.spamsim.Constants;

import java.util.HashSet;
import java.util.Set;

public class Simulation implements Runnable {

    private int simulationsLeft = Constants.SIMULATIONS;
    private Set<SimResult> results = new HashSet<>();

    public synchronized boolean keepSimulating() {
        return simulationsLeft-- > 0;
    }

    public synchronized void addResult(SimResult result) {
        results.add(result);
        if(results.size() == Constants.THREADS)
            synchronized (results) { results.notify(); }
    }

    @Override
    public void run() {
        for(int i = 0; i < Constants.THREADS; i++)
            new SimWorker(this).start();

        long start = System.currentTimeMillis();
        waitForSubworkers();
        long duration = System.currentTimeMillis() - start;

        SimResult combinedResults = new SimResult(results);
        logResult(combinedResults, duration);
    }

    private static void logResult(SimResult result, long duration) {
        System.out.println("DURATION:  " + duration + "ms");
        System.out.println("ROUNDS:    " + result.getRounds());
        System.out.println("HONEST:    " + result.getHonestScore());
        System.out.println("MALICIOUS: " + result.getMaliciousScore());
        System.out.println("SCORE:     " + (result.getHonestScore() / result.getMaliciousScore()));
    }

    private void waitForSubworkers() {
        try {
            synchronized (results) { results.wait(); }
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}
