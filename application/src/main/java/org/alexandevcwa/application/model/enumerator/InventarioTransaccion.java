package org.alexandevcwa.application.model.enumerator;

public enum InventarioTransaccion {
    ENTRADA_DEVOLUCION("ENTRADA POR DEVOLUCIÓN"),
    ENTRADA_NUEVO("ENTRADA NUEVO INGRESO"),
    SALIDA_PRESTAMO("SALIDA POR PRESTAMO"),
    SALIDA_DESGASTE("SALIDA POR DESGASTE"),
    AJUSTE_INV_ENTRADA("AJUSTE DE INVENTARIO (AGREGAR)"),
    AJUSTE_INV_SALIDA("AJUSTE DE INVENTARIO (DESCONTAR)"),
    CONFIGURACION_INICIAL("HABILITAR INVENTARIO");


    private final String transaccion;

    InventarioTransaccion(String transaccion) {
        this.transaccion = transaccion;
    }

    public String getTransaccion() {
        return transaccion;
    }
}
