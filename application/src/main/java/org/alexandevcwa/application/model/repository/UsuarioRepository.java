package org.alexandevcwa.application.model.repository;

import org.alexandevcwa.application.model.Usuario;
import org.alexandevcwa.application.model.database.PostgreSQL;

import java.nio.charset.StandardCharsets;
import java.sql.*;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

/**
 * @Author Alexander Machic
 */
public class UsuarioRepository implements UsuarioRepositoryImp<Usuario> {


    /**
     * Conexión a base de datos
     */
    private final Connection connection;

    public UsuarioRepository() {
        this.connection = PostgreSQL.getInstance().getConnection();
    }

    @Override
    public Usuario findByEmail(String email) {
        try (PreparedStatement preparedStatement = connection.prepareStatement(SELECT_USUARIO_BY_EMAIL)) {
            preparedStatement.setString(1, email);
            ResultSet resultSet = preparedStatement.executeQuery();
            return convertResultSetToObject(resultSet);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Conversión de un ResultSet a objeto {@link Usuario}, si el método retorna un null, significa que la
     * base de datos no retorno ningun registro
     *
     * @param resultSet Datos retornados por la base de datos
     * @return Objetos de tipo {@link Usuario}
     * @throws SQLException
     */
    private Usuario convertResultSetToObject(ResultSet resultSet) throws SQLException {
        if (resultSet.next()) {
            Usuario usuario = Usuario.builder()
                    .usrId(resultSet.getInt("usr_id"))
                    .usrNombre(resultSet.getString("usr_nombres"))
                    .usrApellido(resultSet.getString("usr_apellidos"))
                    .usrDireccion(resultSet.getString("usr_direccion"))
                    .usrCui(resultSet.getString("usr_cui"))
                    .usrTelefono(resultSet.getString("usr_telefono"))
                    .usrEmail(resultSet.getString("usr_email"))
                    //.usrPassword(resultSet.getString("usr_password"))
                    .usrRegistro(resultSet.getDate("usr_registro"))
                    .build();
            resultSet.close();
            return usuario;
        }
        return null;
    }

    /**
     * Conversión de un ResultSet a un {@link List} con objetos de tipo {@link Usuario}
     *
     * @param resultSet Datos retornados por base de datos
     * @return {@link List} de objetos de tipo {@link Usuario}
     * @throws SQLException
     */
    private List<Usuario> convertResultSetToList(ResultSet resultSet) throws SQLException {
        if (resultSet == null) {
            return null;
        }
        List<Usuario> usuariosList = new ArrayList<>();
        while (resultSet.next()) {
            Usuario usuario = Usuario.builder()
                    .usrId(resultSet.getInt("usr_id"))
                    .usrNombre(resultSet.getString("usr_nombres"))
                    .usrApellido(resultSet.getString("usr_apellidos"))
                    .usrCui(resultSet.getString("usr_cui"))
                    .usrTelefono(resultSet.getString("usr_telefono"))
                    .usrEmail(resultSet.getString("usr_email"))
                    //.usrPassword(resultSet.getString("usr_password"))
                    .usrRegistro(resultSet.getDate("usr_registro"))
                    .build();
            usuariosList.add(usuario);
        }
        resultSet.close();
        return usuariosList;
    }

    @Override
    public Usuario findByCuiAndPassword(String cui, String password) {
        try (PreparedStatement preparedStatement = connection.prepareStatement(SELECT_USUARIO_BY_CUI_AND_PASSWORD)) {
            String passwordEncoded = Base64.getEncoder().encodeToString(password.getBytes(StandardCharsets.UTF_8));
            preparedStatement.setString(1, cui);
            preparedStatement.setString(2, passwordEncoded);
            ResultSet resultSet = preparedStatement.executeQuery();
            return convertResultSetToObject(resultSet);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }


    @Override
    public boolean existsByCuiAndPassword(String cui, String password) {
        try (PreparedStatement preparedStatement = connection.prepareStatement(SELECT_EXISTS_USUARIO_BY_CUI_AND_PASSWORD)) {
            preparedStatement.setString(1, cui);
            String passwordEncoded = Base64.getEncoder().encodeToString(password.getBytes(StandardCharsets.UTF_8));
            preparedStatement.setString(2, passwordEncoded);
            ResultSet resultSet = preparedStatement.executeQuery();
            resultSet.next();
            boolean exists = resultSet.getBoolean("exists");
            resultSet.close();
            return exists;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Verificafor de existencia de registros genérico
     *
     * @param sql        Consulta SQL para ejecutar en base de datos
     * @param parameters Parametros de filtro de la consulta SQL en el WHERE
     * @return {@link Boolean} True: existe, False: no existe
     */
    private boolean existsByGeneric(String sql, List<Object> parameters) {
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            for (int i = 0; i < parameters.size(); i++) {
                preparedStatement.setObject(i + 1, parameters.get(i));
            }
            ResultSet resultSet = preparedStatement.executeQuery();
            boolean exists = false;
            if (resultSet.next()){
                exists = resultSet.getBoolean("exists");
            }
            resultSet.close();
            return exists;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }


    @Override
    public boolean existsByCui(String cui) {
        return existsByGeneric(SELECT_EXISTS_USUARIO_BY_CUI, List.of(cui));
    }

    @Override
    public boolean existsByEmail(String email) {
        return existsByGeneric(SELECT_EXISTS_USUARIO_BY_EMAIL, List.of(email));
    }

    @Override
    public boolean existsByTelefono(String telefono) {
        return existsByGeneric(SELECT_EXISTS_USUARIO_BY_TELEFONO, List.of(telefono));
    }

    @Override
    public boolean hasGrants(int usrId) {
        try (PreparedStatement preparedStatement = connection.prepareStatement(SELECT_EXISTS_USUARIO_ROLE_BY_ROLE)){
            ResultSet resultSet = preparedStatement.executeQuery();
            boolean grant = false;
            if (resultSet.next()){
                grant = resultSet.getBoolean("exists");
            }
            return  grant;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Usuario findById(int id) {
        try (PreparedStatement preparedStatement = connection.prepareStatement(SELECT_USUARIO_BY_ID)) {
            preparedStatement.setInt(1, id);
            ResultSet resultSet = preparedStatement.executeQuery();
            return convertResultSetToObject(resultSet);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<Usuario> findAll() {
        try (Statement statement = connection.createStatement()) {
            ResultSet resultSet = statement.executeQuery(SELECT_USUARIO_BY_ALL);
            return convertResultSetToList(resultSet);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public int save(Usuario usuario) {
        try (PreparedStatement preparedStatement = connection.prepareStatement(INSERT_USUARIO)) {
            preparedStatement.setString(1, usuario.getUsrNombre());
            preparedStatement.setString(2, usuario.getUsrApellido());
            preparedStatement.setString(3, usuario.getUsrDireccion());
            preparedStatement.setString(4, usuario.getUsrCui());
            preparedStatement.setString(5, usuario.getUsrTelefono());
            preparedStatement.setString(6, usuario.getUsrEmail());
            String passwordEncoded = Base64.getEncoder().encodeToString(usuario.getUsrPassword().getBytes(StandardCharsets.UTF_8));
            preparedStatement.setString(7, passwordEncoded);
            preparedStatement.setInt(8, usuario.getMunicipio().getMuniId());
            return preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void delete(Usuario usuario) {

    }

    @Override
    public int update(Usuario usuario) {
        try (PreparedStatement preparedStatement = connection.prepareStatement(UPDATE_USUARIO)) {
            preparedStatement.setString(1, usuario.getUsrNombre());
            preparedStatement.setString(2, usuario.getUsrApellido());
            preparedStatement.setString(3, usuario.getUsrDireccion());
            preparedStatement.setString(4, usuario.getUsrCui());
            preparedStatement.setString(5, usuario.getUsrTelefono());
            preparedStatement.setString(6, usuario.getUsrEmail());
            preparedStatement.setInt(7, usuario.getMunicipio().getMuniId());
            preparedStatement.setInt(8, usuario.getUsrId());
            return preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private final String SELECT_USUARIO_BY_EMAIL = "SELECT * FROM usuarios WHERE usr_email = ?";

    private final String SELECT_USUARIO_BY_CUI = "SELECT * FROM usuarios WHERE usr_cui = ?";

    private final String SELECT_USUARIO_BY_CUI_AND_PASSWORD = "SELECT * FROM usuarios WHERE usr_cui = ? AND usr_password = ?";

    private final String SELECT_USUARIO_BY_ID = "SELECT * FROM usuarios WHERE usr_id = ?";

    private final String SELECT_USUARIO_BY_ALL = "SELECT * FROM usuarios";

    private final String INSERT_USUARIO = """ 
            INSERT INTO usuarios (usr_nombres, usr_apellidos, usr_direccion, usr_cui,
            usr_telefono, usr_email, usr_password, muni_id)
            VALUES (?,?,?,?,?,?,?,?)""";

    private final String UPDATE_USUARIO = """
            UPDATE usuarios SET usr_nombres = ?, usr_apellidos = ?, usr_direccion = ?,
            usr_cui = ?, usr_telefono = ?, usr_email = ?, muni_id = ?, usr_registro = CURRENT_TIMESTAMP WHERE usr_id = ?
            """;

    private final String SELECT_EXISTS_USUARIO_BY_CUI_AND_PASSWORD = "SELECT EXISTS (SELECT * FROM usuarios WHERE usr_cui = ? AND usr_password = ?)";

    private final String SELECT_EXISTS_USUARIO_BY_CUI = "SELECT EXISTS (SELECT * FROM usuarios WHERE usr_cui = ?)";

    private final String SELECT_EXISTS_USUARIO_BY_EMAIL = "SELECT EXISTS (SELECT * FROM usuarios WHERE usr_email = ?)";

    private final String SELECT_EXISTS_USUARIO_BY_TELEFONO = "SELECT EXISTS (SELECT * FROM usuarios WHERE usr_telefono = ?)";

    private final String DELETE_USUARIO = "DELETE FROM usuarios WHERE usr_id = ?";

    private final String SELECT_EXISTS_USUARIO_ROLE_BY_ROLE = """
            SELECT EXISTS(SELECT * FROM usuariosroles WHERE usr_id = ? AND role_id = 
            (SELECT role_id FROM roles WHERE role_nombre = ?))
            """;
}
