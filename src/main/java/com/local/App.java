package com.local;

import com.local.core.CellFactory;
import com.local.core.Mesh;
import com.local.core.Problem;
import com.local.scheme.CellFactoryImpl;
import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.ArrayRealVector;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.RealVector;
import org.knowm.xchart.QuickChart;
import org.knowm.xchart.SwingWrapper;
import org.knowm.xchart.XYChart;

import javax.swing.*;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.DoubleStream;

public class App {
    private Problem problem = new SampleShallowWaterProblem();
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
        factory.setTimeStep(1.e-6);
        for (int i = 0; i < 100000; i++) {
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

class SampleBurgersProblem implements Problem {
    @Override
    public RealVector computeFlux(RealVector value) {
        return value.ebeMultiply(value).mapMultiplyToSelf(0.5);
    }

    @Override
    public RealMatrix computeJacobian(RealVector value) {
        RealMatrix matrix = new Array2DRowRealMatrix(1, 1);
        matrix.setEntry(0, 0, value.getEntry(0));
        return matrix;
    }

    @Override
    public RealVector getInitialValue(Double position) {
        RealVector value = new ArrayRealVector(1);
        value.setEntry(0, getInitialValue(position.doubleValue()));
        return value;
    }

    private double getInitialValue(double position) {
//        if (position > 0.25 && position < 0.5)
//            return -1.;
        if (position > 0.51 && position < 0.75)
            return 1.;
        return 0.;
    }

    @Override
    public Double getLeftBoundary() {
        return 0.;
    }

    @Override
    public Double getRightBoundary() {
        return 1.;
    }

    @Override
    public Integer getResolution() {
        return 100;
    }
}

class SampleRiemannProblem implements Problem {
    private final double rho0 = 1.;
    private final double a0 = 1.;

    @Override
    public RealVector computeFlux(RealVector value) {
        double u = value.getEntry(1);
        double rho = value.getEntry(0);
        ArrayRealVector result = new ArrayRealVector(2);
        result.setEntry(0, u * rho0);
        result.setEntry(1, rho * a0 * a0 / rho0);
        return result;
    }

    @Override
    public RealMatrix computeJacobian(RealVector value) {
        Array2DRowRealMatrix result = new Array2DRowRealMatrix(2, 2);
        result.setEntry(0, 1, rho0);
        result.setEntry(1, 0, a0 * a0 / rho0);
        return result;
    }

    @Override
    public RealVector getInitialValue(Double position) {
        RealVector value = new ArrayRealVector(2);
        double initialValue = getInitialValue(position.doubleValue());
        value.setEntry(0, initialValue);
        value.setEntry(1, 0.);
        return value;
    }

    private double getInitialValue(double position) {
        if (position < 0.251)
            return 1.;
        return 0.;
    }

    @Override
    public Double getLeftBoundary() {
        return 0.;
    }

    @Override
    public Double getRightBoundary() {
        return 1.;
    }

    @Override
    public Integer getResolution() {
        return 100;
    }
}

class SampleShallowWaterProblem implements Problem {
    private final double g = 9.81;
    @Override
    public RealVector computeFlux(RealVector value) {
        final double u = value.getEntry(1);
        final double rho = value.getEntry(0);
        ArrayRealVector result = new ArrayRealVector(2);
        result.setEntry(0, u * rho);
        result.setEntry(1, u * u * 0.5 + g * rho);
        return result;
    }

    @Override
    public RealMatrix computeJacobian(RealVector value) {
        final double u = value.getEntry(1);
        final double rho = value.getEntry(0);

        Array2DRowRealMatrix result = new Array2DRowRealMatrix(2, 2);
        result.setEntry(0, 0, u);
        result.setEntry(0, 1, rho);
        result.setEntry(1, 0, g);
        result.setEntry(1, 1, u);
        return result;
    }

    @Override
    public RealVector getInitialValue(Double position) {
        RealVector value = new ArrayRealVector(2);
        double initialValue = getInitialValue(position.doubleValue());
        value.setEntry(0, initialValue + 100.);
        value.setEntry(1, 0.);
        return value;
    }

    private double getInitialValue(double position) {
        if (position < 0.251)
            return 1.;
        return 0.;
    }

    @Override
    public Double getLeftBoundary() {
        return 0.;
    }

    @Override
    public Double getRightBoundary() {
        return 1.;
    }

    @Override
    public Integer getResolution() {
        return 100;
    }
}