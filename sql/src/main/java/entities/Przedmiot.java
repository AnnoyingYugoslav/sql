package entities;

import jakarta.persistence.*;

@Entity
@Table(name = "przedmiot")
public class Przedmiot {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "nazwa", nullable = false, length = 100)
    private String nazwa;

    @ManyToOne
    @JoinColumn(name = "id_nauczyciela", nullable = false)
    private UserNauczyciel nauczyciel;

    // Constructor with parameters

    public Przedmiot(String nazwa, UserNauczyciel nauczyciel) {
        this.nazwa = nazwa;
        this.nauczyciel = nauczyciel;
    }

    // Getters and Setters

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNazwa() {
        return nazwa;
    }

    public void setNazwa(String nazwa) {
        this.nazwa = nazwa;
    }

    public UserNauczyciel getNauczyciel() {
        return nauczyciel;
    }

    public void setNauczyciel(UserNauczyciel nauczyciel) {
        this.nauczyciel = nauczyciel;
    }

    // toString method

    @Override
    public String toString() {
        return "Przedmiot{" +
                "id=" + id +
                ", nazwa='" + nazwa + '\'' +
                ", nauczyciel=" + nauczyciel +
                '}';
    }
}
