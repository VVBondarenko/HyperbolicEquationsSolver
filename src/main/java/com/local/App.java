package com.local;

import com.local.core.CellFactory;
import com.local.core.Mesh;
import com.local.core.Problem;
import com.local.scheme.CellFactoryImpl;
import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.ArrayRealVector;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.RealVector;

public class App {
    public static void main(String[] args) {
        CellFactory factory = new CellFactoryImpl(new Problem() {
            @Override
            public RealVector computeFlux(RealVector value) {
                return value.ebeMultiply(value).mapMultiplyToSelf(0.5);
            }

            @Override
            public RealMatrix computeJacobian(RealVector value) {
                RealMatrix matrix = new Array2DRowRealMatrix(1, 1);
                matrix.setEntry(0, 0, value.getEntry(0));
                return matrix;
            }

            @Override
            public RealVector getInitialValue(Double position) {
                RealVector value = new ArrayRealVector(1);
                value.setEntry(0, getInitialValue(position.doubleValue()));
                return value;
            }

            private double getInitialValue(double position) {
                if (position > 0.25 && position < 0.5)
                    return -1.;
                if (position > 0.5 && position < 0.75)
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
        });
        Mesh mesh = new Mesh(factory);
        mesh.performTimeStep();
    }
}
