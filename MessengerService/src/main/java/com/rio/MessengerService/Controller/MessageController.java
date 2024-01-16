package com.rio.MessengerService.Controller;

import com.rio.MessengerService.Dto.MessageDto;
import com.rio.MessengerService.Entity.Message;
import com.rio.MessengerService.Model.MessageReceived;
import com.rio.MessengerService.Service.MessageService;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.sql.Timestamp;
import java.util.List;

@RestController
public class MessageController {

    final static Logger logger = LoggerFactory.getLogger(MessageController.class);
    @Autowired MessageService messageService;
    @Autowired ModelMapper modelMapper;

    @GetMapping("/user/{username}/message")
    public ResponseEntity<String> fetchUnreadMessages(@PathVariable String username){
        List<Message> messageList = messageService.fetchUnreadMessages(username);
        return ResponseEntity.ok(messageList.toString());
    }

    @PostMapping("/user/{username}/message")
    public ResponseEntity<String> publishMessage(@PathVariable String username, @RequestBody MessageReceived messageReceived){
        MessageDto messageDto = MessageDto.builder()
                .message(messageReceived.getData())
                .userFrom(username)
                .userTo(messageReceived.getToUser())
                .timestamp(new Timestamp(System.currentTimeMillis()))
                .build();
        Message message = modelMapper.map(messageDto, Message.class);
        messageService.persistMessage(message);
        return ResponseEntity.ok("Message saved for user: " + username);
    }
}
