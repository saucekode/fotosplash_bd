package com.app.fotosplash.service;

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
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientService;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.client.RestTemplate;

import java.security.Principal;
import java.util.*;

@Service
@Slf4j
public class UserServiceImpl implements UserService{

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PhotoRepository photoRepository;

    @Autowired
    private OAuth2AuthorizedClientService authorizedClientService;

    @Autowired
    RestTemplate restTemplate;


    private OAuth2AuthorizedClient getUserDetailsFromClient(){
        OAuth2AuthenticationToken authentication = null;
        OAuth2AuthorizedClient client = authorizedClientService
                .loadAuthorizedClient(
                        authentication.getAuthorizedClientRegistrationId(),
                        authentication.getName());
        return client;
    }

    private Map retrieveUserProfileFromClient(){

        Map userDetails = null;

        String userInfo = getUserDetailsFromClient()
                            .getClientRegistration().getProviderDetails().getUserInfoEndpoint().getUri();

        OAuth2AuthorizedClient client = getUserDetailsFromClient();

        if(StringUtils.hasText(userInfo)){
            HttpHeaders headers = new HttpHeaders();
            headers.add(HttpHeaders.AUTHORIZATION, "Bearer " + client.getAccessToken()
                    .getTokenValue());
            HttpEntity entity = new HttpEntity("", headers);
            ResponseEntity<Map> response = restTemplate.exchange(userInfo, HttpMethod.GET, entity, Map.class);

            userDetails = response.getBody();

        }

        return userDetails;
    }

    @Override
    public User saveUserprofile() throws FotoSplashExceptions {
        User user = new User();
        // need for serious refactoring
        Map userInfoRetrieved = retrieveUserProfileFromClient();

        Optional<User> existingEmail = userRepository.findUserByEmail(String.valueOf(userInfoRetrieved.get("email")));

        user.setFirstName(String.valueOf(userInfoRetrieved.get("given_name")));
        user.setLastName(String.valueOf(userInfoRetrieved.get("family_name")));
        user.setUserPhoto(String.valueOf(userInfoRetrieved.get("picture")));
        user.setEmail(String.valueOf(userInfoRetrieved.get("email")));

        if(!existingEmail.isPresent()){
            return saveUserToDatabase(user);
        }else{
            throw new FotoSplashExceptions("User already exists!");
        }
    }

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

    private User saveUserToDatabase(User user){
        return userRepository.save(user);
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

}
