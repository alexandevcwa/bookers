package com.alexandevcwa.bookers.service;

import com.alexandevcwa.bookers.repository.AutorDAOImpl;

public class AutorService {

    private final AutorDAOImpl autorRepository;

    public AutorService(AutorDAOImpl autorRepository){
        this.autorRepository = autorRepository;
    }
}
