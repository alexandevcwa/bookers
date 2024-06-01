package org.alexandevcwa.application.model.repository;

import org.alexandevcwa.application.model.Libro;
import org.alexandevcwa.application.model.Prestamo;
import org.alexandevcwa.application.model.Usuario;

import java.util.List;

public interface PrestamoRepositoryImpl extends CRUDRepository<Prestamo>{

    int save(Usuario usuario, List<Libro> libros);
}
