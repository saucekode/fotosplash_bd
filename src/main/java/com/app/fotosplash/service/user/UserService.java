package com.app.fotosplash.service.user;

import com.app.fotosplash.data.model.User;
import com.app.fotosplash.web.exceptions.FotoSplashExceptions;
import com.app.fotosplash.web.exceptions.UserNotFoundException;
import com.app.fotosplash.web.payload.PhotoRequest;

import java.util.Map;

public interface UserService {

    User saveUserprofile() throws FotoSplashExceptions;



}
