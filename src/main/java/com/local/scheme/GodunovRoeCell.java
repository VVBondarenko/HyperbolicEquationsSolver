package com.local.scheme;

import com.local.core.Cell;
import com.local.core.Linker;
import com.local.core.Problem;
import com.local.core.SchemedCell;
import org.apache.commons.math3.linear.EigenDecomposition;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.RealVector;

public class GodunovRoeCell extends SchemedCell {

    public GodunovRoeCell(Double position, Problem problem, Linker linker) {
        super(position, problem, linker);
    }

    @Override
    protected RealVector computeFluxOverBoundary(Cell left, Cell right) {
        RealVector roesFlux = getRoesFlux(left, right);
        return getMiddle(left.getFlux(), right.getFlux()).subtract(roesFlux);
    }

    private RealVector getRoesFlux(Cell left, Cell right) {
        RealVector leftValue = left.getValue();
        RealVector rightValue = right.getValue();
        RealVector referenceValue = getMiddle(leftValue, rightValue);
        RealMatrix jacobian = problem.computeJacobian(referenceValue);
        EigenDecomposition decomposition = new EigenDecomposition(jacobian);
        RealMatrix diagonal = decomposition.getD();
        for (int i = 0; i < diagonal.getColumnDimension(); i++) {
            double absValue = Math.abs(diagonal.getEntry(i, i));
            diagonal.setEntry(i, i, absValue);
        }
        RealMatrix v = decomposition.getV();
        RealMatrix vt = decomposition.getVT();
        RealVector halfDistance = getHalfDistance(rightValue, leftValue);
        return vt.multiply(diagonal).multiply(v).preMultiply(halfDistance);
    }

    private RealVector getMiddle(RealVector left, RealVector right) {
        return left.add(right).mapMultiplyToSelf(0.5);
    }

    private RealVector getHalfDistance(RealVector from, RealVector to) {
        return from.subtract(to).mapMultiplyToSelf(0.5);
    }
}
