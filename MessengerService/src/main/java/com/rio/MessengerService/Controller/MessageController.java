package com.rio.MessengerService.Controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.rio.MessengerService.Entity.Message;
import com.rio.MessengerService.Model.CustomUserDetails;
import com.rio.MessengerService.Model.MessageReceived;
import com.rio.MessengerService.Repository.UserRepository;
import com.rio.MessengerService.Service.MessageService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Objects;

@RestController
public class MessageController {

    final static Logger logger = LoggerFactory.getLogger(MessageController.class);
    ObjectMapper mapper = new ObjectMapper();
    @Autowired MessageService messageService;
    @Autowired UserRepository userRepository;

    @GetMapping(value = "/user/{username}/message", params = {"filterReadMessages"})
    public ResponseEntity<String> fetchUnreadMessages(@AuthenticationPrincipal CustomUserDetails userDetails,
                                                      @PathVariable String username,
                                                      @RequestParam Boolean filterReadMessages) {
        ResponseEntity<String> body = messageService.validateUserToken(username, userDetails);
        if (!Objects.equals(userDetails.getUsername(), username)) return body;

        List<Message> validMessages;
        if(filterReadMessages){
            validMessages = messageService.updateUnreadMessageForUsername(username);
        } else {
            validMessages = messageService.fetchAllMessagesForUser(username);
        }
        return messageService.prepareResponseForMessageList(validMessages);
    }

    @GetMapping(value = "/user/{username}/message", params = {"friend"})
    public ResponseEntity<String> fetchDirectMessages(@AuthenticationPrincipal CustomUserDetails userDetails,
                                                      @PathVariable String username,
                                                      @RequestParam String friend) {
        ResponseEntity<String> body = messageService.validateUserToken(username, userDetails);
        if(!Objects.equals(userDetails.getUsername(), username)) return body;
        List<Message> messageList = messageService.fetchUnreadMessages(username).stream()
                .filter(x -> messageService.checkFriendInMessage(username, friend, x))
                .toList();
        return messageService.prepareResponseForMessageList(messageList);
    }



    @PostMapping("/user/{username}/message")
    public ResponseEntity<String> publishMessage(@AuthenticationPrincipal CustomUserDetails userDetails,
                                                 @PathVariable String username,
                                                 @RequestBody MessageReceived messageReceived) {
        ResponseEntity<String> body = messageService.validateUserToken(username, userDetails);
        if (body != null) return body;
        Message message = messageService.prepareMessage(username, messageReceived);
        if(receiverDoesNotExists(messageReceived.getToUser())){
            logger.debug("Receiver not registered or is invalid. Please check and resend message.");
            return ResponseEntity
                    .badRequest()
                    .body("Receiver" + messageReceived.getToUser() + " not registered or is invalid.");
        }
        return messageService.persistUserMessage(username, message);
    }

    private boolean receiverDoesNotExists(String receiverUsername) {
        return userRepository.findByUsername(receiverUsername).isEmpty();
    }
}
