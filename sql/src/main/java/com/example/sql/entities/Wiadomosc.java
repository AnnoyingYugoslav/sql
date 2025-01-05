package com.example.sql.entities;

import jakarta.persistence.*;
@Entity
@Table(name = "wiadomosc")
public class Wiadomosc {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "tytul", nullable = false, length = 255)
    private String tytul;

    @Column(name = "tresc", nullable = false, columnDefinition = "TEXT")
    private String tresc;

    @ManyToOne
    @JoinColumn(name = "data", nullable = false)
    private Dzien dzien;

    @Lob //LOB czy BLOB ?
    @Column(name = "zalaczniki")
    private byte[] zalaczniki;

    public Wiadomosc() {
    }

    public Wiadomosc(String tytul, String tresc, Dzien data, byte[] zalaczniki) {
        this.tytul = tytul;
        this.tresc = tresc;
        this.dzien = data;
        this.zalaczniki = zalaczniki;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTytul() {
        return tytul;
    }

    public void setTytul(String tytul) {
        this.tytul = tytul;
    }

    public String getTresc() {
        return tresc;
    }

    public void setTresc(String tresc) {
        this.tresc = tresc;
    }


    public byte[] getZalaczniki() {
        return zalaczniki;
    }

    public void setZalaczniki(byte[] zalaczniki) {
        this.zalaczniki = zalaczniki;
    }

    @Override
    public String toString() {
        return "Wiadomosc{" +
                "id=" + id +
                ", tytul='" + tytul + '\'' +
                ", tresc='" + tresc + '\'' +
                ", data=" + dzien +
                '}';
    }
}
