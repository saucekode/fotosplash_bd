package com.app.fotosplash.service.photo;

import com.app.fotosplash.data.model.Photo;
import com.app.fotosplash.data.model.User;
import com.app.fotosplash.data.repository.PhotoRepository;
import com.app.fotosplash.data.repository.UserRepository;
import com.app.fotosplash.web.exceptions.FotoSplashExceptions;
import com.app.fotosplash.web.exceptions.UserNotFoundException;
import com.app.fotosplash.web.payload.PhotoRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@Slf4j
public class PhotoServiceImpl implements PhotoService{

    @Autowired
    private PhotoRepository photoRepository;

    @Autowired
    private UserRepository userRepository;

    @Override
    public User addPhoto(String userAddingPhoto, PhotoRequest photoRequest) throws UserNotFoundException {
        // need for refactor
        Optional<User> existingUser = userRepository.findById(userAddingPhoto);
//        log.info(existingUser.get().getFirstName());
        if(!existingUser.isPresent()){
            throw new UserNotFoundException("User does not exist");
        }
//        log.info(existingUser.get().getUserPhoto());
        existingUser.get().addUserPhoto(mapPhotoModelToDto(photoRequest));
        return saveUserToDatabase(existingUser.get());
    }

    private Photo mapPhotoModelToDto(PhotoRequest photoRequest){
        Photo newPhoto = new Photo();
        newPhoto.setPhotoLabel(photoRequest.getPhotoLabel());
        newPhoto.setImage(photoRequest.getImage());
        return photoRepository.save(newPhoto);
    }

    @Override
    public Map<String, Object> viewAllPhotos(String photoLabel, int page, int size) {
        List<Photo> allPhotos = new ArrayList<>();
        Pageable paging = PageRequest.of(page, size);

        Page<Photo> pagePhotos;

        if(photoLabel == null){
            pagePhotos = photoRepository.findAll(paging);
        }else{
            pagePhotos = photoRepository.findByPhotoLabelContainingIgnoreCase(photoLabel, paging);
        }

        allPhotos = pagePhotos.getContent();

        Map<String, Object> photoResponse = new HashMap<>();

        photoResponse.put("photos", allPhotos);
        photoResponse.put("currentPage", pagePhotos.getNumber());
        photoResponse.put("totalItems", pagePhotos.getTotalElements());
        photoResponse.put("totalPages", pagePhotos.getTotalPages());

        return photoResponse;

    }

    @Override
    public void deletePhoto(String userDeletingPhoto, String photoToBeDeleted) throws FotoSplashExceptions {
        // need to refactor
        Optional<User> existingUser = userRepository.findById(userDeletingPhoto);
        Optional<Photo> photoToDelete = photoRepository.findById(photoToBeDeleted);
        List<Photo> allUserPhotos = existingUser.get().getUserPhotos();

        boolean findPhoto = existingUser.get().getUserPhotos().contains(photoToDelete.get());

        log.info(String.valueOf(findPhoto));

        if (!existingUser.isPresent()) {
            throw new UserNotFoundException("User does not exist");
        }

        if(!findPhoto || allUserPhotos == null){
            throw new FotoSplashExceptions("You are not authorized to delete this image");
        }else{
            photoRepository.deleteById(photoToBeDeleted);
            allUserPhotos.removeIf(photo -> photo.getPhotoId().equals(photoToDelete.get().getPhotoId()));
            saveUserToDatabase(existingUser.get());
        }


    }

    private User saveUserToDatabase(User user){
        return userRepository.save(user);
    }
}
