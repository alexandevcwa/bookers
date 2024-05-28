-- ACTUALIZAR TABLA PRINCIPAL DE INVENTARIOS CON LOS MOVIMIENTOS DE INVENTARIO
create or replace function actualizar_inventario()
    returns trigger as
$$
begin
    update inventarios
    set inv_disponible = new.mvn_total,
        inv_actualiza  = new.mvn_fecha
    where lib_id = new.lib_id
      and lbr_sku = new.lbr_sku;
    return new;
end;
$$ language plpgsql;

create or replace trigger trigger_actualizar_inventario
    after insert
    on movimientosinventarios
    for each row
execute function actualizar_inventario();



create or replace function habilitar_inventario()
    returns trigger as
$$
begin
    insert into movimientosinventarios
    (mvn_tipo, mvn_cantidad, mvn_disponible, mvn_fecha, mvn_total, lib_id, lbr_sku)
    values ('ENTRADA_NUEVO', new.inv_disponible, 0, current_timestamp, new.inv_disponible, new.lib_id,
            new.lbr_sku);
    return new;
end;
$$ language plpgsql;
create or replace trigger trigger_habilitar_inventario
    after insert
    on inventarios
    for each row
execute function habilitar_inventario();
