package org.alexandevcwa.application;

import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Libreria {
    private int libId;
    private String libNombre;
    private String libDireccion;
    private String libZipCode;
    private String libEmail;
    private String libTelefono;
}
