package com.example.demo.web;

import com.example.demo.model.UserDto;
import com.example.demo.repository.JpaUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.Map;

@RestController
@RequestMapping("/api/user")
public class UserController {

    @Autowired
    private JpaUserRepository jpaUserRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;

    @PostMapping
    public ResponseEntity<UserDto> registerUser(@RequestBody @Validated UserDto userDto, BindingResult bindingResult){
        if(bindingResult.hasErrors()){
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .build();
        }
        else{
            userDto.setPassword(this.passwordEncoder.encode(userDto.getPassword()));
            userDto.setSecurityPin(this.passwordEncoder.encode(userDto.getSecurityPin()));
            UserDto userDto1 = this.jpaUserRepository.save(userDto);
            if(userDto1 != null){
                return ResponseEntity
                        .created(URI.create("/api/user"))
                        .body(userDto1);
            }
            return ResponseEntity.noContent()
                    .build();
        }
    }

}
