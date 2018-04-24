package model.signal;

import model.Signal;

public class Rectangular extends Signal {
    public Rectangular(String signalType, String name, Double amplitude, Integer startTime, Integer duration,
                       Double frequencySampling, Integer baseInterval, Double fillFactor) {
        this.signalType = signalType;
        this.name = name;
        this.amplitude = amplitude;
        this.startTime = startTime;
//        this.duration = (duration / baseInterval) * baseInterval;
        this.startTime = startTime;
        this.duration = duration;
        this.frequencySampling = frequencySampling;
        this.baseInterval = baseInterval;
        this.fillFactor = fillFactor;

        generateSignal();
        calcStats();
    }

    @Override
    protected void generateSignal() {
        double interval = baseInterval * frequencySampling;

        int k = 0;
        double t1, t2;
        t2 = startTime / frequencySampling;

        double i = startTime;
        int counter = 0;
        while (i < startTime + duration) {
            if (counter % interval == 0 && counter != 0) {
                k++;
            }
            t1 = counter / frequencySampling;

            if (t1 >= (k * baseInterval + t2) &&
                    t1 < (fillFactor * baseInterval + k * baseInterval + t2)) {
                this.signal.put(i, amplitude);
            }
            else if (t1 >= (fillFactor * baseInterval - k * baseInterval + t2) &&
                    t1 < (baseInterval + k * baseInterval + t2)) {
                this.signal.put(i, 0.0);
            }
            i += 1 / frequencySampling;
            counter++;
        }
    }
}
