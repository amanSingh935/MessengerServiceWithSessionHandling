package com.rio.MessengerService.Controller;

import com.rio.MessengerService.Dto.UserDto;
import com.rio.MessengerService.Service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/login")
public class SessionController {

    @Autowired
    UserService userService;

    final static Logger logger = LoggerFactory.getLogger(SessionController.class);

    @PostMapping("/user")
    public ResponseEntity<String> authorizeUserLogin(@RequestBody UserDto userDto) {
        Boolean authorized = userService.authorizeUserLogin(userDto);
        if (authorized) {
            return ResponseEntity.ok("Authenticated successfully for user: "+ userDto.getUsername());
        } else {
            return ResponseEntity.status(HttpStatusCode.valueOf(401)).body("User/passwd not matching. ");
        }
    }
}
