package model.signal;

import model.Signal;

import java.util.Random;

public class Gaussian extends Signal {
    public Gaussian(String signalType, String name, Double amplitude, Integer startTime, Integer duration, Double frequencySampling) {
        this.signalType = signalType;
        this.name = name;
        this.amplitude = amplitude;
        this.startTime = startTime;
        this.duration = duration;
        this.frequencySampling = frequencySampling;

        generateSignal();
        calcStats();
    }

    @Override
    protected void generateSignal() {
        Random gen = new Random();

        double i = startTime;
        while (i < startTime + duration) {
            this.signal.put(i, (gen.nextGaussian() * 1));
            i += 1 / frequencySampling;
        }
    }
}
