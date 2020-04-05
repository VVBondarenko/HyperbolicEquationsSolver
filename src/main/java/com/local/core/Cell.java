package com.local.core;

import jdk.internal.vm.compiler.collections.Pair;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.RealVector;

public abstract class Cell {
    protected RealVector position;
    protected Pair<Cell, Cell> neighbours = Pair.empty();

    protected RealVector value;
    protected RealVector valueVelocity;
    protected static double basicTimeStep;

    protected RealMatrix flux; //Flux parts for each unknown value
    protected RealMatrix jacobian;

    protected Problem problem;

    public Cell(RealVector position, Problem problem) {
        this.position = position;
        this.problem = problem;
        value = problem.getInitialValue(position);
    }

    public Cell(Pair<Cell,Cell> neighbours) {
        this.neighbours = neighbours;

        Cell left = neighbours.getLeft();
        Cell right = neighbours.getRight();

        position = left.position.add(right.position).mapMultiplyToSelf(0.5);
        value = left.value.add(right.value).mapMultiplyToSelf(0.5);
        problem = left.problem;
    }

    public void setNeighbours(Pair<Cell, Cell> neighbours) {
        this.neighbours = neighbours;
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

    public void computeVelocity() {
        Cell left = neighbours.getLeft();
        RealVector leftFlux = computeFluxOverBoundary(left, this);

        Cell right = neighbours.getRight();
        RealVector rightFlux = computeFluxOverBoundary(this, right);

        double dx = getNeighboursSpan() * 0.5;
        valueVelocity = rightFlux.subtract(leftFlux).mapDivideToSelf(dx);
    }

    protected double getNeighboursSpan() {
        RealVector right = neighbours.getRight().position;
        RealVector left = neighbours.getLeft().position;
        return right.getDistance(left);
    }

    protected abstract RealVector computeFluxOverBoundary(Cell left, Cell right);

    public void performTimeStep() {
       value = value.subtract(valueVelocity.mapMultiply(basicTimeStep));
    }
}
