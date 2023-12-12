package com.example.repository;

import com.example.entity.Account;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AccountRepository extends JpaRepository<Account, Integer> {

    // Additional methods specific to Account entity can be defined here if needed
    Account findByUsername(String username);
    Account findByUsernameAndPassword(String username, String password);
}
