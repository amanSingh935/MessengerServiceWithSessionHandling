package com.rio.MessengerService.Controller;

import com.rio.MessengerService.Dto.UserDto;
import com.rio.MessengerService.Service.JwtService;
import com.rio.MessengerService.Service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/login")
public class SessionController {

    @Autowired UserService userService;
    @Autowired JwtService jwtService;
    @Autowired AuthenticationManager authenticationManager;

    final static Logger logger = LoggerFactory.getLogger(SessionController.class);

    @PostMapping("/user")
    public ResponseEntity<String> authorizeUserLogin(@RequestBody UserDto userDto) {
        logger.info("Authenticating user: {}", userDto.getUsername());
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(userDto.getUsername(), userDto.getPassword()));
        if (authentication.isAuthenticated()) {
            logger.error("User authenticated successfully.");
            return ResponseEntity.ok(jwtService.generateToken(userDto.getUsername()));
        } else {
            logger.error("User not authenticated");
            return ResponseEntity.badRequest().body("Username does not exist or password not matching.");
        }
    }
}
