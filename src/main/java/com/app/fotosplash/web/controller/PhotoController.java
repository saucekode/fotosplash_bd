package com.app.fotosplash.web.controller;

import com.app.fotosplash.service.photo.PhotoService;
import com.app.fotosplash.web.exceptions.BadRequestException;
import com.app.fotosplash.web.exceptions.FotoSplashExceptions;
import com.app.fotosplash.web.exceptions.UserNotFoundException;
import com.app.fotosplash.web.payload.PhotoRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.Map;

@RestController
@RequestMapping("api/v1/photo/")
public class PhotoController {

    @Autowired
    PhotoService photoService;

    @PostMapping("addphoto/{userAddingPhoto}")
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<?> addPhoto(@PathVariable("userAddingPhoto") String userAddingPhoto, @RequestBody PhotoRequest photoRequest){
        try {
            photoService.addPhoto(userAddingPhoto, photoRequest);
        } catch (FotoSplashExceptions ex) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User does not exist", ex);
        }catch(BadRequestException ex){
            throw new ResponseStatusException(HttpStatus.NOT_ACCEPTABLE, ex.getLocalizedMessage());
        }
        catch(IOException e){
            throw new ResponseStatusException(HttpStatus.NOT_ACCEPTABLE, "I sight you, comrade, ejo, original image \uD83E\uDD2A", e);
        }

        return ResponseEntity.ok("Photo added successfully");
    }

    @GetMapping("viewphotos")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<Map<String, Object>> viewPhotos(
            @RequestParam(required = false) String label,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "7") int size){
        try{
            return ResponseEntity.ok(photoService.viewAllPhotos(label, page, size));
        }catch(Exception ex){
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping("deletephoto/{userDeletingPhoto}/{photoToBeDeleted}")
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<?> deletePhoto(
            @PathVariable("userDeletingPhoto") String userDeletingPhoto,
            @PathVariable("photoToBeDeleted") String photoToBeDeleted
    ){

        try {
            photoService.deletePhoto(userDeletingPhoto, photoToBeDeleted);
        }catch(FotoSplashExceptions e){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Not allowed", e);
        }

        return ResponseEntity.ok("Photo deleted successfully!");
    }
}
