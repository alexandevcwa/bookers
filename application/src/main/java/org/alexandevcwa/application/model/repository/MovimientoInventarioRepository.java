package org.alexandevcwa.application.model.repository;

import org.alexandevcwa.application.model.MovimientoInventario;
import org.alexandevcwa.application.model.database.PostgreSQL;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

public class MovimientoInventarioRepository implements CRUDRepository<MovimientoInventario> {

    private Connection connection;

    public MovimientoInventarioRepository() {
        this.connection = PostgreSQL.getInstance().getConnection();
    }


    @Override
    public MovimientoInventario findById(int id) {
        return null;
    }

    @Override
    public List<MovimientoInventario> findAll() {
        return List.of();
    }

    @Override
    public int save(MovimientoInventario movimientoInventario) {
        try (PreparedStatement preparedStatement = connection.prepareStatement(INSERT_MOVIMIENTO_INVENTARIO)) {
            preparedStatement.setString(1, movimientoInventario.getMvnTipo().name());
            System.out.println( movimientoInventario.getMvnTipo().name());
            preparedStatement.setInt(2, movimientoInventario.getMvnCantidad());
            preparedStatement.setInt(3, movimientoInventario.getInventario().getInvDisponible());
            preparedStatement.setInt(4, (movimientoInventario.getMvnTotal()));
            preparedStatement.setInt(5, movimientoInventario.getInventario().getLibreria().getLibId());
            preparedStatement.setInt(6, movimientoInventario.getInventario().getLibro().getLbrSku());
            return preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void delete(MovimientoInventario movimientoInventario) {

    }

    @Override
    public int update(MovimientoInventario movimientoInventario) {
        return 0;
    }

    private final String INSERT_MOVIMIENTO_INVENTARIO = """
            INSERT INTO movimientosinventarios (mvn_tipo, mvn_cantidad, mvn_disponible, mvn_total, lib_id, lbr_sku)
            VALUES (?::tipo_movimiento_inventario,?,?,?,?,?)
            """;
}