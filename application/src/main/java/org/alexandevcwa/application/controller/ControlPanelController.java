package org.alexandevcwa.application.controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.StringConverter;
import lombok.Setter;
import org.alexandevcwa.application.BookersApplication;
import org.alexandevcwa.application.controller.util.TextFieldConfiguration;
import org.alexandevcwa.application.model.*;
import org.alexandevcwa.application.model.repository.*;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;

public class ControlPanelController {

    @Setter
    private Usuario usuario;

    @Setter
    private Stage controlPanelStage;

    // Repositorios

    private final CategoriaRepository categoriaRepository;

    private final AutorRepository autorRepository;

    private final EditorialRepository editorialRepository;

    private final LibreriaRepository libreriaRepository;

    private final LibroRepository libroRepository;

    // Panel de Control - Libros

    @FXML
    private Label label_Titulo;

    @FXML
    private TextField textField_Titulo;

    @FXML
    private DatePicker datePicker_Publica;

    @FXML
    private TextArea textArea_Previa;

    @FXML
    private TextField textField_Isbn;

    @FXML
    private ComboBox<Autor> comboBox_Autor;

    @FXML
    private ComboBox<Editorial> comboBox_Editorial;

    @FXML
    private ComboBox<Categoria> comboBox_Categoria;

    @FXML
    private ImageView imageView_Cover;

    @FXML
    private Hyperlink hyperlink_BuscarCover;

    @FXML
    private Button button_Guardar;

    @FXML
    private ToggleButton toggleButton_Actualizar;

    @FXML
    private RadioButton radioButton_Estado;

    @FXML
    private Label label_Message;

    @FXML
    private Button button_BuscarLibro;

    @FXML
    private Button button_Limpiar;

    private Libro libro = new Libro();

    public ControlPanelController() {
        this.autorRepository = new AutorRepository();
        this.categoriaRepository = new CategoriaRepository();
        this.libreriaRepository = new LibreriaRepository();
        this.libroRepository = new LibroRepository();
        this.editorialRepository = new EditorialRepository();
    }

    public void initialize() {

        //Configuración de los TextFields al perder el focus
        TextFieldConfiguration.textFieldUpperCase(textField_Titulo);

        TextFieldConfiguration.textFieldFocusLost(textField_Titulo, 1, 50, "El nombre del libro es requerido", label_Message);
        TextFieldConfiguration.textFieldFocusLost(textField_Isbn, 1, 25, "El ISBN es requerido", label_Message);

        //Asignar fecha de publicación al objeto libro a guardar
        datePicker_Publica.setOnAction(actionEvent -> {
            libro.setLbrPublica(datePicker_Publica.getValue());
        });

        //Evento Focus Lost, analizar el texto del TextArea al perder el focus
        textArea_Previa.focusedProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue) {
                if (textArea_Previa.getText().length() > 1 && textArea_Previa.getText().length() < 150) {
                    label_Message.setText(null);
                    libro.setLbrPrevia(textArea_Previa.getText());
                } else {
                    textArea_Previa.requestFocus();
                    label_Message.setText("La previa del libro es requerida");
                }
            }
        });

        button_Limpiar.setOnAction(actionEvent -> cleanComponents());

        //Evento clic para búsqueda de imagen del libro
        hyperlink_BuscarCover.setOnAction(actionEvent -> {
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Buscar Imagen de Libro");
            File file = fileChooser.showOpenDialog(controlPanelStage);
            if (file != null) {
                String path = file.toURI().toString();
                Image image = new Image(path);
                if (image != null) {
                    imageView_Cover.setImage(image);
                    try {
                        convertImageToBytes(path);
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                }
            }
        });
        loadComboBoxAutores();
        loadComboBoxCateogirias();
        loadComboBoxEditoriales();

        button_Guardar.setOnAction(actionEvent -> transactionLibro());

        setUpdateModeOn();
    }

    private void convertImageToBytes(String url) throws IOException {
        BufferedImage bufferedImage = ImageIO.read(new File(url.substring(6)));
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        ImageIO.write(bufferedImage, "png", byteArrayOutputStream);
        byte[] bytes = byteArrayOutputStream.toByteArray();
        libro.setLbrCover(bytes);
    }

    private void transactionLibro() {
        this.libro.setLbrTitulo(textField_Titulo.getText());
        this.libro.setLbrISBN(textField_Isbn.getText());
        this.libro.setLbrPublica(datePicker_Publica.getValue());
        this.libro.setLbrPrevia(textArea_Previa.getText());
        try {
            if (!textField_Titulo.textProperty().getValue().isEmpty()
                    && datePicker_Publica.valueProperty().getValue() != null
                    && !textArea_Previa.getText().isEmpty()
                    && !textField_Isbn.textProperty().getValue().isEmpty()
                    && comboBox_Autor.valueProperty().getValue() != null
                    && comboBox_Editorial.valueProperty().getValue() != null
                    && comboBox_Categoria.valueProperty().getValue() != null
                    && imageView_Cover.getImage() != null
            ) {
                int result = toggleButton_Actualizar.isSelected() ?
                        libroRepository.update(libro) : libroRepository.save(libro);
                label_Message.setText(String.format("Se realizo %s transacción correctamente", result));

            } else {
                label_Message.setText("Todos los datos deben de ser ingresados para poder guardar un nuevo libro");
            }

        } catch (Exception e) {
            System.out.println(e.getMessage());
            if (e.getMessage().startsWith("org.postgresql.util.PSQLException")) {
                label_Message.setText("El ISBN ingresado ya pertenece a otro libro");
            } else {
                label_Message.setText(e.getMessage());
            }

        }
    }

    private void loadComboBoxAutores() {
        var listaAutores = autorRepository.findAll();
        if (!listaAutores.isEmpty()) {
            comboBox_Autor.getItems().clear();
            comboBox_Autor.getItems().addAll(listaAutores);
            comboBox_Autor.setConverter(new StringConverter<Autor>() {
                @Override
                public String toString(Autor autor) {
                    if (autor == null) {
                        return null;
                    }
                    return autor.getAutNombre();
                }

                @Override
                public Autor fromString(String s) {
                    return null;
                }
            });
        }

        comboBox_Autor.setOnAction(actionEvent -> {
            libro.setAutor(comboBox_Autor.getValue());
        });
    }

    private void loadComboBoxCateogirias() {

        var listaCategorias = categoriaRepository.findAll();

        if (!listaCategorias.isEmpty()) {

            comboBox_Categoria.getItems().clear();
            listaCategorias.forEach((categoria) -> {
                comboBox_Categoria.getItems().add(categoria);
            });

            comboBox_Categoria.setConverter(new StringConverter<Categoria>() {
                @Override
                public String toString(Categoria categoria) {
                    if (categoria == null) {
                        return null;
                    }
                    return categoria.getCategoNombre();
                }

                @Override
                public Categoria fromString(String s) {
                    return null;
                }
            });
        }

        comboBox_Categoria.setOnAction(actionEvent -> {
            libro.setCategoria(comboBox_Categoria.getValue());
        });
    }

    private void loadComboBoxEditoriales() {

        var listaEditoriales = editorialRepository.findAll();

        if (!listaEditoriales.isEmpty()) {
            comboBox_Editorial.getItems().addAll(listaEditoriales);
            comboBox_Editorial.setConverter(new StringConverter<Editorial>() {
                @Override
                public String toString(Editorial editorial) {
                    if (editorial == null) {
                        return null;
                    }
                    return editorial.getEditNombre();
                }

                @Override
                public Editorial fromString(String s) {
                    return null;
                }
            });
        }

        comboBox_Editorial.setOnAction(actionEvent -> {
            libro.setEditorial(comboBox_Editorial.getValue());
        });
    }

    private void setUpdateModeOn() {
        toggleButton_Actualizar.setOnAction(actionEvent -> {
            if (toggleButton_Actualizar.isSelected()) {
                radioButton_Estado.setText("Activado");
                radioButton_Estado.setStyle("-fx-mark-color: #006bff;");
                button_BuscarLibro.setDisable(false);
                label_Titulo.setText("Actualización de Libro");
                button_Guardar.setText("Actualizar Libro");
            } else {
                radioButton_Estado.setText("Desactivado");
                radioButton_Estado.setStyle("-fx-mark-color: #404040;");
                button_BuscarLibro.setDisable(true);
                label_Titulo.setText("Agregar Nuevo Libro");
                button_Guardar.setText("Guardar Libro");
                cleanComponents();
            }
        });

        button_BuscarLibro.setOnAction(actionEvent -> {
            FXMLLoader fxmlLoader = new FXMLLoader(BookersApplication.class.getResource("bookers-tool-lista-libros.fxml"));
            Stage toolStage = new Stage(StageStyle.UTILITY);
            toolStage.initModality(Modality.WINDOW_MODAL);
            toolStage.initOwner(controlPanelStage);
            toolStage.setResizable(false);
            Scene scene = null;
            try {
                scene = new Scene(fxmlLoader.load());
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            toolStage.setScene(scene);

            ToolBuscarLibroController controller = fxmlLoader.getController();
            controller.setStage(toolStage);
            toolStage.showAndWait();
            this.libro = controller.getLibro();
            if (libro != null) {
                loadLibroToScreen(libro);
            } else {
                this.libro = new Libro();
            }
        });
    }

    private void cleanComponents(){
        textField_Titulo.clear();
        datePicker_Publica.setValue(null);
        textArea_Previa.clear();
        textField_Isbn.clear();
        comboBox_Categoria.setValue(null);
        comboBox_Editorial.setValue(null);
        comboBox_Autor.setValue(null);
        imageView_Cover.setImage(null);

    }

    private void loadLibroToScreen(Libro libro) {
        textField_Titulo.setText(libro.getLbrTitulo());
        datePicker_Publica.setValue(libro.getLbrPublica());
        textArea_Previa.setText(libro.getLbrPrevia());
        textField_Isbn.setText(libro.getLbrISBN());

        imageView_Cover.setImage(null);
        imageView_Cover.setImage(new Image(new ByteArrayInputStream(libro.getLbrCover())));

        for (Autor autor : comboBox_Autor.getItems()) {
            if (autor.getAutId() == libro.getAutor().getAutId()) {
                comboBox_Autor.setValue(autor);
            }
        }
        for (Editorial editorial : comboBox_Editorial.getItems()) {
            if (editorial.getEditId() == libro.getEditorial().getEditId()) {
                comboBox_Editorial.setValue(editorial);
            }
        }
        for (Categoria categoria : comboBox_Categoria.getItems()) {
            if (categoria.getCategoId() == libro.getCategoria().getCategoId()) {
                comboBox_Categoria.setValue(categoria);
            }
        }
    }
}