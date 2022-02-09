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

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
@Slf4j

// refactor exceptions
public class PhotoServiceImpl implements PhotoService{

    @Autowired
    private PhotoRepository photoRepository;

    @Autowired
    private UserRepository userRepository;

    @Override
    public User addPhoto(String userAddingPhoto, PhotoRequest photoRequest) throws FotoSplashExceptions, IOException {
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

    private Photo mapPhotoModelToDto(PhotoRequest photoRequest) throws FotoSplashExceptions, IOException {
        Photo newPhoto = new Photo();

        imageUrlValidityChecker(photoRequest);

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

        Optional<User> existingUser = userRepository.findById(userDeletingPhoto);
        Optional<Photo> photoToDelete = photoRepository.findById(photoToBeDeleted);
        List<Photo> allUserPhotos = existingUser.get().getUserPhotos();

        if (!existingUser.isPresent()) {
            throw new UserNotFoundException("User does not exist");
        }

        photoRepository.deleteById(photoToBeDeleted);
        allUserPhotos.removeIf(photo -> photo.getPhotoId().equals(photoToDelete.get().getPhotoId()));
        saveUserToDatabase(existingUser.get());
    }

    private User saveUserToDatabase(User user){
        return userRepository.save(user);
    }

    private void imageUrlValidityChecker(PhotoRequest photoRequest) throws FotoSplashExceptions, IOException {
        if(photoRepository.findByImage(photoRequest.getImage()).isPresent()){
            throw new FotoSplashExceptions("Sorry, image already exists. Try again");
        }

        if(photoRequest.getPhotoLabel().isEmpty() || photoRequest.getImage().isEmpty()){
            throw new FotoSplashExceptions("You have to input a label or an image");
        }

        if(!urlHasCorrectImageExtensions(photoRequest.getImage())){
            throw new FotoSplashExceptions("Url does not belong to an image!");
        }

        try{
            urlIsAnImage_Not_A_Random_Text(photoRequest.getImage());
        }catch(MalformedURLException e){
             throw new MalformedURLException("I sight you, comrade, ejo, image url not plain text \uD83E\uDD2A");
        }
    }

    private static boolean urlHasCorrectImageExtensions(String imageUrl){
        String regex = "([^\\s]+(\\.(?i)(jpe?g|png|gif|svg))$)";
        Pattern pattern = Pattern.compile(regex);

        Matcher matcher = pattern.matcher(imageUrl);

        return matcher.matches();
    }

    private static String urlIsAnImage_Not_A_Random_Text(String imageUrl) throws IOException {
        URL url = new URL(imageUrl);
        URLConnection conn = url.openConnection();
        String mimeType = conn.getContentType();

        return mimeType;
    }


}
