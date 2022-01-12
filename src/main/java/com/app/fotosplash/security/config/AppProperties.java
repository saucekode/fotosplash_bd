package com.app.fotosplash.security.config;

import lombok.Data;
import lombok.Getter;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.ArrayList;
import java.util.List;

@ConfigurationProperties(prefix = "app")
@Getter
public class AppProperties {
    private final Auth auth = new Auth();
    private final OAuth oAuth = new OAuth();

    @Data
    public static class Auth{
        private String tokenSecret;
        private String tokenExpTime;

    }

    @Getter
    public static final class OAuth{
        private List<String> authorizedRedirectUris = new ArrayList<>();

        public OAuth authorizedRedirectUris(List<String> authorizedRedirectUris){
            this.authorizedRedirectUris = authorizedRedirectUris;
            return this;
        }
    }

}
