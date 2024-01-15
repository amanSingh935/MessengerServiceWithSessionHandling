package com.rio.MessengerService.Request;

import lombok.Data;
import lombok.Getter;
import lombok.NonNull;

@Data
public class UserCreationRequest {
    @NonNull
    String username;
    @NonNull
    String password;
}
