package com.local.core;

import java.util.ArrayList;
import java.util.List;

public class Mesh {
    protected List<Cell> content = new ArrayList<>();

    public void performTimeStep() {
        content.stream()
                .peek(Cell::updateDynamicProperties)
                .peek(Cell::computeVelocity)
                .forEach(Cell::performTimeStep);
    }
}
