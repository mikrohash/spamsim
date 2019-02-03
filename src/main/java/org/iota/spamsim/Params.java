package org.iota.spamsim;

public enum Params {
        // amount of worker threads to distribute the simulation on
        threads(6, 1, 8),

        // amount of times to run the entire simulation, to gather a weighted average
        simulations(1000),

        // each simulation consists of that many iterations during which nodes exchange transactions
        iterations(40),

        // the amount of neighbors each node should ideally have (not guaranteed, a single node might have less)
        neighborsPerNode(3, 2, 6),

        // total amount of honest participants in the network and their rate of issued transactions per iteration
        honestNodes(15),
        honestRate(1),

        // total amount of malicious participants in the network and their rate of issued transactions per iteration
        maliciousNodes(1),
        maliciousRate(10);

        private final int defaultValue;
        private final int min, max;
        private int value;

        Params(int defaultValue) {
                this.defaultValue = defaultValue;
                this.value = defaultValue;
                this.min = 0;
                this.max = Integer.MAX_VALUE;
        }

        Params(int defaultValue, int min, int max) {
                this.defaultValue = defaultValue;
                this.value = defaultValue;
                this.min = min;
                this.max = max;
        }

        public static void reset() {
            for(Params param : Params.values())
                param.value = param.defaultValue;
        }

        public void setValue(int value) {
            if(value < min || value > max)
                throw new IllegalArgumentException("value '"+value+"' not in allowed interval ["+min+","+max+"]");
            this.value = value;
        }

        public int getValue() {
            return value;
        }
}