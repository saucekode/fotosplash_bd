package com.app.fotosplash.service.user;

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
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.client.RestTemplate;

import java.util.*;


import com.app.fotosplash.data.model.User;
import com.app.fotosplash.data.repository.UserRepository;
import com.app.fotosplash.web.exceptions.FotoSplashExceptions;
import com.app.fotosplash.web.exceptions.OAuth2AuthenticationProcessingException;
import com.sun.security.auth.UserPrincipal;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.security.AuthProvider;
import java.util.Map;
import java.util.Optional;

@Service
@Slf4j
public class UserServiceImpl implements UserService{
    @Autowired
    private UserRepository userRepository;

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


    private User saveUserToDatabase(User user){
        return userRepository.save(user);
    }


}
