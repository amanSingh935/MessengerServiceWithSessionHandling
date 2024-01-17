package com.rio.MessengerService.Service;

import com.rio.MessengerService.Controller.UserController;
import com.rio.MessengerService.Dto.UserDto;
import com.rio.MessengerService.Entity.User;
import com.rio.MessengerService.Repository.UserRepository;
import lombok.NonNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {
    final static Logger logger = LoggerFactory.getLogger(UserService.class);
    @Autowired private UserRepository userRepository;
    @Autowired private PasswordEncoder passwordEncoder;

    public Boolean checkUserExists(String username) {
        return userRepository.findByUsername(username).isPresent();
    }

    public Boolean createUser(@NonNull UserDto userDto) {
        logger.info("Creating user : {}", userDto.getUsername());
        Boolean userExists = checkUserExists(userDto.getUsername());
        if (userExists) {
            logger.error("User exists already, please choose a diff username.");
            return false;
        }
        User user = new User();
        user.setUsername(userDto.getUsername());
        user.setPasswd(passwordEncoder.encode(userDto.getPassword()));
        userRepository.save(user);
        return true;
    }

    public List<User> listAllUsers(){
        return userRepository.findAll();
    }

}
