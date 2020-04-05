package com.local.core;

import java.util.ArrayList;
import java.util.List;

public class Mesh implements Linker {
    protected List<Cell> content = new ArrayList<>();

    public void performTimeStep() {
        content.stream()
                .peek(Cell::updateDynamicProperties)
                .peek(Cell::computeVelocity)
                .forEach(Cell::performTimeStep);
    }


    //todo: implement linkers methods
    @Override
    public Cell getNext(Cell cell) {
        return null;
    }

    @Override
    public Cell getPrevious(Cell cell) {
        return null;
    }
}
