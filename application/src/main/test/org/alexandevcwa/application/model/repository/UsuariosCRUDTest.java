package org.alexandevcwa.application.model.repository;

import org.alexandevcwa.application.model.Municipio;
import org.alexandevcwa.application.model.Usuario;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class UsuariosCRUDTest {

    UsuarioRepository usuarioRepository;

    @BeforeEach
    public void setup() {
        this.usuarioRepository = new UsuarioRepository();
    }

    @Test
    public void crearNuevoUsuario() {
        Usuario usuario = Usuario.builder()
                .usrNombre("John")
                .usrApellido("Connor")
                .usrDireccion("United Stated, New York")
                .usrCui("0000000000000")
                .usrTelefono("12345678")
                .usrEmail("john@alexandevcwa.com")
                .usrPassword("1234")
                .municipio(Municipio.builder().muniId(1).build())
                .build();
        int insertedRows = usuarioRepository.save(usuario);
        assertThat(insertedRows).isGreaterThan(0);
    }

    @Test
    public void verificacionDeCredenciales() {
        boolean exists = usuarioRepository.existsByCuiAndPassword("0000000000000", "1234");
        assertThat(exists).isTrue();
    }

    @Test
    public void actualizacionDeUsuario(){
        Usuario usuario = usuarioRepository.findByCuiAndPassword("0000000000000","1234");
        assertThat(usuario).isNotNull();
        usuario.setUsrCui("111111111111");
        usuario.setMunicipio(Municipio.builder().muniId(1).build());
        int affectedRows = usuarioRepository.update(usuario);
        assertThat(affectedRows).isGreaterThan(0);
    }

}
