package com.local.core;

import org.apache.commons.math3.linear.RealVector;

public abstract class SchemedCell extends Cell {
    protected Linker linker;

    public SchemedCell(Double position, Problem problem, Linker linker) {
        super(position, problem);
        this.linker = linker;
    }

    @Override
    public void computeVelocity() {
        Cell left = linker.getPrevious(this);
        Cell right = linker.getNext(this);

        if (left == null || right == null) {
            valueVelocity = value.mapMultiply(0.);
            return;
        }

        RealVector leftFlux = computeFluxOverBoundary(left, this);
        RealVector rightFlux = computeFluxOverBoundary(this, right);

        double dx = (right.position + left.position) * 0.5;
        valueVelocity = rightFlux.subtract(leftFlux).mapDivideToSelf(dx);
    }

    @Override
    public void performTimeStep() {
        Cell left = linker.getPrevious(this);
        Cell right = linker.getNext(this);
        if (left == null) {
            value = right.getValue().copy();
            return;
        }

        if (right == null) {
            value = left.getValue().copy();
            return;
        }

        super.performTimeStep();
    }

    protected abstract RealVector computeFluxOverBoundary(Cell left, Cell right);
}
