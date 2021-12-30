package com.app.fotosplash.web.payload;

import lombok.Data;
import lombok.NonNull;

@Data
public class PhotoRequest {
    @NonNull
    private String photoLabel;
    @NonNull
    private String image;
}
