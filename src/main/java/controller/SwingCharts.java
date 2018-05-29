package controller;

import javafx.embed.swing.SwingNode;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.SamplingXYLineRenderer;
import org.jfree.chart.renderer.xy.StandardXYItemRenderer;
import org.jfree.chart.renderer.xy.XYStepRenderer;
import org.jfree.data.xy.XYDataset;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;

import javax.swing.*;
import java.awt.*;
import java.util.Map;

public class SwingCharts {
    public static void createQuantizationChart(final SwingNode swingNode, String chartName, Map<Double, Double> quantizedSignal, Map<Double, Double> signal) {
        SwingUtilities.invokeLater(() -> {
            XYSeries series1 = new XYSeries("1");

            quantizedSignal.forEach((k, v) -> {
                series1.add(k, v);
            });

            XYSeries series2 = new XYSeries("2");

            signal.forEach((k, v) -> {
                series2.add(k, v);
            });

            XYSeriesCollection dataset1 = new XYSeriesCollection();
            dataset1.addSeries(series1);

            XYSeriesCollection dataset2 = new XYSeriesCollection();
            dataset2.addSeries(series2);

            XYDataset aa = dataset1;

            JFreeChart chart = ChartFactory.createXYLineChart(
                    chartName,
                    "t[s]",
                    "A",
                    dataset2,
                    PlotOrientation.VERTICAL,
                    false,
                    true,
                    false
            );

            XYPlot plot = (XYPlot) chart.getPlot();
            plot.setDomainPannable(true);
            plot.setRangePannable(true);
            XYStepRenderer renderer = new XYStepRenderer();
            renderer.setSeriesStroke(0, new BasicStroke(2.0f));
            renderer.setDefaultEntityRadius(6);
            plot.setRenderer(1, renderer);
            plot.setDataset(1, aa);

            JPanel panel = new ChartPanel(chart);
            panel.setPreferredSize(new Dimension(680, 230));
            panel.setLayout(null);

            swingNode.setContent(panel);

        });
    }

    public static void createSamplingChart(final SwingNode swingNode, String chartName, Map<Double, Double> sampledSignal, Map<Double, Double> signal) {
        SwingUtilities.invokeLater(() -> {
            XYSeries series1 = new XYSeries("1");

            sampledSignal.forEach((k, v) -> {
                series1.add(k, v);
            });

            XYSeries series2 = new XYSeries("2");

            signal.forEach((k, v) -> {
                series2.add(k, v);
            });

            XYSeriesCollection dataset1 = new XYSeriesCollection();
            dataset1.addSeries(series1);
            dataset1.addSeries(series2);

            JFreeChart chart = ChartFactory.createXYLineChart(
                    chartName,
                    "t[s]",
                    "A",
                    dataset1,
                    PlotOrientation.VERTICAL,
                    false,
                    true,
                    false
            );

            StandardXYItemRenderer renderer = new StandardXYItemRenderer();
            renderer.setSeriesStroke(0, new BasicStroke(2.0f));
            XYPlot plot = (XYPlot) chart.getPlot();
            plot.setDomainPannable(true);
            plot.setRangePannable(true);
            plot.setRenderer(0, renderer);
//
//            plot.getRendererForDataset(plot.getDataset(0)).setSeriesPaint(0, Color.red);
//            plot.getRendererForDataset(plot.getDataset(1)).setSeriesPaint(1, Color.blue);

            JPanel panel = new ChartPanel(chart);
            panel.setPreferredSize(new Dimension(680, 230));
            panel.setLayout(null);

            swingNode.setContent(panel);

        });
    }
}
