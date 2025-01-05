package com.example.sql.entities;

import jakarta.persistence.*;
import java.time.LocalTime;


@Entity
@Table(name = "lekcja")
public class Lekcja {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "start_time", nullable = false)
    private LocalTime start;

    @Column(name = "end_time", nullable = false)
    private LocalTime end;

    @ManyToOne
    @JoinColumn(name = "dzien", nullable = false)
    private Dzien dzien;

    @ManyToOne
    @JoinColumn(name = "id_klasy", nullable = false)
    private Klasa klasa;

    @ManyToOne
    @JoinColumn(name = "id_sali", nullable = false)
    private Sala sala;

    @ManyToOne
    @JoinColumn(name = "id_nauczyciela", nullable = false)
    private UserNauczyciel nauczyciel;

    public Lekcja() {
    }

    public Lekcja(LocalTime start, LocalTime end, Dzien dzien, Klasa klasa, Sala sala, UserNauczyciel nauczyciel) {
        this.start = start;
        this.end = end;
        this.dzien = dzien;
        this.klasa = klasa;
        this.sala = sala;
        this.nauczyciel = nauczyciel;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalTime getStart() {
        return start;
    }

    public void setStart(LocalTime start) {
        this.start = start;
    }

    public LocalTime getEnd() {
        return end;
    }

    public void setEnd(LocalTime end) {
        this.end = end;
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

    @Override
    public String toString() {
        return "Lekcja{" +
                "id=" + id +
                ", start=" + start +
                ", end=" + end +
                ", dzien=" + dzien +
                ", klasa=" + klasa +
                ", sala=" + sala +
                ", nauczyciel=" + nauczyciel +
                '}';
    }
}
