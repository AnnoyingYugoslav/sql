package com.example.sql.entities;

import jakarta.persistence.*;

@Entity
@Table(name = "wiadomosc_n")
public class WiadomoscN {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(cascade = CascadeType.REMOVE)
    @JoinColumn(name = "id_wiadomosci", nullable = false)
    private Wiadomosc wiadomosc;

    @ManyToOne
    @JoinColumn(name = "id_odbiorcy", nullable = false)
    private UserNauczyciel odbiorca;

    public WiadomoscN(Wiadomosc wiadomosc, UserNauczyciel odbiorca) {
        this.wiadomosc = wiadomosc;
        this.odbiorca = odbiorca;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Wiadomosc getWiadomosc() {
        return wiadomosc;
    }

    public void setWiadomosc(Wiadomosc wiadomosc) {
        this.wiadomosc = wiadomosc;
    }

    public UserNauczyciel getOdbiorca() {
        return odbiorca;
    }

    public void setOdbiorca(UserNauczyciel odbiorca) {
        this.odbiorca = odbiorca;
    }

    @Override
    public String toString() {
        return "WiadomoscU{" +
                "id=" + id +
                ", wiadomosc=" + wiadomosc +
                ", odbiorca=" + odbiorca +
                '}';
    }
}