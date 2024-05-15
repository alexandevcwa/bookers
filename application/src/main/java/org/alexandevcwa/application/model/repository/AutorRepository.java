package org.alexandevcwa.application.model.repository;

import org.alexandevcwa.application.model.Autor;
import org.alexandevcwa.application.model.database.PostgreSQL;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class AutorRepository implements CRUDRepository<Autor> {

    private final Connection connection;

    public AutorRepository() {
        this.connection = PostgreSQL.getInstance().getConnection();
    }

    @Override
    public Autor findById(int id) {
        return null;
    }

    @Override
    public List<Autor> findAll() {
        try (Statement statement = connection.createStatement()) {
            ResultSet resultSet = statement.executeQuery(SELECT_AUTORES_ALL);
            if (resultSet.next()) {
                List<Autor> autores = new ArrayList<>();
                do {
                    Autor autor = Autor.builder()
                            .autId(resultSet.getInt("aut_id"))
                            .autNombre(resultSet.getString("aut_nombre"))
                            .build();
                    autores.add(autor);
                } while (resultSet.next());
                return autores;
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return null;
    }

    @Override
    public int save(Autor autor) {
        return 0;
    }

    @Override
    public void delete(Autor autor) {

    }

    @Override
    public int update(Autor autor) {
        return 0;
    }

    private final String SELECT_AUTORES_ALL = "SELECT * FROM autores";
}
