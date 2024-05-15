package org.alexandevcwa.application.controller;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.util.StringConverter;
import org.alexandevcwa.application.controller.util.TextFieldConfiguration;
import org.alexandevcwa.application.model.Departamento;
import org.alexandevcwa.application.model.Municipio;
import org.alexandevcwa.application.model.Pais;
import org.alexandevcwa.application.model.Usuario;
import org.alexandevcwa.application.model.repository.*;
import org.w3c.dom.Text;

import java.nio.Buffer;
import java.util.ArrayList;
import java.util.List;

public class RegistrarController {

    private final PaisRepositoy paisRepositoy;
    private final DepartamentoRepositoryimpl departamentoRepository;
    private final MunicipioRepository municipioRepository;
    private final UsuarioRepository usuarioRepository;

    @FXML
    private ComboBox<Pais> comboBox_Paises;

    @FXML
    private ComboBox<Departamento> comboBox_Departamentos;

    @FXML
    private ComboBox<Municipio> comboBox_Municipios;

    @FXML
    private TextField textField_Nombre;

    @FXML
    private TextField textField_Apellido;

    @FXML
    private TextField textField_Direccion;

    @FXML
    private TextField textField_CUI;

    @FXML
    private TextField textField_Telefono;

    @FXML
    private TextField textField_Email;

    @FXML
    private PasswordField passwordField_Password;

    @FXML
    private Button button_Registrar;

    @FXML
    private Label label_Messages;

    private final Usuario usuario = new Usuario();


    public RegistrarController() {
        this.paisRepositoy = new PaisRepositoy();
        this.departamentoRepository = new DepartamentoRepository();
        this.municipioRepository = new MunicipioRepository();
        this.usuarioRepository = new UsuarioRepository();
    }

    public void initialize() {

        TextFieldConfiguration.textFieldUpperCase(textField_Nombre);
        TextFieldConfiguration.textFieldUpperCase(textField_Apellido);
        TextFieldConfiguration.textFieldUpperCase(textField_Direccion);
        TextFieldConfiguration.textFieldSetRegex(textField_CUI, "\\d*");
        TextFieldConfiguration.textFieldSetRegex(textField_Telefono, "\\d*");

        TextFieldConfiguration.textFieldVerifyContentWithRegex(textField_Email, "([\\w\\.\\-_]+)?\\w+@[\\w-_]+(\\.\\w+){1,}", label_Messages, "El formato del correo no es correcto");

        TextFieldConfiguration.textFieldFocusLost(
                textField_Nombre, 1, 100,
                "Nombres del usuario son requeridos", label_Messages,
                "setUsrNombre", usuario
        );

        TextFieldConfiguration.textFieldFocusLost(
                textField_Apellido, 1, 100,
                "Apellidos del usuario son requeridos", label_Messages,
                "setUsrApellido", usuario
        );

        TextFieldConfiguration.textFieldFocusLost(
                textField_Direccion, 1, 150,
                "Dirección del usuario es requerida", label_Messages,
                "setUsrDireccion", usuario
        );

        TextFieldConfiguration.textFieldFocusLost(
                textField_CUI, 13, 13,
                "El número de CUI es requerido y debe de tener mínimo 13 dígitos", label_Messages,
                "setUsrCui", usuario,
                (usuarioRepository::existsByCui),
                "El número de CUI ingresado ya pertenece a una cuenta existente"
        );

        TextFieldConfiguration.textFieldFocusLost(
                textField_Telefono, 8, 9,
                "El número de teléfono es requerido con 8 o 9 dígitos como mínimo", label_Messages,
                "setUsrTelefono", usuario,
                (usuarioRepository::existsByTelefono),
                "El número de teléfono ingresado ya pertenece a una cuanta existente"
        );

        TextFieldConfiguration.textFieldFocusLost(
                textField_Email, 1, 45,
                "Correo del usuario es requerido", label_Messages,
                "setUsrEmail", usuario,
                (usuarioRepository::existsByEmail),
                "El correo electrónico ingresado ya pertenece a una cuenta existente"
        );

        TextFieldConfiguration.textFieldFocusLost(
                passwordField_Password, 8, 64,
                "La contraseña debe de tener como mínimo 8 caracteres", label_Messages,
                "setUsrPassword", usuario
        );

        cargarPaisesToComboBox();

        button_Registrar.setOnAction(actionEvent -> {
            registrarNuevoUsuario();
        });
    }

    private void cargarPaisesToComboBox() {
        List<Pais> paises = paisRepositoy.findAll();
        comboBox_Paises.getItems().clear();
        comboBox_Paises.getItems().addAll(paises);
        comboBox_Paises.setConverter(new StringConverter<Pais>() {
            @Override
            public String toString(Pais pais) {
                return pais.getPsNombre();
            }

            @Override
            public Pais fromString(String s) {
                return null;
            }
        });

        comboBox_Paises.setOnAction(actionEvent -> {
            Pais pais = comboBox_Paises.getValue();
            cargarDepartamentosToComboBox(pais.getPsId());
        });
    }

    private void cargarDepartamentosToComboBox(int psId) {
        List<Departamento> departamentos = departamentoRepository.findAllByPsId(psId);
        comboBox_Departamentos.getItems().clear();
        comboBox_Departamentos.getItems().addAll(departamentos);
        comboBox_Departamentos.setConverter(new StringConverter<Departamento>() {
            @Override
            public String toString(Departamento departamento) {
                return departamento.getDeptoNombre();
            }

            @Override
            public Departamento fromString(String s) {
                return null;
            }
        });
        comboBox_Departamentos.setOnAction(actionEvent -> {
            Departamento departamento = comboBox_Departamentos.getValue();
            cargarMunicipiosToComboBox(departamento.getDeptoId());
        });
    }

    private void cargarMunicipiosToComboBox(int deptoId) {
        List<Municipio> municipios = municipioRepository.findAllByDeptoId(deptoId);
        comboBox_Municipios.getItems().clear();
        comboBox_Municipios.getItems().addAll(municipios);
        comboBox_Municipios.setConverter(new StringConverter<Municipio>() {
            @Override
            public String toString(Municipio municipio) {
                return municipio.getMuniNombre();
            }

            @Override
            public Municipio fromString(String s) {
                return null;
            }
        });
        comboBox_Municipios.setOnAction(actionEvent -> {
            Municipio municipio = comboBox_Municipios.getValue();
            usuario.setMunicipio(municipio);
        });
    }

    private void registrarNuevoUsuario() {
        if (usuario.getUsrNombre() != null && usuario.getUsrApellido() != null
                && usuario.getUsrDireccion() != null && usuario.getUsrCui() != null
                && usuario.getUsrTelefono() != null && usuario.getUsrEmail() != null
                && usuario.getUsrPassword() != null && usuario.getMunicipio() != null
        ) {
            usuarioRepository.save(usuario);
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Nuevo Usuario");
            alert.setHeaderText(null);
            alert.setContentText("Nuevo Usuario registrado exitosamente,\n inicia sesión en el login para acceder a la plataforma.");
            alert.showAndWait();
        } else {
            label_Messages.setText("El formulario esta incompleto");
        }
    }


}
