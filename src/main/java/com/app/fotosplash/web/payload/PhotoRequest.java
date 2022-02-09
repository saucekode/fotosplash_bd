package com.app.fotosplash.web.payload;

import lombok.AllArgsConstructor;
import lombok.Data;

import javax.validation.constraints.NotEmpty;

@Data
@AllArgsConstructor
public class PhotoRequest {
    @NotEmpty(message = "Image must come with a label")
    private String photoLabel;

    @NotEmpty(message = "Image url can't be empty")
    private String image;
}
