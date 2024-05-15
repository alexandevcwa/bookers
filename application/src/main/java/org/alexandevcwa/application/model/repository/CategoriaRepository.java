package org.alexandevcwa.application.model.repository;

import javafx.geometry.Pos;
import org.alexandevcwa.application.model.Categoria;
import org.alexandevcwa.application.model.database.PostgreSQL;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class CategoriaRepository implements CRUDRepository<Categoria> {

    private final Connection connection;

    public CategoriaRepository() {
        this.connection = PostgreSQL.getInstance().getConnection();
    }

    @Override
    public Categoria findById(int id) {
        return null;
    }

    @Override
    public List<Categoria> findAll() {
        try (Statement statement = connection.createStatement()) {
            ResultSet resultSet = statement.executeQuery(SELECT_CATEGORIA_ALL);
            if (resultSet.next()) {
                List<Categoria> categorias = new ArrayList<>();
                do {
                    Categoria categoria = Categoria.builder()
                            .categoId(resultSet.getInt("catego_id"))
                            .categoNombre(resultSet.getString("catego_nombre"))
                            .build();
                    categorias.add(categoria);
                } while (resultSet.next());
                return categorias;
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return null;
    }

    @Override
    public int save(Categoria categoria) {
        return 0;
    }

    @Override
    public void delete(Categoria categoria) {

    }

    @Override
    public int update(Categoria categoria) {
        return 0;
    }

    private final String SELECT_CATEGORIA_ALL = "SELECT * FROM categorias";
}
