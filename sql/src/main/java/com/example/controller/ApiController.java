package com.example.controller;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.sql.entities.*;
import com.example.sql.repositories.*;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.springframework.web.bind.annotation.RequestParam;


@RestController
@RequestMapping("/api")
public class ApiController {

    private final KlasaRepository klasaRepository;
    private final DzienRepository dzienRepository;
    private final LekcjaRepository lekcjaRepository;
    private final OcenaRepository ocenaRepository;
    private final PrzedmiotRepository przedmiotRepository;
    private final SalaRepository salaRepository;
    private final SprawdzianRepository sprawdzianRepository;
    private final UserNauczycielRepository userNauczycielRepository;
    private final UserRodzicRepository userRodzicRepository;
    private final UserUczenRepository userUczenRepository;
    private final UwagaRepository uwagaRepository;
    private final WiadomoscRepository wiadomoscRepository;
    private final WiadomoscNRepository wiadomoscNRepository;
    private final WiadomoscRRepository wiadomoscRRepository;
    private final WiadomoscURepository wiadomoscURepository;

    public ApiController(KlasaRepository klasaRepository, DzienRepository dzienRepository,
            LekcjaRepository lekcjaRepository, OcenaRepository ocenaRepository, PrzedmiotRepository przedmiotRepository,
            SalaRepository salaRepository, SprawdzianRepository sprawdzianRepository,
            UserNauczycielRepository userNauczycielRepository, UserRodzicRepository userRodzicRepository,
            UserUczenRepository userUczenRepository, UwagaRepository uwagaRepository,
            WiadomoscRepository wiadomoscRepository, WiadomoscNRepository wiadomoscNRepository,
            WiadomoscRRepository wiadomoscRRepository, WiadomoscURepository wiadomoscURepository) {
        this.klasaRepository = klasaRepository;
        this.dzienRepository = dzienRepository;
        this.lekcjaRepository = lekcjaRepository;
        this.ocenaRepository = ocenaRepository;
        this.przedmiotRepository = przedmiotRepository;
        this.salaRepository = salaRepository;
        this.sprawdzianRepository = sprawdzianRepository;
        this.userNauczycielRepository = userNauczycielRepository;
        this.userRodzicRepository = userRodzicRepository;
        this.userUczenRepository = userUczenRepository;
        this.uwagaRepository = uwagaRepository;
        this.wiadomoscRepository = wiadomoscRepository;
        this.wiadomoscNRepository = wiadomoscNRepository;
        this.wiadomoscRRepository = wiadomoscRRepository;
        this.wiadomoscURepository = wiadomoscURepository;
    }
    private String convertMapToJson(Map<Integer, Object> map) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            return objectMapper.writeValueAsString(map);
        } catch (JsonProcessingException e) {
            e.printStackTrace(); 
            return "{\"error\": \"Failed to convert Map to JSON\"}";
        }
    }
            

    @PostMapping("/klasa")
    public String createKlasa(@RequestBody Map<Integer, Object> newMapData) { //1: String
        String jsonData = convertMapToJson(newMapData);
        String param = newMapData.get(1).toString();
        Klasa klasa = new Klasa(param);
        Klasa savedKlasa = klasaRepository.save(klasa);
        return "Klasa created with ID: " + savedKlasa.getId();
    }
    
    @PostMapping("/dzien")
    public String createDzien(@RequestBody Map<Integer, Object> newMapData) { //1: String
        String jsonData = convertMapToJson(newMapData);
        String param = newMapData.get(1).toString();
        LocalDate localDate;
        try {
            localDate = LocalDate.parse(param);
        } catch (Exception e) {
            return "Inalid date format for date\n";
        }
        Dzien dzien = new Dzien(localDate);
        Dzien savedDzien = dzienRepository.save(dzien);
        return "Dzien created with ID: " + savedDzien.getId();
    }
        
}