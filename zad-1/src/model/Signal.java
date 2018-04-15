package model;

import java.util.TreeMap;

public abstract class Signal {
    protected String name;
    protected Double amplitude;
    protected Integer startTime;
    protected Integer duration;
    protected Double frequencySampling;

    protected TreeMap<Integer, Double> signal = new TreeMap<>();
    protected Double avg;               // średnia
    protected Double absoluteAvg;       // średnia bezwzględna
    protected Double avgSignalPower;    // moc średnia sygnału
    protected Double variance;          // wariancja
    protected Double rms;               // wartość skuteczna

    @Override
    public String toString() {
        return name;
    }

    public TreeMap<Integer, Double> getData() {
        return signal;
    }

    protected abstract void generate();
//    private Integer baseInterval;
//    private Integer frequencySampling;
//    private Integer signalFillFactor;
//    private Integer jumpNum;
//    private Integer jumpTime;
//    private Double amplitudeProbability;
}