package com.example.demo.filter;

import com.example.demo.handler.TokenHandler;
import com.example.demo.repository.JpaUserRepository;
import com.nimbusds.jose.JOSEException;
import com.nimbusds.jose.JWSVerifier;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.GenericFilterBean;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.text.ParseException;

public class TokenCheckerGenericBeanFilter extends GenericFilterBean {

    @Autowired
    private TokenHandler tokenHandler;
    @Autowired
    private TokenHandler.JwtHanlder jwtHanlder;
    @Autowired
    private TokenHandler.CustomKeyExchange customKeyExchange;

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;
        final String UNAUTHORIZED_MESSAGE = "Bearer toke isn't represented in authorization header";
        final String BEARER_HEADER = request.getHeader(CustomSuccessAuthentication.AUTHORIZATION_HEADER);
        if(BEARER_HEADER == null){
            //TODO: you can not access resources
            response.sendError(HttpStatus.UNAUTHORIZED.value(), UNAUTHORIZED_MESSAGE);
        }
        else{
            final String TOKEN = BEARER_HEADER.split(" ")[1];
            try {
                JWSVerifier jwsVerifier = this.jwtHanlder.jwsVerifier(this.customKeyExchange.getKey());
                this.tokenHandler.verify(jwsVerifier, TOKEN);
            } catch (JOSEException e) {
                e.printStackTrace();
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        filterChain.doFilter(servletRequest, servletResponse);
    }
}
