package model;

import java.util.TreeMap;

public class OperationalSignal extends Signal {
    OperationalSignal(String name, Double frequencySampling, TreeMap<Double, Double> data) {
        this.name = name;
        this.frequencySampling = frequencySampling;
        this.signalType = "";
        this.signal = data;
        this.startTime = data.firstKey().intValue();
        this.duration = data.lastKey().intValue() - data.firstKey().intValue();
        System.out.println("Sygałek: " + this.signal);
        this.amplitude = signal.values().stream().mapToDouble(Double::doubleValue).max().getAsDouble();

        calcStats();
        System.out.println(data);
    }

    @Override
    protected void generateSignal() { }
}
