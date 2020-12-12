package com.example.demo.repository;

import com.example.demo.model.UserDto;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface JpaUserRepository extends CrudRepository<UserDto, Integer> {

    Optional<UserDto> findByUsername(String username);

}
