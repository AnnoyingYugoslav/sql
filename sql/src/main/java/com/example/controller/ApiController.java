package com.example.controller;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.HashMap;
import java.util.Map;
import java.util.List;

import org.hibernate.Hibernate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.SerializationUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.sql.entities.*;
import com.example.sql.repositories.*;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.transaction.Transactional;

import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;




@RestController
@RequestMapping("/api")
public class ApiController {

    private final KlasaRepository klasaRepository;
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
    private final RelacjaRepository relacjaRepository;

    public ApiController(KlasaRepository klasaRepository,
            LekcjaRepository lekcjaRepository, OcenaRepository ocenaRepository, PrzedmiotRepository przedmiotRepository,
            SalaRepository salaRepository, SprawdzianRepository sprawdzianRepository,
            UserNauczycielRepository userNauczycielRepository, UserRodzicRepository userRodzicRepository,
            UserUczenRepository userUczenRepository, UwagaRepository uwagaRepository,
            WiadomoscRepository wiadomoscRepository, WiadomoscNRepository wiadomoscNRepository,
            WiadomoscRRepository wiadomoscRRepository, WiadomoscURepository wiadomoscURepository, AccountRepository accountRepository, RelacjaRepository relacjaRepository) {
        this.klasaRepository = klasaRepository;
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
        this.relacjaRepository = relacjaRepository;
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
            
    @Transactional
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
    
    @Transactional
    @PostMapping("/add-ocena")
    public String addOcena(@RequestBody Map<Integer, Object> newMapData) { //1: login 2: password 3: Id ucznia 4: ocena (wartość) 5: przedmiot id 6: opis-> 1: true/false
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
            UserUczen userUczen = userUczenRepository.getReferenceById(Long.parseLong(newMapData.get(3).toString()));
            if(!(userUczen instanceof UserUczen)){
                toReturn.put(1, false);
                return convertMapToJson(toReturn);
            }
            Przedmiot przedmiot = przedmiotRepository.getReferenceById(Long.parseLong(newMapData.get(5).toString()));
            if(!(przedmiot instanceof Przedmiot)){
                toReturn.put(1, false);
                return convertMapToJson(toReturn);
            }
            Dzien dzien = new Dzien(LocalDate.now().getYear(), LocalDate.now().getMonthValue(), LocalDate.now().getDayOfMonth());
            Godzina godzina = new Godzina(LocalTime.now().getHour(), LocalTime.now().getMinute());
            Ocena ocena = new Ocena(Integer.parseInt(newMapData.get(4).toString()), dzien, godzina, userUczen, account.getUserNauczyciel(), przedmiot, newMapData.get(6).toString());
            Ocena savedOcena = ocenaRepository.save(ocena);
            if(!(savedOcena instanceof Ocena)){
                toReturn.put(1, false);
                return convertMapToJson(toReturn);
            }
            toReturn.put(1, true);
            return convertMapToJson(toReturn);
        }
        catch(Throwable e){
            System.out.println(e);
            toReturn.put(1, false);
            return convertMapToJson(toReturn);
        }
    }
    @Transactional
    @PutMapping("/edit-ocena/{id}") //id - id oceny
    public String editOcena(@PathVariable Long id, @RequestBody Map<Integer, Object> newMapData) { //1: login 2:password 3: ocena wartość nowa -> 1: true/false
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
            Ocena ocena = ocenaRepository.getReferenceById(id);
            if(!(ocena instanceof Ocena)){
                toReturn.put(1, false);
                return convertMapToJson(toReturn);
            }
            ocena.setOcena(Integer.parseInt( newMapData.get(3).toString()));
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

    @Transactional
    @DeleteMapping("/remove-ocena/{id}")
    public String removeOcena(@PathVariable Long id, @RequestBody Map<Integer, Object> newMapData){ //1:login 2:password -> true, false
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
            Ocena ocena = ocenaRepository.getReferenceById(id);
            if(!(ocena instanceof Ocena)){
                toReturn.put(1, false);
                return convertMapToJson(toReturn);
            }
            ocenaRepository.delete(ocena);
            toReturn.put(1, true);
            return convertMapToJson(toReturn);
        }
        catch(Throwable e){
            toReturn.put(1, false);
            return convertMapToJson(toReturn);
        }
    }

    @Transactional
    @PostMapping("/add-sprawdzian")
    public String addSprawdzian(@RequestBody Map<Integer, Object> newMapData) { //1: login 2: password 3: Id Klasy 4: Przedmiot Id 5: Dzien String(DD.MM.YYYY) 6: Sala Id 7: String kategoria 8: godzina (HH:MM)-> 1: true/false
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
            Przedmiot przedmiot = przedmiotRepository.getReferenceById(Long.parseLong(newMapData.get(4).toString()));
            if(!(przedmiot instanceof Przedmiot)){
                toReturn.put(1, false);
                return convertMapToJson(toReturn);
            }
            Klasa klasa = klasaRepository.getReferenceById(Long.parseLong(newMapData.get(3).toString()));
            if(!(klasa instanceof Klasa)){
                toReturn.put(1, false);
                return convertMapToJson(toReturn);
            }
            Sala sala = salaRepository.getReferenceById(Long.parseLong( newMapData.get(6).toString()));
            if(!(sala instanceof Sala)){
                toReturn.put(1, false);
                return convertMapToJson(toReturn);
            }
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");
            DateTimeFormatter formatter2 = DateTimeFormatter.ofPattern("HH:mm");
            LocalDate date = LocalDate.parse(newMapData.get(5).toString(), formatter);
            LocalTime time = LocalTime.parse(newMapData.get(8).toString(), formatter2);

            Dzien dzien = new Dzien(date.getYear(), date.getMonthValue(), date.getDayOfMonth());
            Godzina godzina = new Godzina(time.getHour(), time.getMinute());
            Sprawdzian sprawdzian = new Sprawdzian(newMapData.get(7).toString(), account.getUserNauczyciel(), sala, klasa, przedmiot, dzien, godzina);
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

    @Transactional
    @PutMapping("/edit-sprawdzian/{id}")//id - id sprawdzianu
    public String editSprawdzian(@PathVariable String id, @RequestBody Map<Integer, Object> newMapData) { //1: login 2:password 3: Id klasy 4: id przedmiotu 5: Dzien String(DD.MM.YYYY) 6: id sala 7: nowa kategoria 8: godzina (HH:MM)-> 1: true/false
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
            Przedmiot przedmiot = przedmiotRepository.getReferenceById(Long.parseLong(newMapData.get(4).toString()));
            if(!(przedmiot instanceof Przedmiot)){
                toReturn.put(1, false);
                return convertMapToJson(toReturn);
            }
            Klasa klasa = klasaRepository.getReferenceById(Long.parseLong(newMapData.get(3).toString()));
            if(!(klasa instanceof Klasa)){
                toReturn.put(1, false);
                return convertMapToJson(toReturn);
            }
            Sala sala = salaRepository.getReferenceById(Long.parseLong( newMapData.get(6).toString()));
            if(!(sala instanceof Sala)){
                toReturn.put(1, false);
                return convertMapToJson(toReturn);
            }
            Sprawdzian sprawdzian = sprawdzianRepository.getReferenceById(Long.parseLong(id));
            if(!(sprawdzian instanceof Sprawdzian)){
                toReturn.put(1, false);
                return convertMapToJson(toReturn);
            }
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");
            DateTimeFormatter formatter2 = DateTimeFormatter.ofPattern("HH:mm");
            LocalDate date = LocalDate.parse(newMapData.get(5).toString(), formatter);
            LocalTime time = LocalTime.parse(newMapData.get(8).toString(), formatter2);

            Dzien dzien = new Dzien(date.getYear(), date.getMonthValue(), date.getDayOfMonth());
            Godzina godzina = new Godzina(time.getHour(), time.getMinute());
            sprawdzian.setPrzedmiot(przedmiot);
            sprawdzian.setKlasa(klasa);
            sprawdzian.setSala(sala);
            sprawdzian.setKategoria(newMapData.get(7).toString());
            sprawdzian.setDzien(dzien);
            sprawdzian.setGodzina(godzina);

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

    @Transactional
    @DeleteMapping("/remove-sprawdzian/{id}")
    public String removeSprawdzian(@PathVariable Long id, @RequestBody Map<Integer, Object> newMapData){ //1: logi 2:password -> true/false
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
            Sprawdzian sprawdzian = sprawdzianRepository.getReferenceById(id);
            if(!(sprawdzian instanceof Sprawdzian)){
                toReturn.put(1, false);
                return convertMapToJson(toReturn);
            }
            sprawdzianRepository.delete(sprawdzian);
            toReturn.put(1, true);
            return convertMapToJson(toReturn);
        }
        catch(Throwable e){
            toReturn.put(1, false);
            return convertMapToJson(toReturn);
        }
    }

    @Transactional
    @PostMapping("/get-klasy")
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
    
    @Transactional
    @PostMapping("/get-uczniowie")
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
            Klasa klasa = klasaRepository.getReferenceById(Long.parseLong( newMapData.get(3).toString()));
            if(!(klasa instanceof Klasa)){
                toReturn.put(1, false);
                return convertMapToJson(toReturn);
            }
            Hibernate.initialize(klasa);
            toReturn.put(1, true);
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
    
    @Transactional
    @PostMapping("/change-password")
    public String changePass(@RequestBody Map<Integer, Object> newMapData) { //1: logn 2: password 3: new password -> 1:true
        Map<Integer, Object> toReturn = new HashMap<>();
        try{
            String login = newMapData.get(1).toString();
            String password = newMapData.get(2).toString();
            String password3 = newMapData.get(3).toString();
            Account account = accountRepository.findIdByLogin(login);
            if(!(account instanceof Account)){
                toReturn.put(1, false);
                return convertMapToJson(toReturn);
            }
            if(!account.checkUserAccount(login, password)){
                toReturn.put(1, false);
                return convertMapToJson(toReturn);
            }
            Integer i = account.changePassowrd(login, password, password3);
            if(i == 1){
                i = account.changeStartPassowrd(login, password, password3); //makes to possible to start out with an easy passowrd
                if(i == 1){
                    toReturn.put(1, false);
                    return convertMapToJson(toReturn);
                }
            }
            toReturn.put(1, true);
            accountRepository.save(account);
            return convertMapToJson(toReturn);
        }
        catch(Throwable e){
            toReturn.put(1, false);
                return convertMapToJson(toReturn);
        }
    }
    
    @Transactional
    @PostMapping("/add-uwaga")
    public String addOUwaga(@RequestBody Map<Integer, Object> newMapData) { //1: login 2: password 3: Id ucznia 4: tresc -> 1: true/false
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
            UserUczen userUczen = userUczenRepository.getReferenceById(Long.parseLong(newMapData.get(3).toString()));
            if(!(userUczen instanceof UserUczen)){
                toReturn.put(1, false);
                return convertMapToJson(toReturn);
            }
            Dzien dzien = new Dzien(LocalDate.now().getYear(), LocalDate.now().getMonthValue(), LocalDate.now().getDayOfMonth());
            Godzina godzina = new Godzina(LocalTime.now().getHour(), LocalTime.now().getMinute());
            Uwaga uwaga = new Uwaga(newMapData.get(4).toString(), dzien, godzina, userUczen, account.getUserNauczyciel());
            Uwaga savedUwaga = uwagaRepository.save(uwaga);
            if(!(savedUwaga instanceof Uwaga)){
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

    @Transactional
    @PutMapping("/edit-uwaga/{id}")
    public String editUwaga(@PathVariable String id, @RequestBody Map<Integer, Object> newMapData) { //1: login 2: password 3: Id Uwagi 4: tresc -> 1: true/false
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
            Uwaga uwaga = uwagaRepository.getReferenceById(Long.parseLong(id));
            uwaga.setTresc(newMapData.get(4).toString());
            Uwaga savedUwaga = uwagaRepository.save(uwaga);
            if(!(savedUwaga instanceof Uwaga)){
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

    @Transactional
    @DeleteMapping("/remove-uwaga/{id}")
    public String removeUwaga(@PathVariable Long id, @RequestBody Map<Integer, Object> newMapData){ //1: logi 2:password -> true/false
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
            Uwaga uwaga = uwagaRepository.getReferenceById(id);
            if(!(uwaga instanceof Uwaga)){
                toReturn.put(1, false);
                return convertMapToJson(toReturn);
            }
            uwagaRepository.delete(uwaga);
            toReturn.put(1, true);
            return convertMapToJson(toReturn);
        }
        catch(Throwable e){
            toReturn.put(1, false);
            return convertMapToJson(toReturn);
        }
    
    }

    @Transactional
    @PostMapping("/add-leckja")
    public String addLekcja(@RequestBody Map<Integer, Object> newMapData) { //1: lgin 2: password 3: dzien 4: id klasa 5: id sala 6: id nauczyciela 7: time start 8: time end 9: id przedmiot 10: dzien tygodnia-> true/false
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
            Klasa klasa = klasaRepository.getReferenceById(Long.parseLong( newMapData.get(4).toString()));
            if(!(klasa instanceof Klasa)){
                toReturn.put(1, false);
                return convertMapToJson(toReturn);
            }
            Sala sala = salaRepository.getReferenceById(Long.parseLong( newMapData.get(5).toString()));
            if(!(sala instanceof Sala)){
                toReturn.put(1, false);
                return convertMapToJson(toReturn);
            }
            UserNauczyciel nauczyciel = userNauczycielRepository.getReferenceById(Long.parseLong( newMapData.get(6).toString()));
            if(!(nauczyciel instanceof UserNauczyciel)){
                toReturn.put(1, false);
                return convertMapToJson(toReturn);
            }
            Przedmiot przedmiot = przedmiotRepository.getReferenceById(Long.parseLong( newMapData.get(9).toString()));
            if(!(przedmiot instanceof Przedmiot)){
                toReturn.put(1, false);
                return convertMapToJson(toReturn);
            }
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");
            DateTimeFormatter formatter2 = DateTimeFormatter.ofPattern("HH:mm");
            LocalDate date = LocalDate.parse(newMapData.get(3).toString(), formatter);
            LocalTime time1 = LocalTime.parse(newMapData.get(8).toString(), formatter2);
            LocalTime time2 = LocalTime.parse(newMapData.get(9).toString(), formatter2);

            Dzien dzien = new Dzien(date.getYear(), date.getMonthValue(), date.getDayOfMonth());
            Godzina godzina1 = new Godzina(time1.getHour(), time1.getMinute());
            Godzina godzina2 = new Godzina(time2.getHour(), time2.getMinute());

            Lekcja lekcja = new Lekcja(godzina1,godzina2, dzien, klasa, sala, nauczyciel, przedmiot, newMapData.get(10).toString());
            Lekcja savedLekcja = lekcjaRepository.save(lekcja);
            if(!(savedLekcja instanceof Lekcja)){
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
    
    @Transactional
    @PutMapping("/edit-lekcja/{id}")
    public String editLekcja(@PathVariable Long id, @RequestBody Map<Integer, Object> newMapData) { //1: lgin 2: password 3: id dzien 4: id klasa 5: id sala 6: id nauczyciela 7: time start 8: time end 9: id przedmiot -> true/false
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
            Klasa klasa = klasaRepository.getReferenceById(Long.parseLong( newMapData.get(4).toString()));
            if(!(klasa instanceof Klasa)){
                toReturn.put(1, false);
                return convertMapToJson(toReturn);
            }
            Sala sala = salaRepository.getReferenceById(Long.parseLong( newMapData.get(5).toString()));
            if(!(sala instanceof Sala)){
                toReturn.put(1, false);
                return convertMapToJson(toReturn);
            }
            UserNauczyciel nauczyciel = userNauczycielRepository.getReferenceById(Long.parseLong( newMapData.get(6).toString()));
            if(!(nauczyciel instanceof UserNauczyciel)){
                toReturn.put(1, false);
                return convertMapToJson(toReturn);
            }
            Przedmiot przedmiot = przedmiotRepository.getReferenceById(Long.parseLong( newMapData.get(9).toString()));
            if(!(przedmiot instanceof Przedmiot)){
                toReturn.put(1, false);
                return convertMapToJson(toReturn);
            }
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");
            DateTimeFormatter formatter2 = DateTimeFormatter.ofPattern("HH:mm");
            LocalDate date = LocalDate.parse(newMapData.get(3).toString(), formatter);
            LocalTime time1 = LocalTime.parse(newMapData.get(8).toString(), formatter2);
            LocalTime time2 = LocalTime.parse(newMapData.get(9).toString(), formatter2);

            Dzien dzien = new Dzien(date.getYear(), date.getMonthValue(), date.getDayOfMonth());
            Godzina godzina1 = new Godzina(time1.getHour(), time1.getMinute());
            Godzina godzina2 = new Godzina(time2.getHour(), time2.getMinute());


            Lekcja lekcja = lekcjaRepository.getReferenceById(id);
            lekcja.setDzien(dzien);
            lekcja.setkongodz(godzina2);
            lekcja.setKlasa(klasa);
            lekcja.setNauczyciel(nauczyciel);
            lekcja.setSala(sala);
            lekcja.setpoczgodz(godzina1);
            lekcja.setPrzedmiot(przedmiot);

            Lekcja savedLekcja = lekcjaRepository.save(lekcja);
            if(!(savedLekcja instanceof Lekcja)){
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

    @Transactional
    @PostMapping("/get-uwagi-uczen")
    public String getUwagiUczen(@RequestBody Map<Integer, Object> newMapData) { //1: login 2: password -> 1: true, flase 2:- rest
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
            if(account.getUserUczen() == null){ //not a uczen
                toReturn.put(1, false);
                return convertMapToJson(toReturn);
            }
            Hibernate.initialize(account.getUserUczen());
            List<Uwaga> allUwaga = uwagaRepository.findByUczen(account.getUserUczen());
            int i = 2;
            toReturn.put(1, true);
            for(Uwaga uwaga:allUwaga){
                toReturn.put(i, uwaga);
                i++;
            }
            return convertMapToJson(toReturn);

        }
        catch(Throwable e){
            toReturn.put(1, false);
            return convertMapToJson(toReturn);
        }
    }
    
    @Transactional
    @PostMapping("/get-uwagi-nauczyciel/{id}") //id uczen you want to look at
    public String getUwagiNau(@RequestBody Map<Integer, Object> newMapData, @PathVariable Long id) { //1: login 2: password -> 1: true, flase 2:- rest
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
            if(account.getUserNauczyciel() == null){ //not a nauczycuel
                toReturn.put(1, false);
                return convertMapToJson(toReturn);
            }
            UserUczen userUczen = userUczenRepository.getReferenceById(id);
            if(!(userUczen instanceof UserUczen)){
                toReturn.put(1, false);
                return convertMapToJson(toReturn);
            }
            Hibernate.initialize(userUczen);
            List<Uwaga> allUwaga = uwagaRepository.findByUczen(userUczen);
            int i = 2;
            toReturn.put(1, true);
            for(Uwaga uwaga:allUwaga){
                toReturn.put(i, uwaga);
                i++;
            }
            return convertMapToJson(toReturn);

        }
        catch(Throwable e){
            toReturn.put(1, false);
            return convertMapToJson(toReturn);
        }
    }

    @Transactional
    @PostMapping("/get-uwagi-nauczyciel-all")
    public String getUwagiNauczycuek(@RequestBody Map<Integer, Object> newMapData) { //1: login 2: password -> 1: true, flase 2:- rest
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
            if(account.getUserNauczyciel() == null){ //not a uczen
                toReturn.put(1, false);
                return convertMapToJson(toReturn);
            }
            Hibernate.initialize(account.getUserNauczyciel());
            List<Uwaga> allUwaga = uwagaRepository.findByNauczyciel(account.getUserNauczyciel());
            int i = 2;
            toReturn.put(1, true);
            for(Uwaga uwaga:allUwaga){
                toReturn.put(i, uwaga);
                i++;
            }
            return convertMapToJson(toReturn);

        }
        catch(Throwable e){
            toReturn.put(1, false);
            return convertMapToJson(toReturn);
        }
    }

    @Transactional
    @PostMapping("/get-oceny-uczen")
    public String getOcenyUczen(@RequestBody Map<Integer, Object> newMapData) { //1: login 2: password -> 1: true, flase 2:- rest
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
            if(account.getUserUczen() == null){ //not a uczen
                toReturn.put(1, false);
                return convertMapToJson(toReturn);
            }
            Hibernate.initialize(account.getUserUczen());
            List<Ocena> allOcena = ocenaRepository.findByUczen(account.getUserUczen());
            int i = 2;
            toReturn.put(1, true);
            for(Ocena ocena:allOcena){
                toReturn.put(i, ocena);
                i++;
            }
            return convertMapToJson(toReturn);

        }
        catch(Throwable e){
            toReturn.put(1, false);
            return convertMapToJson(toReturn);
        }
    }
    
    @Transactional
    @PostMapping("/get-oceny-nauczyciel/{id}") //id uczen you want to look at
    public String getOcenyNau(@RequestBody Map<Integer, Object> newMapData, @PathVariable Long id) { //1: login 2: password -> 1: true, flase 2:- rest
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
            if(account.getUserNauczyciel() == null){ //not a nauczycuel
                toReturn.put(1, false);
                return convertMapToJson(toReturn);
            }
            UserUczen userUczen = userUczenRepository.getReferenceById(id);
            if(!(userUczen instanceof UserUczen)){
                toReturn.put(1, false);
                return convertMapToJson(toReturn);
            }
            Hibernate.initialize(userUczen);
            List<Ocena> allOcena = ocenaRepository.findByUczen(userUczen);
            int i = 2;
            toReturn.put(1, true);
            for(Ocena ocena:allOcena){
                toReturn.put(i, ocena);
                i++;
            }
            return convertMapToJson(toReturn);

        }
        catch(Throwable e){
            toReturn.put(1, false);
            return convertMapToJson(toReturn);
        }
    }

    @Transactional
    @PostMapping("/get-sprawdzian-uczen")
    public String getSprawdzianyUczen(@RequestBody Map<Integer, Object> newMapData) { //1: login 2: password -> 1: true, flase 2:- rest
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
            if(account.getUserUczen() == null){ //not a uczen
                toReturn.put(1, false);
                return convertMapToJson(toReturn);
            }
            Hibernate.initialize(account.getUserUczen().getKlasa());
            List<Sprawdzian> allSprawdzian = sprawdzianRepository.findByKlasa(account.getUserUczen().getKlasa());
            int i = 2;
            toReturn.put(1, true);
            for(Sprawdzian sprawdzian: allSprawdzian){
                toReturn.put(i, sprawdzian);
                i++;
            }
            return convertMapToJson(toReturn);

        }
        catch(Throwable e){
            toReturn.put(1, false);
            return convertMapToJson(toReturn);
        }
    }

    @Transactional
    @PostMapping("/get-sprawdzian-nauczyciel/{id}") //id klasa
    public String getSprawdzianyNau(@RequestBody Map<Integer, Object> newMapData, @PathVariable Long id) { //1: login 2: password -> 1: true, flase 2:- rest
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
            if(account.getUserNauczyciel() == null){ //not a nauczycuel
                toReturn.put(1, false);
                return convertMapToJson(toReturn);
            }
            Klasa klasa = klasaRepository.getReferenceById(id);
            if(!(klasa instanceof Klasa)){
                toReturn.put(1, false);
                return convertMapToJson(toReturn);
            }
            Hibernate.initialize(klasa);
            List<Sprawdzian> allSprawdzian = sprawdzianRepository.findByKlasa(klasa);
            int i = 2;
            toReturn.put(1, true);
            for(Sprawdzian sprawdzian:allSprawdzian){
                toReturn.put(i, sprawdzian);
                i++;
            }
            return convertMapToJson(toReturn);

        }
        catch(Throwable e){
            toReturn.put(1, false);
            return convertMapToJson(toReturn);
        }
    }

    @Transactional
    @PostMapping("/get-uczen-rodzica")
    public String getUczenByRodzic(@RequestBody Map<Integer, Object> newMapData) { //1:login 2: password -> 1: true 2-ile masz dzieci : id dzieci
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
            if(account.getUserRodzic() == null){ //not a nauczycuel
                toReturn.put(1, false);
                return convertMapToJson(toReturn);
            }
            Hibernate.initialize(account.getUserRodzic());
            List<Relacja> allRelacja = relacjaRepository.findByUserRodzic(account.getUserRodzic());
            toReturn.put(1, true);
            int i = 2;
            for(Relacja relacja:allRelacja){
                toReturn.put(i, relacja.getUserUczen());
                i++;
            }
            return convertMapToJson(toReturn);
        }
        catch(Throwable e){
            toReturn.put(1, false);
            return convertMapToJson(toReturn);
        }

    }
    
    @Transactional
    @PostMapping("/get-sprawdzian-rodzic/{id}") //id ucznia
    public String getSprawdzianyRodzic(@RequestBody Map<Integer, Object> newMapData, @PathVariable Long id) { //1: login 2: password -> 1: true, flase 2:- rest
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
            if(account.getUserRodzic() == null){ //not a rodzic
                toReturn.put(1, false);
                return convertMapToJson(toReturn);
            }
            UserUczen userUczen = userUczenRepository.getReferenceById(id);
            if(!(userUczen instanceof UserUczen)){
                toReturn.put(1, false);
                return convertMapToJson(toReturn);
            }
            Relacja relacja = relacjaRepository.findAllByUserUczenAndUserRodzic(userUczen, account.getUserRodzic());
            if(!(relacja instanceof Relacja)){
                toReturn.put(1, false);
                return convertMapToJson(toReturn);
            }
            Hibernate.initialize(userUczen.getKlasa());
            List<Sprawdzian> allSprawdzian = sprawdzianRepository.findByKlasa(userUczen.getKlasa());
            int i = 2;
            toReturn.put(1, true);
            for(Sprawdzian sprawdzian:allSprawdzian){
                toReturn.put(i, sprawdzian);
                i++;
            }
            return convertMapToJson(toReturn);

        }
        catch(Throwable e){
            toReturn.put(1, false);
            return convertMapToJson(toReturn);
        }
    }

    @Transactional
    @PostMapping("/get-uwagi-rodzic/{id}") //id uczen you want to look at
    public String getUwagiRodzic(@RequestBody Map<Integer, Object> newMapData, @PathVariable Long id) { //1: login 2: password -> 1: true, flase 2:- rest
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
            if(account.getUserRodzic() == null){ //not a nauczycuel
                toReturn.put(1, false);
                return convertMapToJson(toReturn);
            }
            UserUczen userUczen = userUczenRepository.getReferenceById(id);
            if(!(userUczen instanceof UserUczen)){
                toReturn.put(1, false);
                return convertMapToJson(toReturn);
            }
            Relacja relacja = relacjaRepository.findAllByUserUczenAndUserRodzic(userUczen, account.getUserRodzic());
            if(!(relacja instanceof Relacja)){
                toReturn.put(1, false);
                return convertMapToJson(toReturn);
            }
            Hibernate.initialize(userUczen);
            List<Uwaga> allUwaga = uwagaRepository.findByUczen(userUczen);
            int i = 2;
            toReturn.put(1, true);
            for(Uwaga uwaga:allUwaga){
                toReturn.put(i, uwaga);
                i++;
            }
            return convertMapToJson(toReturn);

        }
        catch(Throwable e){
            toReturn.put(1, false);
            return convertMapToJson(toReturn);
        }
    }

    @Transactional
    @PostMapping("/get-oceny-rodzic/{id}") //id uczen you want to look at
    public String getOcenyRodzic(@RequestBody Map<Integer, Object> newMapData, @PathVariable Long id) { //1: login 2: password -> 1: true, flase 2:- rest
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
            if(account.getUserRodzic() == null){ //not a nauczycuel
                toReturn.put(1, false);
                return convertMapToJson(toReturn);
            }
            UserUczen userUczen = userUczenRepository.getReferenceById(id);
            if(!(userUczen instanceof UserUczen)){
                toReturn.put(1, false);
                return convertMapToJson(toReturn);
            }
            Relacja relacja = relacjaRepository.findAllByUserUczenAndUserRodzic(userUczen, account.getUserRodzic());
            if(!(relacja instanceof Relacja)){
                toReturn.put(1, false);
                return convertMapToJson(toReturn);
            }
            Hibernate.initialize(userUczen);
            List<Ocena> allOcena = ocenaRepository.findByUczen(userUczen);
            int i = 2;
            toReturn.put(1, true);
            for(Ocena ocena:allOcena){
                toReturn.put(i, ocena);
                i++;
            }
            return convertMapToJson(toReturn);

        }
        catch(Throwable e){
            toReturn.put(1, false);
            return convertMapToJson(toReturn);
        }
    }
    
    @Transactional
    @PostMapping("/get-lekcja-uczen")
    public String getLekcjaUczen(@RequestBody Map<Integer, Object> newMapData) { //the same
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
            if(account.getUserUczen() == null){ //not a uczen
                toReturn.put(1, false);
                return convertMapToJson(toReturn);
            }
            Hibernate.initialize(account.getUserUczen().getKlasa());
            List<Lekcja> allLekcja = lekcjaRepository.findByKlasa(account.getUserUczen().getKlasa());
            int i = 2;
            toReturn.put(1, true);
            for(Lekcja lekcja: allLekcja){
                toReturn.put(i, lekcja);
                i++;
            }
            return convertMapToJson(toReturn);
        }
        catch(Throwable e){
            toReturn.put(1, false);
            return convertMapToJson(toReturn);
        }
    }
    
    @Transactional
    @PostMapping("/get-lekcja-rodzic/{id}")
    public String getLekcjaUczen(@RequestBody Map<Integer, Object> newMapData, @PathVariable Long id) {
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
            if(account.getUserRodzic() == null){ //not a rodzic
                toReturn.put(1, false);
                return convertMapToJson(toReturn);
            }
            UserUczen userUczen = userUczenRepository.getReferenceById(id);
            if(!(userUczen instanceof UserUczen)){
                toReturn.put(1, false);
                return convertMapToJson(toReturn);
            }
            Relacja relacja = relacjaRepository.findAllByUserUczenAndUserRodzic(userUczen, account.getUserRodzic());
            if(!(relacja instanceof Relacja)){
                toReturn.put(1, false);
                return convertMapToJson(toReturn);
            }
            Hibernate.initialize(userUczen.getKlasa());
            List<Lekcja> allLekcja = lekcjaRepository.findByKlasa(userUczen.getKlasa());
            int i = 2;
            toReturn.put(1, true);
            for(Lekcja lekcja: allLekcja){
                toReturn.put(i, lekcja);
                i++;
            }
            return convertMapToJson(toReturn);
        }
        catch(Throwable e){
            toReturn.put(1, false);
            return convertMapToJson(toReturn);
        }
    
    }

    @Transactional
    @PostMapping("/get-lekcja-nauczyciel/{id}") //id klasy!
    public String getLekcjaNauczyciel(@RequestBody Map<Integer, Object> newMapData, @PathVariable Long id) {
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
            if(account.getUserNauczyciel() == null){ //not a uczen
                toReturn.put(1, false);
                return convertMapToJson(toReturn);
            }
            Klasa klasa = klasaRepository.getReferenceById(id);
            if(!(klasa instanceof Klasa)){
                toReturn.put(1, false);
                return convertMapToJson(toReturn);
            }
            Hibernate.initialize(klasa);
            List<Lekcja> allLekcja = lekcjaRepository.findByKlasa(klasa);
            int i = 2;
            toReturn.put(1, true);
            for(Lekcja lekcja: allLekcja){
                toReturn.put(i, lekcja);
                i++;
            }
            return convertMapToJson(toReturn);
        }
        catch(Throwable e){
            toReturn.put(1, false);
            return convertMapToJson(toReturn);
        }
    
    }

    //create message
    @PostMapping("/write-message")
    public String writeMessage(@RequestBody Map<Integer, Object> newMapData) { //1: login 2: password 3: tytuł 4: wiadomość 5: godz (format HH:MM) 6: data (format DD.MM.YYYY) 7: załączniki -> 1: true/false 2: id wiadomosci
        Map<Integer, Object> toReturn = new HashMap<>();
        try{
            String login = newMapData.get(1).toString();
            String password = newMapData.get(2).toString();
            Account account = accountRepository.findIdByLogin(login);
            if(!(account instanceof Account)){
                toReturn.put(1, false);
                return convertMapToJson(toReturn);
            }
            if(!account.checkUserAccount(login, password)){ //is an account
                toReturn.put(1, false);
                return convertMapToJson(toReturn);
            }
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");
            DateTimeFormatter formatter2 = DateTimeFormatter.ofPattern("HH:mm");
            LocalDate date = LocalDate.parse(newMapData.get(6).toString(), formatter);
            LocalTime time = LocalTime.parse(newMapData.get(5).toString(), formatter2);

            Godzina godzina = new Godzina(time.getHour(), time.getMinute());
            Dzien dzien = new Dzien(date.getYear(), date.getMonthValue(), date.getDayOfMonth());
            Wiadomosc wiadomosc = new Wiadomosc(newMapData.get(3).toString(),newMapData.get(4).toString(), godzina, dzien, SerializationUtils.serialize(newMapData.get(7)), account);
            if(!(wiadomosc instanceof Wiadomosc)){
                toReturn.put(1, false);
                return convertMapToJson(toReturn);
            }
            Wiadomosc savedWiadomosc = wiadomoscRepository.save(wiadomosc);
            if(!(savedWiadomosc instanceof Wiadomosc)){
                toReturn.put(1, false);
                return convertMapToJson(toReturn);
            }
            toReturn.put(1, true);
            toReturn.put(1, savedWiadomosc.getId());
            return convertMapToJson(toReturn);

        }
        catch(Throwable e){
            toReturn.put(1, false);
            return convertMapToJson(toReturn);
        }
    }

    @PostMapping("/send-message/nauczyciel/{id}") //id wiadomości
    public String sendMsgNauczyciel(@RequestBody Map<Integer, Object> newMapData,@PathVariable Long id) { //1:login 2: password 3: id odbiorcy 4: typ odbiorcy (1 dla rodzic, 2 uczen, 3 nauczyciel) -> 1: true/false
        Map<Integer, Object> toReturn = new HashMap<>();
        try{
            String login = newMapData.get(1).toString();
            String password = newMapData.get(2).toString();
            Account account = accountRepository.findIdByLogin(login);
            if(!(account instanceof Account)){
                toReturn.put(1, false);
                return convertMapToJson(toReturn);
            }
            if(!account.checkUserAccount(login, password)){ //is an account
                toReturn.put(1, false);
                return convertMapToJson(toReturn);
            }
            if(account.getUserNauczyciel() == null){ //not a nauczyciel
                toReturn.put(1, false);
                return convertMapToJson(toReturn);
            }
            Wiadomosc wiadomosc = wiadomoscRepository.getReferenceById(id);
            if(!(wiadomosc instanceof Wiadomosc)){
                toReturn.put(1, false);
                return convertMapToJson(toReturn);
            }
            if(!wiadomosc.getAccount().equals(account)){
                toReturn.put(1, false);
                return convertMapToJson(toReturn);
            }
            Long i = Long.parseLong(newMapData.get(4).toString());
            Long idUser = Long.parseLong(newMapData.get(3).toString());
            User user;
            switch(i){
                case 1L:
                    user = userRodzicRepository.getReferenceById(idUser);
                    if(!(user instanceof UserRodzic)){
                        toReturn.put(1, false);
                        return convertMapToJson(toReturn);
                    }
                    WiadomoscR wiadomoscR = new WiadomoscR(wiadomosc, (UserRodzic)user);
                    WiadomoscR saveWiadomoscR = wiadomoscRRepository.save(wiadomoscR);
                    if((saveWiadomoscR instanceof WiadomoscR)){
                        toReturn.put(1, true);
                        return convertMapToJson(toReturn);
                    }
                    break;
                case 2L:
                    user = userUczenRepository.getReferenceById(idUser);
                    if(!(user instanceof UserUczen)){
                        toReturn.put(1, false);
                        return convertMapToJson(toReturn);
                    }
                    WiadomoscU wiadomoscU = new WiadomoscU(wiadomosc, (UserUczen)user);
                    WiadomoscU saveWiadomoscU = wiadomoscURepository.save(wiadomoscU);
                    if((saveWiadomoscU instanceof WiadomoscU)){
                        toReturn.put(1, true);
                        return convertMapToJson(toReturn);
                    }
                    break;
                case 3L:
                    user = userNauczycielRepository.getReferenceById(idUser);
                        if(!(user instanceof UserNauczyciel)){
                            toReturn.put(1, false);
                            return convertMapToJson(toReturn);
                        }
                        WiadomoscN wiadomoscN = new WiadomoscN(wiadomosc, (UserNauczyciel)user);
                        WiadomoscN saveWiadomoscN = wiadomoscNRepository.save(wiadomoscN);
                        if((saveWiadomoscN instanceof WiadomoscN)){
                            toReturn.put(1, true);
                            return convertMapToJson(toReturn);
                        }
                    break;
                default:
                    toReturn.put(1, false);
                    return convertMapToJson(toReturn);
            }
            toReturn.put(1, false);
            return convertMapToJson(toReturn);
            

        }
        catch(Throwable e){
            toReturn.put(1, false);
            return convertMapToJson(toReturn);
        }
    }

    @PostMapping("/send-message/uczen/{id}") //id wiadomości - uczen do nauczyciela tylko moze
    public String sendMsgUczen(@RequestBody Map<Integer, Object> newMapData,@PathVariable Long id) { //1:login 2: password 3: id odbiorcy ) -> 1: true/false
        Map<Integer, Object> toReturn = new HashMap<>();
        try{
            String login = newMapData.get(1).toString();
            String password = newMapData.get(2).toString();
            Account account = accountRepository.findIdByLogin(login);
            if(!(account instanceof Account)){
                toReturn.put(1, false);
                return convertMapToJson(toReturn);
            }
            if(!account.checkUserAccount(login, password)){ //is an account
                toReturn.put(1, false);
                return convertMapToJson(toReturn);
            }
            if(account.getUserUczen() == null){ //not a nauczyciel
                toReturn.put(1, false);
                return convertMapToJson(toReturn);
            }
            Wiadomosc wiadomosc = wiadomoscRepository.getReferenceById(id);
            if(!(wiadomosc instanceof Wiadomosc)){
                toReturn.put(1, false);
                return convertMapToJson(toReturn);
            }
            if(!wiadomosc.getAccount().equals(account)){
                toReturn.put(1, false);
                return convertMapToJson(toReturn);
            }
            Long idUser = Long.parseLong(newMapData.get(3).toString());
            UserNauczyciel userNauczyciel = userNauczycielRepository.getReferenceById(idUser);
            if(!(userNauczyciel instanceof UserNauczyciel)){
                toReturn.put(1, false);
                return convertMapToJson(toReturn);
            }
            WiadomoscN wiadomoscN = new WiadomoscN(wiadomosc, userNauczyciel);
            WiadomoscN saveWiadomoscN = wiadomoscNRepository.save(wiadomoscN);
            if((saveWiadomoscN instanceof WiadomoscN)){
                toReturn.put(1, true);
                return convertMapToJson(toReturn);
            }
            toReturn.put(1, false);
            return convertMapToJson(toReturn);

        }
        catch(Throwable e){
            toReturn.put(1, false);
            return convertMapToJson(toReturn);
        }
    }
    
    @PostMapping("/send-message/rodzic/{id}") //id wiadomości - uczen do nauczyciela tylko moze
    public String sendMsgRodzic(@RequestBody Map<Integer, Object> newMapData,@PathVariable Long id) { //1:login 2: password 3: id odbiorcy ) -> 1: true/false
        Map<Integer, Object> toReturn = new HashMap<>();
        try{
            String login = newMapData.get(1).toString();
            String password = newMapData.get(2).toString();
            Account account = accountRepository.findIdByLogin(login);
            if(!(account instanceof Account)){
                toReturn.put(1, false);
                return convertMapToJson(toReturn);
            }
            if(!account.checkUserAccount(login, password)){ //is an account
                toReturn.put(1, false);
                return convertMapToJson(toReturn);
            }
            if(account.getUserRodzic() == null){ //not a nauczyciel
                toReturn.put(1, false);
                return convertMapToJson(toReturn);
            }
            Wiadomosc wiadomosc = wiadomoscRepository.getReferenceById(id);
            if(!(wiadomosc instanceof Wiadomosc)){
                toReturn.put(1, false);
                return convertMapToJson(toReturn);
            }
            if(!wiadomosc.getAccount().equals(account)){
                toReturn.put(1, false);
                return convertMapToJson(toReturn);
            }
            Long idUser = Long.parseLong(newMapData.get(3).toString());
            UserNauczyciel userNauczyciel = userNauczycielRepository.getReferenceById(idUser);
            if(!(userNauczyciel instanceof UserNauczyciel)){
                toReturn.put(1, false);
                return convertMapToJson(toReturn);
            }
            WiadomoscN wiadomoscN = new WiadomoscN(wiadomosc, userNauczyciel);
            WiadomoscN saveWiadomoscN = wiadomoscNRepository.save(wiadomoscN);
            if((saveWiadomoscN instanceof WiadomoscN)){
                toReturn.put(1, true);
                return convertMapToJson(toReturn);
            }
            toReturn.put(1, false);
            return convertMapToJson(toReturn);

        }
        catch(Throwable e){
            toReturn.put(1, false);
            return convertMapToJson(toReturn);
        }
    }

    @PostMapping("/get-wiadomosci/uczen")
    public String getWU(@RequestBody Map<Integer, Object> newMapData) {
        Map<Integer, Object> toReturn = new HashMap<>();
        try{
            String login = newMapData.get(1).toString();
            String password = newMapData.get(2).toString();
            Account account = accountRepository.findIdByLogin(login);
            if(!(account instanceof Account)){
                toReturn.put(1, false);
                return convertMapToJson(toReturn);
            }
            if(!account.checkUserAccount(login, password)){ //is an account
                toReturn.put(1, false);
                return convertMapToJson(toReturn);
            }
            if(account.getUserUczen() == null){ //not a nauczyciel
                toReturn.put(1, false);
                return convertMapToJson(toReturn);
            }
            Hibernate.initialize(account.getUserUczen());
            toReturn.put(1, true);
            List<WiadomoscU> allW = wiadomoscURepository.findByOdbiorca(account.getUserUczen());
            int i = 2;
            for(WiadomoscU wiadomoscU : allW){
                toReturn.put(i,wiadomoscU);
                i++;
            }
            return convertMapToJson(toReturn);
        }
        catch(Throwable e){
            toReturn.put(1, false);
            return convertMapToJson(toReturn);
        }
    }
    
    @PostMapping("/get-wiadomosci/rodzic")
    public String getWR(@RequestBody Map<Integer, Object> newMapData) {
        Map<Integer, Object> toReturn = new HashMap<>();
        try{
            String login = newMapData.get(1).toString();
            String password = newMapData.get(2).toString();
            Account account = accountRepository.findIdByLogin(login);
            if(!(account instanceof Account)){
                toReturn.put(1, false);
                return convertMapToJson(toReturn);
            }
            if(!account.checkUserAccount(login, password)){ //is an account
                toReturn.put(1, false);
                return convertMapToJson(toReturn);
            }
            if(account.getUserRodzic() == null){ //not a nauczyciel
                toReturn.put(1, false);
                return convertMapToJson(toReturn);
            }
            Hibernate.initialize(account.getUserRodzic());
            toReturn.put(1, true);
            List<WiadomoscR> allW = wiadomoscRRepository.findByOdbiorca(account.getUserRodzic());
            int i = 2;
            for(WiadomoscR wiadomoscU : allW){
                toReturn.put(i,wiadomoscU);
                i++;
            }
            return convertMapToJson(toReturn);
        }
        catch(Throwable e){
            toReturn.put(1, false);
            return convertMapToJson(toReturn);
        }
    }

    @PostMapping("/get-wiadomosci/nauczyciel")
    public String getWN(@RequestBody Map<Integer, Object> newMapData) {
        Map<Integer, Object> toReturn = new HashMap<>();
        try{
            String login = newMapData.get(1).toString();
            String password = newMapData.get(2).toString();
            Account account = accountRepository.findIdByLogin(login);
            if(!(account instanceof Account)){
                toReturn.put(1, false);
                return convertMapToJson(toReturn);
            }
            if(!account.checkUserAccount(login, password)){ //is an account
                toReturn.put(1, false);
                return convertMapToJson(toReturn);
            }
            if(account.getUserNauczyciel() == null){ //not a nauczyciel
                toReturn.put(1, false);
                return convertMapToJson(toReturn);
            }
            Hibernate.initialize(account.getUserNauczyciel());
            toReturn.put(1, true);
            List<WiadomoscN> allW = wiadomoscNRepository.findByOdbiorca(account.getUserNauczyciel());
            int i = 2;
            for(WiadomoscN wiadomoscU : allW){
                toReturn.put(i,wiadomoscU);
                i++;
            }
            return convertMapToJson(toReturn);
        }
        catch(Throwable e){
            toReturn.put(1, false);
            return convertMapToJson(toReturn);
        }
    }

    @PostMapping("/get-my-wiadomosci")
    public String getW(@RequestBody Map<Integer, Object> newMapData) {
        Map<Integer, Object> toReturn = new HashMap<>();
        try{
            String login = newMapData.get(1).toString();
            String password = newMapData.get(2).toString();
            Account account = accountRepository.findIdByLogin(login);
            if(!(account instanceof Account)){
                toReturn.put(1, false);
                return convertMapToJson(toReturn);
            }
            if(!account.checkUserAccount(login, password)){ //is an account
                toReturn.put(1, false);
                return convertMapToJson(toReturn);
            }
            Hibernate.initialize(account);
            toReturn.put(1, true);
            List<Wiadomosc> allW = wiadomoscRepository.findByAccount(account);
            int i = 2;
            for(Wiadomosc wiadomoscU : allW){
                toReturn.put(i,wiadomoscU);
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

