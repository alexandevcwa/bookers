-- ACTUALIZAR TABLA PRINCIPAL DE INVENTARIOS CON LOS MOVIMIENTOS DE INVENTARIO
create or replace function actualizar_inventario()
    returns trigger as
$$
begin
    update inventarios
    set inv_disponible = new.mvn_total,
        inv_actualiza  = new.mvn_fecha
    where inv_id = new.inv_id
      and per_periodo = new.per_periodo
      and lib_id = new.lib_id
      and lbr_sku = new.lbr_sku;
    return new;
end;
$$ language plpgsql;

create or replace trigger trigger_actualizar_inventario
    after insert
    on movimientosinventarios
    for each row
execute function actualizar_inventario();

-- INSERTAR MOVIMIENTO INICIAL DE INVENTARIO POR PRODUCTO EN TABLA DE MOVIMIENTOS
-- CADA VEZ QUE SE CREE UN NUEVO PRODUCTO