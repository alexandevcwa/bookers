package org.alexandevcwa.application.model.repository;

import javafx.geometry.Pos;
import org.alexandevcwa.application.model.Editorial;
import org.alexandevcwa.application.model.database.PostgreSQL;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class EditorialRepository implements CRUDRepository<Editorial>{

    private final  Connection connection;

    public EditorialRepository(){
        this.connection = PostgreSQL.getInstance().getConnection();
    }

    @Override
    public Editorial findById(int id) {
        return null;
    }

    @Override
    public List<Editorial> findAll() {
        try (Statement statement = connection.createStatement()){
            ResultSet resultSet = statement.executeQuery(SELECT_EDITORIAL_ALL);
            if (resultSet.next()){
                List<Editorial> editoriales = new ArrayList<>();
                do {
                    Editorial editorial = Editorial.builder()
                            .editId(resultSet.getInt("edit_id"))
                            .editNombre(resultSet.getString("edit_nombre"))
                            .build();
                    editoriales.add(editorial);
                }while (resultSet.next());
                return editoriales;
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return null;
    }

    @Override
    public int save(Editorial editorial) {
        return 0;
    }

    @Override
    public void delete(Editorial editorial) {

    }

    @Override
    public int update(Editorial editorial) {
        return 0;
    }

    private final String SELECT_EDITORIAL_ALL = "SELECT * FROM editoriales";
}
