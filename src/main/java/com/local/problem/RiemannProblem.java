package com.local.problem;

import com.local.core.Problem;
import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.ArrayRealVector;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.RealVector;

public class RiemannProblem implements Problem {
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
        if (position > 0.333 && position < 0.666)
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
