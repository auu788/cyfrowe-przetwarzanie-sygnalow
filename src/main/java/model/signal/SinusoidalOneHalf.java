package model.signal;

import model.Signal;

public class SinusoidalOneHalf extends Sinusoidal {
    public SinusoidalOneHalf(String signalType, String name, Double amplitude, Integer startTime, Integer duration, Double frequencySampling, Integer baseInterval) {
        super(signalType, name, amplitude, startTime, duration, frequencySampling, baseInterval);
    }

    @Override
    protected void generateSignal() {
        double i = startTime;
        int counter = 0;
        while (i < startTime + duration) {
            double tmp = ((2 * Math.PI) / baseInterval * (counter / frequencySampling) - startTime);
            this.signal.put(
                    i,
                    0.5 * amplitude * (Math.sin(tmp) + Math.abs(Math.sin(tmp)))
            );
            i += 1 / frequencySampling;
            counter++;
        }
    }
}
