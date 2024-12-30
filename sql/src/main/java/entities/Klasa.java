package entities;

import jakarta.persistence.*;

@Entity
@Table(name = "klasa")
public class Klasa {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "nazwa", nullable = false, length = 50)
    private String nazwa;

    public Klasa(String nazwa) {
        this.nazwa = nazwa;
    }

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

    @Override
    public String toString() {
        return "Klasa{" +
                "id=" + id +
                ", nazwa='" + nazwa + '\'' +
                '}';
    }
}
