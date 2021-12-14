package com.app.fotosplash.web.exceptions;

public class FotoSplashExceptions extends Exception{
    public FotoSplashExceptions() {
    }

    public FotoSplashExceptions(String message) {
        super(message);
    }

    public FotoSplashExceptions(String message, Throwable cause) {
        super(message, cause);
    }

    public FotoSplashExceptions(Throwable cause) {
        super(cause);
    }
}
