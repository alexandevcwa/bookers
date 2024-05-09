package org.alexandevcwa.application.model.repository;

import org.alexandevcwa.application.model.Departamento;
import org.alexandevcwa.application.model.Pais;
import org.alexandevcwa.application.model.database.PostgreSQL;

import java.awt.image.MultiResolutionImage;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class DepartamentoRepository implements DepartamentoRepositoryimpl {

    private final Connection connection;

    public DepartamentoRepository() {
        this.connection = PostgreSQL.getInstance().getConnection();
    }

    @Override
    public Departamento findById(int id) {
        return null;
    }

    @Override
    public List<Departamento> findAll() {
        return List.of();
    }

    @Override
    public int save(Departamento departamento) {
        return 0;
    }

    @Override
    public void delete(Departamento departamento) {

    }

    @Override
    public int update(Departamento departamento) {
        return 0;
    }

    private final String SELECT_DEPARTAMENTOS_WHERE_PAIS = """
            SELECT ps.ps_id, ps.ps_nombre, depto.depto_id, depto.depto_nombre 
            FROM departamentos depto INNER JOIN paises ps on ps.ps_id = depto.ps_id
            WHERE depto.ps_id = ?
            """;

    @Override
    public List<Departamento> findAllByPsId(int psId) {

        try (PreparedStatement preparedStatement = connection.prepareStatement(SELECT_DEPARTAMENTOS_WHERE_PAIS)) {

            preparedStatement.setInt(1, psId);
            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet == null) {
                return null;
            }

            List<Departamento> departamentos = new ArrayList<>();
            while (resultSet.next()) {
                Departamento departamento = Departamento.builder()
                        .deptoId(resultSet.getInt("depto_id"))
                        .deptoNombre(resultSet.getString("depto_nombre"))
                        .pais(Pais.builder()
                                .psId(resultSet.getInt("ps_id"))
                                .psNombre(resultSet.getString("ps_nombre"))
                                .build())
                        .build();
                departamentos.add(departamento);
            }
            return departamentos;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
