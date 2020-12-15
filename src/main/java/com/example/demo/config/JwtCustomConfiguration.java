package com.example.demo.config;

import com.example.demo.handler.TokenHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class JwtCustomConfiguration {

    @Bean
    public TokenHandler.JwtHanlder jwtHanlder () {
        return new TokenHandler.JwtHanlder();
    }

    @Bean
    public TokenHandler.CustomKeyExchange customKeyExchange(){
        return new TokenHandler.CustomKeyExchange();
    }

}
