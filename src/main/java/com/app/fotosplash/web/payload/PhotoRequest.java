package com.app.fotosplash.web.payload;

import com.sun.istack.internal.NotNull;
import lombok.Data;
import lombok.NonNull;

import javax.validation.constraints.NotEmpty;

@Data
public class PhotoRequest {
    @NotEmpty
    private String photoLabel;

    @NotEmpty
    private String image;
}
