package org.iota.spamsim.simulation;

public class SimResult {

    private int rounds = 0;
    private double honestScore = 0;
    private double maliciousScore = 0;

    public SimResult() {

    }

    public SimResult(Iterable<SimResult> partialResults) {
        for(SimResult r : partialResults) {
            honestScore = (honestScore * rounds + r.honestScore * r.rounds)/(rounds+r.rounds);
            maliciousScore = (maliciousScore * rounds + r.maliciousScore * r.rounds)/(rounds+r.rounds);
            rounds += r.rounds;
        }
    }

    public synchronized void addRound(double partialHonestScore, double partialMaliciousScore) {
        rounds++;
        honestScore = (honestScore * rounds + partialHonestScore)/(rounds+1);
        maliciousScore = (maliciousScore * rounds + partialMaliciousScore)/(rounds+1);
    }

    public int getRounds() {
        return rounds;
    }

    public double getHonestScore() {
        return honestScore;
    }

    public double getMaliciousScore() {
        return maliciousScore;
    }
}
