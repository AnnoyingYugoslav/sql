package entities;

import jakarta.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;


@Entity
@Table(name = "ocena")
public class Ocena {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "ocena", nullable = false)
    private Integer ocena;

    @Column(name = "data", nullable = false)
    private LocalDate data;

    @ManyToOne
    @JoinColumn(name = "id_ucznia", nullable = false)
    private UserUczen uczen;

    @ManyToOne
    @JoinColumn(name = "id_nauczyciela", nullable = false)
    private UserNauczyciel nauczyciel;

    @ManyToOne
    @JoinColumn(name = "id_przedmiotu", nullable = false)
    private Przedmiot przedmiot;

    // Getters and Setters

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

    public LocalDate getData() {
        return data;
    }

    public void setData(LocalDate data) {
        this.data = data;
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

    public Ocena(Integer ocena, LocalDate data, UserUczen uczen, UserNauczyciel nauczyciel, Przedmiot przedmiot) {
        this.ocena = ocena;
        this.data = data;
        this.uczen = uczen;
        this.nauczyciel = nauczyciel;
        this.przedmiot = przedmiot;
    }


    @Override
    public String toString() {
        return "Ocena{" +
                "id=" + id +
                ", ocena=" + ocena +
                ", data=" + data +
                ", uczen=" + uczen +
                ", nauczyciel=" + nauczyciel +
                ", przedmiot=" + przedmiot +
                '}';
    }
}