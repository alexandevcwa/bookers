package org.alexandevcwa.application.model.repository;

import org.alexandevcwa.application.model.Libreria;
import org.alexandevcwa.application.model.database.PostgreSQL;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class LibreriaRepository implements CRUDRepository<Libreria> {

    private final Connection connection;

    public LibreriaRepository() {
        this.connection = PostgreSQL.getInstance().getConnection();
    }

    @Override
    public Libreria findById(int id) {
        return null;
    }

    @Override
    public List<Libreria> findAll() {
        try (Statement statement = connection.createStatement()) {
            ResultSet resultSet = statement.executeQuery(SELECT_LIBRERIA_ALL);
            if (resultSet.next()) {
                List<Libreria> librerias = new ArrayList<>();
                do {
                    Libreria libreria = new Libreria();
                    libreria.setLibId(resultSet.getInt("lib_id"));
                    libreria.setLibNombre(resultSet.getString("lib_nombre"));
                    libreria.setLibDireccion(resultSet.getString("lib_direccion"));
                    libreria.setLibZipcode(resultSet.getString("lib_zipcode"));
                    libreria.setLibEmail(resultSet.getString("lib_email"));
                    libreria.setLibTelefono(resultSet.getString("lib_telefono"));
                    librerias.add(libreria);
                } while (resultSet.next());
                return librerias;
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return null;
    }

    @Override
    public int save(Libreria libreria) {
        return 0;
    }

    @Override
    public void delete(Libreria libreria) {

    }

    @Override
    public int update(Libreria libreria) {
        return 0;
    }

    private final String SELECT_LIBRERIA_ALL = "SELECT * FROM librerias";

}
