package com.app.fotosplash.web.payload;

import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;

@NoArgsConstructor
public class PhotoRequest {
    @NotEmpty(message = "Image must come with a title")
    private String photoLabel;

    @NotEmpty(message = "Image url can't be empty")
    private String image;

    public String getPhotoLabel() {
        return photoLabel;
    }

    public void setPhotoLabel(String photoLabel) {
        this.photoLabel = photoLabel;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
}
