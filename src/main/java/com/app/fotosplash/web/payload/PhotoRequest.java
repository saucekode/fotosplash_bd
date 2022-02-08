package com.app.fotosplash.web.payload;

import lombok.Data;

import javax.validation.constraints.NotEmpty;

@Data
public class PhotoRequest {
    @NotEmpty
    private String photoLabel;

    @NotEmpty
    private String image;
}
