package com.rio.MessengerService.Service;

import com.rio.MessengerService.Controller.UserController;
import com.rio.MessengerService.Dto.UserDto;
import com.rio.MessengerService.Entity.User;
import com.rio.MessengerService.Repository.UserRepository;
import lombok.NonNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService {
    final static Logger logger = LoggerFactory.getLogger(UserController.class);
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;

    public void createUser(@NonNull UserDto userDto){
        logger.info("Creating user : {}", userDto.getUsername());
        Optional<User> optionalUser = checkUserExists(userDto.getUsername());
        if(optionalUser.isPresent()){
            logger.error("User exists already, please choose a diff username.");
            return;
        }
        User user = new User();
        user.setUsername(userDto.getUsername());
        user.setPasswd(userDto.getPassword());
        userRepository.save(user);
    }

    public Optional<User> checkUserExists(String username){
        return userRepository.findByUserName(username);
    }
}
