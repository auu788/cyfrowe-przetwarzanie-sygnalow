package model;

import java.util.Random;

// Szum o rozkładzie jednostajnym
public class UniformSignal extends Signal {
    public UniformSignal(String name, Double amplitude, Integer startTime, Integer duration, Double frequencySampling) {
        this.name = name;
        this.amplitude = amplitude;
        this.startTime = startTime;
        this.duration = duration;
        this.frequencySampling = frequencySampling;

        this.generate();
    }

    @Override
    protected void generate() {
        Random gen = new Random();
        // duration * freqSampling

        for (int i = 0; i < duration * frequencySampling; i++) {
            this.signal.put(i, (gen.nextDouble() * 2 - 1) * amplitude);
        }

        for (int i = 0; i < this.signal.size() - 1; i++) {
            System.out.println(this.signal.get(i));
        }
    }
}