package com.example.SpringSecurityEx2.repository;

import com.example.SpringSecurityEx2.entites.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Integer> {

    @Query("select u from User u where u.username = :username")
    Optional<User> findByUsername(String username);
}
