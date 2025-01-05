package com.example.sql.entities;

import jakarta.persistence.*;

@Entity
public class Relacja {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "id_rodzic", nullable = false)
    private UserRodzic userRodzic;

    @ManyToOne
    @JoinColumn(name = "id_uczen", nullable = false)
    private UserUczen userUczen;

    public Relacja(Long id, UserRodzic userRodzic, UserUczen userUczen) {
        this.id = id;
        this.userRodzic = userRodzic;
        this.userUczen = userUczen;
    }

    public Relacja() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public UserRodzic getUserRodzic() {
        return userRodzic;
    }

    public void setUserRodzic(UserRodzic userRodzic) {
        this.userRodzic = userRodzic;
    }

    public UserUczen getUserUczen() {
        return userUczen;
    }

    public void setUserUczen(UserUczen userUczen) {
        this.userUczen = userUczen;
    }
}
