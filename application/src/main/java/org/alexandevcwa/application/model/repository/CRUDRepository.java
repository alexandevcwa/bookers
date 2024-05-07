package org.alexandevcwa.application.model.repository;

import java.util.List;

public interface CRUDRepository <T> {
    T findById(int id);
    List<T> findAll();
    int save(T t);
    void delete(T t);
    int update(T t);
}
