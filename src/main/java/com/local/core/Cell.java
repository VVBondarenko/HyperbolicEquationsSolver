package com.local.core;

import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.RealVector;

public abstract class Cell {
    protected Double position;

    protected RealVector value;
    protected RealVector valueVelocity;
    protected static double basicTimeStep;

    protected RealVector flux; //Flux parts for each unknown value
    protected RealMatrix jacobian;

    protected Problem problem;

    public Cell(Double position, Problem problem) {
        this.position = position;
        this.problem = problem;
        value = problem.getInitialValue(position);
    }

    public void updateDynamicProperties() {
        updateFlux();
        updateJacobian();
    }

    private void updateFlux() {
        flux = problem.computeFlux(value);
    }

    private void updateJacobian() {
        jacobian = problem.computeJacobian(value);
    }

    public abstract void computeVelocity();

    public void performTimeStep() {
       value = value.subtract(valueVelocity.mapMultiply(basicTimeStep));
    }
}
