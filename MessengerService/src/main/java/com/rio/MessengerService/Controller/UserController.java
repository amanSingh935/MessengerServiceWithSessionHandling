package com.rio.MessengerService.Controller;

import com.rio.MessengerService.Dto.UserDto;
import com.rio.MessengerService.Request.UserCreationRequest;
import com.rio.MessengerService.Service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/users")
public class UserController {
    @Autowired UserService userService;
    final static Logger logger = LoggerFactory.getLogger(UserController.class);

    @PostMapping("/create")
    public ResponseEntity<String> create(@RequestBody UserCreationRequest userCreateRequest){
        logger.info("Received create request for : " + userCreateRequest.getUsername());
        try{
            UserDto userDto = new UserDto(userCreateRequest.getUsername(), userCreateRequest.getPassword());
            Boolean userCreated  = userService.createUser(userDto);
            if(userCreated){
                return ResponseEntity.status(201).body("User : " + userDto.getUsername() + " created successfully");
            } else {
                return ResponseEntity.status(409).body("User already exists.");
            }
        } catch (Exception e){
            logger.error("Could not create user name due to : ", e);
            return ResponseEntity.status(500).body("Internal error : " + e);
        }
    }

    @GetMapping("/all")
    public ResponseEntity<String> listAllUsers(){
        try{
            return ResponseEntity.ok(userService.listAllUsers().toString());
        } catch (Exception e){
            logger.error("Could not fetch all usernames.");
            return ResponseEntity.status(500).body("Internal error : " + e);
        }
    }
}
