package com.app.fotosplash.security.oauth2;

import com.app.fotosplash.utils.CookieUtils;
import com.nimbusds.oauth2.sdk.util.StringUtils;
import org.springframework.security.oauth2.client.web.AuthorizationRequestRepository;
import org.springframework.security.oauth2.core.endpoint.OAuth2AuthorizationRequest;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Component
public class HttpCookieOAuthRequestRepository implements AuthorizationRequestRepository<OAuth2AuthorizationRequest> {
    public static final String OAUTH_REQUEST_COOKIE = "oauth_request";
    public static final String REDIRECT_URI_COOKIE = "redirect_uri";
    private static final int cookieExpireSeconds = 180;

    @Override
    public OAuth2AuthorizationRequest loadAuthorizationRequest(HttpServletRequest request) {
        return CookieUtils.getCookie(request, OAUTH_REQUEST_COOKIE)
                .map(cookie -> CookieUtils.deserialize(cookie, OAuth2AuthorizationRequest.class))
                .orElse(null);
    }

    @Override
    public void saveAuthorizationRequest(OAuth2AuthorizationRequest authorizationRequest, HttpServletRequest request, HttpServletResponse response) {
        if(authorizationRequest == null){
            CookieUtils.deleteCookie(request, response, OAUTH_REQUEST_COOKIE);
            CookieUtils.deleteCookie(request, response, REDIRECT_URI_COOKIE);
            return;
        }

        CookieUtils.addCookie(response, OAUTH_REQUEST_COOKIE, CookieUtils.serialize(authorizationRequest), cookieExpireSeconds);
        String redirectUriAfterLogin  = request.getParameter(REDIRECT_URI_COOKIE);
        if(StringUtils.isNotBlank(redirectUriAfterLogin)){
            CookieUtils.addCookie(response, REDIRECT_URI_COOKIE, redirectUriAfterLogin, cookieExpireSeconds);
        }
    }

    @Override
    public OAuth2AuthorizationRequest removeAuthorizationRequest(HttpServletRequest request) {
        return this.loadAuthorizationRequest(request);
    }

    public void removeAuthorizationRequestCookies(HttpServletRequest request, HttpServletResponse response) {
        CookieUtils.deleteCookie(request, response, OAUTH_REQUEST_COOKIE);
        CookieUtils.deleteCookie(request, response, REDIRECT_URI_COOKIE);
    }
}
