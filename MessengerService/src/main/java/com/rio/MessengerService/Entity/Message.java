package com.rio.MessengerService.Entity;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "messages")
public class Message {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String userFrom;
    private String userTo;
    private String data;
    private Boolean isRead;

    @CreationTimestamp
    private LocalDateTime createdTime;

    public Message(String userFrom, String data, String userTo) {
        this.userFrom = userFrom;
        this.userTo = userTo;
        this.data = data;
        this.createdTime = LocalDateTime.now();
    }

    @Override
    public String toString(){
        return "Message From: " + userFrom + " to: " + userTo + " " + data + " at " + createdTime;
    }
}
