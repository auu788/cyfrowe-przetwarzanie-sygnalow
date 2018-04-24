package model.signal;

import model.Signal;

public class UnitJump extends Signal {
    public UnitJump(String signalType, String name, Double amplitude, Integer startTime, Integer duration,
                    Double frequencySampling, Integer jumpTime) {
        this.signalType = signalType;
        this.name = name;
        this.amplitude = amplitude;
        this.startTime = startTime;
        this.duration = duration;
        this.frequencySampling = frequencySampling;
        this.jumpTime = jumpTime;
    }

    @Override
    protected void generateSignal() {
        double i = startTime;
        int counter = 0;

        double t;
        while (i < startTime + duration) {
            t = counter / frequencySampling;

            if (t < jumpTime - startTime) {
                this.signal.put(i, 0.0);
            }
            else if (t == jumpTime - startTime) {
                this.signal.put(i, 0.5 * amplitude);
            }
            else {
                this.signal.put(i, amplitude);
            }
            i += 1 / frequencySampling;
            counter++;
        }
    }
}
