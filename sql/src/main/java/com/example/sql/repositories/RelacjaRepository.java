package com.example.sql.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.sql.entities.Relacja;
import com.example.sql.entities.UserRodzic;

import java.util.List;
import com.example.sql.entities.UserUczen;



public interface RelacjaRepository extends JpaRepository<Relacja, Long> {
    List<Relacja> findByUserRodzic(UserRodzic userRodzic);
    List<Relacja> findByUserUczen(UserUczen userUczen);
    Relacja findAllByUserUczenAndUserRodzic(UserUczen userUczen, UserRodzic userRodzic);
}
