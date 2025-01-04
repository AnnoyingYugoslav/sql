package com.example.controller;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeParseException;
import java.util.HashMap;
import java.util.Map;
import java.util.List;

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
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.GetMapping;




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
    private final AccountRepository accountRepository;

    public ApiController(KlasaRepository klasaRepository, DzienRepository dzienRepository,
            LekcjaRepository lekcjaRepository, OcenaRepository ocenaRepository, PrzedmiotRepository przedmiotRepository,
            SalaRepository salaRepository, SprawdzianRepository sprawdzianRepository,
            UserNauczycielRepository userNauczycielRepository, UserRodzicRepository userRodzicRepository,
            UserUczenRepository userUczenRepository, UwagaRepository uwagaRepository,
            WiadomoscRepository wiadomoscRepository, WiadomoscNRepository wiadomoscNRepository,
            WiadomoscRRepository wiadomoscRRepository, WiadomoscURepository wiadomoscURepository, AccountRepository accountRepository) {
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
        this.accountRepository = accountRepository;
    }
    private String convertMapToJson(Map<Integer, Object> map) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            return objectMapper.writeValueAsString(map);
        } catch (JsonProcessingException e) {
            e.printStackTrace(); 
            return "Error";
        }
    }
            
    @PostMapping("/login")
    public String loginUser(@RequestBody Map<Integer, Object> newMapData) { //1: login 2: password -> 1: True 2: Naucz (true, false) 3: Rodzic (true, false) 4: uczen(true, false)
        Map<Integer, Object> toReturn = new HashMap<>();
        String login = newMapData.get(1).toString();
        String password = newMapData.get(2).toString();
        Account account = accountRepository.findIdByLogin(login);
        if(!(account instanceof Account)){
            toReturn.put(1, false);
            return convertMapToJson(toReturn);
        }
        if(!account.checkUserAccount(login, password)){
            toReturn.put(1, false);
            return convertMapToJson(toReturn);
        }
        toReturn.put(1, true);
        toReturn.put(2, account.getUserNauczyciel());
        toReturn.put(3, account.getUserRodzic());
        toReturn.put(4, account.getUserUczen());
        return convertMapToJson(toReturn);
    }
    

    @PostMapping("/add-ocena")
    public String addOcena(@RequestBody Map<Integer, Object> newMapData) { //1: login 2: password 3: Id ucznia 4: ocena (wartość) 5: przedmiot id -> 1: true/false
        Map<Integer, Object> toReturn = new HashMap<>();
        try{
            String login = newMapData.get(1).toString();
            String password = newMapData.get(2).toString();
            Account account = accountRepository.findIdByLogin(login);
            if(!(account instanceof Account)){
                toReturn.put(1, false);
                return convertMapToJson(toReturn);
            }
            if(!account.checkUserAccount(login, password)){
                toReturn.put(1, false);
                return convertMapToJson(toReturn);
            }
            if(account.getUserNauczyciel() == null){ //not a teacher
                toReturn.put(1, false);
                return convertMapToJson(toReturn);
            }
            UserUczen userUczen = userUczenRepository.getReferenceById((Long)newMapData.get(3));
            if(!(userUczen instanceof UserUczen)){
                toReturn.put(1, false);
                return convertMapToJson(toReturn);
            }
            Przedmiot przedmiot = przedmiotRepository.getReferenceById((Long)newMapData.get(5));
            if(!(przedmiot instanceof Przedmiot)){
                toReturn.put(1, false);
                return convertMapToJson(toReturn);
            }
            Ocena ocena = new Ocena((Integer)newMapData.get(4), LocalDateTime.now(), userUczen, account.getUserNauczyciel(), przedmiot);
            Ocena savedOcena = ocenaRepository.save(ocena);
            if(!(savedOcena instanceof Ocena)){
                toReturn.put(1, false);
                return convertMapToJson(toReturn);
            }
            toReturn.put(1, true);
            return convertMapToJson(toReturn);
        }
        catch(Throwable e){
            toReturn.put(1, false);
            return convertMapToJson(toReturn);
        }
    }

    @PutMapping("/edit-ocena/{id}") //id - id oceny
    public String editOcena(@PathVariable String id, @RequestBody Map<Integer, Object> newMapData) { //1: login 2:password 3: ocena wartość nowa -> 1: true/false
        Map<Integer, Object> toReturn = new HashMap<>();
        try{
            String login = newMapData.get(1).toString();
            String password = newMapData.get(2).toString();
            Account account = accountRepository.findIdByLogin(login);
            if(!(account instanceof Account)){
                toReturn.put(1, false);
                return convertMapToJson(toReturn);
            }
            if(!account.checkUserAccount(login, password)){
                toReturn.put(1, false);
                return convertMapToJson(toReturn);
            }
            if(account.getUserNauczyciel() == null){ //not a teacher
                toReturn.put(1, false);
                return convertMapToJson(toReturn);
            }
            Ocena ocena = ocenaRepository.getReferenceById(Long.parseLong(id));
            if(!(ocena instanceof Ocena)){
                toReturn.put(1, false);
                return convertMapToJson(toReturn);
            }
            ocena.setOcena((Integer) newMapData.get(3));
            Ocena savedOcena = ocenaRepository.save(ocena);
            if(!(savedOcena instanceof Ocena)){
                toReturn.put(1, false);
                return convertMapToJson(toReturn);
            }
            toReturn.put(1, true);
                return convertMapToJson(toReturn);
        }
        catch(Throwable e){
            toReturn.put(1, false);
            return convertMapToJson(toReturn);
        }
    }
    
    @PostMapping("/add-sprawdzian")
    public String addSprawdzian(@RequestBody Map<Integer, Object> newMapData) { //1: login 2: password 3: Id Klasy 4: Przedmiot Id 5: Dzien Id 6: Sala Id 7: String kategoria-> 1: true/false
        Map<Integer, Object> toReturn = new HashMap<>();
        try{
            String login = newMapData.get(1).toString();
            String password = newMapData.get(2).toString();
            Account account = accountRepository.findIdByLogin(login);
            if(!(account instanceof Account)){
                toReturn.put(1, false);
                return convertMapToJson(toReturn);
            }
            if(!account.checkUserAccount(login, password)){
                toReturn.put(1, false);
                return convertMapToJson(toReturn);
            }
            if(account.getUserNauczyciel() == null){ //not a teacher
                toReturn.put(1, false);
                return convertMapToJson(toReturn);
            }
            Przedmiot przedmiot = przedmiotRepository.getReferenceById((Long)newMapData.get(4));
            if(!(przedmiot instanceof Przedmiot)){
                toReturn.put(1, false);
                return convertMapToJson(toReturn);
            }
            Klasa klasa = klasaRepository.getReferenceById((Long)newMapData.get(3));
            if(!(klasa instanceof Klasa)){
                toReturn.put(1, false);
                return convertMapToJson(toReturn);
            }
            Dzien dzien = dzienRepository.getReferenceById((Long)newMapData.get(5));
            if(!(dzien instanceof Dzien)){
                toReturn.put(1, false);
                return convertMapToJson(toReturn);
            }
            Sala sala = salaRepository.getReferenceById((Long) newMapData.get(6));
            if(!(sala instanceof Sala)){
                toReturn.put(1, false);
                return convertMapToJson(toReturn);
            }
            Sprawdzian sprawdzian = new Sprawdzian(newMapData.get(7).toString(), account.getUserNauczyciel(), sala, klasa, przedmiot, dzien);
            Sprawdzian savedSprawdzian = sprawdzianRepository.save(sprawdzian);
            if(!(savedSprawdzian instanceof Sprawdzian)){
                toReturn.put(1, false);
                return convertMapToJson(toReturn);
            }
            toReturn.put(1, true);
            return convertMapToJson(toReturn);
        }
        catch(Throwable e){
            toReturn.put(1, false);
            return convertMapToJson(toReturn);
        }
    }


    
    @PutMapping("/edit-sprawdzian/{id}")//id - id sprawdzianu
    public String editSprawdzian(@PathVariable String id, @RequestBody Map<Integer, Object> newMapData) { //1: login 2:password 3: Id klasy 4: id przedmiotu 5: id dzien 6: id sala 7: nowa kategoria -> 1: true/false
        Map<Integer, Object> toReturn = new HashMap<>();
        try{
            String login = newMapData.get(1).toString();
            String password = newMapData.get(2).toString();
            Account account = accountRepository.findIdByLogin(login);
            if(!(account instanceof Account)){
                toReturn.put(1, false);
                return convertMapToJson(toReturn);
            }
            if(!account.checkUserAccount(login, password)){
                toReturn.put(1, false);
                return convertMapToJson(toReturn);
            }
            if(account.getUserNauczyciel() == null){ //not a teacher
                toReturn.put(1, false);
                return convertMapToJson(toReturn);
            }
            Przedmiot przedmiot = przedmiotRepository.getReferenceById((Long)newMapData.get(4));
            if(!(przedmiot instanceof Przedmiot)){
                toReturn.put(1, false);
                return convertMapToJson(toReturn);
            }
            Klasa klasa = klasaRepository.getReferenceById((Long)newMapData.get(3));
            if(!(klasa instanceof Klasa)){
                toReturn.put(1, false);
                return convertMapToJson(toReturn);
            }
            Dzien dzien = dzienRepository.getReferenceById((Long)newMapData.get(5));
            if(!(dzien instanceof Dzien)){
                toReturn.put(1, false);
                return convertMapToJson(toReturn);
            }
            Sala sala = salaRepository.getReferenceById((Long) newMapData.get(6));
            if(!(sala instanceof Sala)){
                toReturn.put(1, false);
                return convertMapToJson(toReturn);
            }
            Sprawdzian sprawdzian = sprawdzianRepository.getReferenceById(Long.parseLong(id));
            if(!(sprawdzian instanceof Sprawdzian)){
                toReturn.put(1, false);
                return convertMapToJson(toReturn);
            }
            sprawdzian.setPrzedmiot(przedmiot);
            sprawdzian.setKlasa(klasa);
            sprawdzian.setSala(sala);
            sprawdzian.setDzien(dzien);
            sprawdzian.setKategoria(newMapData.get(7).toString());

            Sprawdzian savedSprawdzian = sprawdzianRepository.save(sprawdzian);
            if(!(savedSprawdzian instanceof Sprawdzian)){
                toReturn.put(1, false);
                return convertMapToJson(toReturn);
            }
            toReturn.put(1, true);
                return convertMapToJson(toReturn);
        }
        catch(Throwable e){
            toReturn.put(1, false);
            return convertMapToJson(toReturn);
        }
    }

    @GetMapping("/get-klasy")
    public String getKlasy(@RequestBody Map<Integer, Object> newMapData) { //1: login 2: password -> 1:true false, rest... GOOD LUCK
        Map<Integer, Object> toReturn = new HashMap<>();
        try{
            String login = newMapData.get(1).toString();
            String password = newMapData.get(2).toString();
            Account account = accountRepository.findIdByLogin(login);
            if(!(account instanceof Account)){
                toReturn.put(1, false);
                return convertMapToJson(toReturn);
            }
            if(!account.checkUserAccount(login, password)){
                toReturn.put(1, false);
                return convertMapToJson(toReturn);
            }
            toReturn.put(1, true);
            List<Klasa> allKlasa = klasaRepository.findAll();
            int i = 2;
            for(Klasa klasa:allKlasa){
                toReturn.put(i, klasa);
                i++;
            }
            return convertMapToJson(toReturn);
        }
        catch(Throwable e){
            toReturn.put(1, false);
            return convertMapToJson(toReturn);
        }
    }
        


    @GetMapping("/get-uczniowie")
    public String getMUczniowieKlasy(@RequestBody Map<Integer, Object> newMapData) { //1: login 2: password 3: Id klasy -> 1:true false, rest... GOOD LUCK
        Map<Integer, Object> toReturn = new HashMap<>();
        try{
            String login = newMapData.get(1).toString();
            String password = newMapData.get(2).toString();
            Account account = accountRepository.findIdByLogin(login);
            if(!(account instanceof Account)){
                toReturn.put(1, false);
                return convertMapToJson(toReturn);
            }
            if(!account.checkUserAccount(login, password)){
                toReturn.put(1, false);
                return convertMapToJson(toReturn);
            }
            if(account.getUserNauczyciel() == null){ //not a teacher
                toReturn.put(1, false);
                return convertMapToJson(toReturn);
            }
            Klasa klasa = klasaRepository.getReferenceById((Long) newMapData.get(3));
            if(!(klasa instanceof Klasa)){
                toReturn.put(1, false);
                return convertMapToJson(toReturn);
            }
            List<UserUczen> allUserUczen = userUczenRepository.findByKlasa(klasa);
            int i = 2;
            for(UserUczen userUczen:allUserUczen){
                toReturn.put(i,userUczen);
                i++;
            }
            return convertMapToJson(toReturn);
        }
        catch(Throwable e){
            toReturn.put(1, false);
            return convertMapToJson(toReturn);
        }
    }
    
}