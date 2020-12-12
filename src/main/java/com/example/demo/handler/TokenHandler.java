package com.example.demo.handler;

import com.example.demo.model.UserDto;
import com.nimbusds.jose.*;
import com.nimbusds.jose.crypto.MACSigner;
import com.nimbusds.jose.crypto.MACVerifier;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import org.springframework.security.oauth2.jose.jws.JwsAlgorithm;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtException;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.security.SecureRandom;
import java.sql.Date;
import java.text.ParseException;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;

@Component
public class TokenHandler {

    private static final SecureRandom SECURE_RANDOM = new SecureRandom();
    private static byte [] sharedS;

    public String sign (UserDto userDto, HttpServletRequest request) throws JOSEException {
        SecureRandom random = new SecureRandom();
        if(sharedS == null) {
            sharedS = new byte[32];
            random.nextBytes(sharedS);
        }
        JWSSigner signer = new MACSigner(sharedS);
        SignedJWT signedJWT
                = new SignedJWT(new JWSHeader(JWSAlgorithm.HS256), geJwtClaimsSet(userDto,request));
        signedJWT.sign(signer);
        System.out.println(signedJWT.serialize());
        return signedJWT.serialize();
    }

    public JWTClaimsSet geJwtClaimsSet(UserDto userDto, HttpServletRequest request) {
        final String issuer = request.getRequestURL().toString();
        return new JWTClaimsSet.Builder()
                .subject(userDto.getUsername())
                .issuer(issuer)
                .audience("")
                .issueTime(Date.from(LocalDateTime.now().atZone(ZoneId.systemDefault()).toInstant()))
                .claim("scope" , "message")
                .expirationTime(Date.from(LocalDateTime.now().plusHours(5).atZone(ZoneId.systemDefault()).toInstant()))
                .build();
    }

    public void verify (String token) throws JOSEException, ParseException {
        JWSVerifier jwsVerifier = new MACVerifier(sharedS);
        SignedJWT signedJWT = SignedJWT.parse(token);
        boolean verify = signedJWT.verify(jwsVerifier);
        if(verify){
            //token is valid
        }

    }


}
