package com.app.fotosplash.data.model;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.validation.constraints.NotEmpty;

@Data
@Document(collection = "photos")
public class Photo {
    @Id
    private String photoId;

    @NotEmpty(message = "Image must come with a title")
    private String photoLabel;

    @NotEmpty(message = "Image url can't be empty")
    private String image;
}
