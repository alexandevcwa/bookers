package org.alexandevcwa.application.model.repository;

import org.alexandevcwa.application.model.Autor;
import org.alexandevcwa.application.model.Categoria;
import org.alexandevcwa.application.model.Editorial;
import org.alexandevcwa.application.model.Libro;
import org.alexandevcwa.application.model.database.PostgreSQL;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class LibroRepository implements LibroRepositoryImpl {

    private final Connection connection;

    public LibroRepository() {
        this.connection = PostgreSQL.getInstance().getConnection();
    }

    @Override
    public Libro findById(int id) {
        return null;
    }

    @Override
    public List<Libro> findAll() {
        try (Statement statement = connection.createStatement()) {

            ResultSet resultSet = statement.executeQuery(SELECT_LIBRO_ALL);
            List<Libro> libros = null;
            if (resultSet.next()) {
                libros = new ArrayList<>();
                do {
                    Libro libro = convertResultSetToLibro(resultSet);
                    libros.add(libro);
                } while (resultSet.next());
                return libros;
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return null;
    }

    @Override
    public int save(Libro libro) {
        try (PreparedStatement statement = connection.prepareStatement(INSERT_LIBRO)){
            return executeUpdate(statement,libro);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private int executeUpdate(PreparedStatement statement, Libro libro) throws SQLException {
            statement.setString(1, libro.getLbrTitulo());
            statement.setDate(2, Date.valueOf(libro.getLbrPublica()));
            statement.setBytes(3, libro.getLbrCover());
            statement.setString(4, libro.getLbrPrevia());
            statement.setString(5, libro.getLbrISBN());
            statement.setInt(6, libro.getCategoria().getCategoId());
            statement.setInt(7, libro.getAutor().getAutId());
            statement.setInt(8, libro.getEditorial().getEditId());
            int result =  statement.executeUpdate();
            statement.close();
            return result;
    }

    @Override
    public void delete(Libro libro) {

    }

    @Override
    public int update(Libro libro) {
        try (PreparedStatement preparedStatement = connection.prepareStatement(UPDATE_LIBRO_BY_SKU)){
            preparedStatement.setInt(9, libro.getLbrSku());
            return executeUpdate(preparedStatement,libro);

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }


    @Override
    public List<Libro> findLikeLbrTitulo(String patter) {
        try (PreparedStatement preparedStatement = connection.prepareStatement(SELECT_LIBRO_LIKE_TITULO)) {
            preparedStatement.setString(1, (new StringBuffer().append("%")
                    .append(patter)
                    .append("%")
                    .toString()));
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                List<Libro> libros = new ArrayList<>();
                do {
                    Libro libro = convertResultSetToLibro(resultSet);
                    libros.add(libro);
                } while (resultSet.next());
                return libros;
            }
            return null;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private Libro convertResultSetToLibro(ResultSet resultSet) throws SQLException {
        Libro libro = new Libro();
        libro.setLbrSku(resultSet.getInt("lbr_sku"));
        libro.setLbrTitulo(resultSet.getString("lbr_titulo"));
        libro.setLbrPublica(resultSet.getDate("lbr_publica").toLocalDate());
        libro.setLbrCover(resultSet.getBytes("lbr_cover"));
        libro.setLbrPrevia(resultSet.getString("lbr_previa"));
        libro.setLbrISBN(resultSet.getString("lbr_isbn"));
        libro.setCategoria(Categoria.builder()
                .categoId(resultSet.getInt("catego_id"))
                .categoNombre(resultSet.getString("catego_nombre")).build());
        libro.setAutor(Autor.builder()
                .autId(resultSet.getInt("aut_id"))
                .autNombre(resultSet.getString("aut_nombre")).build());
        libro.setEditorial(Editorial.builder()
                .editId(resultSet.getInt("edit_id"))
                .editNombre(resultSet.getString("edit_nombre")).build());
        return libro;
    }


    @Override
    public Libro findByLbrISBN(String isbn) {
        try (PreparedStatement statement = connection.prepareStatement(SELECT_LIBRO_BY_ISBN)) {
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                return convertResultSetToLibro(resultSet);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return null;
    }

    @Override
    public boolean existsByLbrISBN(String isbn) {
        try (PreparedStatement statement = connection.prepareStatement(EXISTS_LIBRO_BY_ISBN)) {
            statement.setString(1, isbn);
            ResultSet resultSet = statement.executeQuery();
            resultSet.next();
            return resultSet.getBoolean("exists");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<Libro> findAllByPagination(int start, int end) {
        return List.of();
    }

    private final String SELECT_LIBRO_BY_ISBN = """
            SELECT lbr_sku, lbr_titulo, lbr_publica, lbr_cover, lbr_previa, lbr_isbn, catego.catego_id, catego.catego_nombre, aut.aut_id, aut.aut_nombre, edit.edit_id, edit.edit_nombre
            FROM libros lbr
            INNER JOIN categorias catego on (catego.catego_id = lbr.catego_id)
            INNER JOIN autores aut on (aut.aut_id = lbr.aut_id)
            INNER JOIN editoriales edit on (edit.edit_id = lbr.edit_id)
            WHERE lbr_isbn = ?
            """;

    private final String SELECT_LIBRO_LIKE_TITULO = """
            SELECT lbr_sku, lbr_titulo, lbr_publica, lbr_cover, lbr_previa, lbr_isbn, catego.catego_id, catego.catego_nombre, aut.aut_id, aut.aut_nombre, edit.edit_id, edit.edit_nombre
            FROM libros lbr
            INNER JOIN categorias catego on (catego.catego_id = lbr.catego_id)
            INNER JOIN autores aut on (aut.aut_id = lbr.aut_id)
            INNER JOIN editoriales edit on (edit.edit_id = lbr.edit_id)
            WHERE lbr.lbr_titulo LIKE ?
            """;

    private final String SELECT_LIBRO_ALL = """
            SELECT lbr_sku, lbr_titulo, lbr_publica, lbr_cover, lbr_previa, lbr_isbn, catego.catego_id, catego.catego_nombre, aut.aut_id, aut.aut_nombre, edit.edit_id, edit.edit_nombre
            FROM libros lbr
            INNER JOIN categorias catego on (catego.catego_id = lbr.catego_id)
            INNER JOIN autores aut on (aut.aut_id = lbr.aut_id)
            INNER JOIN editoriales edit on (edit.edit_id = lbr.edit_id)
            """;

    private final String INSERT_LIBRO = """
            INSERT INTO libros (lbr_titulo, lbr_publica, lbr_cover, lbr_previa, lbr_isbn, catego_id, aut_id, edit_id)
            VALUES (?,?,?,?,?,?,?,?)
            """;

    private final String UPDATE_LIBRO_BY_SKU = """
            UPDATE libros SET lbr_titulo = ?, lbr_publica = ?, lbr_cover = ?, lbr_previa = ?, lbr_isbn = ?, catego_id = ?,
            aut_id = ?, edit_id = ?
            WHERE lbr_sku = ?
            """;

    private final String DELETE_LIBRO_BY_SKU = "DELETE FROM libros WHERE lbr_sku = ?";

    private final String EXISTS_LIBRO_BY_ISBN = "SELECT EXISTS(SELECT * FROM libros WHERE lbr_isbn = ?)";
}
