package com.local.scheme;

import com.local.core.Cell;
import com.local.core.CellFactory;
import com.local.core.Linker;
import com.local.core.Problem;

import java.util.ArrayList;
import java.util.List;

public class CellFactoryImpl implements CellFactory {
    private Problem problem;

    public CellFactoryImpl(Problem problem) {
        this.problem = problem;
    }

    @Override
    public Cell createCell(Double position, Linker linker) {
        return new GodunovRoeCell(position, problem, linker);
    }

    @Override
    public List<Cell> createUniformInitialMesh(Linker linker) {
        Double leftBoundary = problem.getLeftBoundary();
        Double rightBoundary = problem.getRightBoundary();
        Integer resolution = problem.getResolution();
        double step = (rightBoundary - leftBoundary) / (double) (resolution - 1);
        List<Cell> uniformMesh = new ArrayList<>();
        for (int i = 0; i < resolution; i++) {
            Cell cell = createCell(step * i, linker);
            uniformMesh.add(cell);
        }
        return uniformMesh;
    }

    @Override
    public void setTimeStep(double step) {
        Cell.setBasicTimeStep(step);
    }
}
