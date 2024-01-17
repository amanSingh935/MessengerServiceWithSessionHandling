package com.rio.MessengerService.Controller;

import com.rio.MessengerService.Dto.UserDto;
import com.rio.MessengerService.Entity.User;
import com.rio.MessengerService.Model.CustomUserDetails;
import com.rio.MessengerService.Repository.UserRepository;
import com.rio.MessengerService.Service.JwtService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RestController
public class SessionController {

    @Autowired UserRepository userRepository;
    @Autowired JwtService jwtService;
    @Autowired AuthenticationManager authenticationManager;

    final static Logger logger = LoggerFactory.getLogger(SessionController.class);

    @PostMapping("/login")
    public ResponseEntity<String> authorizeUserLogin(@RequestBody UserDto userDto) {
        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(userDto.getUsername(), userDto.getPassword()));

        if (authentication.isAuthenticated()) {
            return generateTokenAndUpdateUser(userDto);
        } else {
            logger.error("User not authenticated");
            return ResponseEntity.badRequest().body("Username does not exist or password not matching.");
        }
    }

    @PostMapping("/logout/{username}")
    public ResponseEntity<String> revokeTokenAccess(@AuthenticationPrincipal CustomUserDetails userDetails, @PathVariable String username){
        logger.info("Remove token access flow triggered for : {}", username);
        try{
            if(!userDetails.getUsername().equals(username)){
                return ResponseEntity.badRequest().body("Token passed does not match with user: " + username);
            }
            setEmptyTokenForUser(username);
            return ResponseEntity.ok("Logged out!");
        } catch (Exception e) {
            logger.error("Unable to remove token access for user:{} due to ", userDetails.getUsername(), e);
            return ResponseEntity.internalServerError().body("Unable to log out!");
        }
    }

    private void setEmptyTokenForUser(String username) {
        Optional<User> user = userRepository.findByUsername(username);
        if(user.isPresent()){
            UserDto userDto = new UserDto();
            userDto.setUsername(username);
            saveNewTokenToUser(userDto, "");
        }
    }

    private ResponseEntity<String> generateTokenAndUpdateUser(UserDto userDto) {
        logger.error("User authenticated successfully.");
        String token = jwtService.generateToken(userDto.getUsername());
        saveNewTokenToUser(userDto, token);
        return ResponseEntity.ok(token);
    }

    private void saveNewTokenToUser(UserDto userDto, String token) {
        User currentUser = userRepository.findByUsername(userDto.getUsername()).get();  // Save token to DB
        currentUser.setToken(token);
        userRepository.save(currentUser);
    }
}
