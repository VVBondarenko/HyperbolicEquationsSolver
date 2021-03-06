package com.local.core;

import org.apache.commons.math3.linear.RealVector;

import java.util.*;

public class Mesh implements Linker {
    protected CellFactory factory;
    protected NavigableMap<Double, Cell> content = new TreeMap<>();

    public Mesh(CellFactory factory) {
        this.factory = factory;
        List<Cell> initialMesh = factory.createUniformInitialMesh(this);
        for (Cell cell : initialMesh) {
            content.put(cell.getPosition(), cell);
        }
    }

    public void performTimeStep() {
        //todo: заменить на CyclicBarrier, проверить производительность
        content.keySet().stream().parallel()
                .map(content::get)
                .forEach(Cell::updateDynamicProperties);
        content.keySet().stream().parallel()
                .map(content::get)
                .forEach(Cell::computeVelocity);
        content.keySet().stream().parallel()
                .map(content::get)
                .forEach(Cell::performTimeStep);
    }

    public RealVector getValueAt(Double position) {
        Map.Entry<Double, Cell> floorEntry = content.floorEntry(position);
        Cell floor = floorEntry.getValue();

        Map.Entry<Double, Cell> ceilingEntry = content.ceilingEntry(position);
        Cell ceiling = ceilingEntry.getValue();

        if (floor == ceiling)
            return floor.getValue();

        RealVector valueDifference = ceiling.getValue().subtract(floor.getValue());
        double coefficient = (position - floorEntry.getKey()) / (ceilingEntry.getKey() - floorEntry.getKey());

        return valueDifference.mapMultiplyToSelf(coefficient).add(floor.getValue());
    }

    @Override
    public Cell getNext(Cell cell) {
        Map.Entry<Double, Cell> entry = content.higherEntry(cell.getPosition());
        if (entry == null)
            return null;
        return entry.getValue();
    }

    @Override
    public Cell getPrevious(Cell cell) {
        Map.Entry<Double, Cell> entry = content.lowerEntry(cell.getPosition());
        if (entry == null)
            return null;
        return entry.getValue();
    }
}
