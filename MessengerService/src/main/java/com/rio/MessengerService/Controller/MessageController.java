package com.rio.MessengerService.Controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.rio.MessengerService.Entity.Message;
import com.rio.MessengerService.Model.CustomUserDetails;
import com.rio.MessengerService.Model.MessageReceived;
import com.rio.MessengerService.Service.CustomUserDetailsService;
import com.rio.MessengerService.Service.MessageService;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@RestController
public class MessageController {

    final static Logger logger = LoggerFactory.getLogger(MessageController.class);
    ObjectMapper mapper = new ObjectMapper();
    @Autowired
    MessageService messageService;
    @Autowired
    CustomUserDetailsService userDetailsService;
    @Autowired
    ModelMapper modelMapper;

    private ResponseEntity<String> prepareResponseForMessageList(List<Message> messageList) {
        try {
            List<String> messageEntries = messageList.stream().map(Message::toString).toList();
            return ResponseEntity.ok(mapper.writeValueAsString(messageEntries));
        } catch (JsonProcessingException e) {
            return ResponseEntity.internalServerError().body("Unable to parse object due to : " + e);
        }
    }

    private boolean checkUser(String username, String friend, Message m) {
        return m.getUserTo().equals(friend) || m.getUserFrom().equals(friend);
    }

    private ResponseEntity<String> validateUserToken(String username, CustomUserDetails userDetails) {
        if (!Objects.equals(userDetails.getUsername(), username)) {
            return ResponseEntity.status(403).body("Token does not match the user.");
        }
        return null;
    }

    private Message prepareMessage(String username, MessageReceived messageReceived) {
        return new Message(username, messageReceived.getData(), messageReceived.getToUser());
    }

    @GetMapping(value = "/user/{username}/message")
    public ResponseEntity<String> fetchUnreadMessages(@AuthenticationPrincipal CustomUserDetails userDetails,
                                                      @PathVariable String username) {
        ResponseEntity<String> body = validateUserToken(username, userDetails);
        if (!Objects.equals(userDetails.getUsername(), username)) return body;
        List<Message> messageList = messageService.fetchUnreadMessages(username);
        messageList.forEach(x -> logger.info("Retrieved message : {}", x.toString()));
        return prepareResponseForMessageList(messageList);
    }

    @GetMapping(value = "/user/{username}/message", params = {"friend"})
    public ResponseEntity<String> fetchDirectMessages(@AuthenticationPrincipal CustomUserDetails userDetails,
                                                      @PathVariable String username,
                                                      @RequestParam String friend) {
        ResponseEntity<String> body = validateUserToken(username, userDetails);
        if(!Objects.equals(userDetails.getUsername(), username)) return body;
        List<Message> messageList = messageService.fetchUnreadMessages(username).stream()
                .filter(x -> checkUser(username, friend, x))
                .toList();
        return prepareResponseForMessageList(messageList);
    }

    @PostMapping("/user/{username}/message")
    public ResponseEntity<String> publishMessage(@AuthenticationPrincipal CustomUserDetails userDetails,
                                                 @PathVariable String username,
                                                 @RequestBody MessageReceived messageReceived) {
        ResponseEntity<String> body = validateUserToken(username, userDetails);
        if (body != null) return body;
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
