//package com.app.fotosplash.service;
//
//import com.app.fotosplash.data.model.Photo;
//import com.app.fotosplash.data.model.User;
//import com.app.fotosplash.data.repository.PhotoRepository;
//import com.app.fotosplash.data.repository.UserRepository;
//import com.app.fotosplash.web.exceptions.FotoSplashExceptions;
//import com.app.fotosplash.web.exceptions.UserNotFoundException;
//import com.app.fotosplash.web.payload.PhotoDTO;
//import lombok.extern.slf4j.Slf4j;
//import org.junit.jupiter.api.DisplayName;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
//
//import java.util.Optional;
//
//import static org.assertj.core.api.Assertions.assertThat;
//import static org.junit.jupiter.api.Assertions.*;
//
//@SpringBootTest
//@Slf4j
//class UserServiceImplTest {
//
//    @Autowired
//    UserService userService;
//
//    @Autowired
//    UserRepository userRepository;
//
//    @Autowired
//    PhotoRepository photoRepository;
//
//    @Test
//    @DisplayName("User can add photo and it saves to user object and photo repository")
//    void userCanAddPhoto() throws UserNotFoundException {
//        PhotoDTO photoDTO = new PhotoDTO("cute passaporte", "https://storage.googleapis.com/uploadly-store/073e50ce-4460-4a87-9cfe-1d5c687329ae.jpeg");
//        User userAddingPhoto = userService.addPhoto("6182c51448bdec1a26d02157", photoDTO);
////        log.info(userAddingPhoto.);
//        assertThat(userAddingPhoto.getUserPhotos()).isNotNull();
//        assertThat(userAddingPhoto.getUserPhotos().size()).isEqualTo(1);
//        assertThat(photoRepository.count()).isEqualTo(1);
//
//    }
//
//    // test that photo can be deleted from user object and photo repository
//    @Test
//    @DisplayName("User can delete photo and it deletes from user object and photo repository")
//    void userCanDeletePhoto()  {
////        Optional<User> existingUser = userRepository.findById("6182c54448bdec1a26d02158");
//        try {
//            userService.deletePhoto("6182c51448bdec1a26d02157", "6182cd03bf937c46d199e605");
//        } catch (FotoSplashExceptions e) {
//            log.info(e.getLocalizedMessage());
//        }
//
//        assertThat(photoRepository.count()).isEqualTo(0);
//    }
//}