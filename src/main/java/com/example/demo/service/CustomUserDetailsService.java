package com.example.demo.service;

import com.example.demo.model.CustomUserDetails;
import com.example.demo.model.UserDto;
import com.example.demo.repository.JpaUserRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import sun.nio.cs.US_ASCII;

import java.util.Optional;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    private static final String USER_NOT_FOUND_MESSAGE = "User is not found into system";
    private final JpaUserRepository jpaUserRepository;

    public CustomUserDetailsService(JpaUserRepository jpaUserRepository) {
        this.jpaUserRepository = jpaUserRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<UserDto> userDto = this.jpaUserRepository.findByUsername(username);
        if(userDto.isPresent()){
            return new CustomUserDetails(userDto.get());
        }
        throw new UsernameNotFoundException(USER_NOT_FOUND_MESSAGE);
    }
}
