package com.local.problem;

import com.local.core.Problem;
import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.ArrayRealVector;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.RealVector;

public class ShallowWaterProblem implements Problem {
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
        return 200;
    }
}
