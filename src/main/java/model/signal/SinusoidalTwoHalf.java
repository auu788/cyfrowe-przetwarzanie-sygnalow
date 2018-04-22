package model.signal;

import model.Signal;

public class SinusoidalTwoHalf extends Sinusoidal {
    public SinusoidalTwoHalf(String signalType, String name, Double amplitude, Integer startTime, Integer duration, Double frequencySampling, Integer baseInterval) {
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
                    amplitude * Math.abs(Math.sin((2 * Math.PI) / baseInterval * (counter / frequencySampling) - startTime))
            );
            i += 1 / frequencySampling;
            counter++;
        }
    }
}
