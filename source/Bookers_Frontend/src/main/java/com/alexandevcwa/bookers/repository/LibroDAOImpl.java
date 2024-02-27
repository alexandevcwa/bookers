package com.alexandevcwa.bookers.repository;

import com.alexandevcwa.bookers.model.AutorDTO;
import com.alexandevcwa.bookers.model.LibroDTO;

import java.util.List;

public interface LibroDAOImpl extends CrudRepository<LibroDTO, Integer> {
    LibroDTO buscarPorISBN(String isbn);

    List<LibroDTO> buscarPorTitulo(String titulo);

    List<LibroDTO> buscarPorAutor(AutorDTO autor);

}
