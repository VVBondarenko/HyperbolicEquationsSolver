package com.local;

import com.local.core.CellFactory;
import com.local.core.Mesh;
import com.local.core.Problem;
import com.local.problem.RiemannProblem;
import com.local.scheme.CellFactoryImpl;
import org.knowm.xchart.QuickChart;
import org.knowm.xchart.SwingWrapper;
import org.knowm.xchart.XYChart;

import javax.swing.*;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.DoubleStream;

public class App {
    private Problem problem = new RiemannProblem();
    private CellFactory factory = new CellFactoryImpl(problem);
    private Mesh mesh;
    private XYChart chart;
    private SwingWrapper<XYChart> wrapper;

    public App() {
        mesh = new Mesh(factory);

        prepareChart();
        wrapper = new SwingWrapper<>(chart);
        wrapper.displayChart();
    }

    private void prepareChart() {
        List<Double> xValues = getXValues();
        List<Double> yValues = getYValues();

        chart = QuickChart.getChart(
                "uniform mesh",
                "x", "u", "u(x, t=0.1)",
                xValues, yValues);
    }

    private void updateChart() {
        SwingUtilities.invokeLater(() -> {
            List<Double> xValues = getXValues();
            List<Double> yValues = getYValues();
            chart.updateXYSeries("u(x, t=0.1)", xValues, yValues, null);
            wrapper.repaintChart();
        });
    }

    private List<Double> getXValues() {
        final double leftBoundary = problem.getLeftBoundary();
        final double rightBoundary = problem.getRightBoundary();
        final double step = (rightBoundary - leftBoundary) / (double) (problem.getResolution() - 1);

        List<Double> xValues = new ArrayList<>();
        DoubleStream.iterate(leftBoundary, x -> x <= rightBoundary, x -> x + step).forEach(xValues::add);
        return xValues;
    }

    private List<Double> getYValues() {
        List<Double> xValues = getXValues();
        return xValues.stream()
                .map(mesh::getValueAt).map(vector -> vector.getEntry(0))
                .collect(Collectors.toList());
    }

    public void solve() {
        factory.setTimeStep(1.e-4);
        for (int i = 0; i < 300000; i++) {
            mesh.performTimeStep();
            if (i != 0 && i % 100 == 0)
                updateChart();
        }
        updateChart();
        System.out.println("done");
    }

    public static void main(String[] args) {
        App app = new App();
        app.solve();
    }

}

