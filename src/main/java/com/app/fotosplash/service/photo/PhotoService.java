package com.app.fotosplash.service.photo;

import com.app.fotosplash.data.model.User;
import com.app.fotosplash.web.exceptions.FotoSplashExceptions;
import com.app.fotosplash.web.exceptions.UserNotFoundException;
import com.app.fotosplash.web.payload.PhotoRequest;

import java.util.Map;

public interface PhotoService {
    User addPhoto(String userAddingPhoto, PhotoRequest photoRequest) throws UserNotFoundException;

    Map<String, Object> viewAllPhotos(String photoLabel, int page, int size);

    void deletePhoto(String userDeletingPhoto, String photoToBeDeleted) throws FotoSplashExceptions;
}
