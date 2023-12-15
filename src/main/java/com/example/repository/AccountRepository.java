package com.example.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.entity.Account;

public interface AccountRepository extends JpaRepository<Account, Integer> {
   
       /*Question 1 */
    Account findByUsername(String username);
    // ... other repository methods if needed

    
}



