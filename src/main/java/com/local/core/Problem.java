package com.local.core;

import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.RealVector;

public interface Problem {
    RealVector computeFlux(RealVector value);

    RealMatrix computeJacobian(RealVector value);

    RealVector getInitialValue(Double position);

    Double getLeftBoundary();

    Double getRightBoundary();

    Integer getResolution();
}
