package com.example.sql.entities;

import java.time.LocalDate;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;


public class Dzien {

    private Long id;

    private Integer rok;

    private Integer miesiac;

    private Integer dzien;

    public Dzien(Integer rok, Integer miesiac, Integer dzien) {
        this.rok = rok;
        this.miesiac = miesiac;
        this.dzien = dzien;
    }

    public Dzien() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getRok() {
        return rok;
    }

    public void setRok(Integer rok) {
        this.rok = rok;
    }

    public Integer getMiesiac() {
        return miesiac;
    }

    public void setMiesiac(Integer miesiac) {
        this.miesiac = miesiac;
    }

    public Integer getDzien() {
        return dzien;
    }

    public void setDzien(Integer dzien) {
        this.dzien = dzien;
    }
    @Override
    public String toString() {
        return String.format("%04d-%02d-%02d", rok, miesiac, dzien);
    }

    public static Dzien fromString(String dateStr) {
        String[] parts = dateStr.split("-");
        return new Dzien(Integer.parseInt(parts[0]), Integer.parseInt(parts[1]), Integer.parseInt(parts[2]));
    }
}
