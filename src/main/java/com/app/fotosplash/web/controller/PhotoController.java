package com.app.fotosplash.web.controller;

import com.app.fotosplash.service.photo.PhotoService;
import com.app.fotosplash.web.exceptions.FotoSplashExceptions;
import com.app.fotosplash.web.exceptions.UserNotFoundException;
import com.app.fotosplash.web.payload.PhotoRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.Map;

@RestController
@RequestMapping("api/v1/photo")
public class PhotoController {
    @Autowired
    PhotoService photoService;

    @PostMapping("/addphoto/{userAddingPhoto}")
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<?> addPhoto(@RequestParam("userAddingPhoto") String userAddingPhoto, @RequestBody PhotoRequest photoRequest){
        try {
            photoService.addPhoto(userAddingPhoto, photoRequest);
        } catch (UserNotFoundException ex) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User does not exist", ex);
        }

        return ResponseEntity.ok("Photo added successfully");
    }

    @GetMapping("/viewphotos")
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<Map<String, Object>> viewPhotos(
            @RequestParam(required = false) String label,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "7") int size){

        try{
            return ResponseEntity.ok(photoService.viewAllPhotos(label, page, size));
        }catch(Exception ex){
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping("/deletephoto/{userDeletingPhoto}/{photoToBeDeleted}")
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<?> deletePhoto(
            @RequestParam("userDeletingPhoto") String userDeletingPhoto,
            @RequestParam("photoToBeDeleted") String photoToBeDeleted
    ){

        try {
            photoService.deletePhoto(userDeletingPhoto, photoToBeDeleted);
        }catch(FotoSplashExceptions e){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Not allowed", e);
        }

        return ResponseEntity.ok("Photo deleted successfully!");
    }
}
