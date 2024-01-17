package com.rio.MessengerService.Service;

import com.rio.MessengerService.Controller.UserController;
import com.rio.MessengerService.Entity.User;
import com.rio.MessengerService.Model.CustomUserDetails;
import com.rio.MessengerService.Repository.UserRepository;
import lombok.extern.flogger.Flogger;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class CustomUserDetailsService implements UserDetailsService {
    final static Logger logger = LoggerFactory.getLogger(CustomUserDetailsService.class);
    @Autowired private UserRepository repository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<User> userInfo = repository.findByUsername(username);
        return userInfo.map(CustomUserDetails::new)
                .orElseThrow(() -> new UsernameNotFoundException("user not found " + username));
    }
}
