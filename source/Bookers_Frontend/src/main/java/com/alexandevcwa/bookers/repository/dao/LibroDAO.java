package com.alexandevcwa.bookers.repository.dao;

import com.alexandevcwa.bookers.model.AutorDTO;
import com.alexandevcwa.bookers.model.LibroDTO;
import com.alexandevcwa.bookers.model.enums.HttpMethod;
import com.alexandevcwa.bookers.repository.LibroDAOImpl;

import java.util.List;

public class LibroDAO extends HttpAPIRest<LibroDTO> implements LibroDAOImpl {

    @Override
    public LibroDTO guardar(LibroDTO object) {
        doHttpRequest("http://localhost:8080/api/v1/bookers/libros/", HttpMethod.GET);
        return null;
    }

    @Override
    public LibroDTO editar(LibroDTO object) {
        return null;
    }

    @Override
    public LibroDTO eliminar(LibroDTO object) {
        return null;
    }

    @Override
    public LibroDTO buscar(Integer key) {
        return null;
    }

    @Override
    public List<LibroDTO> buscarTodos() {
        return null;
    }

    @Override
    public LibroDTO buscarPorISBN(String isbn) {
        return null;
    }

    @Override
    public List<LibroDTO> buscarPorTitulo(String titulo) {
        return null;
    }

    @Override
    public List<LibroDTO> buscarPorAutor(AutorDTO autor) {
        return null;
    }
}
