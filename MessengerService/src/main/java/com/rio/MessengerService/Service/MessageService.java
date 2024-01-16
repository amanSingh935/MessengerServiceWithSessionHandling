package com.rio.MessengerService.Service;

import com.rio.MessengerService.Entity.Message;
import com.rio.MessengerService.Repository.MessageRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;

@Component
public class MessageService {
    final static Logger logger = LoggerFactory.getLogger(MessageService.class);
    @Autowired
    MessageRepository messageRepository;

    public List<Message> fetchUnreadMessages(String username) {
        try {
            return messageRepository.findUnreadMessagesForUser(username);
        } catch (Exception e) {
            logger.error("Unable to fetch messages for user due to: ", e);
            return Collections.emptyList();
        }
    }

    public void persistMessage(Message message) {
        messageRepository.save(message);
    }
}
