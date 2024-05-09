package org.alexandevcwa.application.model;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class Departamento {
    private int deptoId;
    private String deptoNombre;
    private Pais pais;
}
