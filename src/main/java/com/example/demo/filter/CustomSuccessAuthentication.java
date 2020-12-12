package com.example.demo.filter;

import com.example.demo.handler.TokenHandler;
import com.example.demo.model.CustomUserDetails;
import com.example.demo.model.UserDto;
import com.example.demo.service.CustomUserDetailsService;
import com.example.demo.source.CustomWebAuthenticationDetailsSource;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.nimbusds.jose.JOSEException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationDetailsSource;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class CustomSuccessAuthentication extends AbstractAuthenticationProcessingFilter {

    public static final String AUTHORIZATION_HEADER = "Authorization";
    private static final String DEFAULT_PROCESSES_FILTER = "/auth";

    @Autowired
    private CustomUserDetailsService customUserDetailsService;
    @Autowired
    private TokenHandler tokenHandler;
    @Autowired
    private CustomWebAuthenticationDetailsSource customWebAuthenticationDetailsSource;

    public CustomSuccessAuthentication(String defaultFilterProcessesUrl, AuthenticationManager authenticationManager) {
        super(DEFAULT_PROCESSES_FILTER, authenticationManager);
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) throws AuthenticationException, IOException, ServletException {
        final String AUTH_HEADER = httpServletRequest.getHeader(AUTHORIZATION_HEADER);
        UserDto userDto = null;
        if(AUTH_HEADER != null && !AUTH_HEADER.isEmpty()){
            //get issuer
            //find in database
            //if exists find database
            //populate username authenticatio token
        }
        else{
            userDto = new ObjectMapper().readValue(httpServletRequest.getInputStream(), UserDto.class);
            String token = null;
            try {
                token = this.tokenHandler.sign(userDto,httpServletRequest);
            } catch (JOSEException e) {
                e.printStackTrace();
            }
            httpServletResponse.addHeader(AUTHORIZATION_HEADER, "Bearer " + token);
        }
        UsernamePasswordAuthenticationToken authentication  = new UsernamePasswordAuthenticationToken(userDto.getUsername(), userDto.getPassword());
        authentication.setDetails(userDto.getSecurityPin());
        return getAuthenticationManager().authenticate(authentication);
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authResult) throws IOException, ServletException {
//        super.successfulAuthentication(request, response, chain, authResult);
        CustomUserDetails customUserDetails = (CustomUserDetails) authResult.getPrincipal();
        System.out.println("Principal is: " + customUserDetails.getUsername());
        SecurityContextHolder.getContext().setAuthentication(authResult);
    }

    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response, AuthenticationException failed) throws IOException, ServletException {
        super.unsuccessfulAuthentication(request, response, failed);
        System.out.println("Authentication with token faild: " + failed.getMessage());
    }
}
