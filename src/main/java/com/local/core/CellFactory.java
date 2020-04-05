package com.local.core;

import java.util.List;

public interface CellFactory {
    Cell createCell(Double position, Linker linker);
    List<Cell> createUniformInitialMesh(Linker linker);
}
