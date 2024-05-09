package org.alexandevcwa.application.model.repository;

import org.alexandevcwa.application.model.Libreria;
import org.alexandevcwa.application.model.database.PostgreSQL;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class LibreriaRepository {

    private final Connection connection;

    public LibreriaRepository() {
        this.connection = PostgreSQL.getInstance().getConnection();
    }

    public List<Libreria> getLibrerias(){
        try (Statement statement = connection.createStatement()){

            ResultSet resultSet = statement.executeQuery(SELECT_LIBRERIAS);
            List<Libreria> librerias = new ArrayList<>();
            while(resultSet.next()){
                Libreria libreria = new Libreria();
                libreria.setLibId(resultSet.getInt("lib_id"));
                libreria.setLibNombre(resultSet.getString("lib_nombre"));
                libreria.setLibDireccion(resultSet.getString("lib_direccion"));
                libreria.setLibZipcode(resultSet.getString("lib_zipcode"));
                libreria.setLibEmail(resultSet.getString("lib_email"));
                libreria.setLibTelefono(resultSet.getString("lib_telefono"));
                librerias.add(libreria);
            }
            return librerias;

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private final String SELECT_LIBRERIAS= "select * from librerias";


}
