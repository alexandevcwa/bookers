package org.alexandevcwa.application.model;

import lombok.*;
import org.alexandevcwa.application.Inventario;
import org.alexandevcwa.application.model.enumerator.InventarioTransaccion;

import java.time.LocalDate;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MovimientoInventario {

    private InventarioTransaccion mvnTipo;

    private int mvnCantidad;

    @Setter(AccessLevel.NONE)
    private LocalDate mvnFecha;

    private int mvnTotal;

    private int mvnReferenicia;

    private Inventario inventario;
}
