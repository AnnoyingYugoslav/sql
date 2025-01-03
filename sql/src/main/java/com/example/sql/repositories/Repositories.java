package com.example.sql.repositories;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.JpaRepository;

import com.example.sql.entities.Dzien;
import com.example.sql.entities.Klasa;
import com.example.sql.entities.Lekcja;
import com.example.sql.entities.Ocena;
import com.example.sql.entities.Przedmiot;
import com.example.sql.entities.Sala;
import com.example.sql.entities.Sprawdzian;
import com.example.sql.entities.UserNauczyciel;
import com.example.sql.entities.UserRodzic;
import com.example.sql.entities.UserUczen;
import com.example.sql.entities.Uwaga;
import com.example.sql.entities.Wiadomosc;
import com.example.sql.entities.WiadomoscN;
import com.example.sql.entities.WiadomoscR;
import com.example.sql.entities.WiadomoscU;


@Configuration
public class Repositories {

    public interface UserRodzicRepository extends JpaRepository<UserRodzic, Long> {
    }

    public interface UserUczenRepository extends JpaRepository<UserUczen, Long> {
    }

    public interface UserNauczycielRepository extends JpaRepository<UserNauczyciel, Long> {
    }

    public interface WiadomoscRepository extends JpaRepository<Wiadomosc, Long> {
    }

    public interface WiadomoscURepository extends JpaRepository<WiadomoscU, Long> {
    }

    public interface WiadomoscRRepository extends JpaRepository<WiadomoscR, Long> {
    }

    public interface WiadomoscNRepository extends JpaRepository<WiadomoscN, Long> {
    }

    public interface KlasaRepository extends JpaRepository<Klasa, Long> {
    }

    public interface LekcjaRepository extends JpaRepository<Lekcja, Long> {
    }

    public interface SalaRepository extends JpaRepository<Sala, Long> {
    }

    public interface PrzedmiotRepository extends JpaRepository<Przedmiot, Long> {
    }

    public interface SprawdzianRepository extends JpaRepository<Sprawdzian, Long> {
    }

    public interface OcenaRepository extends JpaRepository<Ocena, Long> {
    }

    public interface UwagaRepository extends JpaRepository<Uwaga, Long> {
    }

    public interface DzienRepository extends JpaRepository<Dzien, Long> {
    }
}