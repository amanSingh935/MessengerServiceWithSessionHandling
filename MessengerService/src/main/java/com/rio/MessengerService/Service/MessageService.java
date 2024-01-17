package com.rio.MessengerService.Service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.rio.MessengerService.Entity.Message;
import com.rio.MessengerService.Model.CustomUserDetails;
import com.rio.MessengerService.Model.MessageReceived;
import com.rio.MessengerService.Repository.MessageRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;
import java.util.Objects;

@Component
public class MessageService {
    final static Logger logger = LoggerFactory.getLogger(MessageService.class);
    ObjectMapper mapper = new ObjectMapper();
    @Autowired
    MessageRepository messageRepository;

    public ResponseEntity<String> prepareResponseForMessageList(List<Message> messageList) {
        try {
            List<String> messageEntries = messageList.stream().map(Message::toString).toList();
            return ResponseEntity.ok(mapper.writeValueAsString(messageEntries));
        } catch (JsonProcessingException e) {
            return ResponseEntity.internalServerError().body("Unable to parse object due to : " + e);
        }
    }

    public boolean checkFriendInMessage(String username, String friend, Message m) {
        return m.getUserTo().equals(friend) || m.getUserFrom().equals(friend);
    }

    public ResponseEntity<String> validateUserToken(String username, CustomUserDetails userDetails) {
        if (!Objects.equals(userDetails.getUsername(), username)) {
            return ResponseEntity.status(403).body("Token does not match the user.");
        }
        return null;
    }

    public Message prepareMessage(String username, MessageReceived messageReceived) {
        return new Message(username, messageReceived.getData(), messageReceived.getToUser());
    }

    public void persistMessagesAsRead(List<Message> validMessages, String username) {
        for (Message validMessage : validMessages) {
            if(validMessage.getUserFrom().equals(username)) {
                continue;
            }
            validMessage.setIsRead(true);
            persistMessage(validMessage);
        }
    }

    public List<Message> fetchUnreadMessages(String username) {
        try {
            return messageRepository.findUnreadMessagesForUser(username);
        } catch (Exception e) {
            logger.error("Unable to fetch messages for user due to: ", e);
            return Collections.emptyList();
        }
    }

    public List<Message> fetchAllMessagesForUser(String username) {
        try {
            return messageRepository.findAllMessagesForUser(username);
        } catch (Exception e) {
            logger.error("Unable to fetch messages for user due to: ", e);
            return Collections.emptyList();
        }
    }

    public void persistMessage(Message message) {
        messageRepository.save(message);
    }

    public ResponseEntity<String> persistUserMessage(String username, Message message) {
        try {
            logger.info("Trying to persist {}", message);
            persistMessage(message);
            return ResponseEntity.ok("Message saved for user: " + username);
        } catch (Exception e) {
            logger.error("Message {} not sent due to : ", message, e);
            return ResponseEntity.internalServerError().body("Could not send message due to: " + e);
        }
    }

    public List<Message> updateUnreadMessageForUsername(String username) {
        List<Message> validMessages;
        List<Message> dbMessagesFetched = fetchUnreadMessages(username);
        validMessages = dbMessagesFetched.stream().filter(x-> !x.getIsRead()).toList();
        persistMessagesAsRead(validMessages, username);
        return validMessages;
    }
}
