package org.alexandevcwa.application.model.repository;

import org.alexandevcwa.application.model.Municipio;
import org.alexandevcwa.application.model.database.PostgreSQL;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class MunicipioRepository implements MunicipioRepositoryImpl {
    private final Connection connection;

    public MunicipioRepository() {
        this.connection = PostgreSQL.getInstance().getConnection();
    }


    @Override
    public List<Municipio> findAllByDeptoId(int deptoId) {
        try (PreparedStatement preparedStatement = connection.prepareStatement(SELECT_MUNICIPIOS_WHERE_DEPARTAMENTO)) {
            preparedStatement.setInt(1, deptoId);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet == null) {
                return null;
            }
            List<Municipio> municipios = new ArrayList<>();
            while (resultSet.next()) {
                Municipio municipio = Municipio.builder()
                        .muniId(resultSet.getInt("muni_id"))
                        .muniNombre(resultSet.getString("muni_nombre"))
                        .build();
                municipios.add(municipio);
            }
            return municipios;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Municipio findById(int id) {
        return null;
    }

    @Override
    public List<Municipio> findAll() {
        return List.of();
    }

    @Override
    public int save(Municipio municipio) {
        return 0;
    }

    @Override
    public void delete(Municipio municipio) {

    }

    @Override
    public int update(Municipio municipio) {
        return 0;
    }

    private final String SELECT_MUNICIPIOS_WHERE_DEPARTAMENTO = "SELECT muni_id, muni_nombre FROM municipios where depto_id = ?";
}
