package org.alexandevcwa.application.model;

import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Libro {
    private int lbrSku;
    private String lbrTitulo;
    private LocalDate lbrPublica;
    private byte[] lbrCover;
    private String lbrPrevia;
    private String lbrISBN;
    private Autor autor;
    private Editorial editorial;
    private Categoria categoria;
}
