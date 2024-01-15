package com.rio.MessengerService.Model;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;

@Data
@NoArgsConstructor
public class ChatHistory {
    String username;
    String screenName;
    ArrayList<Message> messageList;
}
