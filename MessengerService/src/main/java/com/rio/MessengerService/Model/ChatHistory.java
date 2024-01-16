package com.rio.MessengerService.Model;

import com.rio.MessengerService.Dto.MessageDto;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;

@Data
@NoArgsConstructor
public class ChatHistory {
    String username;
    String screenName;
    ArrayList<MessageDto> messageList;
}
