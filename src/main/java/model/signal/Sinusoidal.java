package model.signal;

import model.Signal;

public class Sinusoidal extends Signal {
    public Sinusoidal(String signalType, String name, Double amplitude, Integer startTime, Integer duration, Double frequencySampling, Integer baseInterval) {
        this.signalType = signalType;
        this.name = name;
        this.amplitude = amplitude;
        this.startTime = startTime;
        this.duration = duration;
        this.frequencySampling = frequencySampling;
        this.baseInterval = baseInterval;

        generateSignal();
        calcStats();
    }

    @Override
    protected void generateSignal() {
        double i = startTime;
        int counter = 0;
        while (i < startTime + duration) {
            this.signal.put(
                    i,
                    amplitude * Math.sin((2*Math.PI) / baseInterval * ((counter / frequencySampling) - startTime))
            );
            i += 1 / frequencySampling;
            counter++;
        }
    }
}
