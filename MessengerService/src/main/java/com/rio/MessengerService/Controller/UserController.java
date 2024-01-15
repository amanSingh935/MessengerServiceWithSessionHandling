package com.rio.MessengerService.Controller;

import com.rio.MessengerService.Dto.UserDto;
import com.rio.MessengerService.Request.UserCreationRequest;
import com.rio.MessengerService.Service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/user")
public class UserController {
    @Autowired UserService userService;
    final static Logger logger = LoggerFactory.getLogger(UserController.class);

    @PostMapping
    public void create(@RequestBody UserCreationRequest userCreateRequest){
        try{
            UserDto userDto = new UserDto(userCreateRequest.getUsername(), userCreateRequest.getPassword());
            userService.createUser(userDto);
        } catch (Exception e){
            logger.error("Could not create user name due to : ", e);
        }

    }
}
