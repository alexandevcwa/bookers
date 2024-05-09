package org.alexandevcwa.application.model.repository;

import org.alexandevcwa.application.model.Departamento;

import java.util.List;

public interface DepartamentoRepositoryimpl extends CRUDRepository<Departamento> {
    List<Departamento> findAllByPsId(int psId);
}
