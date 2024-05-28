package org.alexandevcwa.application.model.repository;

import org.alexandevcwa.application.Inventario;

public interface InventarioRepositoryImpl extends CRUDRepository<Inventario>{
    Inventario findInventarioByIsbnLibro(String isbn);
}
