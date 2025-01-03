package com.example.sql.repositories;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

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

    @Repository
    public interface WiadomoscURepository extends JpaRepository<WiadomoscU, Long> {
    }