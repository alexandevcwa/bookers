package org.alexandevcwa.application.model;

import lombok.*;

import java.util.Date;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Usuario {

    @Setter(AccessLevel.NONE)
    private int usrId;

    private String usrNombre;

    private String usrApellido;

    private String usrDireccion;

    private String usrCui;

    private String usrTelefono;

    private String usrEmail;

    private  String usrPassword;

    @Setter(AccessLevel.NONE)
    private Date usrRegistro;

    private Municipio municipio;
}
