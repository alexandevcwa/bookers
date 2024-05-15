package org.alexandevcwa.application.model.repository;

import java.util.List;

public interface CRUDRepository<T> {


    /**
     * Búsqueda por ID de entidad
     *
     * @param id ID de entidad
     * @return Objeto de entidad
     */
    T findById(int id);


    /**
     * Búsqueda de todas las entidades
     *
     * @return Lista de entidades
     */
    List<T> findAll();


    /**
     * Guardado de entidades
     *
     * @param t Entidad a guardar
     * @return Número de registros insertados
     */
    int save(T t);


    /**
     * Eliminar entidad
     *
     * @param t Entidad a eliminar
     */
    void delete(T t);


    /**
     * Actualización de entidad
     *
     * @param t Actualizar entidad
     * @return Número de registros actualizados
     */
    int update(T t);
}
