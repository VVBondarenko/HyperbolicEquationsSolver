package com.local.scheme;

import com.local.core.Cell;
import com.local.core.Problem;
import jdk.internal.vm.compiler.collections.Pair;
import org.apache.commons.math3.linear.RealVector;

public class GodunovCell extends Cell {
    public GodunovCell(RealVector position, Problem problem) {
        super(position, problem);
    }

    public GodunovCell(Pair<Cell, Cell> neighbours) {
        super(neighbours);
    }

    @Override
    protected RealVector computeFluxOverBoundary(Cell left, Cell right) {
        return null;
    }
}
