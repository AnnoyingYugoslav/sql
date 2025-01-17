package com.example.sql.entities;

import java.time.LocalDateTime;

import com.example.sql.converters.DzienConverter;
import com.example.sql.converters.GodzinaConverter;

import jakarta.persistence.*;

@Entity
@Table(name = "uwaga")
public class Uwaga {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "tresc", nullable = false, columnDefinition = "TEXT")
    private String tresc;

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

    public Uwaga(String tresc, Dzien dzien, Godzina godzina, UserUczen uczen, UserNauczyciel nauczyciel) {
        this.tresc = tresc;
        this.dzien = dzien;
        this.godzina = godzina;
        this.uczen = uczen;
        this.nauczyciel = nauczyciel;
    }

    public Uwaga() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTresc() {
        return tresc;
    }

    public void setTresc(String tresc) {
        this.tresc = tresc;
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

    @Override
    public String toString() {
        return "Uwaga{" +
                "id=" + id +
                ", tresc='" + tresc + '\'' +
                ", data=" + dzien +
                ", uczen=" + uczen +
                ", nauczyciel=" + nauczyciel +
                '}';
    }

    public Dzien getDzien() {
        return dzien;
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
