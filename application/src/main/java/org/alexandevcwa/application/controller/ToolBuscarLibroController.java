package org.alexandevcwa.application.controller;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import lombok.Getter;
import lombok.Setter;
import org.alexandevcwa.application.controller.util.TextFieldConfiguration;
import org.alexandevcwa.application.model.Libro;
import org.alexandevcwa.application.model.repository.LibroRepository;

public class ToolBuscarLibroController {

    @Getter
    private Libro libro;

    @Setter
    private Stage stage;

    @FXML
    private Button button_Buscar;

    @FXML
    private TextField textField_Patter;

    @FXML
    private TableView<Libro> tableView_Libros;

    @FXML
    private Label label_Message;

    private final LibroRepository libroRepository;

    public ToolBuscarLibroController() {

        this.libroRepository = new LibroRepository();
    }

    @FXML
    public void initialize(){

        TableColumn<Libro,Integer> skuColumn = new TableColumn<>("SKU");
        skuColumn.setCellValueFactory(new PropertyValueFactory<>("lbrSku"));

        TableColumn<Libro,String> tituloColumn = new TableColumn<>("Título");
        tituloColumn.setCellValueFactory(new PropertyValueFactory<>("lbrTitulo"));

        TableColumn<Libro,String> isbnColumn = new TableColumn<>("ISBN");
        isbnColumn.setCellValueFactory(new PropertyValueFactory<>("lbrISBN"));

        TableColumn<Libro,String> publicaColumn = new TableColumn<>("Fecha Publicación");
        publicaColumn.setCellValueFactory(new PropertyValueFactory<>("lbrPublica"));



        tableView_Libros.getColumns().clear();
        tableView_Libros.getColumns().addAll(skuColumn, tituloColumn,isbnColumn,publicaColumn);

        TextFieldConfiguration.textFieldUpperCase(textField_Patter);

        button_Buscar.setOnAction(actionEvent -> {
            String patter = textField_Patter.getText();
            var listaLibro =  libroRepository.findLikeLbrTitulo(patter);
            if (listaLibro != null){
                tableView_Libros.getItems().clear();
                tableView_Libros.getItems().addAll(listaLibro);
                label_Message.setText("Libros recuperados: " + listaLibro.size());
            }else {
                label_Message.setText("Ningún libro recurperado");
            }
        });

        tableView_Libros.setOnMouseClicked(mouseEvent -> {
            if (mouseEvent.getClickCount() == 2) {
                this.libro = tableView_Libros.getSelectionModel().getSelectedItem();
                this.stage.fireEvent(new WindowEvent(stage,WindowEvent.WINDOW_CLOSE_REQUEST));
            }
        });
    }
}
