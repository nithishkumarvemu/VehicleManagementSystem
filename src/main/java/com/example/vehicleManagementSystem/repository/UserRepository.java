package com.example.vehicleManagementSystem.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.vehicleManagementSystem.entity.User;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    User findByUserName(String userName);
    User findByRememberMeToken(String rememberMeToken);
    User findBySessionToken(String sessionToken);
}
