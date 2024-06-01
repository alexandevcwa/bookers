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
import org.alexandevcwa.application.Inventario;
import org.alexandevcwa.application.Libreria;
import org.alexandevcwa.application.model.enumerator.InventarioTransaccion;
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

    ///////////////////////////
    //[REPOSITORIOS DE DATOS]//
    ///////////////////////////

    /**
     * Repositorio de Categorías de Libros
     */
    private final CategoriaRepository categoriaRepository;

    /**
     * Repositorio de Autores de Libros
     */
    private final AutorRepository autorRepository;

    /**
     * Repositorio de Editoriales de Libros
     */
    private final EditorialRepository editorialRepository;

    /**
     * Repositorio de Libros
     */
    private final LibroRepository libroRepository;

    /**
     * Repositorio de Inventario
     */
    private final InventarioRepository inventarioRepository;

    /**
     * Repositorio de Movimientos de Inventario
     */
    private final MovimientoInventarioRepository movimientoInventarioRepository;

    /////////////////////////////
    //[COMPONENTES DE PANTALLA]//
    /////////////////////////////

    ////////////////////////////////////
    //[PANTALLA DE REGISTRO DE LIBROS]//
    ////////////////////////////////////

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

    ///////////////////////////////
    //[COMPONENTES DE INVENTARIO]//
    ///////////////////////////////

    @FXML
    private TextField textField_InvIsbn;

    @FXML
    private ComboBox<InventarioTransaccion> comboBox_Transaccion;

    @FXML
    private TextField textField_InvMin;

    @FXML
    private TextField textField_InvMax;

    @FXML
    private TextField textField_InvCantidad;

    @FXML
    private Button button_GuardarTransaccion;

    @FXML
    private Button button_BuscarInventario;

    @FXML
    private Label label_InvTitulo;

    @FXML
    private Label label_InvPublica;

    @FXML
    private Label label_InvAutor;

    @FXML
    private Label label_InvCategoria;

    @FXML
    private Label label_InvEditorial;

    @FXML
    private Label label_InvCantidad;

    @FXML
    private Label label_InvMessage;

    @FXML
    private TextField textField_ReferenciaDevolucion;

    ////////////////////////////////////////
    //[OBJETOS PARA GUARDAR TRANSACCIONES]//
    ////////////////////////////////////////

    private Libro globalLibro;

    private Inventario globalInventario;

    private MovimientoInventario glovalMovimientoInventario;

    /**
     * Método constructor de clase controlador
     */
    public ControlPanelController() {
        this.autorRepository = new AutorRepository();
        this.categoriaRepository = new CategoriaRepository();
        this.libroRepository = new LibroRepository();
        this.editorialRepository = new EditorialRepository();
        this.inventarioRepository = new InventarioRepository();
        this.movimientoInventarioRepository = new MovimientoInventarioRepository();

        this.globalLibro = new Libro();
    }

    /**
     * Método de inicialización de componentes
     */
    public void initialize() {

        //////////////////////////////////////////////////
        //[CONFIGURACIÓN PARA PANEL DE CONTROL - LIBROS]//
        //////////////////////////////////////////////////

        TextFieldConfiguration.textFieldUpperCase(textField_Titulo);
        TextFieldConfiguration.textFieldFocusLost(textField_Titulo, 1, 50, "El nombre del libro es requerido", label_Message);
        TextFieldConfiguration.textFieldFocusLost(textField_Isbn, 1, 25, "El ISBN es requerido", label_Message);

        //Asignar fecha de publicación al objeto libro a guardar
        datePicker_Publica.setOnAction(actionEvent -> {
            globalLibro.setLbrPublica(datePicker_Publica.getValue());
        });

        //Evento Focus Lost, analizar el texto del TextArea al perder el focus
        textArea_Previa.focusedProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue) {
                if (textArea_Previa.getText().length() > 1 && textArea_Previa.getText().length() < 150) {
                    label_Message.setText(null);
                    globalLibro.setLbrPrevia(textArea_Previa.getText());
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

        button_Guardar.setOnAction(actionEvent -> transactionSaveBook());

        // Cambiar a modo actualización
        updateBookModeOn();

        ///////////////////////////////////////////////////////
        //[CONFIGURACIÓN PARA PANEL DE CONTROL - INVENTARIOS]//
        ///////////////////////////////////////////////////////

        TextFieldConfiguration.textFieldSetRegex(textField_InvMin, "\\d*");
        TextFieldConfiguration.textFieldSetRegex(textField_InvMax, "\\d*");
        TextFieldConfiguration.textFieldSetRegex(textField_InvCantidad, "\\d*");
        TextFieldConfiguration.textFieldSetRegex(textField_ReferenciaDevolucion, "\\d*");

        comboBox_Transaccion.getItems().addAll(InventarioTransaccion.values());
        comboBox_Transaccion.setConverter(new StringConverter<InventarioTransaccion>() {
            @Override
            public String toString(InventarioTransaccion invTransaccion) {
                if (invTransaccion != null) {
                    if (invTransaccion != InventarioTransaccion.CONFIGURACION_INICIAL){
                        textField_InvMax.setDisable(true);
                        textField_InvMin.setDisable(true);
                    }else {
                        textField_InvMax.setDisable(false);
                        textField_InvMin.setDisable(false);
                    }
                    if (invTransaccion == InventarioTransaccion.ENTRADA_DEVOLUCION || invTransaccion == InventarioTransaccion.SALIDA_PRESTAMO){
                        textField_ReferenciaDevolucion.setDisable(false);
                    }else {
                        textField_ReferenciaDevolucion.setDisable(true);
                    }
                    return invTransaccion.getTransaccion();
                }
                return null;
            }

            @Override
            public InventarioTransaccion fromString(String s) {
                return null;
            }
        });

    }

    /**
     * Conversión de imágenes a bytes para guardar en base de datos
     */
    private void convertImageToBytes(String url) throws IOException {
        System.out.println(url.substring(6));
        BufferedImage bufferedImage = ImageIO.read(new File(url.substring(6)));
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        ImageIO.write(bufferedImage, "png", byteArrayOutputStream);
        byte[] bytes = byteArrayOutputStream.toByteArray();
        globalLibro.setLbrCover(bytes);
    }

    /**
     * Método para guardas un libro en base de datos
     */
    private void transactionSaveBook() {
        this.globalLibro.setLbrTitulo(textField_Titulo.getText());
        this.globalLibro.setLbrISBN(textField_Isbn.getText());
        this.globalLibro.setLbrPublica(datePicker_Publica.getValue());
        this.globalLibro.setLbrPrevia(textArea_Previa.getText());
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
                        libroRepository.update(globalLibro) : libroRepository.save(globalLibro);
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

    /**
     * Cargar los autores registrados de los libros
     */
    private void loadComboBoxAutores() {
        var listaAutores = autorRepository.findAll();
        if (!listaAutores.isEmpty()) {
            comboBox_Autor.getItems().clear();
            comboBox_Autor.getItems().addAll(listaAutores);
            comboBox_Autor.setConverter(new StringConverter<Autor>() {

                @Override
                public String toString(Autor autor) {
                    return (autor == null ? null : autor.getAutNombre());
                }

                @Override
                public Autor fromString(String s) {
                    return null;
                }
            });
        }

        comboBox_Autor.setOnAction(actionEvent -> {
            globalLibro.setAutor(comboBox_Autor.getValue());
        });
    }

    /**
     * Cargar las categorías de los libros
     */
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
                    return (categoria == null ? null : categoria.getCategoNombre());
                }

                @Override
                public Categoria fromString(String s) {
                    return null;
                }
            });
        }

        comboBox_Categoria.setOnAction(actionEvent -> {
            globalLibro.setCategoria(comboBox_Categoria.getValue());
        });
    }

    /**
     * Cargar las editoriales de los libros
     */
    private void loadComboBoxEditoriales() {

        var listaEditoriales = editorialRepository.findAll();

        if (!listaEditoriales.isEmpty()) {
            comboBox_Editorial.getItems().addAll(listaEditoriales);
            comboBox_Editorial.setConverter(new StringConverter<Editorial>() {
                @Override
                public String toString(Editorial editorial) {
                    return (editorial == null ? null : editorial.getEditNombre());
                }

                @Override
                public Editorial fromString(String s) {
                    return null;
                }
            });
        }

        comboBox_Editorial.setOnAction(actionEvent -> {
            globalLibro.setEditorial(comboBox_Editorial.getValue());
        });
    }

    private void updateBookModeOn() {
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
            this.globalLibro = openWindowToFindBook();
            if (globalLibro != null) {
                loadBookToShowOnScreen(globalLibro);
            } else {
                this.globalLibro = new Libro();
            }
        });
    }

    private Libro openWindowToFindBook() {
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
        return controller.getLibro();
    }

    private void cleanComponents() {
        textField_Titulo.clear();
        datePicker_Publica.setValue(null);
        textArea_Previa.clear();
        textField_Isbn.clear();
        comboBox_Categoria.setValue(null);
        comboBox_Editorial.setValue(null);
        comboBox_Autor.setValue(null);
        imageView_Cover.setImage(null);

    }

    private void loadBookToShowOnScreen(Libro libro) {
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

    @FXML
    public void button_BuscarInventario() {
        String isbn = textField_InvIsbn.getText();
        globalInventario = inventarioRepository.findInventarioByIsbnLibro(isbn);
        System.out.println(globalInventario);
        if (globalInventario != null) {
            label_InvTitulo.setText(globalInventario.getLibro().getLbrTitulo());
            label_InvPublica.setText(globalInventario.getLibro().getLbrPublica().toString());
            label_InvAutor.setText(globalInventario.getLibro().getAutor().getAutNombre());
            label_InvCategoria.setText(globalInventario.getLibro().getCategoria().getCategoNombre());
            label_InvEditorial.setText(globalInventario.getLibro().getEditorial().getEditNombre());
            textField_InvMin.setText(String.valueOf(globalInventario.getInvStockMin()));
            textField_InvMax.setText(String.valueOf(globalInventario.getInvStockMax()));
            label_InvCantidad.setText(String.valueOf(globalInventario.getInvDisponible()));
        } else {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setContentText("El ISBN ingresado no pertenece a ningún libro o no contiene inventario habilitado");
            alert.setTitle("Alerta de Inventario");
            alert.setHeaderText(null);
            alert.showAndWait();
        }
    }

    @FXML
    private void button_BuscarLibroInventario() {
        globalLibro = openWindowToFindBook();
        if (globalLibro != null) {
            globalLibro.setLbrCover(null);
            textField_InvIsbn.setText(globalLibro.getLbrISBN());
        }
    }

    @FXML
    private void button_GuardarTransaccion() {
        try {
            int stockMin = Integer.parseInt(textField_InvMin.getText());
            int stockMax = Integer.parseInt(textField_InvMax.getText());
            int dispMin = Integer.parseInt(textField_InvCantidad.getText());

            if (comboBox_Transaccion.getValue() == InventarioTransaccion.CONFIGURACION_INICIAL) {
                globalInventario = Inventario.builder()
                        .invStockMin(stockMin)
                        .invStockMax(stockMax)
                        .invDisponible(dispMin)
                        .libro(globalLibro)
                        .libreria(Libreria.builder()
                                .libId(1)
                                .build())
                        .build();
                inventarioRepository.save(globalInventario);
                showAlertOnScreen(Alert.AlertType.INFORMATION, "Inventario inicial habilitado y cargado en pantalla");
                button_BuscarInventario();
            } else if (comboBox_Transaccion.getValue() == InventarioTransaccion.AJUSTE_INV_SALIDA ||
                    comboBox_Transaccion.getValue() == InventarioTransaccion.SALIDA_DESGASTE ||
                    comboBox_Transaccion.getValue() == InventarioTransaccion.SALIDA_PRESTAMO) {
                saveTransactionMovimientoInventario(dispMin, false);
            } else {
                saveTransactionMovimientoInventario(dispMin, true);
            }
        } catch (Exception e) {
            showAlertOnScreen(Alert.AlertType.ERROR, e.getMessage());
        }

    }

    private void saveTransactionMovimientoInventario(int disponible, boolean isSume) {
        disponible = (isSume) ? disponible : (disponible * -1);
        MovimientoInventario transaccion = MovimientoInventario.builder()
                .mvnTipo(comboBox_Transaccion.getValue())
                .mvnCantidad(disponible)
                .mvnTotal(globalInventario.getInvDisponible() + disponible)
                .inventario(globalInventario)
                .build();
        int rowsInserted = movimientoInventarioRepository.save(transaccion);
        showAlertOnScreen(Alert.AlertType.INFORMATION,"Nueva transacción guardada con éxito. \n Transacciones: " + rowsInserted);
        button_BuscarInventario();
    }

    private void showAlertOnScreen(Alert.AlertType alertType, String message){
        Alert alert = new Alert(alertType);
        alert.setTitle("Panel de Control");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}