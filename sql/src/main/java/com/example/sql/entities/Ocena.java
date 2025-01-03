package com.example.sql.entities;

import jakarta.persistence.*;


@Entity
@Table(name = "ocena")
public class Ocena {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "ocena", nullable = false)
    private Integer ocena;

    @ManyToOne
    @JoinColumn(name = "data", nullable = false)
    private Dzien dzien;

    @ManyToOne
    @JoinColumn(name = "id_ucznia", nullable = false)
    private UserUczen uczen;

    @ManyToOne
    @JoinColumn(name = "id_nauczyciela", nullable = false)
    private UserNauczyciel nauczyciel;

    @ManyToOne
    @JoinColumn(name = "id_przedmiotu", nullable = false)
    private Przedmiot przedmiot;

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

    public Ocena(Integer ocena, Dzien data, UserUczen uczen, UserNauczyciel nauczyciel, Przedmiot przedmiot) {
        this.ocena = ocena;
        this.dzien = data;
        this.uczen = uczen;
        this.nauczyciel = nauczyciel;
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
}