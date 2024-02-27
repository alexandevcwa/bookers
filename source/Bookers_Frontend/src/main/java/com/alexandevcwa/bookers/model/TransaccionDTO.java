package com.alexandevcwa.bookers.model;

import com.alexandevcwa.bookers.model.enums.TipoTransaccion;

import java.time.LocalDate;
import java.util.List;

public class TransaccionDTO {
    private int id;
    private List<LibroDTO> libros;
    private UsuarioDTO usuario;
    private LocalDate fecha;
    private LocalDate fechaVence;
    private LocalDate fechaDevolucion;
    private TipoTransaccion tipoTransaccion;
    private float multa;
}
