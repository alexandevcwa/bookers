package org.alexandevcwa.application.model.repository;

import javafx.geometry.Pos;
import org.alexandevcwa.application.Inventario;
import org.alexandevcwa.application.Libreria;
import org.alexandevcwa.application.model.Autor;
import org.alexandevcwa.application.model.Categoria;
import org.alexandevcwa.application.model.Editorial;
import org.alexandevcwa.application.model.Libro;
import org.alexandevcwa.application.model.database.PostgreSQL;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.ListResourceBundle;

public class InventarioRepository implements InventarioRepositoryImpl {

    private final Connection connection;

    public InventarioRepository() {
        this.connection = PostgreSQL.getInstance().getConnection();
    }

    @Override
    public Inventario findById(int id) {
        return null;
    }

    @Override
    public List<Inventario> findAll() {
        return List.of();
    }

    @Override
    public int save(Inventario inventario) {
        try (PreparedStatement preparedStatement = connection.prepareStatement(INSERT_INVENTARIO_PRODUCTO)) {

            preparedStatement.setInt(1, inventario.getLibro().getLbrSku());
            preparedStatement.setInt(2, inventario.getInvDisponible());
            preparedStatement.setInt(3, inventario.getInvStockMin());
            preparedStatement.setInt(4, inventario.getInvStockMax());

            return preparedStatement.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void delete(Inventario inventario) {

    }

    @Override
    public int update(Inventario inventario) {
        return 0;
    }

    @Override
    public Inventario findInventarioByIsbnLibro(String isbn) {

        try (PreparedStatement preparedStatement = connection.prepareStatement(SELECT_INVENTARIO_BY_LIBRO_ISBN)) {
            preparedStatement.setString(1, isbn);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                return formatResultSetToInventario(resultSet, false);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return null;
    }

    @Override
    public List<Inventario> findAllWithCover() {
        try (PreparedStatement statement = connection.prepareStatement(SELECT_INVENTARIO_ALL_WITCH_COVER)) {
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                List<Inventario> inventarios = new ArrayList<>();
                do {
                    Inventario inventario = formatResultSetToInventario(resultSet, true);
                    inventarios.add(inventario);
                } while (resultSet.next());
                return inventarios;
            }
            return null;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<Inventario> findAllByTittleRegex(String regex) {
        try (PreparedStatement statement = connection.prepareStatement(SELECT_INVENTARIO_ALL_BY_TITTLE_REGEX)) {
            regex = "%" + regex + "%";
            statement.setString(1, regex);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                List<Inventario> inventarios = new ArrayList<>();
                do {
                    Inventario inventario = formatResultSetToInventario(resultSet, true);
                    inventarios.add(inventario);
                } while (resultSet.next());
                return inventarios;
            }
            return null;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private Inventario formatResultSetToInventario(ResultSet resultSet, boolean withCover) throws SQLException {
        Inventario inventario = Inventario.builder()
                .libreria(Libreria.builder()
                        .libId(resultSet.getInt("lib_id"))
                        .build())
                .libro(Libro.builder()
                        .lbrSku(resultSet.getInt("lbr_sku"))
                        .lbrTitulo(resultSet.getString("lbr_titulo"))
                        .lbrPublica(resultSet.getDate("lbr_publica").toLocalDate())
                        .lbrISBN(resultSet.getString("lbr_isbn"))
                        .autor(Autor.builder()
                                .autId(resultSet.getInt("aut_id"))
                                .autNombre(resultSet.getString("aut_nombre"))
                                .build())
                        .editorial(Editorial.builder()
                                .editId(resultSet.getInt("edit_id"))
                                .editNombre(resultSet.getString("edit_nombre"))
                                .build())
                        .categoria(Categoria.builder()
                                .categoId(resultSet.getInt("catego_id"))
                                .categoNombre(resultSet.getString("catego_nombre"))
                                .build())
                        .build())
                .invDisponible(resultSet.getInt("inv_disponible"))
                .invStockMin(resultSet.getInt("inv_stockmin"))
                .invStockMax(resultSet.getInt("inv_stockmax"))
                .invActualiza(resultSet.getDate("inv_actualiza").toLocalDate())
                .build();
        if (withCover) {
            inventario.getLibro().setLbrCover(resultSet.getBytes("lbr_cover"));
        }
        return inventario;
    }

    private final String INSERT_INVENTARIO_PRODUCTO = """
            INSERT INTO inventarios (lib_id, lbr_sku, inv_disponible, inv_stockmin, inv_stockmax)
            VALUES (1,?,?,?,?)
            """;

    private final String SELECT_INVENTARIO_BY_LIBRO_ISBN = """
            select inventarios.lib_id,libros.lbr_sku,libros.lbr_titulo,libros.lbr_publica,
                libros.lbr_isbn,autores.aut_id,autores.aut_nombre,editoriales.edit_id,editoriales.edit_nombre,
                categorias.catego_id,categorias.catego_nombre,inventarios.inv_disponible,inventarios.inv_stockmin,
                inventarios.inv_stockmax,inventarios.inv_actualiza
            FROM inventarios
            inner join libros on (libros.lbr_sku = inventarios.lbr_sku) and (libros.lbr_isbn = ?)
            inner join autores on (autores.aut_id = libros.aut_id)
            inner join editoriales on (editoriales.edit_id = libros.edit_id)
            inner join categorias on (categorias.catego_id = libros.catego_id)
            """;

    private final String SELECT_INVENTARIO_ALL_WITCH_COVER = """
            select inventarios.lib_id,libros.lbr_sku,libros.lbr_titulo,libros.lbr_cover,libros.lbr_publica,
                libros.lbr_isbn,autores.aut_id,autores.aut_nombre,editoriales.edit_id,editoriales.edit_nombre,
                categorias.catego_id,categorias.catego_nombre,inventarios.inv_disponible,inventarios.inv_stockmin,
                inventarios.inv_stockmax,inventarios.inv_actualiza
            FROM inventarios
            inner join libros on (libros.lbr_sku = inventarios.lbr_sku)
            inner join autores on (autores.aut_id = libros.aut_id)
            inner join editoriales on (editoriales.edit_id = libros.edit_id)
            inner join categorias on (categorias.catego_id = libros.catego_id)
            """;

    private final String SELECT_INVENTARIO_ALL_BY_TITTLE_REGEX = """
            select inventarios.lib_id,libros.lbr_sku,libros.lbr_titulo,libros.lbr_cover,libros.lbr_publica,
                libros.lbr_isbn,autores.aut_id,autores.aut_nombre,editoriales.edit_id,editoriales.edit_nombre,
                categorias.catego_id,categorias.catego_nombre,inventarios.inv_disponible,inventarios.inv_stockmin,
                inventarios.inv_stockmax,inventarios.inv_actualiza
            FROM inventarios
            inner join libros on (libros.lbr_sku = inventarios.lbr_sku and libros.lbr_titulo like ?)
            inner join autores on (autores.aut_id = libros.aut_id)
            inner join editoriales on (editoriales.edit_id = libros.edit_id)
            inner join categorias on (categorias.catego_id = libros.catego_id)
            """;

}