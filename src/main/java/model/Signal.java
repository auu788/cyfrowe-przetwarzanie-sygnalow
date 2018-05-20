package model;

import java.util.ArrayList;
import java.util.List;
import java.util.TreeMap;

public abstract class Signal {
    protected String signalType;
    protected String name;
    protected Double amplitude;
    protected Integer startTime;
    protected Integer duration;
    protected Double frequencySampling;

    protected Integer baseInterval;
    protected Double fillFactor;
    protected Integer jumpTime;
    protected Double amplitudeProbability;

    protected TreeMap<Double, Double> signal = new TreeMap<>();
    protected List<Double> analogSignal = new ArrayList<>();

    protected Double avg;               // średnia
    protected Double absoluteAvg;       // średnia bezwzględna
    protected Double avgSignalPower;    // moc średnia sygnału
    protected Double variance;          // wariancja
    protected Double rms;               // wartość skuteczna

    public Signal() {}

    @Override
    public String toString() {
        return name;
    }

    public TreeMap<Double, Double> getData() {
        return signal;
    }

    protected void calcStats() {
        System.out.println(signal.values());
        avg = signal.values().stream().mapToDouble(Double::doubleValue).sum() / (double) signal.size();
        absoluteAvg = signal.values().stream().mapToDouble(Double::doubleValue).map(Math::abs).sum() / (double) signal.size();
        avgSignalPower = signal.values().stream().mapToDouble(Double::doubleValue).map(i -> Math.pow(i, 2)).sum() / (double) signal.size();
        variance = signal.values().stream().mapToDouble(Double::doubleValue).map(i -> Math.pow(i - avg, 2)).sum() / (double) signal.size();
        rms = Math.sqrt(avgSignalPower);
    }

    protected abstract void generateSignal();

    public String getName() { return name; }
    public Double getAvg() {
        return avg;
    }
    public Double getAbsoluteAvg() {
        return absoluteAvg;
    }
    public Double getAvgSignalPower() {
        return avgSignalPower;
    }
    public Double getVariance() {
        return variance;
    }
    public Double getRms() {
        return rms;
    }

    public int getMinValue() {
        return (int) signal.values().stream().mapToDouble(Double::doubleValue).min().getAsDouble();
    }

    public int getMaxValue() {
        return (int) signal.values().stream().mapToDouble(Double::doubleValue).max().getAsDouble();
    }

    public Integer getStartTime() {
        return startTime;
    }

    public Double getFrequencySampling() {
        return frequencySampling;
    }

    public Integer getDuration() {
        return duration;
    }

    public Double getAmplitude() {
        return amplitude;
    }

    public Integer getBaseInterval() {
        return baseInterval;
    }

    public Double getFillFactor() {
        return fillFactor;
    }

    public Integer getJumpNum() {
        return null;
    }

    public Integer getJumpTime() {
        return jumpTime;
    }

    public Double getAmplitudeProbability() {
        return amplitudeProbability;
    }

    public String getSignalType() {
        return signalType;
    }

    public void setData(TreeMap<Double, Double> data) {
        this.signal = data;
    }

    public void setSignalType(String signalType) {
        this.signalType = signalType;
    }
}