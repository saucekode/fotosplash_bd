package com.app.fotosplash.web.payload;

import lombok.Data;

@Data
public class Response {
    private boolean isSuccessful;
    private String response;

    public Response(boolean isSuccessful, String response) {
        this.isSuccessful = isSuccessful;
        this.response = response;
    }
}
