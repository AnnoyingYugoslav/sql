package entities;

import jakarta.persistence.*;
import java.time.LocalDateTime;

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

    @Column(name = "data", nullable = false)
    private LocalDateTime data;

    @Lob //LOB czy BLOB ?
    @Column(name = "zalaczniki")
    private byte[] zalaczniki;

    public Wiadomosc(String tytul, String tresc, LocalDateTime data, byte[] zalaczniki) {
        this.tytul = tytul;
        this.tresc = tresc;
        this.data = data;
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

    public LocalDateTime getData() {
        return data;
    }

    public void setData(LocalDateTime data) {
        this.data = data;
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
                ", data=" + data +
                '}';
    }
}
