package org.alexandevcwa.application.model;

import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PrestamoDetalle {
    private int presdId;
    private Prestamo prestamo;
    private Libreria libreria;
    private Libro libro;
    private int presdCantidad;
    private int presdEstado;
}
