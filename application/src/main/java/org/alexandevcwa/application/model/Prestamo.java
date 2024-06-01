package org.alexandevcwa.application.model;

import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Prestamo {
    private int presId;
    private LocalDate presFecha;
    private boolean presEstado;
    private LocalDate presFecVence;
    private LocalDate presFecDevo;
    private Usuario usuario;

}
