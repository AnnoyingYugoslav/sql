package com.example.sql.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.sql.entities.Account;
import com.example.sql.entities.Klasa;

public interface AccountRepository extends JpaRepository<Klasa, Long> {
    Account findIdByLogin(String login);
}

