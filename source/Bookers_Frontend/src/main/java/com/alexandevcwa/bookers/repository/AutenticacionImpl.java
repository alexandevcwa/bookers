package com.alexandevcwa.bookers.repository;

import com.alexandevcwa.bookers.model.MensajeDTO;
import com.alexandevcwa.bookers.model.UsuarioDTO;

public interface AutenticacionImpl {
    void login(String email, String password);

    MensajeDTO logout();

    MensajeDTO register(UsuarioDTO usuario);


}
