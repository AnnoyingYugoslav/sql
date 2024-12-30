package entities;

import jakarta.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "uwaga")
public class Uwaga {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "tresc", nullable = false, columnDefinition = "TEXT")
    private String tresc;

    @Column(name = "data", nullable = false)
    private LocalDateTime data;

    @ManyToOne
    @JoinColumn(name = "id_ucznia", nullable = false)
    private UserUczen uczen;

    @ManyToOne
    @JoinColumn(name = "id_nauczyciela", nullable = false)
    private UserNauczyciel nauczyciel;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public Uwaga(String tresc, LocalDateTime data, UserUczen uczen, UserNauczyciel nauczyciel) {
        this.tresc = tresc;
        this.data = data;
        this.uczen = uczen;
        this.nauczyciel = nauczyciel;
    }

    @Override
    public String toString() {
        return "Uwaga{" +
                "id=" + id +
                ", tresc='" + tresc + '\'' +
                ", data=" + data +
                ", uczen=" + uczen +
                ", nauczyciel=" + nauczyciel +
                '}';
    }
}
