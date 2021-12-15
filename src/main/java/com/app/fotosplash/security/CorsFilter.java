//package com.app.fotosplash.security;
//
//import javax.servlet.*;
//import javax.servlet.http.HttpServletRequest;
//import javax.servlet.http.HttpServletResponse;
//import java.io.IOException;
//
//public class CorsFilter implements Filter {
//    private String localUrl = "http://localhost:3000";
//    private String devUrl = "https://objective-dubinsky-cb9c46.netlify.app/";
//
//    @Override
//    public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain) throws IOException, ServletException {
//        HttpServletResponse response = (HttpServletResponse) res;
//        HttpServletRequest request = (HttpServletRequest) req;
//
//        if(localUrl.equalsIgnoreCase(request.getHeader("Origin"))){
//            response.setHeader("Access-Control-Allow-Origin", localUrl);
//        }
//
//        if(devUrl.equalsIgnoreCase(response.getHeader("Origin"))){
//            response.setHeader("Access-Control-Allow-Origin", devUrl);
//        }
//
//        response.setHeader("Access-Control-Allow-Credentials", "true");
//        response.setHeader("Allow-Control-Allow-Methods", "POST, GET, PUT, PATCH, OPTIONS, DELETE");
//        response.setHeader("Access-Control-Max-Age", "3600");
//        response.setHeader("Access-Control-Allow-Headers", "x-Requested-With, Content-Type, Authorization, Origin, Accept, Access-Control-Request-Method, Access-Control-Request-Headers");
//
//        chain.doFilter(req, res);
//    }
//}
