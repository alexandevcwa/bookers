package org.alexandevcwa.application.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.cell.PropertyValueFactory;
import org.alexandevcwa.application.model.Libro;
import javafx.scene.control.*;
import org.alexandevcwa.application.model.Usuario;
import org.alexandevcwa.application.model.repository.PrestamoRepository;

import java.util.List;

public class DetallePedidoController {

    private List<Libro> libroList;

    private Usuario usuario;

    @FXML
    private TableView<Libro> tableView_DetallePedido;

    @FXML
    private Button button_Alquilar;

    @FXML
    private Label label_CantidadPerido;

    private final PrestamoRepository prestamoRepository;

    public DetallePedidoController() {
        this.prestamoRepository = new PrestamoRepository();
    }


    public void customInitialize(List<Libro> libroList, Usuario usuario) {
        this.libroList = libroList;
        this.usuario = usuario;

        TableColumn<Libro, Integer> skuColumn = new TableColumn<>("SKU");
        skuColumn.setCellValueFactory(new PropertyValueFactory<>("lbrSku"));

        TableColumn<Libro, String> tituloColumn = new TableColumn<>("Título");
        tituloColumn.setCellValueFactory(new PropertyValueFactory<>("lbrTitulo"));

        TableColumn<Libro, String> isbnColumn = new TableColumn<>("ISBN");
        isbnColumn.setCellValueFactory(new PropertyValueFactory<>("lbrISBN"));

        TableColumn<Libro, String> publicaColumn = new TableColumn<>("Fecha Publicación");
        publicaColumn.setCellValueFactory(new PropertyValueFactory<>("lbrPublica"));

        tableView_DetallePedido.getColumns().clear();
        tableView_DetallePedido.getColumns().addAll(skuColumn, tituloColumn, isbnColumn, publicaColumn);
        tableView_DetallePedido.getItems().clear();
        tableView_DetallePedido.getItems().addAll(libroList);
        label_CantidadPerido.setText("TOTAL DE LIBROS EN DETALLE: " + libroList.size());

        button_Alquilar.setOnAction(actionEvent -> {
            int transaccion =  prestamoRepository.save(usuario,libroList);
            if (transaccion > 0){
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Préstamo de Libros");
                alert.setHeaderText(null);
                alert.initOwner(null);
                alert.setContentText("El préstamo ha sido creado con exito");
                alert.showAndWait();
                libroList.clear();
                tableView_DetallePedido.getItems().clear();
            }
        });
    }

}
