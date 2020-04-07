package com.local.problem;

import com.local.core.Problem;
import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.ArrayRealVector;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.RealVector;

public class VoidFormationProblem implements Problem {
    private final double densityScale = 1.e-3;
    private final double delta = 1.e-3;

    @Override
    public RealVector computeFlux(RealVector value) {
        final double density = value.getEntry(0);
        final double velocity = value.getEntry(1);
        ArrayRealVector result = new ArrayRealVector(2);
        result.setEntry(0, density * velocity);
        result.setEntry(1, delta * Math.log(density));
        return result;
    }

    @Override
    public RealMatrix computeJacobian(RealVector value) {
        final double density = value.getEntry(0);
        final double velocity = value.getEntry(1);

        Array2DRowRealMatrix result = new Array2DRowRealMatrix(2, 2);
        result.setEntry(0, 0, density);
        result.setEntry(0, 1, velocity);
        result.setEntry(1, 0, delta / density);
        result.setEntry(1, 1, 0.);
        return result;
    }

    @Override
    public RealVector getInitialValue(Double position) {
        RealVector value = new ArrayRealVector(2);
        double initialValue = densityScale + densityScale * 10. * getInitialStepDistribution(position);
        value.setEntry(0, initialValue);
        value.setEntry(1, 0.);
        return value;
    }

    private double getInitialStepDistribution(double position) {
        if (position < 0.5)
            return 1.;
        return 0.;
    }

    @Override
    public Double getLeftBoundary() {
        return 0.;
    }

    @Override
    public Double getRightBoundary() {
        return 4.;
    }

    @Override
    public Integer getResolution() {
        return 200;
    }
}
