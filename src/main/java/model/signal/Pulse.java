package model.signal;

import model.Signal;

import java.util.Random;

public class Pulse extends Signal {
    public Pulse(String signalType, String name, Double amplitude, Integer startTime, Integer duration,
                    Double frequencySampling, Double amplitudeProbability) {
        this.signalType = signalType;
        this.name = name;
        this.amplitude = amplitude;
        this.startTime = startTime;
        this.duration = duration;
        this.frequencySampling = frequencySampling;
        this.amplitudeProbability = amplitudeProbability;
    }

    @Override
    protected void generateSignal() {
        int counter = 0;
        double i = startTime;
        Random rand = new Random();
        while (i < startTime + duration) {

            if (rand.nextDouble() <= amplitudeProbability) {
                this.signal.put(i, amplitude);
            }
            else {
                this.signal.put(i, 0.0);
            }
            i = 1 / frequencySampling;
        }
    }
}
