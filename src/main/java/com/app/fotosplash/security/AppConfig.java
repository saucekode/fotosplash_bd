package com.app.fotosplash.security;

import lombok.Data;
import lombok.Getter;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.ArrayList;
import java.util.List;

@ConfigurationProperties(prefix = "app")
@Getter
public class AppConfig {
    private final Auth auth = new Auth();
    private final OAuth2 oAuth2 = new OAuth2();

    @Data
    public static class Auth{
        private String jwtSecret;
        private long jwtExpirationInMsec;
    }

    public static class OAuth2{
        private List<String> authorizedRedirectUris = new ArrayList<>();

        public List<String> getAuthorizedRedirectUris(){
            return authorizedRedirectUris;
        }

        public OAuth2 authorizedRedirectUris(List<String> authorizedRedirectUris){
            this.authorizedRedirectUris = authorizedRedirectUris;
            return this;
        }
    }
}