package com.example.demo.filter;

import com.example.demo.handler.TokenHandler;
import com.example.demo.model.CustomUserDetails;
import com.example.demo.model.UserDto;
import com.example.demo.service.CustomUserDetailsService;
import com.example.demo.source.CustomWebAuthenticationDetailsSource;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.nimbusds.jose.JOSEException;
import com.nimbusds.jose.JWSAlgorithm;
import com.nimbusds.jose.JWSHeader;
import com.nimbusds.jose.JWSSigner;
import com.nimbusds.jwt.SignedJWT;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationDetailsSource;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jose.jws.JwsAlgorithm;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.file.AccessDeniedException;

public class CustomSuccessAuthentication extends AbstractAuthenticationProcessingFilter {

    public static final String AUTHORIZATION_HEADER = "Authorization";
    private static final String DEFAULT_PROCESSES_FILTER = "/auth";

    @Autowired
    private CustomUserDetailsService customUserDetailsService;
    @Autowired
    private TokenHandler tokenHandler;
    @Autowired
    private CustomWebAuthenticationDetailsSource customWebAuthenticationDetailsSource;
    @Autowired
    private TokenHandler.JwtHanlder jwtHanlder;
    @Autowired
    private TokenHandler.CustomKeyExchange customKeyExchange;

    public CustomSuccessAuthentication(String defaultFilterProcessesUrl, AuthenticationManager authenticationManager) {
        super(DEFAULT_PROCESSES_FILTER, authenticationManager);
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) throws AuthenticationException, IOException, ServletException {
        UserDto userDto = null;
        userDto = new ObjectMapper().readValue(httpServletRequest.getInputStream(), UserDto.class);
        if(userDto != null) {
            UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(userDto.getUsername(), userDto.getPassword());
            authentication.setDetails(userDto.getSecurityPin());
            return getAuthenticationManager().authenticate(authentication);
        }
        throw new AccessDeniedException("invalid authentication");
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authResult) throws IOException, ServletException {
//        super.successfulAuthentication(request, response, chain, authResult);
        final String AUTH_HEADER = request.getHeader(AUTHORIZATION_HEADER);
        CustomUserDetails customUserDetails = (CustomUserDetails) authResult.getPrincipal();
        if(AUTH_HEADER != null && !AUTH_HEADER.isEmpty()){
            //get issuer
            //find in database
            //if exists find database
            //populate username authenticatio token
        }
        else{
            String token = null;
            try {
                byte [] key = this.customKeyExchange.getKey();
                SignedJWT signedJWT = this.jwtHanlder.signedJWT(jwsHeader(), this.tokenHandler.geJwtClaimsSet(customUserDetails.getUserDto(), request));
                token = this.tokenHandler.sign(this.jwtHanlder.jwsSigner(key),signedJWT);
            } catch (JOSEException e) {
                e.printStackTrace();
            }
            response.addHeader(AUTHORIZATION_HEADER, "Bearer " + token);
        }
        System.out.println("Principal is: " + customUserDetails.getUsername());

        SecurityContextHolder.getContext().setAuthentication(authResult);
    }

    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response, AuthenticationException failed) throws IOException, ServletException {
        super.unsuccessfulAuthentication(request, response, failed);
        System.out.println("Authentication with token faild: " + failed.getMessage());
    }

    private JWSHeader jwsHeader () {
        return this.jwtHanlder.jwsHeader(JWSAlgorithm.HS256);
    }

}
