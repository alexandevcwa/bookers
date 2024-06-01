package org.alexandevcwa.application.model.repository;

import org.alexandevcwa.application.model.Libro;
import org.alexandevcwa.application.model.Prestamo;
import org.alexandevcwa.application.model.Usuario;
import org.alexandevcwa.application.model.database.PostgreSQL;
import org.alexandevcwa.application.model.enumerator.EnumEstadoPrestamo;

import java.sql.*;
import java.time.LocalDate;
import java.util.List;

public class PrestamoRepository implements PrestamoRepositoryImpl  {

    private final Connection connection;

    public PrestamoRepository() {
        this.connection = PostgreSQL.getInstance().getConnection();
    }

    @Override
    public Prestamo findById(int id) {
        return null;
    }

    @Override
    public List<Prestamo> findAll() {
        return List.of();
    }

    @Override
    public int save(Prestamo prestamo) {
        return 0;
    }

    @Override
    public int save(Usuario usuario, List<Libro> libros) {
        try (PreparedStatement statement = connection.prepareStatement(INSERT_PRESTAMOS)){
            statement.setString(1, EnumEstadoPrestamo.PRESTAMO_ACTIVO.name());
            statement.setDate(2,Date.valueOf(LocalDate.now().plusDays(16)));
            statement.setDate(3,Date.valueOf(LocalDate.now().plusDays(15)));
            statement.setInt(4, usuario.getUsrId());
            int rows = statement.executeUpdate();

            if (rows == 0){
                return 0;
            }

            Prestamo prestamo = findLastPrestamoByUsuario(usuario);

            if (prestamo == null){
                return 0;
            }
            return savePrestamoDetalle(prestamo,libros);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void delete(Prestamo prestamo) {

    }

    @Override
    public int update(Prestamo prestamo) {
        return 0;
    }

    private Prestamo findLastPrestamoByUsuario(Usuario usuario) {
        try (PreparedStatement statement = connection.prepareStatement(SELECT_PRESTAMO_BY_CURRENT_DATE_AND_USUARIO)){
            statement.setInt(1,usuario.getUsrId());
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()){
                return Prestamo.builder()
                        .presId(resultSet.getInt("pre_id"))
                        .presFecha(resultSet.getDate("pres_fecha").toLocalDate())
                        .presFecVence(resultSet.getDate("pres_fecvence").toLocalDate())
                        .presFecDevo(resultSet.getDate("pres_fecdevo").toLocalDate())
                        .usuario(usuario)
                        .build();
            }
            return null;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private int savePrestamoDetalle(Prestamo prestamo, List<Libro> libros) throws SQLException {
        try (PreparedStatement statement =  connection.prepareStatement(INSERT_DETALLE_PRESTAMO)){
            connection.setAutoCommit(false);
            for (Libro libro : libros){
                statement.setInt(1,prestamo.getPresId());
                statement.setInt(2,1);
                statement.setInt(3,libro.getLbrSku());
                statement.setInt(4,1);
                statement.addBatch();
            }
            statement.executeBatch();
            connection.commit();
            return 1;
        } catch (SQLException e) {
            connection.rollback();
            throw new RuntimeException(e);
        }
    }

    private final String INSERT_PRESTAMOS = """
            INSERT INTO prestamos (pres_estado, pres_fecvence, pres_fecdevo, usr_id) VALUES (?::estado_prestamo,?,?,?);
            """;

    private final String SELECT_PRESTAMO_BY_CURRENT_DATE_AND_USUARIO = """
            SELECT * FROM prestamos WHERE pres_estado = 1 AND pres_fecha = CURRENT_DATE AND usr_id = ?;
            """;

    private final String INSERT_DETALLE_PRESTAMO = """
            INSERT INTO prestamosdetalle (pres_id,lib_id,lbr_sku,presd_cantidad) VALUES (?,?,?,?);
            """;
}
