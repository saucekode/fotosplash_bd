package com.app.fotosplash.data.repository;

import com.app.fotosplash.data.model.Photo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PhotoRepository extends MongoRepository<Photo, String> {
    Page<Photo> findByPhotoLabelContainingIgnoreCase(String photoLabel, Pageable pageable);

    Photo findByPhotoLabel(String photoLabel);

}
