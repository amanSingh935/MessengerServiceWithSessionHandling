package com.rio.MessengerService.Controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.rio.MessengerService.Entity.Message;
import com.rio.MessengerService.Model.MessageReceived;
import com.rio.MessengerService.Service.MessageService;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class MessageController {

    final static Logger logger = LoggerFactory.getLogger(MessageController.class);
    @Autowired
    MessageService messageService;
    @Autowired
    ModelMapper modelMapper;

    @GetMapping("/user/{username}/message")
    public ResponseEntity<String> fetchUnreadMessages(@PathVariable String username) {
        List<Message> messageList = messageService.fetchUnreadMessages(username);
        messageList.forEach(x -> logger.info("Retrieved message : {}", x.toString()));
        ObjectMapper mapper = new ObjectMapper();
        try {
            List<String> messageEntries = messageList.stream().map(Message::toString).toList();
            return ResponseEntity.ok(mapper.writeValueAsString(messageEntries));
        } catch (JsonProcessingException e) {
            return ResponseEntity.internalServerError().body("Unable to parse object due to : " + e);
        }
    }

    private Message prepareMessage(String username, MessageReceived messageReceived) {
        return new Message(username, messageReceived.getData(), messageReceived.getToUser());
    }

    @PostMapping("/user/{username}/message")
    public ResponseEntity<String> publishMessage(@PathVariable String username, @RequestBody MessageReceived messageReceived) {
        Message message = prepareMessage(username, messageReceived);
        try {
            logger.info("Trying to persist {}", message);
            messageService.persistMessage(message);
            return ResponseEntity.ok("Message saved for user: " + username);
        } catch (Exception e) {
            logger.error("Message {} not sent due to : ", message, e);
            return ResponseEntity.internalServerError().body("Could not send message due to: " + e);
        }
    }
}
