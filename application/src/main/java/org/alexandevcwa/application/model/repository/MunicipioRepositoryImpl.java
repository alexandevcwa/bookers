package org.alexandevcwa.application.model.repository;

import org.alexandevcwa.application.model.Municipio;

import java.util.List;

public interface MunicipioRepositoryImpl extends CRUDRepository<Municipio>{
    List<Municipio> findAllByDeptoId(int deptoId);
}
