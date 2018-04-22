package model.signal;

import model.Signal;

import java.util.Random;

// Szum o rozkładzie jednostajnym
public class Uniform extends Signal {
    public Uniform(String signalType, String name, Double amplitude, Integer startTime, Integer duration, Double frequencySampling) {
        this.signalType = signalType;
        this.name = name;
        this.amplitude = amplitude;
        this.startTime = startTime;
        this.duration = duration;
        this.frequencySampling = frequencySampling;

        this.generateSignal();
        this.calcStats();
    }

    @Override
    protected void generateSignal() {
        Random gen = new Random();

        double i = startTime;
        while (i < startTime + duration) {
            this.signal.put(i, (gen.nextDouble() * 2 - 1) * amplitude);
            i += 1 / frequencySampling;
        }

        System.out.println(signal);
    }
}