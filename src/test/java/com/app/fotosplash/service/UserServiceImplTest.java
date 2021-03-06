package com.app.fotosplash.service;

import com.app.fotosplash.data.model.Photo;
import com.app.fotosplash.data.model.User;
import com.app.fotosplash.data.repository.PhotoRepository;
import com.app.fotosplash.data.repository.UserRepository;
import com.app.fotosplash.service.photo.PhotoService;
import com.app.fotosplash.web.exceptions.FotoSplashExceptions;
import com.app.fotosplash.web.exceptions.UserNotFoundException;
import com.app.fotosplash.web.payload.PhotoRequest;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import javax.imageio.ImageIO;
import javax.imageio.ImageReader;
import javax.imageio.spi.ImageReaderSpi;
import java.io.IOException;
import java.net.URL;
import java.net.URLConnection;
import java.util.Iterator;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Slf4j
class UserServiceImplTest {

    @Autowired
    PhotoService userService;

    @Autowired
    UserRepository userRepository;

    @Autowired
    PhotoRepository photoRepository;

    @Test
    @DisplayName("User can add photo and it saves to user object and photo repository")
    void userCanAddPhoto() throws FotoSplashExceptions, IOException {
        PhotoRequest photoDTO = new PhotoRequest();
        photoDTO.setPhotoLabel("cute");
        photoDTO.setImage("jkwk.jpg");
        User userAddingPhoto = userService.addPhoto("62015a21109b1320caa83a5e", photoDTO);
//        log.info(userAddingPhoto.);
        assertThat(userAddingPhoto.getUserPhotos()).isNotNull();
        assertThat(userAddingPhoto.getUserPhotos().size()).isEqualTo(3);
        assertThat(photoRepository.count()).isEqualTo(3);
    }

    // test that photo can be deleted from user object and photo repository
    @Test
    @DisplayName("User can delete photo and it deletes from user object and photo repository")
    void userCanDeletePhoto() throws FotoSplashExceptions {
//        Optional<User> existingUser = userRepository.findById("6182c54448bdec1a26d02158");
        userService.deletePhoto("62015a21109b1320caa83a5e", "620351f8c85db656cc920c11");

        assertThat(userRepository.findById("62015a21109b1320caa83a5e").get().getUserPhotos().size()).isEqualTo(2);
    }

    @Test
    void viewPhotos_paginated(){
        userService.viewAllPhotos(null, 0, 1);
    }
}