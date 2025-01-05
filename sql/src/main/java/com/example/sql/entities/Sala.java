package com.example.sql.entities;
import jakarta.persistence.*;

@Entity
@Table(name = "sala")
public class Sala {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "nr_sali", nullable = false, length = 10)
    private String nazwa;

    public Sala(){
    }

    public Sala(Long id, String nazwa) {
        this.id = id;
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
        return "Sala{" +
                "id=" + id +
                ", nazwa=" + nazwa +
                '}';
    }
}
