package com.alexandevcwa.bookers.service;

import com.alexandevcwa.bookers.repository.UsuarioDAOImpl;

public class UsuarioServicio {
    private final UsuarioDAOImpl usuarioRepository;

    public UsuarioServicio(UsuarioDAOImpl usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
    }
}
