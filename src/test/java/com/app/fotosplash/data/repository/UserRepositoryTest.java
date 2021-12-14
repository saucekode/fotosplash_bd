package com.app.fotosplash.data.repository;

import com.app.fotosplash.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;


@SpringBootTest
@Slf4j

class UserRepositoryTest {
    @Autowired
    UserRepository userRepository;

    @Autowired
    UserService userService;


}