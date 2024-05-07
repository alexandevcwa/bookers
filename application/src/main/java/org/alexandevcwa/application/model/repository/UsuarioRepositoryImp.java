package org.alexandevcwa.application.model.repository;

public interface UsuarioRepositoryImp <T> extends CRUDRepository<T>{
    T findByEmail(String email);
    T findByCuiAndPassword(String cui, String password);
    boolean existsByCuiAndPassword(String cui, String password);
}
