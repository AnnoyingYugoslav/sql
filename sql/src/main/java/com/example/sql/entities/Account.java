package com.example.sql.entities;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import jakarta.transaction.Transactional;

@Entity
@Table(name = "accounts")
public class Account {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "password", nullable = false, length = 100)
    private String password; //hash!

    @Column(name = "login", nullable = false, length = 100)
    private String login;

    @OneToOne
    @JoinColumn(name = "user_r_id", nullable = true)
    private UserRodzic userRodzic;

    @OneToOne
    @JoinColumn(name = "user_u_id", nullable = true)
    private UserUczen userUczen;

    @OneToOne
    @JoinColumn(name = "user_n_id", nullable = true)
    private UserNauczyciel userNauczyciel;

    public Account() {
    }

    private String hashPassword(String rawPassword){
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        return passwordEncoder.encode(rawPassword);
    }

    public Account(String login, String password,User user) {
        if (user instanceof UserRodzic) {
            this.userRodzic = (UserRodzic) user;
        } else if (user instanceof UserUczen) {
            this.userUczen = (UserUczen) user;
        } else if (user instanceof UserNauczyciel) {
            this.userNauczyciel = (UserNauczyciel) user;
        }
        this.login = login;
        this.password = hashPassword(password);
    }


    public  Boolean checkUserAccount(String login, String password){
        if(this.login.equals(login)){
         BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
             if(passwordEncoder.matches(password, this.password)){
                 return true;
             }
         }
         return false;
     }

    @Transactional
    public Integer changePassowrd(String login, String password2, String newPassword){
        if(checkUserAccount(login, password2)){
            this.password = hashPassword(newPassword);
            return 0;
        }
        return 1;
    }
    @Transactional
    public Integer changeStartPassowrd(String login, String password2, String newPassword){
        if(password.equals(password2)){
            this.password = hashPassword(newPassword);
            return 0;
        }
        return 1;
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

    public UserNauczyciel getUserNauczyciel() {
        return userNauczyciel;
    }

    public void setUserNauczyciel(UserNauczyciel userNauczyciel) {
        this.userNauczyciel = userNauczyciel;
    }
}
