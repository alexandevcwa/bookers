package org.alexandevcwa.application.model.repository;

import org.alexandevcwa.application.model.Pais;
import org.alexandevcwa.application.model.database.PostgreSQL;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class PaisRepositoy {
    private Connection connection;

    public PaisRepositoy() {
        this.connection = PostgreSQL.getInstance().getConnection();
    }

    public List<Pais> getAll() {
        try (Statement statement = connection.createStatement())  {
            ResultSet resultSet = statement.executeQuery(SELECT_PAISES_ALL);
            List<Pais> paises = new ArrayList<>();
            while (resultSet.next()){
                Pais pais = new Pais();
                pais.setPsId(resultSet.getInt("ps_id"));
                pais.setPsNombre(resultSet.getString("ps_nombre"));
                paises.add(pais);
            }
            return paises;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
    private final String SELECT_PAISES_ALL = "SELECT * FROM paises";
}
