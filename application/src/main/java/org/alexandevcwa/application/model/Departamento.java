package org.alexandevcwa.application.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Departamento {
    private int deptoId;
    private String deptoNombre;
    private Pais pais;
}
