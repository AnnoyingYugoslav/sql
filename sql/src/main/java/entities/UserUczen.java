package entities;

import jakarta.persistence.*;

@Entity
@Table(name = "user_uczen")
public class UserUczen {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "imie", nullable = false, length = 50)
    private String imie;

    @Column(name = "imie2", length = 50)
    private String imie2;

    @Column(name = "nazwisko", nullable = false, length = 100)
    private String nazwisko;

    @Column(name = "email", length = 100)
    private String email;

    @ManyToOne
    @JoinColumn(name = "id_klasy", nullable = false)
    private Klasa klasa;

    public UserUczen(String imie, String imie2, String nazwisko, String email, Klasa klasa) {
        this.imie = imie;
        this.imie2 = imie2;
        this.nazwisko = nazwisko;
        this.email = email;
        this.klasa = klasa;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getImie() {
        return imie;
    }

    public void setImie(String imie) {
        this.imie = imie;
    }

    public String getImie2() {
        return imie2;
    }

    public void setImie2(String imie2) {
        this.imie2 = imie2;
    }

    public String getNazwisko() {
        return nazwisko;
    }

    public void setNazwisko(String nazwisko) {
        this.nazwisko = nazwisko;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Klasa getKlasa() {
        return klasa;
    }

    public void setKlasa(Klasa klasa) {
        this.klasa = klasa;
    }

    @Override
    public String toString() {
        return "UserUczen{" +
                "id=" + id +
                ", imie='" + imie + '\'' +
                ", imie2='" + imie2 + '\'' +
                ", nazwisko='" + nazwisko + '\'' +
                ", email='" + email + '\'' +
                ", klasa=" + klasa +
                '}';
    }
}