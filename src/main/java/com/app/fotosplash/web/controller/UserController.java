package com.app.fotosplash.web.controller;

import com.app.fotosplash.data.model.User;
import com.app.fotosplash.service.UserService;
import com.app.fotosplash.web.exceptions.FotoSplashExceptions;
import com.app.fotosplash.web.exceptions.UserNotFoundException;
import com.app.fotosplash.web.payload.PhotoRequest;
import com.app.fotosplash.web.payload.Response;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.security.Principal;
import java.util.Map;

@RestController
@RequestMapping("/api/")
@Slf4j
public class UserController {

    @Autowired
    UserService userService;

    @GetMapping("/login")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<?> loginUser(Principal user){
        userService.loginUser(user);
        return ResponseEntity.ok(new Response(true, "User authenticated successfully!"));
    }

    @GetMapping("/userinfo")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<?> viewUserProfile(User user, OAuth2AuthenticationToken userToken){
        try {
            return ResponseEntity.ok(userService.saveUserprofile(user, userToken));
        } catch (FotoSplashExceptions e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User already exists!", e);
        }
    }

    @PostMapping("/addphoto/{userAddingPhoto}")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<?> addPhoto(@RequestParam("userAddingPhoto") String userAddingPhoto, @RequestBody PhotoRequest photoRequest){
        try {
            userService.addPhoto(userAddingPhoto, photoRequest);
        } catch (UserNotFoundException ex) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User does not exist", ex);
        }

        return ResponseEntity.ok("Photo added successfully");
    }

    @GetMapping("/viewphotos")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<Map<String, Object>> viewPhotos(
            @RequestParam(required = false) String label,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "7") int size){

        try{
            return ResponseEntity.ok(userService.viewAllPhotos(label, page, size));
        }catch(Exception ex){
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping("/deletephoto/{userDeletingPhoto}/{photoToBeDeleted}")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<?> deletePhoto(
            @RequestParam("userDeletingPhoto") String userDeletingPhoto,
            @RequestParam("photoToBeDeleted") String photoToBeDeleted
            ){

            try {
                userService.deletePhoto(userDeletingPhoto, photoToBeDeleted);
            }catch(FotoSplashExceptions e){
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Not allowed", e);
            }

        return ResponseEntity.ok("Photo deleted successfully!");
    }
}
