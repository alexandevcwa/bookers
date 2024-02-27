package com.alexandevcwa.bookers.repository;

import java.util.List;

public interface CrudRepository<T, R> {
    T guardar(T object);
    T editar(T object);
    T eliminar(T object);
    T buscar(R key);
    List<T> buscarTodos();
}
