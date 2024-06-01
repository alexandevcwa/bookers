package org.alexandevcwa.application.model.repository;

import org.alexandevcwa.application.Inventario;

import java.util.List;

public interface InventarioRepositoryImpl extends CRUDRepository<Inventario>{
    Inventario findInventarioByIsbnLibro(String isbn);

    List<Inventario> findAllWithCover();

    List<Inventario> findAllByTittleRegex(String regex);
}
