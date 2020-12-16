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

    public String sign (JWSSigner signer, SignedJWT signedJWT) throws JOSEException {
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

    public void verify (JWSVerifier jwsVerifier, String token) throws JOSEException, ParseException { ;
        SignedJWT signedJWT = SignedJWT.parse(token);
        boolean verify = signedJWT.verify(jwsVerifier);
        if(verify){
            //token is valid
        }

    }

    public static class JwtHanlder{

        public SignedJWT signedJWT (JWSHeader jwsHeader, JWTClaimsSet jwtClaimsSet) {
            return new SignedJWT(jwsHeader, jwtClaimsSet);
        }

        public JWSHeader jwsHeader (JWSAlgorithm jwsAlgorithm){
            return  new JWSHeader(jwsAlgorithm);
        }

        public JWSSigner jwsSigner (byte [] key) throws KeyLengthException {
            return new MACSigner(key);
        }

        public JWSVerifier jwsVerifier (byte [] key) throws JOSEException {
            return new MACVerifier(key);
        }
    }

    public static class CustomKeyExchange{

        private static  final byte [] KEY = new byte[32];
        private SecureRandom secureRandom = new SecureRandom();

        public byte [] generateKey () throws KeyLengthException {
            secureRandom.nextBytes(KEY);
            return KEY;
        }

        public byte [] getKey (){
            return KEY;
        }
    }


}
