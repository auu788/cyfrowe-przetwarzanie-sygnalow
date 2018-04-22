package model;

import java.util.Random;

public class GaussianNoise extends Signal {
    public GaussianNoise(String signalType, String name, Double amplitude, Integer startTime, Integer duration, Double frequencySampling) {
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

        for(int i = 0; i < duration * frequencySampling; i++) {
            this.signal.put(i, gen.nextGaussian() * 1);
        }
    }
}
