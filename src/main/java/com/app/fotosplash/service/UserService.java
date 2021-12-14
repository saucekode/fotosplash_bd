package com.app.fotosplash.service;

import com.app.fotosplash.data.model.Photo;
import com.app.fotosplash.data.model.User;
import com.app.fotosplash.web.exceptions.FotoSplashExceptions;
import com.app.fotosplash.web.exceptions.UserNotFoundException;
import com.app.fotosplash.web.payload.PhotoDTO;
import org.springframework.data.domain.Pageable;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.util.List;
import java.util.Map;

public interface UserService {
    Principal loginUser(Principal user);

    User saveUserprofile(User user, OAuth2AuthenticationToken userAuth) throws FotoSplashExceptions;

    User addPhoto(String userAddingPhoto, PhotoDTO photoDTO) throws UserNotFoundException;

    Map<String, Object> viewAllPhotos(String photoLabel, int page, int size);

    void deletePhoto(String userDeletingPhoto, String photoToBeDeleted) throws FotoSplashExceptions;

}
