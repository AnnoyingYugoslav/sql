package com.example.sql.entities;
import com.example.sql.converters.DzienConverter;
import com.example.sql.converters.GodzinaConverter;

import jakarta.persistence.*;

@Entity
@Table(name = "sprawdzian")
public class Sprawdzian {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "kategoria", nullable = false, length = 100)
    private String kategoria;

    @ManyToOne
    @JoinColumn(name = "id_nauczyciela", nullable = false)
    private UserNauczyciel nauczyciel;

    @ManyToOne
    @JoinColumn(name = "id_sali", nullable = false)
    private Sala sala;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "id_klasy", nullable = false)
    private Klasa klasa;

    @ManyToOne
    @JoinColumn(name = "id_przedmiotu", nullable = false)
    private Przedmiot przedmiot;

    @Convert(converter = DzienConverter.class)
    private Dzien dzien;

    @Convert(converter = GodzinaConverter.class)
    private Godzina godzina;

    public Sprawdzian(String kategoria, UserNauczyciel nauczyciel, Sala sala, Klasa klasa, Przedmiot przedmiot,
            Dzien dzien, Godzina godzina) {
        this.kategoria = kategoria;
        this.nauczyciel = nauczyciel;
        this.sala = sala;
        this.klasa = klasa;
        this.przedmiot = przedmiot;
        this.dzien = dzien;
        this.godzina = godzina;
    }


    public Sprawdzian() {
    }


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getKategoria() {
        return kategoria;
    }

    public void setKategoria(String kategoria) {
        this.kategoria = kategoria;
    }

    public UserNauczyciel getNauczyciel() {
        return nauczyciel;
    }

    public void setNauczyciel(UserNauczyciel nauczyciel) {
        this.nauczyciel = nauczyciel;
    }

    public Sala getSala() {
        return sala;
    }

    public void setSala(Sala sala) {
        this.sala = sala;
    }
    public void setDzien(Dzien dzien){
        this.dzien = dzien;
    }

    public Dzien getDzien(){
        return dzien;
    }
    
    public Klasa getKlasa() {
        return klasa;
    }

    public void setKlasa(Klasa klasa) {
        this.klasa = klasa;
    }

    public Przedmiot getPrzedmiot() {
        return przedmiot;
    }

    public void setPrzedmiot(Przedmiot przedmiot) {
        this.przedmiot = przedmiot;
    }


    @Override
    public String toString() {
        return "Sprawdzian{" +
                "id=" + id +
                ", kategoria='" + kategoria + '\'' +
                ", nauczyciel=" + nauczyciel +
                ", sala=" + sala +
                ", klasa=" + klasa +
                ", przedmiot=" + przedmiot +
                '}';
    }

    public Godzina getGodzina() {
        return godzina;
    }

    public void setGodzina(Godzina godzina) {
        this.godzina = godzina;
    }
}
