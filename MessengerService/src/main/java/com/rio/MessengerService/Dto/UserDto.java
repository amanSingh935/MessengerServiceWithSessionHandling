package com.rio.MessengerService.Dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@Builder
public class UserDto {
    private String username;
    private String password;
    private String email;
    private String name;
    public UserDto(String username, String password){
        this.username = username;
        this.password = password;
    }
}
