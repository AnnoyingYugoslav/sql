package com.example.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.sql.entities.Klasa;
import com.example.sql.repositories.Repositories.*;

import org.springframework.web.bind.annotation.RequestParam;


@RestController
@RequestMapping("/api")
public class ApiController {

    private final KlasaRepository klasaRepository;

    @Autowired
    public ApiController(KlasaRepository klasaRepository) {
        this.klasaRepository = klasaRepository;
    }

    @PostMapping("/klasa")
    public String createKlasa(@RequestParam String param) {
        Klasa klasa = new Klasa(param);
        Klasa savedKlasa = klasaRepository.save(klasa);
        return "Klasa created with ID: " + savedKlasa.getId();
    }
        
}