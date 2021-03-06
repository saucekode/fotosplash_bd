package com.app.fotosplash.data.repository;

import com.app.fotosplash.data.model.Photo;
import com.app.fotosplash.data.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends MongoRepository<User, String> {
    Optional<User> findUserByEmail(String email);

    Boolean existsByEmail(String email);
}
