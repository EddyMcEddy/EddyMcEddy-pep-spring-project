package com.example.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.example.entity.Message;
import com.example.repository.AccountRepository;
import com.example.repository.MessageRepository;
@Service
public class MessageService {

    @Autowired
    private MessageRepository messageRepository;

    @Autowired
    private AccountRepository accountRepository;
/*Question 1 */
public Message createMessage(Message newMessage) {
    if (newMessage.getMessage_text() == null || newMessage.getMessage_text().isEmpty() ||
        newMessage.getMessage_text().length() > 255 ||
        !accountRepository.existsById(newMessage.getPosted_by())) {
        throw new IllegalArgumentException("Invalid message or user does not exist");
    }
    return messageRepository.save(newMessage);
}

    /*question 4 */
    public List<Message> getAllMessages() {
        return messageRepository.findAll(); // Fetches all messages
    }

    /*question 5 */
    public Optional<Message> getMessageById(Integer messageId) {
        return messageRepository.findById(messageId);
    }

    /*question 6 */
    public int deleteMessageById(Integer messageId) {
        boolean exists = messageRepository.existsById(messageId);
        if (exists) {
            messageRepository.deleteById(messageId);
            return 1; // One row affected
        }
        return 0; // No rows affected
    }
    

/*question 7 */
public int updateMessageText(Integer messageId, String newMessageText) {
    if (newMessageText == null || newMessageText.isBlank() || newMessageText.length() > 255) {
        throw new IllegalArgumentException("Invalid message text");
    }

    return messageRepository.findById(messageId).map(message -> {
        message.setMessage_text(newMessageText);
        messageRepository.save(message);
        return 1; // One row affected
    }).orElseThrow(() -> new IllegalArgumentException("Message not found"));
}

//public List<Message> findMessagesByUserId(Integer userId) {
//    return messageRepository.findByPosted_By(userId);
//}










}








