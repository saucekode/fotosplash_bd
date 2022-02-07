package com.app.fotosplash.web.controller;

import com.app.fotosplash.data.model.User;
import com.app.fotosplash.data.repository.UserRepository;
import com.app.fotosplash.security.UserPrincipal;
import com.app.fotosplash.security.oauth2.CurrentUser;
import com.app.fotosplash.service.photo.PhotoService;
import com.app.fotosplash.web.exceptions.FotoSplashExceptions;
import com.app.fotosplash.web.exceptions.UserNotFoundException;
import com.app.fotosplash.web.payload.PhotoRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.Map;

@RestController
@RequestMapping("/api/v1/")
@Slf4j
public class UserController {

    @Autowired
    private UserRepository userRepository;

    @GetMapping("/user/profile")
    @PreAuthorize("hasRole('USER')")
    public User getCurrentUser(@CurrentUser UserPrincipal userPrincipal) throws UserNotFoundException {
        log.info("user principal -> {}", userPrincipal);
        return userRepository.findById(userPrincipal.getId())
                .orElseThrow(() -> new UserNotFoundException("user npt found"));
    }

}
