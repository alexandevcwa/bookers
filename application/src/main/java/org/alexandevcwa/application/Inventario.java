package org.alexandevcwa.application;

import lombok.*;
import org.alexandevcwa.application.model.Libro;

import java.time.LocalDate;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Inventario {
    private Libreria libreria;
    private Libro libro;
    private int invDisponible;
    private int invStockMin;
    private int invStockMax;
    private LocalDate invActualiza;
}