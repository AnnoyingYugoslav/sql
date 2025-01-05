package com.example.sql.entities;

import jakarta.persistence.*;


public class Godzina {

    private Integer h;

    private Integer m;


    public Godzina(Integer h, Integer m) {
        this.h = h;
        this.m = m;
    }

    public Godzina() {
    }

    public Integer getH() {
        return h;
    }

    public void setH(Integer h) {
        this.h = h;
    }

    public Integer getM() {
        return m;
    }

    public void setM(Integer m) {
        this.m = m;
    }
    @Override
    public String toString() {
        return String.format("%02d:%02d", h, m);
    }

    public static Godzina fromString(String timeStr) {
        String[] parts = timeStr.split(":");
        return new Godzina(Integer.parseInt(parts[0]), Integer.parseInt(parts[1]));
    }
}
