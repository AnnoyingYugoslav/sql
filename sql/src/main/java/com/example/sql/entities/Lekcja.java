package com.example.sql.entities;

import jakarta.persistence.*;
import java.time.LocalDateTime;

import com.example.sql.converters.DzienConverter;
import com.example.sql.converters.GodzinaConverter;


@Entity
@Table(name = "lekcja")
public class Lekcja {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Convert(converter = GodzinaConverter.class)
    private Godzina poczgodz;

    @Convert(converter = GodzinaConverter.class)
    private Godzina kongodz;

    @Convert(converter = DzienConverter.class)
    private Dzien dzien;

    @ManyToOne
    @JoinColumn(name = "id_klasy", nullable = false)
    private Klasa klasa;

    @ManyToOne
    @JoinColumn(name = "id_sali", nullable = false)
    private Sala sala;

    @Column(name = "dzien_tyodnia", nullable = false)
    private String dzTyg;

    @ManyToOne
    @JoinColumn(name = "id_nauczyciela", nullable = false)
    private UserNauczyciel nauczyciel;

    @ManyToOne
    @JoinColumn(name = "przedmiot", nullable = false)
    private Przedmiot przedmiot;

    public Lekcja(Godzina poczgodz, Godzina kongodz, Dzien dzien, Klasa klasa, Sala sala, UserNauczyciel nauczyciel,
            Przedmiot przedmiot, String dzienTyg) {
        this.poczgodz = poczgodz;
        this.kongodz = kongodz;
        this.dzien = dzien;
        this.klasa = klasa;
        this.sala = sala;
        this.nauczyciel = nauczyciel;
        this.przedmiot = przedmiot;
        this.dzTyg = dzienTyg;
    }



    public Lekcja() {
    }

    

    @Override
    public String toString() {
        return "Lekcja{" +
                "id=" + id +
                ", poczgodz=" + poczgodz +
                ", kongodz=" + kongodz +
                ", dzien=" + dzien +
                ", klasa=" + klasa +
                ", sala=" + sala +
                ", nauczyciel=" + nauczyciel +
                '}';
    }

    public Przedmiot getPrzedmiot() {
        return przedmiot;
    }

    public void setPrzedmiot(Przedmiot przedmiot) {
        this.przedmiot = przedmiot;
    }



    public Long getId() {
        return id;
    }



    public void setId(Long id) {
        this.id = id;
    }



    public void setpoczgodz(Godzina poczgodz) {
        this.poczgodz = poczgodz;
    }



    public void setkongodz(Godzina kongodz) {
        this.kongodz = kongodz;
    }



    public Dzien getDzien() {
        return dzien;
    }



    public void setDzien(Dzien dzien) {
        this.dzien = dzien;
    }



    public Klasa getKlasa() {
        return klasa;
    }



    public void setKlasa(Klasa klasa) {
        this.klasa = klasa;
    }



    public Sala getSala() {
        return sala;
    }



    public void setSala(Sala sala) {
        this.sala = sala;
    }



    public UserNauczyciel getNauczyciel() {
        return nauczyciel;
    }



    public void setNauczyciel(UserNauczyciel nauczyciel) {
        this.nauczyciel = nauczyciel;
    }



    public Godzina getPoczgodz() {
        return poczgodz;
    }



    public void setPoczgodz(Godzina poczgodz) {
        this.poczgodz = poczgodz;
    }



    public Godzina getKongodz() {
        return kongodz;
    }



    public void setKongodz(Godzina kongodz) {
        this.kongodz = kongodz;
    }



    public String getDzTyg() {
        return dzTyg;
    }



    public void setDzTyg(String dzTyg) {
        this.dzTyg = dzTyg;
    }
}
