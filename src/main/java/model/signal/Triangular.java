package model.signal;

public class Triangular extends Rectangular {
    public Triangular(String signalType, String name, Double amplitude, Integer startTime, Integer duration,
                      Double frequencySampling, Integer baseInterval, Double fillFactor) {
        super(signalType, name, amplitude, startTime, duration, frequencySampling, baseInterval, fillFactor);
    }

    @Override
    protected void generateSignal() {
        double interval = baseInterval * frequencySampling;

        int k = 0;
        double t1, t2;
        t2 = startTime / frequencySampling;

        double i = startTime;
        int counter = 0;
        while (i < duration + startTime) {
            if (counter % interval == 0 && counter != 0) {
                k++;
            }
            t1 = counter / frequencySampling;

            if (t1 >= (k * baseInterval + t2) &&
                    t1 < (fillFactor * baseInterval + k * baseInterval + t2)) {
                this.signal.put(i, (amplitude * (t1 - k * baseInterval - t2)) / (fillFactor * baseInterval));
            }
            else if (t1 >= (fillFactor * baseInterval - k * baseInterval + t2) &&
                    t1 < (baseInterval + k * baseInterval + t2)) {
                this.signal.put(i, ((-amplitude*(t1 - k * baseInterval - t2)) / (baseInterval * (1 - fillFactor))) +
                        amplitude / (1 - fillFactor));
            }
            i += 1 / frequencySampling;
            counter++;
        }
    }
}
