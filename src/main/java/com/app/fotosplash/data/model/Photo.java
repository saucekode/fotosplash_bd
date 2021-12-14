package com.app.fotosplash.data.model;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Document(collection = "photos")
public class Photo {
    @Id
    private String photoId;
    private String photoLabel;
    private String image;
}
