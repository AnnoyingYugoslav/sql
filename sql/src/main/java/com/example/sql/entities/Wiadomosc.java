package com.example.sql.entities;

import com.example.sql.converters.DzienConverter;
import com.example.sql.converters.GodzinaConverter;

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

    @Convert(converter = GodzinaConverter.class)
    private Godzina godz;

    @Convert(converter = DzienConverter.class)
    private Dzien dzien;

    @OneToMany
    @JoinColumn(name = "nadawca", nullable = true)
    private Account account;

    @Lob //LOB czy BLOB ?
    @Column(name = "zalaczniki")
    private byte[] zalaczniki;

    public Wiadomosc(String tytul, String tresc, Godzina godz, Dzien dzien, byte[] zalaczniki, Account account) {
        this.tytul = tytul;
        this.tresc = tresc;
        this.godz = godz;
        this.dzien = dzien;
        this.zalaczniki = zalaczniki;
        this.account = account;
    }

    public Wiadomosc() {
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
                '}';
    }

    public Godzina getGodz() {
        return godz;
    }

    public void setGodz(Godzina godz) {
        this.godz = godz;
    }

    public Dzien getDzien() {
        return dzien;
    }

    public void setDzien(Dzien dzien) {
        this.dzien = dzien;
    }

    public Account getAccount() {
        return account;
    }

    public void setAccount(Account account) {
        this.account = account;
    }
}
