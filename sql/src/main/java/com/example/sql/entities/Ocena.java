package com.example.sql.entities;

import java.time.LocalDateTime;

import com.example.sql.converters.DzienConverter;
import com.example.sql.converters.GodzinaConverter;

import jakarta.persistence.*;


@Entity
@Table(name = "ocena")
public class Ocena {

    public Ocena(Integer ocena, Dzien dzien, Godzina godzina, UserUczen uczen, UserNauczyciel nauczyciel,
            Przedmiot przedmiot, String opis) {
        this.ocena = ocena;
        this.dzien = dzien;
        this.godzina = godzina;
        this.uczen = uczen;
        this.nauczyciel = nauczyciel;
        this.przedmiot = przedmiot;
        this.opis = opis;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "ocena", nullable = false)
    private Integer ocena;

    @Convert(converter = DzienConverter.class)
    private Dzien dzien;

    @Convert(converter = GodzinaConverter.class)
    private Godzina godzina;

    @ManyToOne
    @JoinColumn(name = "id_ucznia", nullable = false)
    private UserUczen uczen;

    @ManyToOne
    @JoinColumn(name = "id_nauczyciela", nullable = false)
    private UserNauczyciel nauczyciel;

    @ManyToOne
    @JoinColumn(name = "id_przedmiotu", nullable = false)
    private Przedmiot przedmiot;

    @Column(name = "opis", nullable = false)
    private String opis;

    public Ocena() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getOcena() {
        return ocena;
    }

    public void setOcena(Integer ocena) {
        this.ocena = ocena;
    }
    public UserUczen getUczen() {
        return uczen;
    }

    public void setUczen(UserUczen uczen) {
        this.uczen = uczen;
    }

    public UserNauczyciel getNauczyciel() {
        return nauczyciel;
    }

    public void setNauczyciel(UserNauczyciel nauczyciel) {
        this.nauczyciel = nauczyciel;
    }

    public Przedmiot getPrzedmiot() {
        return przedmiot;
    }

    public void setPrzedmiot(Przedmiot przedmiot) {
        this.przedmiot = przedmiot;
    }

    

    @Override
    public String toString() {
        return "Ocena{" +
                "id=" + id +
                ", ocena=" + ocena +
                ", data=" + dzien +
                ", uczen=" + uczen +
                ", nauczyciel=" + nauczyciel +
                ", przedmiot=" + przedmiot +
                '}';
    }


    public String getOpis() {
        return opis;
    }

    public void setOpis(String opis) {
        this.opis = opis;
    }

    public void setDzien(Dzien dzien) {
        this.dzien = dzien;
    }

    public Godzina getGodzina() {
        return godzina;
    }

    public void setGodzina(Godzina godzina) {
        this.godzina = godzina;
    }
}