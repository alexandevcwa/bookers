package com.alexandevcwa.bookers.repository.dao;

import com.alexandevcwa.bookers.model.MensajeDTO;
import com.alexandevcwa.bookers.model.UsuarioDTO;
import com.alexandevcwa.bookers.model.enums.HttpMethod;
import com.alexandevcwa.bookers.repository.AutenticacionImpl;

public class Autenticacion extends HttpAPIRest<UsuarioDTO> implements AutenticacionImpl {

    @Override
    public void login(String email, String password) {

    }

    @Override
    public MensajeDTO logout() {
        return null;
    }

    @Override
    public MensajeDTO register(UsuarioDTO usuario) {
        return null;
    }

    @Override
    protected String doHttpRequest(String endpoint, HttpMethod httpMethod, UsuarioDTO body) {
        return super.doHttpRequest(endpoint, httpMethod, body);
    }

    @Override
    protected String doHttpRequest(String endpoint, HttpMethod httpMethod, String authorization) {
        return super.doHttpRequest(endpoint, httpMethod, authorization);
    }
}
