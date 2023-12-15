package com.example.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.example.entity.Account;
import com.example.entity.Message;
import com.example.service.AccountService;
import com.example.service.MessageService;

/**
 * TODO: You will need to write your own endpoints and handlers for your controller using Spring. The endpoints you will need can be
 * found in readme.md as well as the test cases. You be required to use the @GET/POST/PUT/DELETE/etc Mapping annotations
 * where applicable as well as the @ResponseBody and @PathVariable annotations. You should
 * refer to prior mini-project labs and lecture materials for guidance on how a controller may be built.
 */
@RestController
public class SocialMediaController {

    @Autowired
    private AccountService accountService;
    @Autowired
    private MessageService messageService;

    /*Question 1 */
    @PostMapping("/register")
public ResponseEntity<Account> registerUser(@RequestBody Account newAccount) {
    try {
        Account savedAccount = accountService.registerUser(newAccount);
        return ResponseEntity.status(HttpStatus.OK).body(savedAccount);
    } catch (DuplicateKeyException e) {
        return ResponseEntity.status(HttpStatus.CONFLICT).build();
    } catch (IllegalArgumentException e) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
    }
}


/*Question 2: User Login */
@PostMapping("/login")
    public ResponseEntity<Account> loginUser(@RequestBody Account loginAccount) {
        try {
            // Find the user by username
            Account existingAccount = accountService.findByUsername(loginAccount.getUsername());

            // Check if the account exists and the password matches
            if (existingAccount != null && existingAccount.getPassword().equals(loginAccount.getPassword())) {
                // Login successful
                return ResponseEntity.status(HttpStatus.OK).body(existingAccount);
            } else {
                // Login failed
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
            }
        } catch (IllegalArgumentException e) {
            // Handle invalid input
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

/*Question 3 */
@PostMapping("/messages")
    public ResponseEntity<Message> createMessage(@RequestBody Message newMessage) {
        try {
            Message savedMessage = messageService.createMessage(newMessage);
            return ResponseEntity.ok(savedMessage);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

    /*Question 4 */
    @GetMapping("/messages")
    public ResponseEntity<List<Message>> getAllMessages() {
        List<Message> messages = messageService.getAllMessages();
        return ResponseEntity.ok(messages); // This will always return HTTP 200
    }

    /*question 5 */
    @GetMapping("/messages/{message_id}")
    public ResponseEntity<Message> findMessageById(@PathVariable Integer message_id) {
        Optional<Message> message = messageService.getMessageById(message_id);
        return message.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.ok(null));
    }

    /*question 6 */
    @DeleteMapping("/messages/{message_id}")
    public ResponseEntity<?> deleteMessage(@PathVariable Integer message_id) {
        int rowsAffected = messageService.deleteMessageById(message_id);
        if (rowsAffected == 0) {
            return ResponseEntity.ok().body(""); // Return empty response body
        }
        return ResponseEntity.ok().body(rowsAffected); // This line might not be needed based on your test cases
    }

 
    
   /*question 7 */
   @PatchMapping("/messages/{message_id}")
   public ResponseEntity<Integer> updateMessage(@PathVariable Integer message_id, @RequestBody String newMessageText) {
       try {
           int rowsAffected = messageService.updateMessageText(message_id, newMessageText);
           return ResponseEntity.ok(rowsAffected); // Returns the count of rows affected
       } catch (IllegalArgumentException e) {
           return ResponseEntity.status(HttpStatus.BAD_REQUEST).build(); // Client error
       }
   }

  
  // @GetMapping("/accounts/{userId}/messages")
   //public ResponseEntity<List<Message>> getMessagesForUser(@PathVariable Integer userId) {
   //    List<Message> messages = messageService.findMessagesByUserId(userId);
   //    return ResponseEntity.ok(messages);
  // }
   

  






}













