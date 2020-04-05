package com.local.core;

import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.RealVector;

public interface Problem {
    RealMatrix computeFlux(RealVector value);

    RealMatrix computeJacobian(RealVector value);

    RealVector getInitialValue(RealVector position);
}
