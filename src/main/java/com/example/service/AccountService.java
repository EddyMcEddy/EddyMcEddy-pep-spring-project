package com.example.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;

import com.example.entity.Account;
import com.example.repository.AccountRepository;

@Service
public class AccountService {

    @Autowired
    private AccountRepository accountRepository;

        /*Question 1 */
        public Account registerUser(Account newAccount) {
            if (newAccount.getUsername() == null || newAccount.getUsername().trim().isEmpty() ||
                newAccount.getPassword() == null || newAccount.getPassword().length() < 4) {
                throw new IllegalArgumentException("Invalid username or password");
            }
        
            if (accountRepository.findByUsername(newAccount.getUsername()) != null) {
                throw new DuplicateKeyException("Username already exists");
            }
        
            return accountRepository.save(newAccount);
        }
        



            /*Question 2 */
        public Account findByUsername(String username) {
            return accountRepository.findByUsername(username);
        }
}