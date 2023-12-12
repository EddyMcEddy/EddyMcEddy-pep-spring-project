package com.example.controller;
import java.util.List;
import java.util.Map;
import java.util.Optional;


import com.example.entity.Account;
import com.example.entity.Message;
import com.example.repository.AccountRepository;
import com.example.repository.MessageRepository;

//import org.hibernate.mapping.Map;
//import org.hibernate.mapping.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


/**
 * TODO: You will need to write your own endpoints and handlers for your controller using Spring. The endpoints you will need can be
 * found in readme.md as well as the test cases. You be required to use the @GET/POST/PUT/DELETE/etc Mapping annotations
 * where applicable as well as the @ResponseBody and @PathVariable annotations. You should
 * refer to prior mini-project labs and lecture materials for guidance on how a controller may be built.
 */
@RestController
 public class SocialMediaController {
    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private MessageRepository messageRepository;

    // Existing registration endpoint
    @PostMapping("/register")
    public ResponseEntity<Account> registerUser(@RequestBody Account account) {
        // Check if the username is not blank and the password is at least 4 characters long
        if (isValidRegistration(account)) {
            // Check if an Account with that username already exists
            if (accountRepository.findByUsername(account.getUsername()) == null) {
                // Registration is successful
                Account registeredAccount = accountRepository.save(account);
                return new ResponseEntity<>(registeredAccount, HttpStatus.OK);
            } else {
                // Duplicate username, return 409 Conflict
                return new ResponseEntity<>(HttpStatus.CONFLICT);
            }
        } else {
            // Invalid registration, return 400 Bad Request
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

   

    @GetMapping("/messages")
    public ResponseEntity<List<Message>> getAllMessages() {
        List<Message> messages = messageRepository.findAll();

        // Return the list of messages in the response body
        return new ResponseEntity<>(messages, HttpStatus.OK);
    }

    // Existing login endpoint
    @PostMapping("/login")
    public ResponseEntity<Account> loginUser(@RequestBody Account loginRequest) {
        // Check if the provided username and password match an existing account
        Account existingAccount = accountRepository.findByUsernameAndPassword(
                loginRequest.getUsername(), loginRequest.getPassword());

        if (existingAccount != null) {
            // Login successful, return the account details with 200 OK
            return new ResponseEntity<>(existingAccount, HttpStatus.OK);
        } else {
            // Login unsuccessful, return 401 Unauthorized
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
    }

    // New message creation endpoint
    @PostMapping("/messages")
    public ResponseEntity<Message> createMessage(@RequestBody Message message) {
        // Check if the message_text is not blank and under 255 characters
        if (isValidMessageCreation(message)) {
            // Check if posted_by refers to a real, existing user
            Account existingAccount = accountRepository.findById(message.getPosted_by()).orElse(null);

            if (existingAccount != null) {
                // Creation of the message is successful
                Message createdMessage = messageRepository.save(message);
                return new ResponseEntity<>(createdMessage, HttpStatus.OK);
            } else {
                // Invalid user, return 400 Bad Request
                return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
            }
        } else {
            // Invalid message creation, return 400 Bad Request
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/messages/{message_id}")
public ResponseEntity<Message> getMessageById(@PathVariable("message_id") Integer messageId) {
    // Find the message by ID
    Message message = messageRepository.findById(messageId).orElse(null);

    // Return the message in the response body
    return new ResponseEntity<>(message, HttpStatus.OK);
}

@DeleteMapping("/messages/{message_id}")
public ResponseEntity<Object> deleteMessageById(@PathVariable("message_id") Integer messageId) {
    // Check if the message exists
    if (messageRepository.existsById(messageId)) {
        // Delete the message
        messageRepository.deleteById(messageId);

        // Return the number of rows updated (1) in the response body
        return new ResponseEntity<>(1, HttpStatus.OK);
    } else {
        // Message does not exist, return an empty response body
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
    // Helper method to check if the registration is valid
    private boolean isValidRegistration(Account account) {
        return account.getUsername() != null &&
                !account.getUsername().isBlank() &&
                account.getPassword() != null &&
                account.getPassword().length() >= 4;
    }

    // Helper method to check if the message creation is valid
    private boolean isValidMessageCreation(Message message) {
        return message.getMessage_text() != null &&
                !message.getMessage_text().isBlank() &&
                message.getMessage_text().length() < 255;
    }


    


    




    @PatchMapping("/messages/{message_id}")
public ResponseEntity<Object> updateMessageText(
        @PathVariable("message_id") Integer messageId,
        @RequestBody Map<String, String> requestBody) {

    // Check if the message exists
    Optional<Message> optionalMessage = messageRepository.findById(messageId);
    if (optionalMessage.isPresent()) {
        Message existingMessage = optionalMessage.get();

        // Get the new message_text from the request body
        String newMessageText = requestBody.get("message_text");

        // Check if the new message_text is not blank and not over 255 characters
        if (newMessageText != null && !newMessageText.isBlank() && newMessageText.length() <= 255) {
            // Update the message_text
            existingMessage.setMessage_text(newMessageText);

            // Save the updated message to the database
            messageRepository.save(existingMessage);

            // Return the number of rows updated (1) in the response body
            return new ResponseEntity<>(1, HttpStatus.OK);
        } else {
            // Invalid new message_text, return 400 Bad Request
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    } else {
        // Message does not exist, return 400 Bad Request
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }
}








    
}





