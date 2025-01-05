package com.example.sql.entities;

import jakarta.persistence.*;

@Entity
@Table(name = "wiadomosc_u")
public class WiadomoscU {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(cascade = CascadeType.REMOVE)
    @JoinColumn(name = "id_wiadomosci", nullable = false)
    private Wiadomosc wiadomosc;

    @ManyToOne
    @JoinColumn(name = "id_odbiorcy", nullable = false)
    private UserUczen odbiorca;

    public WiadomoscU() {
    }

    public WiadomoscU(Wiadomosc wiadomosc, UserUczen odbiorca) {
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

    public UserUczen getOdbiorca() {
        return odbiorca;
    }

    public void setOdbiorca(UserUczen odbiorca) {
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