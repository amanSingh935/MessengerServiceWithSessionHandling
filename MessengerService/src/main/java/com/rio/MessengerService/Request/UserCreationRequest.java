package com.rio.MessengerService.Request;

import lombok.Data;
import lombok.Getter;
import lombok.NonNull;

@Data
public class UserCreationRequest {
    private String username;
    private String password;
}
