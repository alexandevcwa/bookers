package org.alexandevcwa.application.model.repository;

import org.alexandevcwa.application.model.Pais;
import org.alexandevcwa.application.model.database.PostgreSQL;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class PaisRepositoy  implements CRUDRepository<Pais>{
    private Connection connection;

    public PaisRepositoy() {
        this.connection = PostgreSQL.getInstance().getConnection();
    }



    @Override
    public Pais findById(int id) {
        return null;
    }

    @Override
    public List<Pais> findAll() {
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

    @Override
    public int save(Pais pais) {
        return 0;
    }

    @Override
    public void delete(Pais pais) {

    }

    @Override
    public int update(Pais pais) {
        return 0;
    }

    private final String SELECT_PAISES_ALL = "SELECT * FROM paises";
}
