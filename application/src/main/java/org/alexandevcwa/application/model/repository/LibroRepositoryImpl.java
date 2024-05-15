package org.alexandevcwa.application.model.repository;

import org.alexandevcwa.application.model.Libro;

import java.util.List;

public interface LibroRepositoryImpl extends CRUDRepository<Libro> {

    /**
     * Búsqueda de libros por palabras claves
     *
     * @param patter Patron de búsqueda de libros
     * @return {@link List} de objetos de tipo {@link Libro}
     */
    List<Libro> findLikeLbrTitulo(String patter);

    /**
     * Búsqueda de libro por ISBN
     *
     * @param isbn ISBN del libro a buscar
     * @return Objeto de tipo {@link Libro}
     */
    Libro findByLbrISBN(String isbn);

    boolean existsByLbrISBN(String isbn);

    List<Libro> findAllByPagination(int start, int end);

}
