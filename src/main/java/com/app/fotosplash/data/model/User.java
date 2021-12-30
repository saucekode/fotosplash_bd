package com.app.fotosplash.data.model;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.ArrayList;
import java.util.List;

@Data
@Document(collection = "users")
public class User {
    @Id
    private String userId;
    private String firstName;
    private String lastName;
    private String email;
    private String userPhoto;
    private String authToken;
    private List<Photo> userPhotos;

    public void addUserPhoto(Photo photo){
        if(userPhotos == null){
            userPhotos = new ArrayList<>();
        }
        userPhotos.add(photo);
    }

}
