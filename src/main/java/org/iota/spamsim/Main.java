package org.iota.spamsim;

import org.iota.spamsim.simulation.SimResult;
import org.iota.spamsim.simulation.Simulation;

public class Main {

    public static void main(String[] args) {
        runAllSimulations();
    }

    private static void runAllSimulations() {
        for(Params par : Params.values()) {

            switch (par) {
                case threads: // influences performance but not score
                case simulations: // influences score accuracy but not score directly
                case iterations: // influences performance and score accuracy but not score directly
                    continue; // skip execution related parameters
            }

            System.out.println("***** " + par.name() + " *****");

            int lastValue = -1;

            int maxTest = 29;
            int medianTest = 14;

            for(int i = 0; i < maxTest; i++) {

                if(i == 4 && par != Params.honestNodes)
                    continue; // do run default setting only once

                Params.reset();
                double factor = Math.pow(2, (i-medianTest)*0.2);
                int newValue = (int)Math.round(factor * par.getValue());
                if(lastValue == newValue)
                    continue; // already tested that value
                lastValue = newValue;
                try {
                    par.setValue(newValue);
                } catch (IllegalArgumentException e) {
                    continue; // invalid value: too small or too large
                }

                long begin = System.currentTimeMillis();
                SimResult result = Simulation.run();
                logResult(par, result, System.currentTimeMillis() - begin);
            }

            System.out.println();
        }
    }

    private static void logResult(Params par, SimResult result, long duration) {
        //System.out.println("***** " + par.name() + " = "+par.getValue()+" *****");
        //System.out.println("DURATION:  " + duration + "ms");
        //System.out.println("SCORE:     " + (result.getHonestScore() / result.getMaliciousScore()));
        //System.out.println();
        System.out.println(par.getValue()+" "+result.getHonestScore()+" "+result.getMaliciousScore());
    }
}
