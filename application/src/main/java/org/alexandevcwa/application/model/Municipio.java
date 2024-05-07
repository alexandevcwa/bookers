package org.alexandevcwa.application.model;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class Municipio {
    private int muniId;
    private String muniNombre;
    private Departamento departamento;
}
